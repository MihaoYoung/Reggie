package com.misun.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.misun.reggie.common.R;
import com.misun.reggie.dto.DishDto;
import com.misun.reggie.dto.SetmealDto;
import com.misun.reggie.entity.Category;
import com.misun.reggie.entity.Dish;
import com.misun.reggie.service.CategoryService;
import com.misun.reggie.service.DishFlavorService;
import com.misun.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("菜品新建成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("菜品修改成功");
    }

    /**
     * 修改菜品状态
     * @param ids
     * @return
     */
    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable int type, Long[] ids){

        for(Long id: ids){
            Dish dish = dishService.getById(id);

            if(dish != null){
                dish.setStatus(type);
                dishService.updateById(dish);
            }
        }

        return R.success("菜品状态修改成功");
    }

    @DeleteMapping
    public R<String> remove(Long[] ids){

        for(Long id:ids){
            dishService.removeWithFlavor(id);
        }

        return R.success("删除菜品成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Long categoryId){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(queryWrapper);

        return R.success(dishes);
    }


}
