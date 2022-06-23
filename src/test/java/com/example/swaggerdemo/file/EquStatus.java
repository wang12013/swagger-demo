package com.example.swaggerdemo.file;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author wangzy
 * @date 2022/6/21 11:07
 */
@Data
@AllArgsConstructor
public class EquStatus {

    private Long orgId;
    private String equName;
    private String status;
    private List<taskInfo> taskList;


}
