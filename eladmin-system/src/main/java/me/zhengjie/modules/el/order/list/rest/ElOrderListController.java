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
package me.zhengjie.modules.el.order.list.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.el.order.list.domain.ElOrderList;
import me.zhengjie.modules.el.order.list.service.ElOrderListService;
import me.zhengjie.modules.el.order.list.service.dto.ElOrderListQueryCriteria;
import me.zhengjie.modules.el.requirement.domain.ElRequirement;
import me.zhengjie.modules.el.requirement.service.ElRequirementService;
import me.zhengjie.modules.el.requirement.service.dto.ElRequirementDto;
import me.zhengjie.modules.el.requirement.service.dto.ElRequirementQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author zjdr
* @date 2022-02-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "订单列表管理")
@RequestMapping("/api/elOrderList")
public class ElOrderListController {

    private final ElOrderListService elOrderListService;

    private final UserService userService;

    private final ElRequirementService elRequirementService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('elOrderList:list')")
    public void exportElOrderList(HttpServletResponse response, ElOrderListQueryCriteria criteria) throws IOException {
        if (SecurityUtils.getUserLevel()>=4){
            criteria.setUserId(SecurityUtils.getCurrentUserId());
        }
        elOrderListService.download(elOrderListService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询订单列表")
    @ApiOperation("查询订单列表")
    @PreAuthorize("@el.check('elOrderList:list')")
    public ResponseEntity<Object> queryElOrderList(ElOrderListQueryCriteria criteria, Pageable pageable){
        if (SecurityUtils.getUserLevel()>=3){
            criteria.setUserId(SecurityUtils.getCurrentUserId());
        }
        return new ResponseEntity<>(elOrderListService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @PostMapping
    @Log("新增订单列表")
    @ApiOperation("新增订单列表")
    @PreAuthorize("@el.check('elOrderList:add')")
    public ResponseEntity<Object> createElOrderList(@Validated @RequestBody ElOrderList resources){
        ElRequirementQueryCriteria elRequirementQueryCriteria = new ElRequirementQueryCriteria();
        elRequirementQueryCriteria.setStatus(0);
        List<ElRequirementDto> elRequirementDtos = elRequirementService.queryAll(elRequirementQueryCriteria);


        if (elRequirementDtos==null || elRequirementDtos.size()==0){
            throw new BadRequestException("没有需求,暂时无法提交!");
        }
        if (resources.getMobile().length()!=11){
            throw new BadRequestException("手机格式错误!");
        }
        ElOrderList byMobile = elOrderListService.findByMobile(resources.getMobile());
        if (byMobile!=null){
            throw new BadRequestException("手机号重复!");
        }

        ElRequirementDto elRequirementDto = elRequirementDtos.get(0);

        Integer now = elRequirementDto.getNowConut()-1;
        if (now==0){
            elRequirementDto.setStatus(1);
        }

        List<User> userDtoList = userService.findByUpId(SecurityUtils.getCurrentUserId());
//        if (!userService.CheckMomey(userDtoList))
//            throw new BadRequestException("费率溢出,请联系管理员重新设置!");
        for (User userdto: userDtoList) {
            //设置金额
            BigDecimal nowMoney = userdto.getRate().add(userdto.getMoney());
            userdto.setMoney(nowMoney);
            //修改金额
            userService.updateList(userDtoList);
        }
        elRequirementDto.setNowConut(now);
        ElRequirement elRequirement = new ElRequirement();
        elRequirement.setNowConut(elRequirementDto.getNowConut());
        elRequirement.setStatus(elRequirementDto.getStatus());
        elRequirement.setId(elRequirementDto.getId());

        elRequirementService.update(elRequirement);

        resources.setMerchantId(elRequirementDto.getMerchantId());
        resources.setUserId(SecurityUtils.getCurrentUserId());
        resources.setStatus(0);
        resources.setMoney(elRequirementDto.getMoney());

        return new ResponseEntity<>(elOrderListService.create(resources),HttpStatus.CREATED);
    }


    @PutMapping
    @Log("修改订单列表")
    @ApiOperation("修改订单列表")
    @PreAuthorize("@el.check('elOrderList:edit')")
    public ResponseEntity<Object> updateElOrderList(@Validated @RequestBody ElOrderList resources){
        elOrderListService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除订单列表")
    @ApiOperation("删除订单列表")
    @PreAuthorize("@el.check('elOrderList:del')")
    public ResponseEntity<Object> deleteElOrderList(@RequestBody Long[] ids) {
        elOrderListService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}