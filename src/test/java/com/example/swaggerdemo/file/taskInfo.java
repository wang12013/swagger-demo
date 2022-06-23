package com.example.swaggerdemo.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class taskInfo {
        private String taskId;
        private String taskName;
}