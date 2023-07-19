package com.atmingshi.controller;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.atmingshi.common.R;
import com.atmingshi.dto.OrdersDto;
import com.atmingshi.pojo.OrderDetail;
import com.atmingshi.pojo.Orders;
import com.atmingshi.service.OrderDetailService;
import com.atmingshi.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @create 2023-07-18 20:48
 */
@RequestMapping("/order")
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单支付，添加订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> addOrder(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("支付成功");
    }

    /**
     * 用户页面订单展示
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    @GetMapping("userPage")
    @Transactional
    public R<Page<OrdersDto>> getUserOrder(int page, int pageSize, HttpSession session){
        // 新建 Page 对象
        Page<Orders> pageOfOrder = new Page<>(page,pageSize);
        Page<OrdersDto> pageOfDto = new Page<>();
        // 查询数据
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,session.getAttribute("user"));
        wrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageOfOrder,wrapper);
        // 将数据转换为 Dto 数据
        BeanUtils.copyProperties(pageOfOrder,pageOfDto,"records");
        List<OrdersDto> listOfDto = new ArrayList<>();
        List<Orders> listOfOrder = pageOfOrder.getRecords();
        for (int i = 0;i < listOfOrder.size();i++){
            Orders order = listOfOrder.get(i);
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(order,dto);
            LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrderDetail::getOrderId,order.getId());
            List<OrderDetail> list = orderDetailService.list(wrapper1);
            int count = 0;
            for (int j = 0;j < list.size();j++){
                count += list.get(j).getNumber();
            }
            dto.setSumNum(count);
            listOfDto.add(dto);
        }
        pageOfDto.setRecords(listOfDto);
        //返回数据
        return R.success(pageOfDto);
    }

    /**
     * 后台订单明细界面展示 (缺点：无菜品展示 因为不会前端暂时放弃)
     * @return
     */
    @GetMapping("/page")
    public R<Page<Orders>> pageOfOrderDetail(Integer page, Integer pageSize, Long number, String beginTime,String endTime){
        // 查询数据
        Page<Orders> pageOfOrder = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(number != null,Orders::getNumber,number);
        wrapper.ge(beginTime != null,Orders::getCheckoutTime,beginTime);
        wrapper.le(endTime != null,Orders::getCheckoutTime,endTime);
        wrapper.orderByAsc(Orders::getStatus).orderByAsc(Orders::getCheckoutTime);
        orderService.page(pageOfOrder,wrapper);
        // 返回数据
        return R.success(pageOfOrder);
    }

    /**
     * 修改订单状态
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Map map){
        // 获取传输进来的订单号与状态信息
        Integer status = (Integer)map.get("status");
        Long number = Long.parseLong((String) map.get("id"));
        // 修改状态
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(number != null, Orders::getNumber,number);
        wrapper.set(status != null,Orders::getStatus,status);
        orderService.update(wrapper);
        return R.success("修改成功");
    }

}
