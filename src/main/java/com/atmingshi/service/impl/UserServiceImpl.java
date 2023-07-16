package com.atmingshi.service.impl;

import com.atmingshi.mapper.UserMapper;
import com.atmingshi.pojo.User;
import com.atmingshi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-16 21:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
