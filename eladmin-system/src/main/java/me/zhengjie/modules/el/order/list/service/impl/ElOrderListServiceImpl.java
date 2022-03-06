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
package me.zhengjie.modules.el.order.list.service.impl;

import me.zhengjie.modules.el.order.list.domain.ElOrderList;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.el.order.list.repository.ElOrderListRepository;
import me.zhengjie.modules.el.order.list.service.ElOrderListService;
import me.zhengjie.modules.el.order.list.service.dto.ElOrderListDto;
import me.zhengjie.modules.el.order.list.service.dto.ElOrderListQueryCriteria;
import me.zhengjie.modules.el.order.list.service.mapstruct.ElOrderListMapper;
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
public class ElOrderListServiceImpl implements ElOrderListService {

    private final ElOrderListRepository elOrderListRepository;
    private final ElOrderListMapper elOrderListMapper;

    @Override
    public Map<String,Object> queryAll(ElOrderListQueryCriteria criteria, Pageable pageable){
        Page<ElOrderList> page = elOrderListRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(elOrderListMapper::toDto));
    }

    @Override
    public List<ElOrderListDto> queryAll(ElOrderListQueryCriteria criteria){
        return elOrderListMapper.toDto(elOrderListRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ElOrderListDto findById(Long id) {
        ElOrderList elOrderList = elOrderListRepository.findById(id).orElseGet(ElOrderList::new);
        ValidationUtil.isNull(elOrderList.getId(),"ElOrderList","id",id);
        return elOrderListMapper.toDto(elOrderList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElOrderListDto create(ElOrderList resources) {
        return elOrderListMapper.toDto(elOrderListRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ElOrderList resources) {
        ElOrderList elOrderList = elOrderListRepository.findById(resources.getId()).orElseGet(ElOrderList::new);
        ValidationUtil.isNull( elOrderList.getId(),"ElOrderList","id",resources.getId());
        elOrderList.copy(resources);
        elOrderListRepository.save(elOrderList);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            elOrderListRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ElOrderListDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElOrderListDto elOrderList : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", elOrderList.getUserId());
            map.put("商家id", elOrderList.getMerchantId());
            map.put("金额", elOrderList.getMoney());
            map.put("手机号", elOrderList.getMobile());
            map.put("姓名", elOrderList.getName());
            map.put("地址", elOrderList.getAddress());
            map.put("类型", elOrderList.getType());
            map.put("图片", elOrderList.getImg());
            map.put("图片MD5", elOrderList.getImgMd5());
            map.put("储存时间", elOrderList.getInsertTime());
            map.put("更新时间", elOrderList.getUpdateTime());
            map.put("状态", elOrderList.getStatus());
            map.put("是否删除", elOrderList.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public ElOrderList findByMobile(String mobile) {
        return elOrderListRepository.findByMobile(mobile);
    }
}