package org.moonholder.cloud.damocles.common.core.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel(value = "Word对象", description = "单词记录表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "英文")
    private String english;

    @ApiModelProperty(value = "汉语")
    private String chinese;

    @ApiModelProperty(value = "作者id")
    private Integer userId;

    @TableField(exist = false)
    private String author;

    @ApiModelProperty(value = "点赞数")
    private Integer likeNum;

    @TableField(exist = false)
    private Boolean likeStatus;

    @TableField(exist = false)
    private Boolean collectStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDel;

}
