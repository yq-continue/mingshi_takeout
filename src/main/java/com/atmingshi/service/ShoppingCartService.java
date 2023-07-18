package com.atmingshi.service;

import com.atmingshi.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-18 15:08
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加购物车数量加一
     * @param id
     */
    public void updateNumber(Long id);

    /**
     * 添加购物车数量减一
     * @param id
     */
    public void subNumber(Long id);
}
