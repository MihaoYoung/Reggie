package com.misun.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.misun.reggie.common.R;
import com.misun.reggie.dto.DishDto;
import com.misun.reggie.entity.Dish;
import com.misun.reggie.service.DishFlavorService;
import com.misun.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("菜品新建成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page pageInfo = new Page(page, pageSize);
        dishService.page(pageInfo);

        return R.success(pageInfo);
    }

}
