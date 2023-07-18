package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.pojo.ShoppingCart;
import com.atmingshi.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @create 2023-07-18 14:59
 */
@RequestMapping("shoppingCart")
@RestController
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        // 设置用户 id
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        // 查询是否添加过此菜品或套餐
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        wrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        if (cart == null){
            // 若不存在此菜品则新增菜品
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }else {
            //若存在此菜品则数量加一
            shoppingCartService.updateNumber(cart.getId());
            ShoppingCart shoppingCart1 = shoppingCartService.getById(cart.getId());
            return R.success(shoppingCart1);
        }
    }

    /**
     * 删除购物车中的菜品
     * @param shoppingCart 封装的是前端传输过来的 dishId
     * @param session
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart,HttpSession session){
        // 获取 dishId 和 userId
        Long dishId = shoppingCart.getDishId();
        Long userId = (Long) session.getAttribute("user");
        // 查询此数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dishId != null,ShoppingCart::getDishId,dishId);
        wrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(wrapper);
        Integer number = shoppingCart1.getNumber();
        if (number == 1){
            //删除此条数据
            shoppingCartService.remove(wrapper);
            return R.success(new ShoppingCart());
        }else{
            // 将此条数据的数量减一
            shoppingCartService.subNumber(shoppingCart1.getId());
            ShoppingCart one = shoppingCartService.getOne(wrapper);
            return R.success(one);
        }
    }

    /**
     * 购物车展示
     * @param session
     * @return
     */
    @GetMapping("list")
    public R<List<ShoppingCart>> list(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @param session
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart(HttpSession session){
        // 获取 userId
        Long userId = (Long) session.getAttribute("user");
        // 删除此 userId 下的所有选项
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        shoppingCartService.remove(wrapper);
        return R.success("删除成功");
    }
}
