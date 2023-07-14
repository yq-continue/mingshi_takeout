package com.atmingshi.mapper;

import com.atmingshi.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author yang
 * @create 2023-07-13 17:19
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
