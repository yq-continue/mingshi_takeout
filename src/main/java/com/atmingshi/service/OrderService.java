package com.atmingshi.service;

import com.atmingshi.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-18 20:37
 */
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
