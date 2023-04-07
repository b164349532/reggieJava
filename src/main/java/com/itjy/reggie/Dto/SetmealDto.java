package com.itjy.reggie.Dto;


import com.itjy.reggie.entity.Setmeal;
import com.itjy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
