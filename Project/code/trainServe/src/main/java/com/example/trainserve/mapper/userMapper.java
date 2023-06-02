package com.example.trainserve.mapper;

import com.example.trainserve.entiy.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface userMapper {

    @Select("select * from login where user_id=#{user_id}")
    public List<User> getUser(String user_id);

    @Insert("insert into login (user_id,name,password,time) values (#{user_id},#{name},#{password},now())")
    public void addUser(String user_id,String name,String password,String time);

    @Insert("insert into logintime (user_id,loginTime) values (#{user_id},now())")
    public void addLogin(String user_id);

    @Select("select * from loginTime where user_id=#{user_id}")
    public List<User> getLoginTime(String user_id);

    //修改个人信息
    @Update("update login set name=#{name} , password=#{password} , phone=#{phone} where user_id=#{user_id}")
    public void changeUserById(String user_id,String name,String password,String phone);

    @Select("select * from users where second_user not in(select second_user from black) and user_id=#{user_id}")
    public List<User> getSecondUser(String user_id);

    @Delete("delete  from users where user_id=#{user_id} and second_user=#{second_user}")
    public void deleteSecondUser(String user_id,String second_user);

    //添加乘车人信息
    @Insert("insert into users(user_id,second_user,phone,name,id_type,ticket_type) values (#{user_id},#{second_user},#{phone},#{name},#{id_type},#{ticket_type})")
    public void addSecondUser(String user_id,String second_user,String phone,String name,String id_type,String ticket_type);

    //更改主用户余额
    @Update("update login set money=#{money} where user_id=#{user_id}")
    public void changeMoney(String user_id,double money);

    @Select("select * from users where user_id=#{user_id} and second_user=#{second_user}")
    public User findByUserAndSecond(String user_id,String second_user);


    //上传头像
    @Update("update login set profile=#{profile} where user_id=#{user_id}")
    void addAvatar(byte[]  profile, String user_id);

    //查找所有用户
    @Select("select * from login where user_id <> \"admin\"")
    public List<User> getAllUser();
    //查找所有黑名单中的用户
    @Select("select second_user from black")
    public List<String> getAllBlack();
    //将用户加入黑名单
    @Insert("insert into black (second_user,blackTime) values (#{second_user},now())")
    public void addBlack(String user_id);
    //将用户从黑名单中删除
    @Delete("delete  from black where second_user=#{second_user}")
    public void removeBlack(String second_user);

}

