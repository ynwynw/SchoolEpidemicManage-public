package com.fanchen.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.utils.HttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanchen
 * @date 2021/12/19
 * @time 16:53
 */
@RestController
public class NewsController extends BaseController {

    @GetMapping("/news")
    public Result news(){
        try {
            String res = HttpUtils.sendGet("https://opendata.baidu.com/data/inner?tn=reserved_all_res_tn&dspName=iphone&from_sf=1&dsp=iphone&resource_id=28565&alr=1&query=%E5%9B%BD%E5%86%85%E6%96%B0%E5%9E%8B%E8%82%BA%E7%82%8E%E6%9C%80%E6%96%B0%E5%8A%A8%E6%80%81");
            JSONObject jsonObject = JSONUtil.parseObj(res);
            Object result = jsonObject.getByPath("Result[0].items_v2[0].aladdin_res.DisplayData.result.items");
            return Result.succ(result);
        }catch (Exception e){
            return Result.fail("最新疫情新闻获取失败");
        }
    }

    @GetMapping("/riskarea")
    public Result riskarea(){
        String res = HttpUtils.sendGet("https://m.sm.cn/api/rest?format=json&method=Huoshenshan.riskArea");
        JSONObject jsonObject = JSONUtil.parseObj(res);
        Object data = jsonObject.getByPath("data.map");
        return Result.succ(data);
    }

    @GetMapping("/history")
    public Result history(){
        String data;
        if (redisUtil.hasKey(Const.HISTORY_DAY_LIST)){
            data = (String) redisUtil.get(Const.HISTORY_DAY_LIST);
        }else {
            String res = HttpUtils.sendGet("https://c.m.163.com/ug/api/wuhan/app/data/list-total");
            JSONObject jsonObject = JSONUtil.parseObj(res);
            data = jsonObject.getByPath("data.chinaDayList").toString();
            redisUtil.set(Const.HISTORY_DAY_LIST, data, 1800);
        }
        return Result.succ(data);
    }

    @GetMapping("/infiniteNews")
    public Result infiniteNews(String id){
        String url = StrUtil.isNotBlank(id) ? "https://m.sm.cn/api/rest?format=json&method=Huoshenshan.feed&type=latest&id=" + id : "https://m.sm.cn/api/rest?format=json&method=Huoshenshan.feed&type=latest";
        String res = HttpUtils.sendGet(url);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        Object data = jsonObject.get("data");
        return Result.succ(data);
    }

    @GetMapping("/vaccineTopData")
    public Result vaccineTopData(){
        Object object;
        if (redisUtil.hasKey(Const.VaccineTopData)){
            object = redisUtil.get(Const.VaccineTopData);
        }else {
            String res = HttpUtils.sendGet("https://api.inews.qq.com/newsqa/v1/automation/modules/list?modules=VaccineTopData");
            JSONObject jsonObject = JSONUtil.parseObj(res);
            object = jsonObject.getByPath("data.VaccineTopData");
            redisUtil.set(Const.VaccineTopData, object, 1800);
        }
        return Result.succ(object);
    }

    @GetMapping("/chinaData")
    public Result data(){
        Object object;
        if (redisUtil.hasKey(Const.ChinaData)){
            object = redisUtil.get(Const.ChinaData);
        }else {
            String res = HttpUtils.sendGet("https://c.m.163.com/ug/api/wuhan/app/data/list-total");
            object = JSONUtils.parse(res);
            redisUtil.set(Const.ChinaData, object, 1800);
        }
        return Result.succ(object);
    }

    @GetMapping("/chinaVaccineTrendData")
    public Result chinaVaccineTrendData(){
        Object object;
        if (redisUtil.hasKey(Const.ChinaVaccineTrendData)){
            object = redisUtil.get(Const.ChinaVaccineTrendData);
        }else {
            String res = HttpUtils.sendGet("https://api.inews.qq.com/newsqa/v1/automation/modules/list?modules=ChinaVaccineTrendData");
            JSONObject jsonObject = JSONUtil.parseObj(res);
            object = jsonObject.getByPath("data.ChinaVaccineTrendData");
            redisUtil.set(Const.ChinaVaccineTrendData, object, 1800);
        }
        return Result.succ(object);
    }

    @GetMapping("/rumor")
    public Result rumor(String offset){
        String url = StrUtil.isNotBlank(offset) ? "https://c.m.163.com/ug/api/wuhan/app/article/list?limit=6&offset=" + offset : "https://c.m.163.com/ug/api/wuhan/app/article/list?limit=6";
        String res = HttpUtils.sendGet(url);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        Object object = jsonObject.getByPath("data.items");
        return Result.succ(object.toString());
    }

}
