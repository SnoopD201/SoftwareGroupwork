package com.example.trainserve.entiy;

import lombok.Data;

@Data
public class User {
    private String user_id;
    private String identity;
    private String name;
    private String password;
    private String time;
    private String loginTime;
    private String second_user;
    private String phone;
    private String ticket_type;
    private double money;
    private String id_type;
    private String seat_type;
    private byte[] profile;
    private String black;
    private String city;
}
