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
 * 请假审批表
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LeaveApply对象", description="请假审批表")
public class LeaveApply implements Serializable {

    private static final long serialVersionUID = 954313152L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "请假原因")
    @NotBlank(message = "请假信息不能为空")
    private String reason;

    @ApiModelProperty(value = "请假类型（1：事假 2：病假）")
    @NotNull(message = "请假类型不能为空")
    private Integer leaveType;

    @ApiModelProperty(value = "状态（0：撤销 1：待审核 2：审核通过 3：审核不通过）")
    private Integer status;

    @ApiModelProperty(value = "学生类型（1：本科生 2：研究生 3：博士生）")
    @NotNull(message = "学生类型不能为空")
    private Integer studentType;

    @ApiModelProperty(value = "学生姓名")
    private String nickname;

    @ApiModelProperty(value = "请假时间区间")
    @NotBlank(message = "请假时间区间不能为空")
    private String time;

    @ApiModelProperty(value = "请假天数")
    @NotBlank(message = "请假天数不能为空")
    private String day;

    @ApiModelProperty(value = "目的地")
    @NotBlank(message = "目的地不能为空")
    private String address;

    @ApiModelProperty(value = "交通工具")
    @NotBlank(message = "交通工具不能为空")
    private String traffic;

    @ApiModelProperty(value = "是否有课程（1：没有 0：有）")
    @NotNull(message = "是否有课程不能为空")
    private Integer clazz;

    @ApiModelProperty(value = "宿舍")
    @NotBlank(message = "宿舍不能为空")
    private String dormitory;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;

    @ApiModelProperty(value = "考试（1：没有 0：有）")
    @NotNull(message = "考试不能为空")
    private Integer exam;

    @ApiModelProperty(value = "审核意见")
    private String opinion;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDelete;


}
