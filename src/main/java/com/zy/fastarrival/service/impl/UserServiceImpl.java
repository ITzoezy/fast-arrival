package com.zy.fastarrival.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.fastarrival.entity.User;
import com.zy.fastarrival.mapper.UserMapper;
import com.zy.fastarrival.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
