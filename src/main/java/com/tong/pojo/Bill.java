package com.tong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    private int id;
    private  String billCode;
    private  String productName;
    private String productDesc;
    private String productUnit;
    private BigDecimal productCount;
    private BigDecimal totalPrice;
    private int isPayment;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
    private int providerId;//供应商ID
    private String providerName;//供应商名称
}
