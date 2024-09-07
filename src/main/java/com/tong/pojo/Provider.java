package com.tong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    private int id;
    private  String proCode;
    private  String proName;
    private String proDesc;
    private String proContact;
    private String proPhone;
    private String proAddress;
    private String proFax;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
}
