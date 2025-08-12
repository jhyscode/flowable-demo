package com.example.demo555.controller;

import com.example.demo555.entity.TaskDTO;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：jhys
 * @date ：Created in 2025/8/7 10:51
 * @Description ：
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 启动流程
    @PostMapping("/start")
    public String startProcess(@RequestParam String employee) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("employee", employee);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("leave_process", vars);
        return "流程启动成功，流程ID: " + instance.getId();
    }

    // 查询当前用户任务
    @GetMapping("/tasks")
    public List<TaskDTO> getTasks(@RequestParam String assignee) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();

        return taskList.stream()
                .map(task -> new TaskDTO(task.getId(), task.getName()))
                .collect(Collectors.toList());    }

    // 查询候选组任务
    @GetMapping("/groupTasks")
    public List<TaskDTO> getGroupTasks(@RequestParam String group) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(group).list();

        return taskList.stream()
                .map(task -> new TaskDTO(task.getId(), task.getName()))
                .collect(Collectors.toList());    }

    // 完成任务
    @PostMapping("/complete")
    public String completeTask(@RequestParam String taskId) {
        taskService.complete(taskId);
        return "任务完成: " + taskId;
    }

    /**
     * 查询某用户处理过的历史任务
     */
    @GetMapping("/historyTasks")
    public List<TaskDTO> getHistoryTasks(@RequestParam String assignee) {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)   // 指定办理人
                .finished()               // 只查已完成的任务
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        return list.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getStartTime(),
                        task.getEndTime(),
                        task.getProcessInstanceId()
                ))
                .collect(Collectors.toList());
    }


    @GetMapping("/historyInstances")
    public List<Map<String, Object>> getHistoryInstances() {
        List<HistoricProcessInstance> list = historyService
                .createHistoricProcessInstanceQuery()
                .finished()
                .orderByProcessInstanceEndTime().desc()
                .list();

        return list.stream().map(h -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", h.getId());
            map.put("name", h.getName());
            map.put("startTime", h.getStartTime());
            map.put("endTime", h.getEndTime());
            map.put("startUserId", h.getStartUserId());
            return map;
        }).collect(Collectors.toList());
    }

}
