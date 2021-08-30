package org.moonholder.cloud.damocles.common.core.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
@ApiModel(value = "DispatchRecord对象", description = "")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @TableField(exist = false)
    private String nickName;

    @ApiModelProperty(value = "所在小组")
    private Integer teamId;

    @TableField(exist = false)
    private String teamName;

    @ApiModelProperty(value = "调度类型")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDel;

}
