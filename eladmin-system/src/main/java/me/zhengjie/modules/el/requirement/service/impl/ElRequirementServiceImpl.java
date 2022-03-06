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
package me.zhengjie.modules.el.requirement.service.impl;

import me.zhengjie.modules.el.requirement.domain.ElRequirement;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.el.requirement.repository.ElRequirementRepository;
import me.zhengjie.modules.el.requirement.service.ElRequirementService;
import me.zhengjie.modules.el.requirement.service.dto.ElRequirementDto;
import me.zhengjie.modules.el.requirement.service.dto.ElRequirementQueryCriteria;
import me.zhengjie.modules.el.requirement.service.mapstruct.ElRequirementMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author zjdr
* @date 2022-02-19
**/
@Service
@RequiredArgsConstructor
public class ElRequirementServiceImpl implements ElRequirementService {

    private final ElRequirementRepository elRequirementRepository;
    private final ElRequirementMapper elRequirementMapper;
    private final UserService userService;
    @Override
    public Map<String,Object> queryAll(ElRequirementQueryCriteria criteria, Pageable pageable){
        Page<ElRequirement> page = elRequirementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);

        Integer userLevel = SecurityUtils.getUserLevel();
        if (userLevel>2) {
            //计算动态费率
            User user = userService.findById1(SecurityUtils.getCurrentUserId());
            for (ElRequirement elRequirement: page.getContent() ) {
                elRequirement.setMoney(user.getRate().multiply(elRequirement.getMoney()));
            }
            return PageUtil.toPage(page.map(elRequirementMapper::toDto));
        }
        return PageUtil.toPage(page.map(elRequirementMapper::toDto));
    }

    @Override
    public List<ElRequirementDto> queryAll(ElRequirementQueryCriteria criteria){
        return elRequirementMapper.toDto(elRequirementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ElRequirementDto findById(Long id) {
        ElRequirement elRequirement = elRequirementRepository.findById(id).orElseGet(ElRequirement::new);
        ValidationUtil.isNull(elRequirement.getId(),"ElRequirement","id",id);
        return elRequirementMapper.toDto(elRequirement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElRequirementDto create(ElRequirement resources) {
        return elRequirementMapper.toDto(elRequirementRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ElRequirement resources) {
        ElRequirement elRequirement = elRequirementRepository.findById(resources.getId()).orElseGet(ElRequirement::new);
        ValidationUtil.isNull( elRequirement.getId(),"ElRequirement","id",resources.getId());
        elRequirement.copy(resources);
        elRequirementRepository.save(elRequirement);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            elRequirementRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ElRequirementDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElRequirementDto elRequirement : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商家id", elRequirement.getMerchantId());
            map.put("状态", elRequirement.getStatus());
            map.put("数据量", elRequirement.getCount());
            map.put("单价", elRequirement.getMoney());
            map.put("储存时间", elRequirement.getInsertTime());
            map.put("更新时间", elRequirement.getUpdateTime());
            map.put("类型", elRequirement.getType());
            map.put("剩余数量", elRequirement.getNowConut());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}