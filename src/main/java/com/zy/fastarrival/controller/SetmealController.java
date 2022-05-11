package com.zy.fastarrival.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.fastarrival.common.R;
import com.zy.fastarrival.dto.DishDto;
import com.zy.fastarrival.dto.SetmealDto;
import com.zy.fastarrival.entity.Category;
import com.zy.fastarrival.entity.Dish;
import com.zy.fastarrival.entity.Setmeal;
import com.zy.fastarrival.service.CategoryService;
import com.zy.fastarrival.service.DishService;
import com.zy.fastarrival.service.SetmealDishService;
import com.zy.fastarrival.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.update;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");

    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件,根据更新时间降序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
                    SetmealDto setmealDto = new SetmealDto();

                    BeanUtils.copyProperties(item,setmealDto);

                    Long categoryId = item.getCategoryId();//分类id
                    //根据id查询分类对象
                    Category category = categoryService.getById(categoryId);

                    if(category != null){
                        String categoryName = category.getName();
                        setmealDto.setCategoryName(categoryName);
                    }
                    return setmealDto;
                }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);

    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){

        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }


/**
    * 回显套餐数据：根据套餐id查询套餐
    * @return
    */
   @GetMapping("/{id}")
   public R<SetmealDto> getData(@PathVariable Long id){
       SetmealDto setmealDto = setmealService.getByIdWithFlavor(id);

       return R.success(setmealDto);
   }

    /**
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetMeal(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.updateWithMeal(setmealDto);
        return R.success("修改菜品成功");
    }



    /**
     * 对菜品批量或者是单个 进行停售或者是起售
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        setmealService.updateSetmealStatusById(status,ids);
        return R.success("售卖状态修改成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache" ,key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);

    }

}
