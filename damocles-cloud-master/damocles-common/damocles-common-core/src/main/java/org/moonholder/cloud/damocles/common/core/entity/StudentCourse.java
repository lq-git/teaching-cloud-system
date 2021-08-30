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
 * @ClassName StudentCourse
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 16:02
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Course对象")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "scid",type = IdType.AUTO)
    @ApiModelProperty(value = "学生选课记录编号")
    private Integer scid;

    @ApiModelProperty(value = "学生编号")
    private Integer sid;

    @ApiModelProperty(value = "课程编号")
    private Integer cid;

    @ApiModelProperty(value = "逻辑删除")
    @JsonIgnore
    @TableLogic
    private Integer isDel;
}
