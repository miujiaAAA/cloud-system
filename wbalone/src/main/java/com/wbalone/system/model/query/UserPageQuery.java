package com.wbalone.system.model.query;

import cn.hutool.db.sql.Direction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wbalone.base.BasePageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户分页查询对象
 *
 * @author haoxr
 * @since 2022/1/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPageQuery extends BasePageQuery {

    private String keywords;

    private Integer status;

    private Long deptId;

    private List<Long> roleIds;

    private List<String> createTime;

    private String field;

    private Direction direction;

    /**
     * 是否超级管理员
     */
    @JsonIgnore
    private Boolean isRoot;

}
