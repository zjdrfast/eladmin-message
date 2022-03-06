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
package me.zhengjie.modules.el.dispatch.order.service.impl;

import me.zhengjie.modules.el.dispatch.order.domain.ElDispatchOrder;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.el.dispatch.order.repository.ElDispatchOrderRepository;
import me.zhengjie.modules.el.dispatch.order.service.ElDispatchOrderService;
import me.zhengjie.modules.el.dispatch.order.service.dto.ElDispatchOrderDto;
import me.zhengjie.modules.el.dispatch.order.service.dto.ElDispatchOrderQueryCriteria;
import me.zhengjie.modules.el.dispatch.order.service.mapstruct.ElDispatchOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.math.BigDecimal;
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
* @date 2022-02-23
**/
@Service
@RequiredArgsConstructor
public class ElDispatchOrderServiceImpl implements ElDispatchOrderService {

    private final ElDispatchOrderRepository elDispatchOrderRepository;
    private final ElDispatchOrderMapper elDispatchOrderMapper;

    @Override
    public Map<String,Object> queryAll(ElDispatchOrderQueryCriteria criteria, Pageable pageable){
        Page<ElDispatchOrder> page = elDispatchOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(elDispatchOrderMapper::toDto));
    }

    @Override
    public List<ElDispatchOrderDto> queryAll(ElDispatchOrderQueryCriteria criteria){
        return elDispatchOrderMapper.toDto(elDispatchOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ElDispatchOrderDto findById(Integer id) {
        ElDispatchOrder elDispatchOrder = elDispatchOrderRepository.findById(id).orElseGet(ElDispatchOrder::new);
        ValidationUtil.isNull(elDispatchOrder.getId(),"ElDispatchOrder","id",id);
        return elDispatchOrderMapper.toDto(elDispatchOrder);
    }

    @Override
    public BigDecimal findByIdMoney(Integer id) {
        List<ElDispatchOrder> byAndUserIdAndStatus = elDispatchOrderRepository.findByAndUserIdAndStatus(id, 0);
        BigDecimal money = new BigDecimal("0");
        for (ElDispatchOrder elDispatchOrder: byAndUserIdAndStatus) {
            money = elDispatchOrder.getMoney().add(money);
        }
        return money;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElDispatchOrderDto create(ElDispatchOrder resources) {
        return elDispatchOrderMapper.toDto(elDispatchOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ElDispatchOrder resources) {
        ElDispatchOrder elDispatchOrder = elDispatchOrderRepository.findById(resources.getId()).orElseGet(ElDispatchOrder::new);
        ValidationUtil.isNull( elDispatchOrder.getId(),"ElDispatchOrder","id",resources.getId());
        elDispatchOrder.copy(resources);
        elDispatchOrderRepository.save(elDispatchOrder);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            elDispatchOrderRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ElDispatchOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElDispatchOrderDto elDispatchOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", elDispatchOrder.getUserId());
            map.put("商家id", elDispatchOrder.getMerchantId());
            map.put("储存时间", elDispatchOrder.getInsertTime());
            map.put("更新时间", elDispatchOrder.getUpdateTime());
            map.put("图片", elDispatchOrder.getImg());
            map.put("状态", elDispatchOrder.getStatus());
            map.put("金额", elDispatchOrder.getMoney());
            map.put("姓名", elDispatchOrder.getName());
            map.put("账号", elDispatchOrder.getAccount());
            map.put("地址", elDispatchOrder.getLocation());
            map.put("备注", elDispatchOrder.getMake());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}