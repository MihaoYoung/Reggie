package com.misun.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.misun.reggie.dto.SetmealDto;
import com.misun.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveSetmealWithDish(SetmealDto setmealDto);

    public void removeSetmealWithDishById(List<Long> id);
}
