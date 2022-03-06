/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.el.dispatch.order.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author zjdr
* @date 2022-02-23
**/
@Entity
@Data
@Table(name="el_dispatch_order")
public class ElDispatchOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @Column(name = "merchant_id")
    @ApiModelProperty(value = "商家id")
    private Integer merchantId;

    @Column(name = "insert_time")
    @CreationTimestamp
    @ApiModelProperty(value = "储存时间")
    private Timestamp insertTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "img")
    @ApiModelProperty(value = "图片")
    private String img;

    @Column(name = "dow_img")
    @ApiModelProperty(value = "图片1")
    private String dowImg;


    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @Column(name = "money",nullable = false)
    @NotNull
    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "姓名")
    private String name;

    @Column(name = "account",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "账号")
    private String account;

    @Column(name = "location")
    @ApiModelProperty(value = "地址")
    private String location;

    @Column(name = "make")
    @ApiModelProperty(value = "备注")
    private String make;

    public void copy(ElDispatchOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}