package com.fanchen.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysDept;
import com.fanchen.entity.SysUser;
import com.fanchen.utils.UploadUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author fanchen
 */
@RestController
public class KaptchaController extends BaseController {

    @Resource
    private Producer producer;

    @GetMapping("/captcha")
    public Result captcha() {
        String key = UUID.randomUUID().toString().replace("-", "");
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String img = str + encoder.encode(os.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 60);
        return Result.succ(
                MapUtil.builder().put("base64", img).put("key", key).build()
        );
    }

    @GetMapping("/userInfo")
    public Result userInfo(Principal principal){
        SysUser sysUser = (SysUser) redisUtil.get("User:" + principal.getName());
        sysUser.setPassword("");
        SysDept sysDept;
        if (redisUtil.hasKey("DeptId:" + sysUser.getDeptId())){
            sysDept = (SysDept) redisUtil.get("DeptId:" + sysUser.getDeptId());
        }else {
            sysDept = sysDeptService.getById(sysUser.getDeptId());
            redisUtil.set("DeptId:" + sysUser.getDeptId(), sysDept, 3660);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", sysUser);
        map.put("dept", sysDept);
        return Result.succ(map);
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        if (!file.isEmpty()){
            String uploadImg = UploadUtil.uploadImg(file);
            if (StrUtil.isEmpty(uploadImg)) {
                return Result.fail("图片上传失败");
            }
            return Result.succ(Const.IMG_PATH + uploadImg);
        }
        return Result.fail("图片上传失败！");
    }

}
