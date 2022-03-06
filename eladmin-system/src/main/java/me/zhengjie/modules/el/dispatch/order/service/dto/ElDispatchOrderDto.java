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
package me.zhengjie.modules.el.dispatch.order.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author zjdr
* @date 2022-02-23
**/
@Data
public class ElDispatchOrderDto implements Serializable {

    /** id */
    private Integer id;

    /** 用户id */
    private Integer userId;

    /** 商家id */
    private Integer merchantId;

    /** 储存时间 */
    private Timestamp insertTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 图片 */
    private String img;

    /** 状态 */
    private Integer status;


    private String dowImg;

    /** 金额 */
    private BigDecimal money;

    /** 姓名 */
    private String name;

    /** 账号 */
    private String account;

    /** 地址 */
    private String location;

    /** 备注 */
    private String make;
}