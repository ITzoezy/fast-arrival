package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.entity.ShoppingCart;
import com.zy.fastarrival.mapper.ShoppingCartMapper;
import com.zy.fastarrival.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
