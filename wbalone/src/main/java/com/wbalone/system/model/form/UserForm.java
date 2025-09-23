package com.wbalone.system.model.form;

import lombok.Data;

import java.util.List;

/**
 * 用户表单对象
 *
 * @author haoxr
 * @since 2022/4/12 11:04
 */
@Data
public class UserForm {

    private Long id;

    private String username;

    private String nickname;


    private String mobile;

    private Integer gender;

    private String avatar;

    private String email;

    private Integer status;

    private Long deptId;

    private List<Long> roleIds;

    private String openId;

}
