package com.atmingshi.mapper;

import com.atmingshi.pojo.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yang
 * @create 2023-07-14 17:24
 */
@Mapper
@Repository
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    public int deleteDishflavorOfdishId(@Param("dishId") Long dishId);

}
