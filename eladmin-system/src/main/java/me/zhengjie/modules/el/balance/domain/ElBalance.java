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
package me.zhengjie.modules.el.balance.domain;

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
* @date 2022-02-19
**/
@Entity
@Data
@Table(name="el_balance")
public class ElBalance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "lead_id")
    @ApiModelProperty(value = "上级id")
    private Long leadId;

    @Column(name = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @Column(name = "freeze_moey")
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeMoey;

    @Column(name = "ratio")
    @ApiModelProperty(value = "费率")
    private BigDecimal ratio;

    @Column(name = "money")
    @ApiModelProperty(value = "可用金额")
    private BigDecimal money;

    @Column(name = "insert_time")
    @CreationTimestamp
    @ApiModelProperty(value = "储存时间")
    private Timestamp insertTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(ElBalance source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}