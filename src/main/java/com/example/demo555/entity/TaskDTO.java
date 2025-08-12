package com.example.demo555.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String id;
    private String name;

    public TaskDTO(String id, String name, Date startTime, Date endTime, String processInstanceId) {
    }
}
