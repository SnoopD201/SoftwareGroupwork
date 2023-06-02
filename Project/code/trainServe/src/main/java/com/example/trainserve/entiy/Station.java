package com.example.trainserve.entiy;

import lombok.Data;

@Data
public class Station {
    private int station_id;
    private String station;
    private String station_time;
    private String arrive_time;
    private String city;
}
