package org.moonholder.cloud.damocles.common.core.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.moonholder.cloud.damocles.common.core.annotation.TemplatePlaceholder;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Daily对象", description = "日报表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Daily implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者id
     */
    @ApiModelProperty(value = "作者id")
    private Integer userId;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    @TemplatePlaceholder
    private String author;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TemplatePlaceholder
    private String time;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    @TemplatePlaceholder
    private String major;

    /**
     * 教练
     */
    @ApiModelProperty(value = "教练")
    @TemplatePlaceholder
    private String coach;

    /**
     * 督导
     */
    @ApiModelProperty(value = "督导")
    @TemplatePlaceholder
    private String councilor;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    @TemplatePlaceholder
    private String content;

    /**
     * 事件
     */
    @ApiModelProperty(value = "事件")
    @TemplatePlaceholder
    private String event;

    /**
     * 表现
     */
    @ApiModelProperty(value = "表现")
    @TemplatePlaceholder
    private String starred;

    /**
     * 收获
     */
    @ApiModelProperty(value = "收获")
    @TemplatePlaceholder
    private String harvest;

    /**
     * 疑惑
     */
    @ApiModelProperty(value = "疑惑")
    @TemplatePlaceholder
    private String doubt;

    /**
     * 帮助
     */
    @ApiModelProperty(value = "帮助")
    @TemplatePlaceholder
    private String help;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TemplatePlaceholder
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 逻辑删除 1->删除
     */
    @ApiModelProperty(value = "逻辑删除 1->删除")
    @JsonIgnore
    @TableLogic
    private Boolean isDel;
}

