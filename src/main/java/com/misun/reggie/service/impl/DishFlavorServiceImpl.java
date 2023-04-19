package com.misun.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.misun.reggie.entity.DishFlavor;
import com.misun.reggie.mapper.DishFlavorMapper;
import com.misun.reggie.service.DishFlavorService;
import com.misun.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
