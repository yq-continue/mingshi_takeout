package com.atmingshi.service.impl;

import com.atmingshi.mapper.ShoppingCartMapper;
import com.atmingshi.pojo.ShoppingCart;
import com.atmingshi.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-18 15:09
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper mapper;

    /**
     * 购物车添加菜品 数量加一
     * @param id
     */
    @Override
    public void updateNumber(Long id) {
        mapper.updateNumber(id);
    }

    /**
     * 购物车添加菜品 数量减一
     * @param id
     */
    @Override
    public void subNumber(Long id) {
        mapper.updateNumberOfSub(id);
    }
}
