package com.example.demo555.controller;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LeaveAppController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 启动流程
    @PostMapping("/start")
    public String startProcess(@RequestParam String employee, @RequestParam String manager) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("employee", employee);
        vars.put("manager", manager);
        runtimeService.startProcessInstanceByKey("leaveApproval", vars);
        return "流程已启动";
    }

    // 查询待办任务
    @GetMapping("/tasks")
    public List<String> getTasks(@RequestParam String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        return tasks.stream()
                .map(t -> t.getId() + ":" + t.getName())
                .collect(Collectors.toList());
    }

    // 完成任务
    @PostMapping("/complete")
    public String completeTask(@RequestParam String taskId) {
        taskService.complete(taskId);
        return "任务已完成";
    }

    // 查询历史任务（正式 HistoryService 写法）
    @GetMapping("/history")
    public List<String> history(@RequestParam String assignee) {
        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .finished() // 只查已完成
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        return historicTasks.stream()
                .map(t -> t.getId() + ":" + t.getName() + " (完成时间: " + t.getEndTime() + ")")
                .collect(Collectors.toList());
    }
}
