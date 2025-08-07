package com.example.demo555.controller;

import com.example.demo555.entity.TaskDTO;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
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
}
