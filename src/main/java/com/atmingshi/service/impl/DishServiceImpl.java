package com.atmingshi.service.impl;

import com.atmingshi.mapper.DishMapper;
import com.atmingshi.pojo.Dish;
import com.atmingshi.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-13 21:30
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
