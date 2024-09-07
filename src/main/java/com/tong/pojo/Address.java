package com.tong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private int id;
    private  String contact;
    private  String addressDesc;
    private String postcode;
    private String tel;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
    private int userId;
}
