package com.tong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private int id;
    private String  roleCode;
    private String roleName;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
}
