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
 * 后台用户表
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "后台用户表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "学号")
    private String sno;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "班级")
    private String className;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "入学时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date admissionDate;

    @ApiModelProperty(value = "学历")
    private String degree;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色编号")
    private Integer roleId;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    private String status;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDel;

    @TableField(exist = false)
    private String isSubmitted;
}
