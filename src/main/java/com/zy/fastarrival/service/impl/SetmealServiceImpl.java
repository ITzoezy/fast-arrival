package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.common.CustomerException;
import com.zy.fastarrival.dto.DishDto;
import com.zy.fastarrival.dto.SetmealDto;
import com.zy.fastarrival.entity.Dish;
import com.zy.fastarrival.entity.DishFlavor;
import com.zy.fastarrival.entity.Setmeal;
import com.zy.fastarrival.entity.SetmealDish;
import com.zy.fastarrival.mapper.SetmealMapper;
import com.zy.fastarrival.service.SetmealDishService;
import com.zy.fastarrival.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
       * 删除套餐，同时需要删除套餐和菜品的关联数据
       * @param ids
       */
      @Transactional
      public void removeWithDish(List<Long> ids) {
          //select count(*) from setmeal where id in (1,2,3) and status = 1
          //查询套餐状态，确定是否可用删除
          LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
          queryWrapper.in(Setmeal::getId,ids);
          queryWrapper.eq(Setmeal::getStatus,1);

          int count = this.count(queryWrapper);
          if(count > 0){
              //如果不能删除，抛出一个业务异常
              throw new CustomerException("套餐正在售卖中，不能删除");
          }

          //如果可以删除，先删除套餐表中的数据---setmeal
          this.removeByIds(ids);

          //delete from setmeal_dish where setmeal_id in (1,2,3)
          LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
          lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
          //删除关系表中的数据----setmeal_dish
          setmealDishService.remove(lambdaQueryWrapper);
      }

    @Override
    public void updateWithMeal(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //清理当前套餐的信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        //添加当前提交过来的套餐信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithFlavor(Long id) {
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /**
     * 根据套餐id修改售卖状态
     * @param status
     * @param ids
     */
    @Override
    public void updateSetmealStatusById(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null,Setmeal::getId,ids);

        List<Setmeal> list = this.list(queryWrapper);
        for (Setmeal setmeal: list) {
            if (setmeal != null){
                setmeal.setStatus(status);
                this.updateById(setmeal);
            }
        }
    }

    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
//    @Override
//    public SetmealDto getDate(Long id) {
//        Setmeal setmeal = this.getById(id);
//        SetmealDto setmealDto = new SetmealDto();
//        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
//        //在关联表中查询，setmealdish
//        queryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);
//
//        if (setmeal != null){
//            BeanUtils.copyProperties(setmeal,setmealDto);
//            List<SetmealDish> list = setmealDishService.list(queryWrapper);
//            setmealDto.setSetmealDishes(list);
//            return setmealDto;
//        }
//        return null;
//    }
}
