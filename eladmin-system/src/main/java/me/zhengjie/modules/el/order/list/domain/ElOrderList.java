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
package me.zhengjie.modules.el.order.list.domain;

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
@Table(name="el_order_list")
public class ElOrderList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @Column(name = "merchant_id")
    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @Column(name = "money")
    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @Column(name = "mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @Column(name = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    @Column(name = "address")
    @ApiModelProperty(value = "地址")
    private String address;

    @Column(name = "type")
    @ApiModelProperty(value = "类型")
    private String type;
    @Column(name = "express_type")
    @ApiModelProperty(value = "快递类型")
    private String expressType;

    @Column(name = "img")
    @ApiModelProperty(value = "图片")
    private String img;

    @Column(name = "img_md5")
    @ApiModelProperty(value = "图片MD5")
    private String imgMd5;

    @Column(name = "insert_time")
    @CreationTimestamp
    @ApiModelProperty(value = "储存时间")
    private Timestamp insertTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private Integer status = 0;

    @Column(name = "is_delete")
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete = 0;

    public void copy(ElOrderList source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}