package com.example.trainserve.service.impl;

import com.example.trainserve.controller.AlipayController;
import com.example.trainserve.controller.Info;
import com.example.trainserve.entiy.*;
import com.example.trainserve.mapper.ticketMapper;
import com.example.trainserve.mapper.userMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ticketImpl {
    @Resource
    ticketMapper ticketMapper;
    @Resource
    userMapper userMapper;

    //查询某车次的信息
    public List<Ticket> findCar(String car_id) {
        return ticketMapper.findCar(car_id);
    }

    //查询直达的车辆
    public List<Ticket> findCar_nonStop(String start, String end, String ticketTime) {
        //设置距离，后期删除
        setDistance();

//        System.out.println("具体实现层");
        //出发站的车站信息
        List<Ticket> startList = ticketMapper.findCarByCity(start);
        //到达站的车站信息
        List<Ticket> endList = ticketMapper.findCarByCity(end);
        //存放结果查询结果的信息链表
        List<Ticket> outList = new ArrayList<>();
        //寻找相对应的车站信息
        for (Ticket startTicket : startList) {
            for (Ticket endTicket : endList) {
                List<Station> outStation = new ArrayList<>();
                //出发地和目的地是在同一个车次上
                if (startTicket.getCar_id().equals(endTicket.getCar_id()) && endTicket.getStation_id() > startTicket.getStation_id()) {
//                    System.out.println("出发地和目的地一致");
                    //查找该车次该站点的车票信息
                    Ticket ticket = new Ticket();
                    ticket.setCar_id(startTicket.getCar_id());
                    //出发地
                    ticket.setStart_id(startTicket.getStation_id());
                    ticket.setStart(startTicket.getStation());
                    ticket.setStart_time(startTicket.getStation_time());
                    //终止地
                    ticket.setEnd_id(endTicket.getStation_id());
                    ticket.setEnd(endTicket.getStation());
                    ticket.setEnd_time(endTicket.getStation_time());
                    //车次列表信息
                    List<Ticket> ticketList = ticketMapper.findCar(startTicket.getCar_id());
                    for (Ticket ticket1 : ticketList) {
                        Station station = new Station();
                        station.setStation(ticket1.getStation());
                        station.setStation_id(ticket1.getStation_id());
                        station.setStation_time(ticket1.getStation_time());
                        station.setArrive_time(ticket1.getArrive_time());
                        outStation.add(station);
                    }
                    ticket.setStationList(outStation);
                    //座位信息
                    List<Integer> fSeats = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), endTicket.getStation_id(), "一等");
                    List<Integer> sSeats = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), endTicket.getStation_id(), "二等");
                    List<Integer> tSeats = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), endTicket.getStation_id(), "三等");
                    ticket.setFSeat(fSeats.size());
                    ticket.setSSeat(sSeats.size());
                    ticket.setTSeat(tSeats.size());
                    //距离和价格
                    int distance = 0;
                    for (int i = startTicket.getStation_id() + 1; i <= endTicket.getStation_id(); i++) {
                        distance += ticketList.get(i - 1).getDistance();
                    }
                    ticket.setDistance(distance);
                    outList.add(ticket);
                }

            }
        }
        return outList;
    }

    //查询需要转站的车辆信息
    public List<List<Ticket>> findSecond(String start, String end, String ticketTime) {
        List<List<Ticket>> list = new ArrayList<>();
        //出发站的车站信息
        List<Ticket> startList = ticketMapper.findCarByCity(start);
        //到达站的车站信息
        List<Ticket> endList = ticketMapper.findCarByCity(end);
        for (Ticket startTicket : startList) {
            for (Ticket endTicket : endList) {
                if(!startTicket.getCar_id().equals(endTicket.getCar_id())){
                    List<Ticket> list1 = ticketMapper.findCar(startTicket.getCar_id());
                    List<Ticket> list2 = ticketMapper.findCar(endTicket.getCar_id());
                    //寻找两个车次是否有相同的车站点
                    int distance1 = 0;
                    int distance2 = 0;
                    for (int i = startTicket.getStation_id() + 1; i <= list1.size(); i++) {
                        distance1 += list1.get(i - 1).getDistance();
                        //每次对第二条路线的距离进行归零
                        distance2 = 0;
                        for (int j = 1; j < endTicket.getStation_id(); j++) {
                            distance2 += list2.get(j - 1).getDistance();
                            if (list1.get(i - 1).getStation().equals(list2.get(j - 1).getStation())) {
                                //两个站点相同
                                //查看两个车的发车时间
                                String time1 = list1.get(i - 1).getStation_time();
                                String time2 = list2.get(j).getStation_time();
                                if (time1.compareTo(time2) < 0) {
                                    Ticket ticket1 = new Ticket();
                                    Ticket ticket2 = new Ticket();
                                    //对两个车次进行封装
                                    //第一个车次
                                    ticket1.setCar_id(startTicket.getCar_id());
                                    ticket1.setStart_time(startTicket.getStation_time());
                                    ticket1.setEnd_time(list1.get(i - 1).getStation_time());
                                    ticket1.setStart(startTicket.getStation());
                                    ticket1.setEnd(list1.get(i - 1).getStation());
                                    ticket1.setStart_id(startTicket.getStation_id());
                                    ticket1.setEnd_id(i);
                                    //距离
                                    ticket1.setDistance(distance1);
                                    //车次列表信息
                                    List<Station> stationList1 = new ArrayList<>();
                                    for (Ticket t1 : list1) {
                                        Station station = new Station();
                                        station.setStation(t1.getStation());
                                        station.setStation_id(t1.getStation_id());
                                        station.setStation_time(t1.getStation_time());
                                        station.setArrive_time(t1.getArrive_time());
                                        stationList1.add(station);
                                    }
                                    ticket1.setStationList(stationList1);
                                    //座位信息
                                    List<Integer> fSeats1 = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), i, "一等");
                                    List<Integer> sSeats1 = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), i, "二等");
                                    List<Integer> tSeats1 = findSeat(startTicket.getCar_id(), ticketTime, startTicket.getStation_id(), i, "三等");
                                    ticket1.setFSeat(fSeats1.size());
                                    ticket1.setSSeat(sSeats1.size());
                                    ticket1.setTSeat(tSeats1.size());
                                    //第二个车次
                                    ticket2.setCar_id(endTicket.getCar_id());
                                    ticket2.setStart_time(list2.get(j - 1).getStation_time());
                                    ticket2.setEnd_time(endTicket.getStation_time());
                                    ticket2.setStart(list2.get(j - 1).getStation());
                                    ticket2.setEnd(endTicket.getStation());
                                    ticket2.setStart_id(j);
                                    ticket2.setEnd_id(endTicket.getStation_id());
                                    //距离
                                    ticket2.setDistance(distance2);
                                    //车次列表信息
                                    List<Station> stationList2 = new ArrayList<>();
                                    for (Ticket t2 : list2) {
                                        Station station = new Station();
                                        station.setStation(t2.getStation());
                                        station.setStation_id(t2.getStation_id());
                                        station.setStation_time(t2.getStation_time());
                                        stationList2.add(station);
                                    }
                                    ticket2.setStationList(stationList2);
                                    //座位信息
                                    List<Integer> fSeats2 = findSeat(endTicket.getCar_id(), ticketTime, j, endTicket.getEnd_id(), "一等");
                                    List<Integer> sSeats2 = findSeat(endTicket.getCar_id(), ticketTime, j, endTicket.getEnd_id(), "二等");
                                    List<Integer> tSeats2 = findSeat(endTicket.getCar_id(), ticketTime, j, endTicket.getEnd_id(), "三等");
                                    ticket2.setFSeat(fSeats2.size());
                                    ticket2.setSSeat(sSeats2.size());
                                    ticket2.setTSeat(tSeats2.size());

                                    List<Ticket> tickets = new ArrayList<>();
                                    tickets.add(ticket1);
                                    tickets.add(ticket2);
                                    list.add(tickets);
                                }

                            }
                        }
                    }


                }
                }

        }

        return list;
    }

    //查找某种类型的座位数量
    public List<Integer> findSeat(String car_id, String time, int start_id, int end_id, String seat_type) {

        List<Integer> integers = ticketMapper.findAllSeat(car_id, time, start_id, end_id, seat_type);
        List<Ticket> tickets = ticketMapper.findSeatByCar(car_id);
        List<Integer> outList = new ArrayList<>();
        if (!tickets.isEmpty()) {
            if (seat_type.equals("一等")) {
                for (Integer i = 1; i <= tickets.get(0).getFSeat(); i++) {
                    if (!integers.contains(i)) {
                        outList.add(i);
                    }
                }
            } else if (seat_type.equals("二等")) {
                for (Integer i = 1; i <= tickets.get(0).getSSeat(); i++) {
                    if (!integers.contains(i)) {
                        outList.add(i);
                    }
                }
            } else if (seat_type.equals("三等")) {
                for (Integer i = 1; i <= tickets.get(0).getTSeat(); i++) {
                    if (!integers.contains(i)) {
                        outList.add(i);
                    }
                }
            }

            return outList;
        }
        return null;
    }

    //购买票
    public Info buyTickets(List<User> users, String seat_location, String ticketTime, String car_id, String start, String end, int ticket_id) throws IOException, URISyntaxException {
        Info info = new Info();
        info.setMoney(0);
        if (users.isEmpty()) {
            info.setInfo("请选择用户信息");
            return info;
        }
        String user_id = users.get(0).getUser_id();
        List<User> userList = userMapper.getUser(user_id);
        if (userList.isEmpty()) {

            info.setInfo("该用户不存在");
            return info;
        }
        User myUser = userList.get(0);

        for (User user : users) {
            if (user.getSeat_type() == null || user.getSeat_type().equals("")) {
                info.setInfo("请选择相应的座位信息");
                return info;
            }
        }
        //找到车次的相关信息
        Ticket t = ticketMapper.getT(car_id, start);
        Ticket start_car = t;
        List<Ticket> ticketList = ticketMapper.findCarBySameStation(car_id, end);
        Ticket end_car = ticketList.get(0);
        double allMoney = 0;
        //添加票
        for (User user : users) {
            //寻找该用户是否已经购买该车次该时间段的票
            List<Ticket> tickets1 = ticketMapper.findTicket(user.getSecond_user(), car_id, ticketTime);
            for (Ticket ticket : tickets1) {
                if (ticket.getType().equals("购买")) {
                    info.setInfo("该用户不能重复购买同一时间段同一车次的票");
                    return info;
                }
            }
            //查找剩余的座位信息
            List<Integer> integers = findSeat(car_id, ticketTime, start_car.getStation_id(), end_car.getStation_id(), user.getSeat_type());
            if (integers.isEmpty()) {
                info.setInfo("购票失败，座位不够");
                return info;
            }
            List<Ticket> seatList = ticketMapper.findSeatByCar(car_id);
            if (seatList.isEmpty()) {
                info.setInfo("该车次尚未分配座位");
                return info;
            }

            allMoney += user.getMoney();
        }
        info.setInfo("购票成功");
        info.setMoney(allMoney);
        this.buyPay(users,seat_location,ticketTime,car_id,start,end,ticket_id);
        return info;
    }

    public void buyPay(List<User> users, String seat_location, String ticketTime, String car_id, String start, String end, int ticket_id){
        //找到车次的相关信息
        Ticket t = ticketMapper.getT(car_id,start);
        Ticket start_car = t;
        List<Ticket> ticketList = ticketMapper.findCarBySameStation(car_id,end);
        Ticket end_car = ticketList.get(0);
        double allMoney=0;
        //添加票
        for (User user : users) {
            //寻找该用户是否已经购买该车次该时间段的票
            List<Ticket> tickets1 = ticketMapper.findTicket(user.getSecond_user(), car_id, ticketTime);

            //查找剩余的座位信息
            List<Integer> integers = findSeat(car_id, ticketTime, start_car.getStation_id(), end_car.getStation_id(), user.getSeat_type());

            List<Ticket> seatList = ticketMapper.findSeatByCar(car_id);

            int seatNum = 0;
            if (user.getSeat_type().equals("一等")) {
                seatNum = seatList.get(0).getFSeat();
            } else if (user.getSeat_type().equals("二等")) {
                seatNum = seatList.get(0).getSSeat();
            } else if (user.getSeat_type().equals("三等")) {
                seatNum = seatList.get(0).getTSeat();
            }
            int seat_id = 0;
            int integer = 1;
            if (seat_location.equals("")) {
                for (integer = 1; integer <= seatNum; integer++) {
                    if (integers.contains(integer)) {
                        seat_id = integer;
                        break;
                    }
                }
            } else {
                int i = Integer.parseInt(seat_location);
                int j = 0;
                while (j * 5 + i <= seatNum) {
                    if (integers.contains(new Integer(j * 5 + i))) {
                        seat_id = j * 5 + i;
                        break;
                    }
                    j++;
                }
                //没有合适的编号
                if (seat_id == 0) {
                    for (integer = 1; integer <= seatNum; integer++) {
                        if (integers.contains(integer)) {
                            seat_id = integer;
                            break;
                        }
                    }
                }
            }
            int id = 0;
            if (ticket_id == 0) {
                id = ticketMapper.getCountTicket();
            } else id = ticket_id;

            ticketMapper.addTicket(user.getUser_id(), user.getSecond_user(), car_id, start, end, start_car.getStation_id(), end_car.getStation_id(), user.getSeat_type(), seat_id, user.getMoney(), ticketTime, "购买", user.getTicket_type(), id);
            allMoney+=user.getMoney();
        }
    }

    public List<Ticket> buyHistory(String user_id) {
        List<Ticket> tickets = ticketMapper.buyHistory(user_id);
        for (Ticket ticket : tickets) {
            String s = new String();
            int i = ticket.getSeat_id();
            int j = i / 5 + 1;
            switch (i % 5) {
                case 1:
                    s = "" + (j/20+1)+"车"+j%20 + "A";
                    break;
                case 2:
                    s = "" + (j/20+1)+"车"+j%20+ "B";
                    break;
                case 3:
                    s = "" + (j/20+1)+"车"+j%20+ "C";
                    break;
                case 4:
                    s = "" + (j/20+1)+"车"+j%20+ "D";
                    break;
                case 0:
                    s = "" + (j/20+1)+"车"+j%20 + "E";
                    break;
            }
            ticket.setSeat_string(s);
            //寻找所有途径的站点
            List<Ticket> ticketList = ticketMapper.findCar(ticket.getCar_id());
            List<Station> stationList = new ArrayList<>();
            for (Ticket ticket1 : ticketList) {
                //封装起止时间
                if (ticket1.getStation().equals(ticket.getStart())) {
                    ticket.setStart_time(ticket1.getStation_time());
                }
                if (ticket1.getStation().equals(ticket.getEnd())) {
                    ticket.setEnd_time(ticket1.getStation_time());
                }
                Station station = new Station();
                station.setStation(ticket1.getStation());
                station.setStation_id(ticket1.getStation_id());
                station.setStation_time(ticket1.getStation_time());
                station.setArrive_time(ticket1.getArrive_time());
                stationList.add(station);
            }
            ticket.setStationList(stationList);
            //封装乘车人姓名
            User user = userMapper.findByUserAndSecond(ticket.getUser_id(), ticket.getSecond_user());
            if (user != null) {
                ticket.setName(user.getName());
            }
        }
        return tickets;
    }

    //获得类型为购买的车票
    public List<Ticket> getChangeTickets(String user_id) {
        List<Ticket> tickets = ticketMapper.getChangeTickets(user_id);
        for (Ticket ticket : tickets) {
            String s = new String();
            int i = ticket.getSeat_id();
            int j = i / 5 + 1;
            switch (i % 5) {
                case 1:
                    s = "" + j + "A";
                    break;
                case 2:
                    s = "" + j + "B";
                    break;
                case 3:
                    s = "" + j + "C";
                    break;
                case 4:
                    s = "" + j + "E";
                    break;
                case 0:
                    s = "" + j + "F";
                    break;
            }
            ticket.setSeat_string(s);
//            System.out.println(ticket.getSecond_user());
//            System.out.println(s);
            //寻找所有途径的站点
            List<Ticket> ticketList = ticketMapper.findCar(ticket.getCar_id());
            List<Station> stationList = new ArrayList<>();
            for (Ticket ticket1 : ticketList) {
                //封装起止时间
                if (ticket1.getStation().equals(ticket.getStart())) {
                    ticket.setStart_time(ticket1.getStation_time());
                }
                if (ticket1.getStation().equals(ticket.getEnd())) {
                    ticket.setEnd_time(ticket1.getStation_time());
                }
                Station station = new Station();
                station.setStation(ticket1.getStation());
                station.setStation_id(ticket1.getStation_id());
                station.setStation_time(ticket1.getStation_time());
                station.setArrive_time(ticket1.getArrive_time());
                stationList.add(station);
            }
            ticket.setStationList(stationList);
            //封装乘车人姓名
            User user = userMapper.findByUserAndSecond(ticket.getUser_id(), ticket.getSecond_user());
            if (user != null) {
                ticket.setName(user.getName());
            }
        }
        return tickets;
    }

    //退票
    public String getReturn(Ticket ticket) {
        //验证该票是否是购买
        List<Ticket> tickets = ticketMapper.findTicket(ticket.getSecond_user(), ticket.getCar_id(), ticket.getTicketTime());
        if (tickets.isEmpty()) {
            return "退票异常";
        }
        if (tickets.get(0).getType().equals("退票")) {
            return "该票已退，不能重复退票";
        }
        //获得当前的时间
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = format.format(d);
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        Date d8 = new Date();
        Date d2 = new Date();
        Date d1 = new Date();
        ca.add(Calendar.DATE, 1);// num为增加的天数，可以改变的
        d1 = ca.getTime();
        ca.add(Calendar.DATE, 1);// num为增加的天数，可以改变的
        d2 = ca.getTime();
        ca.add(Calendar.DATE, 6);// num为增加的天数，可以改变的
        d8 = ca.getTime();
        String enddate8 = format.format(d8);
        String enddate2 = format.format(d2);
        String enddate1 = format.format(d1);
//        System.out.println(enddate8);
//        System.out.println(enddate2);
//        System.out.println(enddate1);
        String time = ticket.getTicketTime() + " " + ticket.getStart_time();
        double money = ticket.getMoney();
        System.out.println(time);
        if (enddate8.compareTo(time) <= 0) {
            //距离火车到站时间还有八天
            //不收取手续费
        } else if (enddate2.compareTo(time) <= 0) {
            //距离开车时间还有两天到八天
            //手续费5%
            money = money - money * 0.05;
        } else if (enddate1.compareTo(time) <= 0) {
            //距离开车时间一天到两天
            //手续费10%
            money = money - money * 0.1;
        } else if (current.compareTo(time) <= 0) {
            //距离开车时间不到一天
            //手续费20%
            money = money - money * 0.2;
        } else {
            return "火车已经到站，不能进行更改";
        }
        List<User> userList = userMapper.getUser(ticket.getUser_id());
        if (userList.isEmpty()) {
            return "该用户不存在";
        }
        User user = userList.get(0);
        //账户加上要退票的钱
        userMapper.changeMoney(ticket.getUser_id(), user.getMoney() + money);
        //寻找该用户的信息，将购买更改为退票
        ticketMapper.changeBack(ticket.getUser_id(), ticket.getSecond_user(), ticket.getCar_id(), ticket.getTicketTime());
        return "退票成功";
    }

    public String getChange(Ticket ticket, String end, String time, String seat_location, String seat_type, double money) throws IOException, URISyntaxException {

        System.out.println(money);
        //查找关于该车次该时间段购票的信息
        List<Ticket> tickets = ticketMapper.getTicketById(ticket.getTicket_id());
        if (tickets.isEmpty()) {
            return "改签异常";
        }
        //其中包括已经退票或者已经改签的票，也有可能出现先退票又买票的可能
        if (tickets.size() > 1) {
            return "已经改签，变更业务只能办理一次";
        }
        if (tickets.get(0).equals("退票")) {
            return "改签异常";
        }
        //正常改签

        //封装userList
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUser_id(ticket.getUser_id());
        user.setSecond_user(ticket.getSecond_user());
        user.setSeat_type(seat_type);
        user.setTicket_type(ticket.getTicket_type());
        user.setMoney(money);
        userList.add(user);
        //购买新票
        List<User> userList1 = userMapper.getUser(ticket.getUser_id());
        if (userList1.isEmpty()) {
            return "该用户不存在";
        }
        User MyUser = userList1.get(0);


        //获得当前的时间
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = format.format(d);
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        Date d8 = new Date();
        Date d2 = new Date();
        Date d1 = new Date();
        ca.add(Calendar.DATE, 1);// num为增加的天数，可以改变的
        d1 = ca.getTime();
        ca.add(Calendar.DATE, 1);// num为增加的天数，可以改变的
        d2 = ca.getTime();
        ca.add(Calendar.DATE, 6);// num为增加的天数，可以改变的
        d8 = ca.getTime();
        String enddate8 = format.format(d8);
        String enddate2 = format.format(d2);
        String enddate1 = format.format(d1);
//        System.out.println(enddate8);
//        System.out.println(enddate2);
//        System.out.println(enddate1);
        String time1 = ticket.getTicketTime() + " " + ticket.getStart_time();
        double money1 = 0;
        if (ticket.getMoney() > money) {
            money1 = ticket.getMoney() - money;
        } else money1 = money - ticket.getMoney();
        if (enddate8.compareTo(time1) <= 0) {
            //距离火车到站时间还有八天
            //不收取手续费
        } else if (enddate2.compareTo(time1) <= 0) {
            //距离开车时间还有两天到八天
            //手续费5%
            money1 = money1 * 0.05;
        } else if (enddate1.compareTo(time1) <= 0) {
            //距离开车时间一天到两天
            //手续费10%
            money1 = money1 * 0.1;
        } else if (current.compareTo(time1) <= 0) {
            //距离开车时间不到一天
            //手续费20%
            money1 = money1 * 0.2;
        } else {
            money1 = money1 * 0.2;
        }
        if (MyUser.getMoney() - money - money1 <= 0) {
            return "账户余额不足";
        }

        //将原来的票更改为改签
        ticketMapper.setChange(ticket.getUser_id(), ticket.getSecond_user(), ticket.getCar_id(), ticket.getTicketTime());
        //返回原用户票钱
        userMapper.changeMoney(ticket.getUser_id(), MyUser.getMoney() + ticket.getMoney() - money1);
        //购买新的票
        buyTickets(userList, seat_location, time, ticket.getCar_id(), ticket.getStart(), end, ticket.getTicket_id());
        return "改签成功";
    }

    //获得改签信息的票
    public List<Ticket> getChildChange(Ticket ticket) {
        List<Ticket> outList = new ArrayList<>();
        List<Ticket> tickets = ticketMapper.findCar(ticket.getCar_id());
        int distance = 0;
        List<Station> outStation = new ArrayList<>();
        for (Ticket ticket1 : tickets) {
            Station station = new Station();
            station.setStation(ticket1.getStation());
            station.setStation_id(ticket1.getStation_id());
            station.setStation_time(ticket1.getStation_time());
            outStation.add(station);
        }
        for (int i = ticket.getStart_id() + 1; i <= tickets.size(); i++) {
            Ticket ticket1 = new Ticket();
            ticket1.setCar_id(ticket.getCar_id());
            ticket1.setStart(ticket.getStart());
            ticket1.setStart_time(ticket.getStart_time());
            ticket1.setStart_id(ticket1.getStart_id());
            ticket1.setEnd(tickets.get(i - 1).getStation());
            ticket1.setEnd_time(tickets.get(i - 1).getStation_time());
            ticket1.setEnd_id(tickets.get(i - 1).getStation_id());
            distance += tickets.get(i - 1).getDistance();
            ticket1.setDistance(distance);
            ticket1.setStationList(outStation);
            //座位信息
            List<Integer> fSeats = findSeat(ticket.getCar_id(), ticket.getTicketTime(), ticket.getStart_id(), i, "一等");
            List<Integer> sSeats = findSeat(ticket.getCar_id(), ticket.getTicketTime(), ticket.getStart_id(), i, "二等");
            List<Integer> tSeats = findSeat(ticket.getCar_id(), ticket.getTicketTime(), ticket.getStart_id(), i, "三等");
            ticket1.setFSeat(fSeats.size());
            ticket1.setSSeat(sSeats.size());
            ticket1.setTSeat(tSeats.size());
            outList.add(ticket1);
        }
        return outList;
    }

    //获得所有车辆信息
    public List<String> getCars() {
        List<String> strings = ticketMapper.getCars();
        return strings;
    }

    //获得某年所有月份的销售信息
    public List<Integer> getByYear(String year, String car_id) {
        List<Integer> integers = new ArrayList<>();
        String s1 = year + "-01-01";
        String s2 = year + "-02-01";
        String s3 = year + "-03-01";
        String s4 = year + "-04-01";
        String s5 = year + "-05-01";
        String s6 = year + "-06-01";
        String s7 = year + "-07-01";
        String s8 = year + "-08-01";
        String s9 = year + "-09-01";
        String s10 = year + "-10-01";
        String s11 = year + "-11-01";
        String s12 = year + "-12-01";
        String s13 = (Integer.parseInt(year) + 1) + "-01-01";
        integers.add(ticketMapper.getByMonth(car_id, s1, s2, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s2, s3, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s3, s4, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s4, s5, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s5, s6, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s6, s7, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s7, s8, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s8, s9, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s9, s10, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s10, s11, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s11, s12, "购买"));
        integers.add(ticketMapper.getByMonth(car_id, s12, s13, "购买"));
        return integers;
    }


    //设置两站之间的距离：按照高铁每分钟4公里的速度进行设置两站之间的距离
    public void setDistance() {
        List<String> cars = ticketMapper.findAllCar_id();
        //更改每一个车次的距离
        for (String car : cars) {
            List<Ticket> ticketList = ticketMapper.findCar(car);
            //如果该车次不为空
            if (!ticketList.isEmpty()) {
                int count = 0;
                double distance = 0;
                String time = ticketList.get(0).getStation_time();
                String hour = time.substring(0, 2);
                String minute = time.substring(3, 5);
//                System.out.println(car);
//                System.out.println(minute);
                int numTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
                while (count < ticketList.size()) {
                    //计算两地相差的时间
                    String dis_time = ticketList.get(count).getStation_time();
                    String dis_hour = dis_time.substring(0, 2);
                    String dis_minute = dis_time.substring(3, 5);
                    int dis_numTime = Integer.parseInt(dis_hour) * 60 + Integer.parseInt(dis_minute);
                    distance = (dis_numTime - numTime) * 4;
                    numTime = dis_numTime;
                    ticketMapper.setDistance(car, ++count, distance);
                }
            }
        }
    }

    //实现最短距离的排序
    public List<disSort> getShort(List<List<Ticket>> listList) {
        //绑定的链表
        List<disSort> disSorts = new ArrayList<>();
        //将票的链表和距离进行绑定
        for (int i = 0; i < listList.size(); i++) {
            List<Ticket> tickets = listList.get(i);
            int dis = 0;
            for (int j = 0; j < tickets.size(); j++) {
                dis += tickets.get(j).getDistance();
            }
            disSort disSort = new disSort();
            disSort.setDistance(dis);
            disSort.setTickets(tickets);
            disSorts.add(disSort);
        }
        //对数据进行排序
        List<disSort> outList = Sort(disSorts);
        //打印sort
//        for (disSort disSort : outList) {
//            for (int i = 0; i < disSort.getTickets().size(); i++) {
//                System.out.println(disSort.getTickets().get(i));
//            }
//
//        }
        return outList;
    }

    //排序的实际函数
    public List<disSort> Sort(List<disSort> disSorts) {
        for (int i = 0; i < disSorts.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < disSorts.size(); j++) {
                if (disSorts.get(min).getDistance() > disSorts.get(j).getDistance()) {
                    min = j;
                }
            }
            //进行交换
            disSort d = disSorts.get(min);
            disSorts.remove(min);
            disSorts.set(i, d);
        }
        return disSorts;
    }

    //查找需要转站和直达的车辆信息
    public List<disSort> getSortSecond(String start, String end, String ticketTime) {
        List<List<Ticket>> outList = findSecond(start, end, ticketTime);
        List<Ticket> tickets = findCar_nonStop(start, end, ticketTime);
        for (Ticket ticket : tickets) {
            List<Ticket> list = new ArrayList<>();
            list.add(ticket);
            outList.add(list);
        }
        return getShort(outList);
    }

    //查找所有座位
    public List<Ticket> getAllSeats(){
        return ticketMapper.getAllSeats();
    }
    //更改座位信息
    public void changeSeat(Ticket ticket)
    {
        ticketMapper.changeSeats(ticket.getCar_id(),ticket.getFSeat(),ticket.getSSeat(),ticket.getTSeat());
    }
    //获得余票
    public List<remainTicket> getRemainTicket(String ticketTime,String car_id){
        //所有座位
        List<Ticket> tickets=ticketMapper.findSeatByCar(car_id);
        Ticket seat=tickets.get(0);
        List<remainTicket> remainTickets=new ArrayList<>();
        List<Ticket> stationList=findCar(car_id);
        for(Ticket ticket:stationList){
            remainTicket remainTicket=new remainTicket();
            remainTicket.setStation(ticket.getStation());
            int i=ticket.getStation_id();
            remainTicket.setFseat(seat.getFSeat()-ticketMapper.getTicketNum(car_id,i,ticketTime,"一等"));
            remainTicket.setSseat(seat.getSSeat()-ticketMapper.getTicketNum(car_id,i,ticketTime,"二等"));
            remainTicket.setTseat(seat.getTSeat()-ticketMapper.getTicketNum(car_id,i,ticketTime,"三等"));
            remainTickets.add(remainTicket);
            System.out.println(remainTicket);
        }
        return remainTickets;
    }

    public List<List<Ticket>> getAllCars(){
        List<List<Ticket>> listList=new ArrayList<>();
        List<String> strings=ticketMapper.findAllCar_id();
        for(String s:strings){
            List<Ticket> tickets=ticketMapper.findCar(s);
            if(!ticketMapper.findSeatByCar(s).get(0).getState().equals("停运")){
                tickets.get(0).setState("正常运行");
            }else tickets.get(0).setState(ticketMapper.findSeatByCar(s).get(0).getState());

            listList.add(tickets);
        }
        return listList;
    }

    public List<String> stations(String car_id){
        return ticketMapper.getStations(car_id);
    }

    public String changeStation(Ticket ticket){
        if(ticket.getStation_time().compareTo(ticket.getArrive_time())<0){
            return "到达时间不能晚于发车时间";
        }
        List<Ticket> tickets=ticketMapper.findCar(ticket.getCar_id());
        if(ticket.getStation_id()==1){
            if(tickets.size()==1){
                ticketMapper.changeStation(ticket.getCar_id(),ticket.getStation(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getStation_id());
                return "修改成功";
            }
            if(ticket.getStation_time().compareTo(tickets.get(ticket.getStation_id()).getArrive_time())>0){
                return "该站的发车时间不能晚于下一站的到达时间";
            }
            ticketMapper.changeStation(ticket.getCar_id(),ticket.getStation(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getStation_id());
            return "修改成功";
        }else if(ticket.getStation_id()==tickets.size()){
            if(tickets.size()==1){
                ticketMapper.changeStation(ticket.getCar_id(),ticket.getStation(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getStation_id());
                return "修改成功";
            }
            if(ticket.getArrive_time().compareTo(tickets.get(ticket.getStation_id()-2).getStation_time())<0){
                return "该站的到站时间不能早于上一站的发车时间";
            }
            ticketMapper.changeStation(ticket.getCar_id(),ticket.getStation(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getStation_id());
            return "修改成功";
        }else{
            if(ticket.getArrive_time().compareTo(tickets.get(ticket.getStation_id()-2).getStation_time())<0){
                return "该站的到站时间不能早于上一站的发车时间";
            }
            System.out.println("进行到这");
            System.out.println(ticket.getStation_time());
            System.out.println(tickets.get(ticket.getStation_id()).getArrive_time());
            if(ticket.getStation_time().compareTo(tickets.get(ticket.getStation_id()).getArrive_time())>0){
                return "该站的发车时间不能晚于下一站的到达时间";
            }
            ticketMapper.changeStation(ticket.getCar_id(),ticket.getStation(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getStation_id());
            return "修改成功";
        }
    }
    //将车的状态设置为正常运行
    public void changeState(String car_id){
        ticketMapper.changeState(car_id);
    }
    //将车设置为停运
    public String stopState(String car_id) {
        List<Ticket> tickets = ticketMapper.findSeatByCar(car_id);
        if (tickets.size() == 0) {
            return "车辆信息异常";
        }
        if (tickets.get(0).getState().equals("停运")) {
            return "车辆已是停运状态";
        }
        //获得现在的时间
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = format.format(d);
        current = current.substring(0, 10);
        System.out.println(current);
        //查找所有大于该时间的票
        List<Ticket> tickets1 = ticketMapper.getTicketByTime(car_id, current);
        //将每一张票退款到用户
        for (Ticket ticket : tickets1) {
            ticketMapper.changeStopTicket(ticket.getTicket_id());
            List<User> userList = userMapper.getUser(ticket.getUser_id());
            if (userList.size() == 0) {
                return "用户账户有问题";
            }
            userMapper.changeMoney(ticket.getUser_id(), ticket.getMoney() + userList.get(0).getMoney());
        }
        //更改车的状态
        ticketMapper.changeStop(car_id);
        return "停运成功，已成功退还金额";
    }
    //添加Excel表格文件
    public String addExcel(List<Ticket> tickets){
        List<Ticket> ticketList=new ArrayList<>();
        //先检查是否全部可以向数据库中添加
        for(Ticket ticket:tickets){
          ticketList= ticketMapper.findCar(ticket.getCar_id());
          if(!ticketList.isEmpty()){
              return "添加失败";
          }
        }
        //向数据库中进行添加
        for(Ticket ticket:tickets){
          ticketMapper.addExcel(ticket.getCar_id(),ticket.getStation(),ticket.getDistance(),ticket.getStation_id(),ticket.getStation_time(),ticket.getArrive_time(),ticket.getCity());
        }

        return "添加成功";
    }
}
