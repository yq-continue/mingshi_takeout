package com.atmingshi.mapper;

import com.atmingshi.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yang
 * @create 2023-07-16 21:53
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
