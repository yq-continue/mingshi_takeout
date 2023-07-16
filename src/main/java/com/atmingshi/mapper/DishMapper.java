package com.atmingshi.mapper;

import com.atmingshi.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yang
 * @create 2023-07-13 21:28
 */
@Repository
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    public Long count(@Param("id")Long id);

    public String queryName(@Param("id")Long id);

}
