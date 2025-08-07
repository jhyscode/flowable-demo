package com.example.demo555.controller;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    // 启动流程
    @PostMapping("/start")
    public String startProcess(@RequestParam String processKey) {
        runtimeService.startProcessInstanceByKey(processKey);
        return "流程 [" + processKey + "] 已启动";
    }

    // 获取所有任务
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.createTaskQuery().list();
    }

    // 完成任务
    @PostMapping("/complete")
    public String completeTask(@RequestParam String taskId) {
        taskService.complete(taskId);
        return "任务 [" + taskId + "] 已完成";
    }

    // 带变量启动流程
    @PostMapping("/start-with-vars")
    public String startWithVariables(@RequestParam String processKey, @RequestBody Map<String, Object> variables) {
        runtimeService.startProcessInstanceByKey(processKey, variables);
        return "流程启动，变量: " + variables;
    }
}
