package com.fanchen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 打卡健康表
 * </p>
 *
 * @author fanchen
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HealthClock对象", description="打卡健康表")
public class HealthClock implements Serializable {

    private static final long serialVersionUID = 529661106L;

    @ApiModelProperty(value = "打卡id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不可以为空")
    private String username;

    @ApiModelProperty(value = "健康状况")
    @NotNull(message = "健康状况不可以为空")
    private Integer healthType;

    @ApiModelProperty(value = "温度")
    @NotNull(message = "温度不可以为空")
    private Float temperature;

    @ApiModelProperty(value = "中高风险")
    @NotNull(message = "中高风险不可以为空")
    private Integer middleHigh;

    @ApiModelProperty(value = "确诊")
    @NotNull(message = "确诊不可以为空")
    private Integer diagnosis;

    @ApiModelProperty(value = "境外返回")
    @NotNull(message = "境外返回不可以为空")
    private Integer returnInfo;

    @ApiModelProperty(value = "地址")
    @NotBlank(message = "打卡位置不可为空")
    private String address;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "部门id")
    @NotNull(message = "所属部门不可为空")
    private Integer deptId;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDelete;


}
