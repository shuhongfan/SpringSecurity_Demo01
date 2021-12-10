package com.shf.securitydemo01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users implements Serializable {
    private Integer id;
    private String username;
    private String password;
}
