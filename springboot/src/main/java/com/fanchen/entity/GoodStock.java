package com.fanchen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fanchen.common.dto.GoodDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物资出入库表
 * </p>
 *
 * @author fanchen
 * @since 2021-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodStock对象", description="物资出入库表")
public class GoodStock implements Serializable {

    private static final long serialVersionUID = 573801567L;

    @ApiModelProperty(value = "出入库信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "去向")
    @NotBlank(message = "物资去向不可以为空")
    private String accept;

    @ApiModelProperty(value = "操作人")
    private String createBy;

    @ApiModelProperty(value = "物资数量")
    private Integer goodNum;

    @ApiModelProperty(value = "物资规格")
    private String goodSize;

    @ApiModelProperty(value = "物资名")
    private String goodName;

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人信息不能为空")
    private String peopleName;

    @ApiModelProperty(value = "联系人电话")
    @NotBlank(message = "联系人电话不能为空")
    private String peoplePhone;

    @ApiModelProperty(value = "操作类型")
    @NotNull(message = "操作类型不能为空")
    private Integer operateType;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    @TableField(exist = false)
    private List<GoodDto> list;

}
