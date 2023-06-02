package com.example.trainserve.controller;

import com.example.trainserve.entiy.User;
import com.example.trainserve.service.impl.userImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Controller
@RestController//这个接口将所传进来的数据全部格式化为json返回前台
@RequestMapping("/user")//请求的地址
public class userController {
    @Resource
    userImpl userImpl;


    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userImpl.login(user.getUser_id(), user.getPassword());
    }

    @PostMapping("/forget")
    public String forget(@RequestBody User user) {
        return userImpl.forget(user.getUser_id(), user.getPassword());
    }

    @PostMapping("/sign")
    public String sign(@RequestBody User user) {
        return userImpl.sign(user);
    }

    @PostMapping("/loginTime")
    public List<User> getLoginTime(@RequestParam String user_id) {
        return userImpl.getLoginTime(user_id);
    }

    //获得个人信息
    @PostMapping("/getUser")
    public User getUserById(@RequestParam String user_id) {
        return userImpl.getUserById(user_id);
    }

    //修改个人信息
    @PostMapping("/changeUser")
    public String getUserById(@RequestBody User user) {
        return userImpl.changeUserById(user);
    }

    //获得乘车人信息
    @PostMapping("getSecondUser")
    public List<User> getSecondUser(@RequestParam String user_id) {
        return userImpl.getSecondUser(user_id);
    }

    //删除某个乘车人信息
    @PostMapping("deleteSecondUser")
    public void deleteSecondUser(@RequestBody User user) {
        userImpl.deleteSecondUser(user);
    }

    //添加乘车人信息
    @PostMapping("addSecondUser")
    public String addSecondUser(@RequestBody User user) { return userImpl.addSecondUser(user); }
    @GetMapping("/profile")
    public User findProfile(@RequestParam(defaultValue = "user_id") String user_id)
    {
        return userImpl.getUserById(user_id);
    }
    @RequestMapping("/toUpdateAvatar")
    public String updateAvatar( @RequestParam(value = "") MultipartFile file,
                                @RequestParam(defaultValue = "") String user_id) throws Exception {
//        System.out.println("已经连接上");
//
//        System.out.println("学号" + name);
//        System.out.println(file);
//        //判断文件类型
//        String pType = file.getContentType();
//        pType = pType.substring(pType.indexOf("/") + 1);
//        System.out.println(pType);
//
//        if ("jpeg".equals(pType)) {
//            pType = "jpg";
//        }
//        long time = System.currentTimeMillis();
//        //这里我采用绝对路径
//        String path = "C:\\WEB\\exam\\src\\assets\\images\\" + time + "." + pType;
//        System.out.println(path);
        userImpl.addAvatar(file.getBytes(), user_id);

//        userMapper.addAvatars(path, name);
//        System.out.println(file.getBytes());
//
//        try{
//        file.transferTo(new File(path));

//            //文件路径保存到数据库中从而读取
//            userMapper.addAvatar("http://192.168.191.3:9090/"+path.substring(path.indexOf("images/")),name);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return "success";
    }
    @PostMapping("/allUsers")
    public List<User> getAllUsers(){
        return userImpl.getAllUsers();
    }
    //忘记密码
    @PostMapping("/forgetPass")
    public void forgetPass(){
        userImpl.forgetPass("1617299497@qq.com");
    }

    //将用户加入黑名单
    @PostMapping("/addBlack")
    public void addBlack(@RequestBody User user){
        System.out.println("已经连接上");
        userImpl.addBlack(user);
    }
    //获得所有黑名单用户
    @PostMapping("/BlackUser")
    public List<User> getBlackUsers(){
        return userImpl.getBlackUsers();
    }
    //将用户从黑名单中删除
    @PostMapping("/removeBlack")
    public void removeBlack(@RequestBody User user){
        userImpl.removeBlack(user.getUser_id());
    }
}
