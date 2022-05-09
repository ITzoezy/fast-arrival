package com.zy.fastarrival.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.fastarrival.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
