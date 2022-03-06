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
package me.zhengjie.modules.el.requirement.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.el.balance.service.ElBalanceService;
import me.zhengjie.modules.el.balance.service.dto.ElBalanceDto;
import org.hibernate.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author zjdr
* @date 2022-02-19
**/
@Entity
@Data
@Table(name="el_requirement")
public class ElRequirement implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "merchant_id")
    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private Integer status =0;

    @Column(name = "count",nullable = false)
    @NotNull
    @ApiModelProperty(value = "数据量")
    private Integer count;

    @Column(name = "money",nullable = false)
    @NotNull
    @ApiModelProperty(value = "单价")
    private BigDecimal money;


    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name = "insert_time")
    @CreationTimestamp
    @ApiModelProperty(value = "储存时间")
    private Timestamp insertTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "type",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "类型")
    private String type;

    @Column(name = "now_conut")
    @ApiModelProperty(value = "剩余数量")
    private Integer nowConut;

    public void copy(ElRequirement source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}