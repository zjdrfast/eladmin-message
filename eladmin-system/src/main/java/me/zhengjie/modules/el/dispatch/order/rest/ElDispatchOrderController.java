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
package me.zhengjie.modules.el.dispatch.order.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.el.dispatch.order.domain.ElDispatchOrder;
import me.zhengjie.modules.el.dispatch.order.service.ElDispatchOrderService;
import me.zhengjie.modules.el.dispatch.order.service.dto.ElDispatchOrderDto;
import me.zhengjie.modules.el.dispatch.order.service.dto.ElDispatchOrderQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author zjdr
* @date 2022-02-23
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "下发列表管理")
@RequestMapping("/api/elDispatchOrder")
public class ElDispatchOrderController {

    private final ElDispatchOrderService elDispatchOrderService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('elDispatchOrder:list')")
    public void exportElDispatchOrder(HttpServletResponse response, ElDispatchOrderQueryCriteria criteria) throws IOException {
        if (SecurityUtils.getUserLevel()>=3){
            criteria.setUserId(SecurityUtils.getCurrentUserId().intValue());
        }
        elDispatchOrderService.download(elDispatchOrderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询下发列表")
    @ApiOperation("查询下发列表")
    @PreAuthorize("@el.check('elDispatchOrder:list')")
    public ResponseEntity<Object> queryElDispatchOrder(ElDispatchOrderQueryCriteria criteria, Pageable pageable){
        if (SecurityUtils.getUserLevel()>=3){
            criteria.setUserId(SecurityUtils.getCurrentUserId().intValue());
        }
        return new ResponseEntity<>(elDispatchOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    private final UserService userService;


    @PostMapping
    @Log("新增下发列表")
    @ApiOperation("新增下发列表")
    @PreAuthorize("@el.check('elDispatchOrder:add')")
    public ResponseEntity<Object> createElDispatchOrder(@Validated @RequestBody ElDispatchOrder resources){
        BigDecimal Money = elDispatchOrderService.findByIdMoney(resources.getId());
        if (resources.getMoney()!=null){
            User user = userService.findById1(SecurityUtils.getCurrentUserId());
            if (user.getMoney().subtract(Money.add(resources.getMoney())).signum()== -1)
                throw new BadRequestException("超过提现金额!");
            user.setId(user.getId());
            user.setMoney(user.getMoney().subtract(resources.getMoney()));
            resources.setUserId(user.getId().intValue());
            resources.setStatus(0);
            try {
                userService.updateMoney(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new BadRequestException("金额不能是空!");
        }
        return new ResponseEntity<>(elDispatchOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改下发列表")
    @ApiOperation("修改下发列表")
    @PreAuthorize("@el.check('elDispatchOrder:edit')")
    public ResponseEntity<Object> updateElDispatchOrder(@Validated @RequestBody ElDispatchOrder resources){
        elDispatchOrderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping
    @Log("修改下发列表状态")
    @ApiOperation("修改下发列表状态")
    @PreAuthorize("@el.check('elDispatchOrder:editStatus')")
    @RequestMapping("/editStatus")
    public ResponseEntity<Object> editStatus(@Validated @RequestBody ElDispatchOrder resources){
        ElDispatchOrderDto elDispatchOrder = elDispatchOrderService.findById(resources.getId());
        if (elDispatchOrder.getStatus()==0){
            ElDispatchOrder elDispatchOrder1 = new ElDispatchOrder();
            elDispatchOrder1.setStatus(1);
            elDispatchOrder1.setId(resources.getId());
            elDispatchOrder1.setMerchantId(SecurityUtils.getCurrentUserId().intValue());
            elDispatchOrderService.update(elDispatchOrder1);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除下发列表")
    @ApiOperation("删除下发列表")
    @PreAuthorize("@el.check('elDispatchOrder:del')")
    public ResponseEntity<Object> deleteElDispatchOrder(@RequestBody Integer[] ids) {
        elDispatchOrderService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}