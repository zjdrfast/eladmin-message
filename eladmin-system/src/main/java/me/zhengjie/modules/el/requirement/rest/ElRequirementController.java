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
package me.zhengjie.modules.el.requirement.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.el.balance.service.ElBalanceService;
import me.zhengjie.modules.el.requirement.domain.ElRequirement;
import me.zhengjie.modules.el.requirement.service.ElRequirementService;
import me.zhengjie.modules.el.requirement.service.dto.ElRequirementQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author zjdr
* @date 2022-02-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "需求列表管理")
@RequestMapping("/api/elRequirement")
public class ElRequirementController {

    private final ElRequirementService elRequirementService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('elRequirement:list')")
    public void exportElRequirement(HttpServletResponse response, ElRequirementQueryCriteria criteria) throws IOException {
        elRequirementService.download(elRequirementService.queryAll(criteria), response);
    }


    private UserService userService;

    @GetMapping
    @Log("查询需求列表")
    @ApiOperation("查询需求列表")
    @PreAuthorize("@el.check('elRequirement:list')")
    public ResponseEntity<Object> queryElRequirement(ElRequirementQueryCriteria criteria, Pageable pageable){
            return new ResponseEntity<>(elRequirementService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增需求列表")
    @ApiOperation("新增需求列表")
    @PreAuthorize("@el.check('elRequirement:add')")
    public ResponseEntity<Object> createElRequirement(@Validated @RequestBody ElRequirement resources){
        resources.setMerchantId(SecurityUtils.getCurrentUserId());
        resources.setStatus(0);
        resources.setNowConut(resources.getCount());
        return new ResponseEntity<>(elRequirementService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改需求列表")
    @ApiOperation("修改需求列表")
    @PreAuthorize("@el.check('elRequirement:edit')")
    public ResponseEntity<Object> updateElRequirement(@Validated @RequestBody ElRequirement resources){
        resources.setNowConut(resources.getCount());
        elRequirementService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除需求列表")
    @ApiOperation("删除需求列表")
    @PreAuthorize("@el.check('elRequirement:del')")
    public ResponseEntity<Object> deleteElRequirement(@RequestBody Long[] ids) {
        elRequirementService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}