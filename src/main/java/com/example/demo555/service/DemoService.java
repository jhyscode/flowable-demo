package com.example.demo555.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public String startProcess() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("demoProcess");
        return instance.getId();
    }

    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }
}
