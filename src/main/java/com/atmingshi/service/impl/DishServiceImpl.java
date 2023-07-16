package com.atmingshi.service.impl;

import com.atmingshi.common.R;
import com.atmingshi.dto.DishDto;
import com.atmingshi.mapper.DishMapper;
import com.atmingshi.pojo.Dish;
import com.atmingshi.pojo.DishFlavor;
import com.atmingshi.service.DishFlavorService;
import com.atmingshi.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yang
 * @create 2023-07-13 21:30
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //将数据保存到 dish 表中
        this.save(dishDto);
        Long dishId = dishDto.getId();
        //将数据保存到 dishflavor 表中
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0;i < flavors.size();i++){
            flavors.get(i).setDishId(dishId);
        }
        dishService.saveBatch(flavors);
    }

    /**
     * 用于修改菜品页面的表单回显
     * 查询出 DishDto 类型的数据并回显
     * @param id
     * @return
     */
    @Override
    public R<DishDto> queryDishDto(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishService.list(wrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //1 将 dishdto 中的 dish 数据取出
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        //2 更新 dish 表
        this.updateById(dish);
        //3 更新 dishFlavor 表
        //3.1 先删除原有菜品的所有 dishflavor 信息
        int count = dishService.deleteFlavorOfDish(dish.getId());
        log.info("删除了{}行数据",count);
        //3.2 将传输进来的 dishflavors 添加进数据库
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0;i < flavors.size();i++){
            flavors.get(i).setDishId(dish.getId());
            flavors.get(i).setId(null);
        }
        dishService.saveBatch(flavors);
    }

    @Override
    public void deleteWithFlavor(String ids) {
        // 获取所传输进来的 dishId
        String[] dishId = ids.split(",");
        for (int i = 0;i < dishId.length;i++){
            // 将字符串转换为包装类
            long id = Long.parseLong(dishId[i]);
            //删除 dishId 所对应的口味信息
            dishService.deleteFlavorOfDish(id);
            //删除菜品信息
            this.removeById(id);
        }
    }


}
