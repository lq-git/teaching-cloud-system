package org.moonholder.cloud.damocles.common.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @author MoonHolder
 * @since 2020-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Team对象", description="")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "小组名称")
    private String teamName;

    @ApiModelProperty(value = "组长id")
    private Integer teamLeaderId;

    @ApiModelProperty(value = "组长")
    @TableField(exist = false)
    private String teamLeaderName;

    @ApiModelProperty(value = "教练id")
    private Integer teamCoachId;

    @ApiModelProperty(value = "教练")
    @TableField(exist = false)
    private String teamCoachName;

    @ApiModelProperty(value = "所在教室")
    private String classroom;

    @ApiModelProperty(value = "学习阶段")
    private String stage;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDel;


}
