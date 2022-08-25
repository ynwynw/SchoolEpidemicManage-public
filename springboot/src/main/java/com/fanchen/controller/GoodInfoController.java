package com.fanchen.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.GoodInfo;
import com.fanchen.utils.UploadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 物资信息表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-10
 */
@RestController
@RequestMapping("/good/info")
public class GoodInfoController extends BaseController<GoodInfo> {

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('good:info:list')")
    public Result all(){
        QueryWrapper<GoodInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "unit", "size", "total");
        List<GoodInfo> list = goodInfoService.list(wrapper);
        return Result.succ(list);
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('good:info:list')")
    public Result total(String name, Long typeId) {
        QueryWrapper<GoodInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);
        wrapper.eq(typeId != null, "type_id", typeId);
        wrapper.select("id", "name", "unit", "size", "total");
        Page<GoodInfo> page = goodInfoService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('good:info:list')")
    public Result list(String name, String createBy, Integer status, Long typeId) {
        QueryWrapper<GoodInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);
        wrapper.like(StrUtil.isNotBlank(createBy), "create_by", createBy);
        wrapper.eq(status != null, "status", status);
        wrapper.eq(typeId != null, "type_id", typeId);
        Page<GoodInfo> page = goodInfoService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "物资资料", businessType = "添加物资")
    @PreAuthorize("hasAnyAuthority('good:info:save')")
    public Result save(@RequestParam("img") MultipartFile file, @RequestParam("goodInfo") String json, Principal principal) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GoodInfo goodInfo = mapper.readValue(json, GoodInfo.class);
        if (!file.isEmpty()) {
            String uploadImg = UploadUtil.uploadImg(file);
            if (StrUtil.isEmpty(uploadImg)) {
                return Result.fail("图片上传失败");
            }
            goodInfo.setImg(Const.IMG_PATH + uploadImg);
        }
        goodInfo.setCreateBy(principal.getName());
        boolean save = goodInfoService.save(goodInfo);
        if (save) {
            return Result.succ("新增成功");
        } else {
            return Result.fail("新增失败");
        }
    }

    @PutMapping
    @Log(title = "物资资料", businessType = "修改物资")
    @PreAuthorize("hasAnyAuthority('good:info:update')")
    public Result update(@RequestParam(value = "img", required = false) MultipartFile file, @RequestParam("goodInfo") String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GoodInfo goodInfo = mapper.readValue(json, GoodInfo.class);
        if (file != null && !file.isEmpty()) {
            String uploadImg = UploadUtil.uploadImg(file);
            if (StrUtil.isEmpty(uploadImg)) {
                return Result.fail("图片上传失败");
            }
            goodInfo.setImg(Const.IMG_PATH + uploadImg);
        }
        boolean update = goodInfoService.updateById(goodInfo);
        if (update) {
            return Result.succ("修改成功");
        } else {
            return Result.fail("修改失败");
        }
    }

    @DeleteMapping
    @Log(title = "物资资料", businessType = "删除物资")
    @PreAuthorize("hasAnyAuthority('good:info:delete')")
    public Result delete(Long[] ids) {
        boolean remove = goodInfoService.removeByIds(Arrays.asList(ids));
        if (remove) {
            return Result.succ("删除成功！");
        } else {
            return Result.fail("删除成功！");
        }
    }

}

