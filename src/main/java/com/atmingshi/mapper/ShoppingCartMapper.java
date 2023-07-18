package com.atmingshi.mapper;

import com.atmingshi.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yang
 * @create 2023-07-18 15:07
 */
@Mapper
@Repository
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    public int updateNumber(@Param("id")Long id);

    public int updateNumberOfSub(@Param("id")Long id);

}