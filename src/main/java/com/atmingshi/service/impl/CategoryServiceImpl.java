package com.atmingshi.service.impl;

import com.atmingshi.exception.CustomException;
import com.atmingshi.mapper.CategoryMapper;
import com.atmingshi.mapper.DishMapper;
import com.atmingshi.mapper.SetmealMapper;
import com.atmingshi.pojo.Category;
import com.atmingshi.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-13 17:21
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void remove(Long id) {
        //1.查询此分类中是否存在套餐
        Long count = dishMapper.count(id);
        //2.查询此分类中是否存在菜品
        Long count1 = setmealMapper.count(id);
        //3.若存在套餐则抛出异常
        if (count != 0){
            //抛出异常
            throw new CustomException("此分类存在套餐，删除失败");
        }
        //4.若存在菜品则抛异常
        if (count1 != 0){
            //抛出异常
            throw new CustomException("此分类存在菜品，删除失败");
        }
        //5.删除分类
        super.removeById(id);
    }

    @Override
    public String queryCategoryName(Long id) {
        return categoryMapper.queryName(id);
    }


}
