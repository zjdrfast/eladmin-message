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
package me.zhengjie.modules.el.balance.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.el.balance.domain.ElBalance;
import me.zhengjie.modules.el.balance.service.ElBalanceService;
import me.zhengjie.modules.el.balance.service.dto.ElBalanceQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author zjdr
* @date 2022-02-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "用户余额管理")
@RequestMapping("/api/elBalance")
public class ElBalanceController {

    private final ElBalanceService elBalanceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('elBalance:list')")
    public void exportElBalance(HttpServletResponse response, ElBalanceQueryCriteria criteria) throws IOException {
        elBalanceService.download(elBalanceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户余额")
    @ApiOperation("查询用户余额")
    @PreAuthorize("@el.check('elBalance:list')")
    public ResponseEntity<Object> queryElBalance(ElBalanceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(elBalanceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户余额")
    @ApiOperation("新增用户余额")
    @PreAuthorize("@el.check('elBalance:add')")
    public ResponseEntity<Object> createElBalance(@Validated @RequestBody ElBalance resources){
        return new ResponseEntity<>(elBalanceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户余额")
    @ApiOperation("修改用户余额")
    @PreAuthorize("@el.check('elBalance:edit')")
    public ResponseEntity<Object> updateElBalance(@Validated @RequestBody ElBalance resources){
        elBalanceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户余额")
    @ApiOperation("删除用户余额")
    @PreAuthorize("@el.check('elBalance:del')")
    public ResponseEntity<Object> deleteElBalance(@RequestBody Long[] ids) {
        elBalanceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}