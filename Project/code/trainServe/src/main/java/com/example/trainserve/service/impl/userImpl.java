package com.example.trainserve.service.impl;

import com.example.trainserve.entiy.QaRandom;
import com.example.trainserve.entiy.User;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class userImpl {

    @Resource
    com.example.trainserve.mapper.userMapper userMapper;

    public String login(String user_id, String password) {
        System.out.println("连接至服务层");
        List<User> listUser = userMapper.getUser(user_id);
        System.out.println(user_id);
        if (!listUser.isEmpty()) {
            if (listUser.get(0).getPassword().equals(password)) {
                userMapper.addLogin(listUser.get(0).getUser_id());
                return "登录成功";
            }
        }
        return "登录失败";
    }

    public String forget(String user_id, String name) {
        List<User> listUser = userMapper.getUser(user_id);
        if (!listUser.isEmpty()) {
            if (listUser.get(0).getName().equals(name)) {
                return listUser.get(0).getPassword();
            }
        }
        return "错误";
    }

    public String sign(User user) {
        List<User> listUser = userMapper.getUser(user.getUser_id());
        if (!listUser.isEmpty()) {
            return "该用户已经注册";
        } else {
            userMapper.addUser(user.getUser_id(), user.getName(), user.getPassword(), user.getTime());
            return "注册成功";
        }
    }

    public List<User> getLoginTime(String user_id) {
        List<User> listUser = userMapper.getUser(user_id);
        if (!listUser.isEmpty()) {
            return userMapper.getLoginTime(user_id);
        }
        return null;
    }

    //获得某个人的信息
    public User getUserById(String user_id) {
        List<User> users = userMapper.getUser(user_id);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    //修改个人信息
    public String changeUserById(@RequestBody User user) {
        try {
            userMapper.changeUserById(user.getUser_id(), user.getName(), user.getPassword(), user.getPhone());
        } catch (Exception e) {
            return "修改失败";
        } catch (Error error) {
            return "修改失败";
        }
        return "修改成功";
    }

    //获得乘车人信息
    public List<User> getSecondUser(String user_id) {
        return userMapper.getSecondUser(user_id);
    }

    //删除某个乘车人信息
    public void deleteSecondUser(User user) {
        userMapper.deleteSecondUser(user.getUser_id(), user.getSecond_user());
    }

    //添加乘车人信息
    public String addSecondUser(User user) {
        List<User> users = getSecondUser(user.getUser_id());
        for (User user1 : users) {
            if (user1.getSecond_user().equals(user.getSecond_user())) {
                return "该用户已经添加";
            }
        }
        userMapper.addSecondUser(user.getUser_id(), user.getSecond_user(), user.getPhone(), user.getName(), user.getId_type(), user.getTicket_type());
        return "添加成功";
    }

    //实现充值功能
    public void addMoney(String user_id,double money){
//        List<User> users=userMapper.getUser(user_id);
//        User user=users.get(0);
//        userMapper.changeMoney(user_id,user.getMoney()+money);
    }
    //实现添加更改头像的功能
    public void addAvatar(byte[] file, String user_id) {
     userMapper.addAvatar(file,user_id);
    }
    //查找所有用户
    public List<User> getAllUsers(){
      List<User> userList=  userMapper.getAllUser();
      List<String> users=userMapper.getAllBlack();
      List<User> userList1=new ArrayList<>();
      for(User user:userList)
      {
          if (!users.contains(user.getUser_id())){
              userList1.add(user);
          }
      }
        return userList1;
    }
    //忘记密码
    public void forgetPass(String qq){

        try {
            com.example.trainserve.entiy.QaRandom QqRandom;
            QqRandom=new QaRandom();
            String authCode= QqRandom.achieveCode();
            SimpleEmail mail = new SimpleEmail();
            mail.setHostName("smtp.qq.com");//发送邮件的服务器
            mail.setAuthentication("1617299497@qq.com","arudtirdpfmgbbic");//刚刚记录的授权码，是开启SMTP的密码
            mail.setFrom("1617299497@qq.com","12306");  //发送邮件的邮箱和发件人
            //mail.setSSLOnConnect(true); //使用安全链接
            mail.setSmtpPort(587);
            mail.addTo(qq);//接收的邮箱
            //System.out.println("email"+email);
            mail.setSubject("12306忘记密码");//设置邮件的主题
            mail.setMsg("尊敬的用户:你好!\n 请查看邮件获得密码");//设置邮件的内容
            mail.send();//发送
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    //将用户拉黑
    public void addBlack(User user){
        userMapper.addBlack(user.getUser_id());
    }
    //从黑名单中移出
    public void removeBlack(String user_id){
        userMapper.removeBlack(user_id);
    }

    //获得黑名单用户
    public List<User> getBlackUsers(){
        List<User> userList=  userMapper.getAllUser();
        List<String> users=userMapper.getAllBlack();
        List<User> userList1=new ArrayList<>();
        for(User user:userList)
        {
            if (users.contains(user.getUser_id())){
                userList1.add(user);
            }
        }
        return userList1;
    }
}
