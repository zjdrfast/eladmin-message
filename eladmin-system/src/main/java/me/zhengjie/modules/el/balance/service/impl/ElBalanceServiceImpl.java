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
package me.zhengjie.modules.el.balance.service.impl;

import me.zhengjie.modules.el.balance.domain.ElBalance;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.el.balance.repository.ElBalanceRepository;
import me.zhengjie.modules.el.balance.service.ElBalanceService;
import me.zhengjie.modules.el.balance.service.dto.ElBalanceDto;
import me.zhengjie.modules.el.balance.service.dto.ElBalanceQueryCriteria;
import me.zhengjie.modules.el.balance.service.mapstruct.ElBalanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
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
public class ElBalanceServiceImpl implements ElBalanceService {

    private final ElBalanceRepository elBalanceRepository;
    private final ElBalanceMapper elBalanceMapper;

    @Override
    public Map<String,Object> queryAll(ElBalanceQueryCriteria criteria, Pageable pageable){
        Page<ElBalance> page = elBalanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(elBalanceMapper::toDto));
    }

    @Override
    public List<ElBalanceDto> queryAll(ElBalanceQueryCriteria criteria){
        return elBalanceMapper.toDto(elBalanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ElBalanceDto findById(Long id) {
        ElBalance elBalance = elBalanceRepository.findById(id).orElseGet(ElBalance::new);
        ValidationUtil.isNull(elBalance.getId(),"ElBalance","id",id);
        return elBalanceMapper.toDto(elBalance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElBalanceDto create(ElBalance resources) {
        return elBalanceMapper.toDto(elBalanceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ElBalance resources) {
        ElBalance elBalance = elBalanceRepository.findById(resources.getId()).orElseGet(ElBalance::new);
        ValidationUtil.isNull( elBalance.getId(),"ElBalance","id",resources.getId());
        elBalance.copy(resources);
        elBalanceRepository.save(elBalance);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            elBalanceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ElBalanceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElBalanceDto elBalance : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("上级id", elBalance.getLeadId());
            map.put("用户id", elBalance.getUserId());
            map.put("冻结金额", elBalance.getFreezeMoey());
            map.put("可用金额", elBalance.getMoney());
            map.put("储存时间", elBalance.getInsertTime());
            map.put("更新时间", elBalance.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}