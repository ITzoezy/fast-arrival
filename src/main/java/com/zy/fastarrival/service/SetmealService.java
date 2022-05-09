package com.zy.fastarrival.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zy.fastarrival.dto.SetmealDto;
import com.zy.fastarrival.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    public void updateWithMeal(SetmealDto setmealDto);

    SetmealDto getByIdWithFlavor(Long id);

    void updateSetmealStatusById(Integer status, List<Long> ids);

    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
//    SetmealDto getDate(Long id);
}
