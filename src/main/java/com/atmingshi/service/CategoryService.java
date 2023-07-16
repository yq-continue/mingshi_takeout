package com.atmingshi.service;

import com.atmingshi.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-13 17:20
 */

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

    /**
     * 用于菜品管理中查询菜品分类名称
     * @param id
     */
    public String queryCategoryName(Long id);

}
