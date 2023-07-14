package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.pojo.Category;
import com.atmingshi.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

/**
 * @author yang
 * @create 2023-07-13 17:23
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService service;

    /**
     * 套餐界面的分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> categoryPage(int page,int pageSize){
        //1.新建分页对象
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //2.创建条件构造器拼装条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        //3.查询所有数据，返回给前端
        service.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    /**
     * 添加分类或套餐
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        service.save(category);
        return R.success("添加成功");
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteCategory(Long ids){
        service.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 修改套餐信息
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改套餐，修改值为{}",category);
        service.updateById(category);
        return R.success("修改套餐成功");
    }

}
