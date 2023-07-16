package com.atmingshi.service;

import com.atmingshi.pojo.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-14 17:24
 */
public interface DishFlavorService extends IService<DishFlavor> {
    /**
     * 根据 dishId 将 dishflavor 全部删除
     * @param dishId 菜品 id
     * @return 返回删除的条数
     */
    public int deleteFlavorOfDish(Long dishId);

}
