package com.example.demo555.controller;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：jhys
 * @date ：Created in 2025/8/12 15:20
 * @Description ：
 */
@RestController
@RequestMapping("/api/process")
public class ProcessDeploymentController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/deploy")
    public String deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/approval-process.bpmn20.xml")
                .name("审批流程部署")
                .deploy();
        return "流程部署成功，ID: " + deployment.getId();
    }

    @PostMapping("/start")
    public String startProcess() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("approvalProcess");
        return "流程实例启动成功，ID: " + instance.getId();
    }

    @GetMapping("/list/{assignee}")
    public List<Task> getTasks(@PathVariable String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(@PathVariable String taskId) {
        taskService.complete(taskId);
        return "任务完成成功";
    }

}
