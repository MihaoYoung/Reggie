package com.misun.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.misun.reggie.common.R;
import com.misun.reggie.dto.SetmealDto;
import com.misun.reggie.entity.Category;
import com.misun.reggie.entity.Setmeal;
import com.misun.reggie.service.CategoryService;
import com.misun.reggie.service.SetmealDishService;
import com.misun.reggie.service.SetmealService;
import com.sun.org.apache.xpath.internal.objects.XNodeSetForDOM;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveSetmealWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        pageInfo = setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> pageInfoRecords = pageInfo.getRecords();
        List<SetmealDto> setmealList = pageInfoRecords.stream().map((item) -> {

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealList);

        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> remove(@RequestParam List<Long> ids){


        setmealService.removeSetmealWithDishById(ids);

        return R.success("套餐删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, @RequestParam List<Long> ids){

        List<Setmeal> setmeals = setmealService.listByIds(ids);
        setmeals.stream().map((item)->{
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());

        setmealService.updateBatchById(setmeals);

//        for(Long id:ids){
//
//            Setmeal setmeal = setmealService.getById(id);
//            setmeal.setStatus(status);
//            setmealService.updateById(setmeal);
//
//        }

        return R.success("套餐状态更新成功");
    }


}


