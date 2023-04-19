package com.misun.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.misun.reggie.dto.DishDto;
import com.misun.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void removeWithFlavor(Long id);
}
