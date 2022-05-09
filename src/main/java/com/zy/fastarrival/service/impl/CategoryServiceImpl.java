package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.LADD;
import com.zy.fastarrival.common.CustomerException;
import com.zy.fastarrival.common.R;
import com.zy.fastarrival.entity.Category;
import com.zy.fastarrival.entity.Dish;
import com.zy.fastarrival.entity.Setmeal;
import com.zy.fastarrival.mapper.CategoryMapper;
import com.zy.fastarrival.service.CategoryService;
import com.zy.fastarrival.service.DishService;
import com.zy.fastarrival.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            throw new CustomerException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联，如果套餐已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2= setmealService.count(setmealLambdaQueryWrapper);

        if(count2 > 0){
            //已关联套餐，抛出一个业务异常
            throw new CustomerException("当前分类下关联了套餐，不能删除");
        }

        super.removeById(ids);
    }
}
