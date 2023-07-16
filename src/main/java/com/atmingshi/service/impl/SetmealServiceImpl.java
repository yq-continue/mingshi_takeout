package com.atmingshi.service.impl;

import com.atmingshi.dto.SetmealDto;
import com.atmingshi.mapper.SetmealMapper;
import com.atmingshi.pojo.Setmeal;
import com.atmingshi.pojo.SetmealDish;
import com.atmingshi.service.CategoryService;
import com.atmingshi.service.SetmealDishService;
import com.atmingshi.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author yang
 * @create 2023-07-13 21:31
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService service;

    @Override
    @Transactional
    public void saveSetmealWithSetmealDish(SetmealDto setmealDto) {
        // 将数据保存到 setmeal 表中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);
        // 将数据保存到 setmealDish 表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (int i = 0;i < setmealDishes.size();i++){
            SetmealDish setmealDish = setmealDishes.get(i);
            setmealDish.setSetmealId(setmeal.getId());
            service.save(setmealDish);
        }
    }

    @Override
    @Transactional
    public void updateSetmealWithSetmealDish(SetmealDto setmealDto) {
        //修改 setmeal 基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.updateById(setmeal);
        // 修改 setmealdish 基本信息
        // 先根据 setmealid 将对应的 setmealdish 信息全部删除
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        service.remove(wrapper);
        // 再将所修改的 setmealdish 数据添加到 setmealdish 表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (int i = 0;i < setmealDishes.size();i++){
            // 添加 setmealdish 信息中的 setmealid 信息
            SetmealDish setmealDish = setmealDishes.get(i);
            setmealDish.setSetmealId(setmeal.getId());
            setmealDish.setId(null);
        }
        service.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteSetmealWithSetmealDish(String ids) {
        // 获取所有 setmealid（long）
        String[] idsOfString = ids.split(",");
        Long[] idsOfLong = new Long[idsOfString.length];
        for (int i = 0;i < idsOfString.length;i++){
            long id = Long.parseLong(idsOfString[i]);
            idsOfLong[i] = id;
        }
        List<Long> list = Arrays.asList(idsOfLong);
        // 删除 setmealdish 表中的数据
        LambdaQueryWrapper<SetmealDish> wrapperOfSetmealDish = new LambdaQueryWrapper<>();
        wrapperOfSetmealDish.in(list != null,SetmealDish::getSetmealId,list);
        service.remove(wrapperOfSetmealDish);
        // 删除 setmeal 表中的数据
        LambdaQueryWrapper<Setmeal> wrapperOfSetmeal = new LambdaQueryWrapper<>();
        wrapperOfSetmeal.in(list != null,Setmeal::getId,list);
        this.remove(wrapperOfSetmeal);
    }
}
