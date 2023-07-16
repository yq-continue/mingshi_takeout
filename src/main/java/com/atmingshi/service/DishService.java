package com.atmingshi.service;

import com.atmingshi.common.R;
import com.atmingshi.dto.DishDto;
import com.atmingshi.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-13 21:29
 */
public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public R<DishDto> queryDishDto(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(String ids);

}
