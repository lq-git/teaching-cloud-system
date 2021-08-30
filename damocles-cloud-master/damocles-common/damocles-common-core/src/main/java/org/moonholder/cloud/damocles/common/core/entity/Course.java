package org.moonholder.cloud.damocles.common.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName Course
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 15:54
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Course对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cid",type = IdType.AUTO)
    @ApiModelProperty(value = "课程编号")
    private Integer cid;

    @ApiModelProperty(value = "课程名称")
    private String cname;

    @ApiModelProperty(value = "学分")
    private Integer ccredit;

    @ApiModelProperty(value = "学时")
    private Integer cperiod;

    @ApiModelProperty(value = "已选人数")
    private Integer cselcount;

    @ApiModelProperty(value = "限定人数")
    private Integer cmaxcount;

    @ApiModelProperty(value = "授课教师编号")
    private Integer cteacherid;

    @ApiModelProperty(value = "授课教师姓名")
    private String cteachername;

    @ApiModelProperty(value = "逻辑删除")
    @JsonIgnore
    @TableLogic
    private Integer isDel;
}
