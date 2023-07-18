package com.atmingshi.service.impl;

import com.atmingshi.mapper.OrderDetailMapper;
import com.atmingshi.pojo.OrderDetail;
import com.atmingshi.service.OrderDetailService;
import com.atmingshi.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-18 20:44
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
