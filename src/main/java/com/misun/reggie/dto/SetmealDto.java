package com.misun.reggie.dto;

import com.misun.reggie.entity.Setmeal;
import com.misun.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
