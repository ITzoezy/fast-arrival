package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.dto.SetmealDto;
import com.zy.fastarrival.entity.SetmealDish;
import com.zy.fastarrival.mapper.SetmealDishMapper;
import com.zy.fastarrival.service.SetmealDishService;
import com.zy.fastarrival.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
