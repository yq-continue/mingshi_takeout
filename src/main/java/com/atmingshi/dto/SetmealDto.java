package com.atmingshi.dto;


import com.atmingshi.pojo.Setmeal;
import com.atmingshi.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
