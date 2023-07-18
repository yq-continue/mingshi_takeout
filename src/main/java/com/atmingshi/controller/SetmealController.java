package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.dto.SetmealDishDto;
import com.atmingshi.dto.SetmealDto;
import com.atmingshi.pojo.Dish;
import com.atmingshi.pojo.Setmeal;
import com.atmingshi.pojo.SetmealDish;
import com.atmingshi.service.CategoryService;
import com.atmingshi.service.DishService;
import com.atmingshi.service.SetmealDishService;
import com.atmingshi.service.SetmealService;
import com.atmingshi.utils.TransferId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.WrappedPlainView;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yang
 * @create 2023-07-16 14:44
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 套餐分页     需要查询套餐分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @Transactional
    public R<Page> pageOfSetmeal(int page, int pageSize,String name){
        //1.查询数据
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        //创建 Dto Page 对象，进行封装
        Page<SetmealDto> pageInfoOfSetmealSto = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null,Setmeal::getName,name);
        setmealService.page(pageInfo,wrapper);
        //将查询到的 setmeal Page对象转换为 SetmealDto Page对象
        BeanUtils.copyProperties(pageInfo,pageInfoOfSetmealSto,"records");
        //将 Setmeal 对象集合 转换为 SetmealDto 对象集合
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> recordsOfDto = new ArrayList<>();
        for (int i = 0;i < records.size();i++){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(records.get(i),setmealDto);
            //设置套餐名字
            setmealDto.setCategoryName(categoryService.getById(records.get(i).getCategoryId()).getName());
            recordsOfDto.add(setmealDto);
        }
        pageInfoOfSetmealSto.setRecords(recordsOfDto);
        //2.返回数据给前端展示
        return R.success(pageInfoOfSetmealSto);
    }

    /**
     * 新增套餐  setmeal setmealDish
     * 将套餐数据保存到 setmeal 表中 将套餐中的菜品数据保存到 setmealdish 中
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealWithSetmealDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 修改套餐界面表单回显
     * @param setmealId
     * @return
     */
    @GetMapping("/{setmealid}")
    public R<SetmealDto> getsetmeal(@PathVariable("setmealid")Long setmealId){
        //查询 setmeal 基本信息
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getId,setmealId);
        Setmeal one = setmealService.getOne(wrapper);
        //查询 setmeal 的 setmealdish 基本信息
        LambdaQueryWrapper<SetmealDish> dishOfWrapper = new LambdaQueryWrapper<>();
        dishOfWrapper.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishList = setmealDishService.list(dishOfWrapper);
        //将查询到的信息封装为 Dto 对象返回
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(one,setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        return R.success(setmealDto);
    }

    /**
     * 更新套餐信息
     * 用在修改套餐界面
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmealWithSetmealDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 删除套餐   批量删除
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(String ids){
        setmealService.deleteSetmealWithSetmealDish(ids);
        return R.success("删除成功");
    }

    /**
     * 批量停售&停售       批量起售&起售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status")int status,String ids){
        // 获取所有 setmealid（long）
        String[] idsOfString = ids.split(",");
        Long[] idsOfLong = new Long[idsOfString.length];
        for (int i = 0;i < idsOfString.length;i++){
            long id = Long.parseLong(idsOfString[i]);
            idsOfLong[i] = id;
        }
        List<Long> list = Arrays.asList(idsOfLong);
        //将状态修改为 status
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Setmeal::getStatus,status);
        wrapper.set(Setmeal::getUpdateUser, TransferId.gteId());
        wrapper.set(Setmeal::getUpdateTime, LocalDateTime.now());
        wrapper.in(list != null,Setmeal::getId,list);
        setmealService.update(wrapper);
        return R.success("修改成功");
    }

    /**
     * 用户选购页面的套餐展示
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> getSetmeal(Long categoryId,int status){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId);
        wrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }

    @GetMapping("/dish/{setmealId}")
    public R<List<SetmealDishDto>> getSetmealDish(@PathVariable Long setmealId){
        // 通过 setmealId 查询套餐下所包含的菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        // 将 setmealdish 对象转换为 setmealdishDto 对象并添加图片信息
        List<SetmealDishDto> dtoOfList = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            SetmealDish setmealDish = list.get(i);
            SetmealDishDto setmealDishDto = new SetmealDishDto();
            // 查询出 setmealdish 的图片信息
            String image = dishService.getImage(setmealDish.getDishId());
            BeanUtils.copyProperties(setmealDish,setmealDishDto);
            // 添加图片信息
            setmealDishDto.setImage(image);
            // 将 setmealdto 对象添加到数组
            dtoOfList.add(setmealDishDto);
        }
        return R.success(dtoOfList);
    }


}
