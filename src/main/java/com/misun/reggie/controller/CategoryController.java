package com.misun.reggie.controller;

import com.misun.reggie.common.R;
import com.misun.reggie.entity.Category;
import com.misun.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}", category);
        categoryService.save(category);

        return R.success("新增分类成功");
    }
}
