package com.zy.fastarrival.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.fastarrival.common.BaseContext;
import com.zy.fastarrival.common.R;
import com.zy.fastarrival.entity.ShoppingCart;
import com.zy.fastarrival.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 将菜品或套餐添加到购物车中
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        log.info("购物车数据：{}",shoppingCart);

        //设置当前用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,dishId);
        }

        //查询当前菜品或套餐是否在购物车中
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne != null){
            //如果已经存在，加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);

        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;

        }
        return R.success(cartServiceOne);
    }

    /**
     * 购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);

    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }

    /**
      * 客户端的套餐或者是菜品数量减少设置
      * 没必要设置返回值
      * @param shoppingCart
      */
     @PostMapping("/sub")
     @Transactional
     public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

         Long dishId = shoppingCart.getDishId();
         LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
         //代表数量减少的是菜品数量
         if (dishId != null){
             //通过dishId查出购物车对象
             queryWrapper.eq(ShoppingCart::getDishId,dishId);
             ShoppingCart cart1 = shoppingCartService.getOne(queryWrapper);
             cart1.setNumber(cart1.getNumber()-1);
             Integer LatestNumber = cart1.getNumber();
             if (LatestNumber > 0){
                 //对数据进行更新操作
                 shoppingCartService.updateById(cart1);
             }else if(LatestNumber == 0){
                 //如果购物车的菜品数量减为0，那么就把菜品从购物车删除
                 shoppingCartService.removeById(cart1.getId());
             }else if (LatestNumber < 0){
                 return R.error("操作异常");
             }

             return R.success(cart1);
         }

         Long setmealId = shoppingCart.getSetmealId();
         if (setmealId != null){
             //代表是套餐数量减少
             queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
             ShoppingCart cart2 = shoppingCartService.getOne(queryWrapper);
             cart2.setNumber(cart2.getNumber()-1);
             Integer LatestNumber = cart2.getNumber();
             if (LatestNumber > 0){
                 //对数据进行更新操作
                 shoppingCartService.updateById(cart2);
             }else if(LatestNumber == 0){
                 //如果购物车的套餐数量减为0，那么就把套餐从购物车删除
                 shoppingCartService.removeById(cart2.getId());
             }else if (LatestNumber < 0){
                 return R.error("操作异常");
             }
             return R.success(cart2);
         }
             //如果两个大if判断都进不去
             return R.error("操作异常");
     }
}
