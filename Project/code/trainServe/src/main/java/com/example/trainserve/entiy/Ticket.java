package com.example.trainserve.entiy;

import com.example.trainserve.mapper.ticketMapper;
import lombok.Data;

import java.util.List;

@Data
public class Ticket {
    private String user_id;
    private String car_id;
    private String station;
    private int station_id;
    private String seat_type;
    private int seat_id;
    private String start;
    private String end;
    private int start_id;
    private int end_id;
    private String station_time;
    private String ticketTime;
    private String buyTime;
    private String type;
    private int distance;
    private double money;
    private int fSeat;
    private int sSeat;
    private int tSeat;
    private List<Station> stationList;//某个车次的车站列表
    private String start_time;
    private String end_time;
    private String seat_location;
    private String seat_string;
    private String second_user;
    private String ticket_type;
    private String name;
    private int ticket_id;
    private String arrive_time;
    private String state;
    private String city;
}
