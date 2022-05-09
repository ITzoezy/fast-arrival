package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.entity.OrderDetail;
import com.zy.fastarrival.mapper.OrderDetailMapper;
import com.zy.fastarrival.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
