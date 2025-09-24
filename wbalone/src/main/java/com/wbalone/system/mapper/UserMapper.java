package com.wbalone.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbalone.system.model.dto.UserAuthInfo;
import com.wbalone.system.model.entity.User;
import com.wbalone.system.model.form.UserForm;
import com.wbalone.system.model.bo.UserBO;
import com.wbalone.system.model.query.UserPageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author yourname
 * @since 2024-07-01
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户分页列表
     *
     * @param queryParams 分页查询参数
     * @return 用户分页列表
     */
    List<UserBO> getUserPage(@Param("queryParams") UserPageQuery queryParams);

    /**
     * 根据用户ID获取用户表单数据
     *
     * @param userId 用户ID
     * @return 用户表单数据
     */
    UserForm getUserFormData(@Param("userId") Long userId);

    /**
     * 根据用户名获取用户认证信息
     *
     * @param username 用户名
     * @return 用户认证信息
     */
    UserAuthInfo getUserAuthInfo(@Param("username") String username);

    /**
     * 根据微信openid获取用户认证信息
     *
     * @param openid 微信openid
     * @return 用户认证信息
     */
    UserAuthInfo getUserAuthInfoByOpenId(@Param("openid") String openid);

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return 用户认证信息
     */
    UserAuthInfo getUserAuthInfoByMobile(@Param("mobile") String mobile);

    /**
     * 根据用户ID获取用户个人资料
     *
     * @param userId 用户ID
     * @return 用户个人资料
     */
    UserBO getUserProfile(@Param("userId") Long userId);
}