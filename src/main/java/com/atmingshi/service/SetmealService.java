package com.atmingshi.service;

import com.atmingshi.dto.SetmealDto;
import com.atmingshi.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-13 21:30
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存 setmeal 套餐信息
     * @param setmealDto
     */
    public void saveSetmealWithSetmealDish(SetmealDto setmealDto);

    /**
     * 修改 setmeal 套餐信息
     * @param setmealDto
     */
    public void updateSetmealWithSetmealDish(SetmealDto setmealDto);

    /**
     * 批量删除套餐
     * @param ids
     */
    public void deleteSetmealWithSetmealDish(String ids);

}
