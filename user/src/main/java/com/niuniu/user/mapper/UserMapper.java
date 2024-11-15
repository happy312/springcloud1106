package com.niuniu.user.mapper;

import com.niuniu.user.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select id,name from t_user where id = #{id}")
    User getById(@Param("id") Long id);

    @Select("select * from t_user where name = #{name} and password = #{password}")
    User login(@Param("name") String name, @Param("password") String password);

    @Insert("insert into t_user(id, name, password) values(#{user.id}, #{user.name}, #{user.password}) ")
    Integer insertUser(@Param("user") User user);
}
