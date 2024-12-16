package com.niuniu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuniu.user.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from t_user where name = #{name} and password = #{password}")
    User login(@Param("name") String name, @Param("password") String password);
}
