package com.example.trainserve.mapper;

import com.example.trainserve.entiy.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public interface ticketMapper {

    @Select("select * from car where  car_id=#{car_id} order by station_id ")
    public List<Ticket> findCar(String car_id);

    @Select("select * from car where car_id not in (select car_id from seat where state=\"停运\") and station like '%${station}%' order by station_id")
    public List<Ticket> findCarByStation(String station);

    //根据城市名查找信息
    @Select("select * from car where car_id not in (select car_id from seat where state=\"停运\") and city =#{city} order by station_id")
    public List<Ticket> findCarByCity(String city);

    @Select("select * from car where station=#{station} and car_id=#{car_id}")
    public List<Ticket> findCarBySameStation(String car_id,String station);


    //设置两站之间的距离
    @Update("update car set distance=#{distance} where car_id=#{car_id} and station_id=#{station_id}")
    public void setDistance(String car_id, int station_id, double distance);

    //查找所有的车次
    @Select("select distinct car_id from car")
    public List<String> findAllCar_id();

    //查找某一天某一个车次上某一个站点使用的座位编号列表

    //type类型为购买
    @Select("select distinct seat_id from buyticket where car_id=#{car_id} and tickettime=#{time} and start_id<=#{start_id} and end_id>=#{end_id} and seat_type=#{seat_type} and type=\"购买\"")
    public List<Integer> findAllSeat(String car_id, String time, int start_id, int end_id, String seat_type);

    //查找某个车次的座位信息
    @Select("select * from seat where car_id=#{car_id}")
    public List<Ticket> findSeatByCar(String car_id);

    //添加票
    @Insert("insert into buyticket(user_id,second_user,car_id,start,end,start_id,end_id,seat_type,seat_id,money,ticketTime,type,ticket_type ,buyTime,ticket_id) values (#{user_id},#{second_user},#{car_id},#{start},#{end},#{start_id},#{end_id},#{seat_type},#{seat_id},#{money},#{ticketTime},#{type},#{ticket_type},now(),#{ticket_id})")
    public void addTicket(String user_id, String second_user, String car_id, String start, String end, int start_id, int end_id, String seat_type, int seat_id, double money, String ticketTime, String type, String ticket_type,int ticket_id);

    @Select("select * from buyticket where second_user=#{second_user} and car_id=#{car_id} and ticketTime=#{ticketTime} order by buyTime desc ")
    public List<Ticket> findTicket(String second_user, String car_id, String ticketTime);

    //查找消费历史
    @Select("select * from buyticket where user_id=#{user_id}")
    public List<Ticket> buyHistory(String user_id);

    //查找类型为购买的票
    @Select("select * from buyticket where user_id=#{user_id} and type=\"购买\"")
    public List<Ticket> getChangeTickets(String user_id);

    //将票的类型改变为退票
    @Update("update buyticket set type=\"退票\" where user_id=#{user_id} and second_user=#{second_user} and car_id=#{car_id} and ticketTime=#{ticketTime}")
    public void changeBack(String user_id,String second_user,String car_id,String ticketTime);

    //将票的类型改变为改签
    @Update("update buyticket set type=\"改签\" where user_id=#{user_id} and second_user=#{second_user} and car_id=#{car_id} and ticketTime=#{ticketTime}")
    public void setChange(String user_id,String second_user,String car_id,String ticketTime);

    @Select("select distinct count(ticket_id) from buyticket")
    public int getCountTicket();

    @Select("select * from buyTicket where ticket_id=#{ticket_id}")
    public List<Ticket> getTicketById(int ticket_id);

    //查找所有车次的信息
    @Select("select distinct car_id from car ")
    public List<String> getCars();
    //寻找某个时间段的票销售量

    @Select("select count(*) from buyticket where car_id=#{car_id} and ticketTime>=#{start} and ticketTime<#{end} and type=#{type}")
    public int getByMonth(String car_id,String start,String end,String type);

    //查找所有座位
    @Select("select * from seat")
    public List<Ticket> getAllSeats();
 //更改座位信息
    @Update("update seat set fseat=#{fseat},sseat=#{sseat},tseat=#{tseat} where car_id=#{car_id}")
    public void changeSeats(String car_id,int fseat,int sseat,int tseat);

  //查找经过某一个站点,某一个时间的数量
    @Select("select count(distinct ticket_id) from buyTicket where car_id=#{car_id} and ticketTime=#{ticketTime} and start_id<=#{station_id} and end_id>=#{station_id} and type=\"购买\" and seat_type=#{seat_type}")
    public int getTicketNum(String car_id,int station_id,String ticketTime,String seat_type);

    //查找所有的信息
    @Select("select * from car,seat where seat.car_id=car.car_id order by station_id")
    public List<Ticket> getAllTicket();

    @Select("select distinct station from car where station not in (select distinct station from car where car_id =#{car_id})")
    public List<String> getStations(String car);

    @Update("update car set station=#{station},station_time=#{station_time},arrive_time=#{arrive_time} where station_id=#{station_id} and car_id=#{car_id}}")
    public void changeStation(String car_id,String station,String station_time,String arrive_time,int station_id);

    //更改车的状态
    @Update("update seat set state=\"运行\" where car_id=#{car_id}")
    public void changeState(String car_id);
    //更改车的状态
    @Update("update seat set state=\"停运\" where car_id=#{car_id}")
    public void changeStop(String car_id);
    //查找所有大于某一时间的票
    @Select("select * from buyTicket where car_id=#{car_id} and ticketTime>#{ticketTime} and type=\"购买\"")
    public List<Ticket> getTicketByTime(String car_id,String ticketTime);

    @Update("update buyTicket set type=\"退票\" where ticket_id=#{ticket_id} and type=\"购买\"")
    public void changeStopTicket(int ticket_id);

    //寻找车次和车站
    @Select("select * from car where car_id=#{car_id} and station=#{station}")
    public Ticket getT(String car_id,String station);

    @Insert("insert into car(car_id,station,distance,station_id,station_time,arrive_time,city) values (#{car_id},#{station},#{distance},#{station_id},#{station_time},#{arrive_time},#{city})")
    public void addExcel(String car_id,String station,int distance,int station_id,String station_time,String arrive_time,String city);
}
