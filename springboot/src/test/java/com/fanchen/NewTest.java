package com.fanchen;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fanchen.utils.HttpUtils;

/**
 * @author fanchen
 * @date 2021/12/19
 * @time 16:41
 */
public class NewTest {
    public static void main(String[] args) {
        String res = HttpUtils.sendGet("https://c.m.163.com/ug/api/wuhan/app/data/list-total");
        JSONObject jsonObject = JSONUtil.parseObj(res);
        Object chinaTotal = jsonObject.getByPath("data.chinaTotal");
        Object chinaToday = jsonObject.getByPath("data.chinaDayList[-1]");
        Object children = jsonObject.getByPath("data.areaTree[2].children");
        Object lastUpdateTime = jsonObject.getByPath("data.lastUpdateTime");
        System.out.println(chinaTotal);
    }
}
