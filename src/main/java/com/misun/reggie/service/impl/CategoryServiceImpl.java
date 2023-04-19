package com.misun.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.misun.reggie.common.CustomException;
import com.misun.reggie.entity.Category;
import com.misun.reggie.entity.Dish;
import com.misun.reggie.entity.Setmeal;
import com.misun.reggie.mapper.CategoryMapper;
import com.misun.reggie.service.CategoryService;
import com.misun.reggie.service.DishService;
import com.misun.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {

        // 是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);

        if(dishCount>0){
            // 已关联菜品，抛出业务异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        // 是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);

        if(setmealCount>0){
            // 已关联套餐，抛出业务异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        // 正常删除分类
        super.removeById(id);

    }
}
