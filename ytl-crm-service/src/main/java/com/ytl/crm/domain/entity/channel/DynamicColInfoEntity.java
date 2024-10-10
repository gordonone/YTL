package com.ytl.crm.domain.entity.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_dynamic_col_info")
@ApiModel(value="DynamicColInfoEntity对象", description="")
public class DynamicColInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联基础表名称")
    private String tableName;

    @ApiModelProperty(value = "类型 1. 渠道策略 2. 渠道分配规则")
    private Integer type;

    @ApiModelProperty(value = "列名称")
    private String colName;

    @ApiModelProperty(value = "1 文本编辑  2 下拉列表单选 3 下拉列表多选 4 radio 5 checkbox")
    private Integer colFormType;

    @ApiModelProperty(value = "列值关联数据源类型：1. 字典表 2. 表数据 3. 其他")
    private Integer colRelateDataType;

    @ApiModelProperty(value = "列关联的类型的名称：字典类型名称或者表名称")
    private String colRelateTypeName;

    @ApiModelProperty(value = "列表展示模式 1 只展示列名称  2 展示列名称，字典code名称")
    private Integer colListShowType;

    @ApiModelProperty(value = "列字典code展示名称")
    private String colShowNameForCode;

    @ApiModelProperty(value = "列字典value展示名称")
    private String colShowNameForValue;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;

    @ApiModelProperty(value = "列数据类型  1 文本 2 数值 3 日期格式")
    private Integer colDataType;

    private Integer sort;


}
