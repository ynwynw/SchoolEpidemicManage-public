package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.dto.GoodDto;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.GoodInfo;
import com.fanchen.entity.GoodStock;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物资出入库表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-11
 */
@RestController
@RequestMapping("/good/stock")
public class GoodStockController extends BaseController<GoodStock> {

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('good:stock:list')")
    public Result list(String accept, Integer operateType, String start, String end) {
        LambdaQueryWrapper<GoodStock> wrapper = Wrappers.lambdaQuery(GoodStock.class);
        wrapper.like(StrUtil.isNotBlank(accept), GoodStock::getAccept, accept);
        wrapper.eq(operateType != null, GoodStock::getOperateType, operateType);
        if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
            DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            wrapper.between(GoodStock::getCreateTime, a, b);
        }
        wrapper.orderByDesc(GoodStock::getCreateTime);
        Page<GoodStock> page = goodStockService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "物资库存", businessType = "物资出入库")
    @PreAuthorize("hasAnyAuthority('good:stock:operate')")
    public Result save(@Validated @RequestBody GoodStock goodStock, Principal principal) {
        List<GoodDto> list = goodStock.getList();
        List<GoodStock> goodStockList = new ArrayList<>();
        list.forEach(goodDto -> {
            GoodStock stock = new GoodStock();
            stock.setCreateBy(principal.getName());
            stock.setAccept(goodStock.getAccept());
            stock.setGoodName(goodDto.getGoodName());
            stock.setGoodNum(goodDto.getGoodNum());
            stock.setGoodSize(goodDto.getGoodSize());
            stock.setPeopleName(goodStock.getPeopleName());
            stock.setPeoplePhone(goodStock.getPeoplePhone());
            stock.setOperateType(goodStock.getOperateType());
            stock.setRemark(goodStock.getRemark());
            goodStockList.add(stock);
            GoodInfo goodInfo = goodInfoService.getById(goodDto.getId());
            int res;
            if (goodStock.getOperateType() == 0) {
                res = goodInfo.getTotal() + goodDto.getGoodNum();
            } else {
                res = goodInfo.getTotal() - goodDto.getGoodNum();
            }
            goodInfo.setTotal(res);
            goodInfoService.updateById(goodInfo);
        });
        boolean batch = goodStockService.saveBatch(goodStockList);
        if (batch) {
            return Result.succ("操作成功！");
        } else {
            return Result.fail("操作失败！");
        }
    }

}

