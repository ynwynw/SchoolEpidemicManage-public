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
 * 物资信息表
 * </p>
 *
 * @author fanchen
 * @since 2021-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodInfo对象", description="物资信息表")
public class GoodInfo implements Serializable {

    private static final long serialVersionUID = 471380316L;

    @ApiModelProperty(value = "物资信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型id")
    @NotNull(message = "物资类型不能为空！")
    private Long typeId;

    @ApiModelProperty(value = "物资名称")
    @NotBlank(message = "物资名称不能为空！")
    private String name;

    @ApiModelProperty(value = "图片链接")
    private String img;

    @ApiModelProperty(value = "物资规格")
    private String size;

    @ApiModelProperty(value = "物资单位")
    private String unit;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "库存")
    private Integer total;

    @ApiModelProperty(value = "是否启用")
    private Integer status;

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

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
