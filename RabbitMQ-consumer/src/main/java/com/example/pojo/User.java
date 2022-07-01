package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangzy
 * @date 2022/6/27 15:16
 *
 * 实现了Serializable的才能被序列化
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String name;
    private String age;
    private Double altitude;
}
