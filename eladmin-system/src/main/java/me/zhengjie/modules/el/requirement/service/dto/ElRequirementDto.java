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
package me.zhengjie.modules.el.requirement.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author zjdr
* @date 2022-02-19
**/
@Data
public class ElRequirementDto implements Serializable {

    /** id */
    private Long id;

    /** 商家id */
    private Long merchantId;

    /** 状态 */
    private Integer status;

    /** 数据量 */
    private Integer count;

    /** 单价 */
    private BigDecimal money;

    /** 储存时间 */
    private Timestamp insertTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 类型 */
    private String type;

    /** 剩余数量 */
    private Integer nowConut;
}