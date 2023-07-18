package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.dto.DishDto;
import com.atmingshi.pojo.Dish;
import com.atmingshi.pojo.DishFlavor;
import com.atmingshi.service.CategoryService;
import com.atmingshi.service.DishFlavorService;
import com.atmingshi.service.DishService;
import com.atmingshi.utils.TransferId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yang
 * @create 2023-07-14 17:27
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * dish 分页查询
     * @return
     */
    @GetMapping("/page")
    @Transactional
    public R<Page> page(Integer page,Integer pageSize,String name){
        //创建 Page 对象
        Page<Dish> pageDishInfo = new Page<>(page,pageSize);
        Page<DishDto> pageDishDtoInfo = new Page<>();
        //拼装条件查询
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null,Dish::getName,name);
        wrapper.orderByAsc(Dish::getSort);
        //将 pageDishInfo 中的数据转移到 pageDishDtoInfo 对象中
        pageDishInfo = dishService.page(pageDishInfo,wrapper);
        BeanUtils.copyProperties(pageDishInfo,pageDishDtoInfo,"records");
        List<DishDto> dishDtoList = new ArrayList<>();
        List<Dish> dishRecords = pageDishInfo.getRecords();
        for (int i = 0;i < dishRecords.size();i++){
            Dish dish = dishRecords.get(i);
            String categoryName = categoryService.queryCategoryName(dish.getCategoryId());
            DishDto dishDto = new DishDto();
            dishDto.setCategoryName(categoryName);
            BeanUtils.copyProperties(dish,dishDto);
            dishDtoList.add(dishDto);
        }
        pageDishDtoInfo.setRecords(dishDtoList);
        return R.success(pageDishDtoInfo);
    }

    /**
     * 修改菜品信息时 进行表单回显
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable("id")Long id){
        return dishService.queryDishDto(id);
    }

    /**
     * 保存菜品的修改  更新操作
     * @return
     */
    @PutMapping
    public R<String> saveUpdate(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新成功");
    }

    /**
     * 菜品管理页面的批量删除功能
     * 还需要将对应的 口味信息也一并删除
     * @return
     */
    @DeleteMapping
    public R<String> deleteBatch(String ids){
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }


    /**
     * 批量起售 批量停售
     * @param status  销售状态 0 停售 1 起售
     * @param ids  dishId
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> batchOfSale(@PathVariable("status")int status,String ids){
        log.info("修改的状态为{}，获取到的主键为{}",status,ids);
        //获取到菜品数据的 DishId 值
        String[] dishIdsOfString = ids.split(",");
        Long[] dishIds = new Long[dishIdsOfString.length];
        for (int i = 0;i < dishIds.length;i++){
            dishIds[i] = Long.parseLong(dishIdsOfString[i]);
        }
        List<Long> idsOfList = Arrays.asList(dishIds);
        //修改数据库中的值
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Dish::getStatus,status);
        wrapper.set(Dish::getUpdateUser, TransferId.gteId());
        wrapper.set(Dish::getUpdateTime, LocalDateTime.now());
        wrapper.in(Dish::getId,idsOfList);
        dishService.update(wrapper);
        return R.success("修改成功");
    }



    /**
     * 根据传入的分类信息 查询分类中包含的菜品
     * 在套餐管理中的添加套餐中使用
     * 在用户前台页面使用展示 选择规格与菜品
     * @param categoryId  种类id
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getDishByCategory(Long categoryId,String name){
        //查询数据
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null,Dish::getCategoryId,categoryId);
        wrapper.like(name != null,Dish::getName,name);
        List<Dish> dishList = dishService.list(wrapper);
        //将数据迁移到 DishDto 对象中
        List<DishDto> dishDtoList = new ArrayList<>();
        for (int i = 0;i < dishList.size();i++){
            //根据 DishId 查询出对应的 DishFlavor 信息
            Dish dish = dishList.get(i);
            DishDto dishDto = new DishDto();
            LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
            dishFlavorWrapper.eq(DishFlavor::getDishId,dish.getId());
            List<DishFlavor> list = dishFlavorService.list(dishFlavorWrapper);
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setFlavors(list);
            dishDtoList.add(dishDto);
        }
        //将数据返回至前端
        return R.success(dishDtoList);

    }
    /*@GetMapping("/list")
    public R<List<Dish>> getDishByCategory(Long categoryId,String name){
        //查询数据
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null,Dish::getCategoryId,categoryId);
        wrapper.like(name != null,Dish::getName,name);
        List<Dish> dishList = dishService.list(wrapper);
        //将数据返回至前端
        return R.success(dishList);
    }*/


}


