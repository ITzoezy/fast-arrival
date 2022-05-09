package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.entity.DishFlavor;
import com.zy.fastarrival.mapper.DishFlavorMapper;
import com.zy.fastarrival.service.DishFlavorService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
