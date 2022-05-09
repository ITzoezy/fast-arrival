package com.zy.fastarrival.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zy.fastarrival.dto.DishDto;
import com.zy.fastarrival.dto.SetmealDto;
import com.zy.fastarrival.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    void deleteByIds(List<Long> ids);
}
