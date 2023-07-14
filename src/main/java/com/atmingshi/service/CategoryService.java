package com.atmingshi.service;

import com.atmingshi.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-13 17:20
 */

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
