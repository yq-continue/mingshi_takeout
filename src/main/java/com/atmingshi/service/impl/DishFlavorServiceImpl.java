package com.atmingshi.service.impl;

import com.atmingshi.mapper.DishFlavorMapper;
import com.atmingshi.pojo.DishFlavor;
import com.atmingshi.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-14 17:25
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

    @Autowired
    private DishFlavorMapper mapper;

    @Override
    public int deleteFlavorOfDish(Long dishId) {
        int countOfDelete = mapper.deleteDishflavorOfdishId(dishId);
        return countOfDelete;
    }
}
