package com.zy.fastarrival.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zy.fastarrival.entity.Orders;

public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
