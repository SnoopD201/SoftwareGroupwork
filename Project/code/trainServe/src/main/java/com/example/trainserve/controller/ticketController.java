package com.example.trainserve.controller;

import com.example.trainserve.TrainServeApplication;
import com.example.trainserve.entiy.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController//这个接口将所传进来的数据全部格式化为json返回前台
@RequestMapping("/ticket")//请求的地址
public class ticketController {

    @Resource
    com.example.trainserve.service.impl.ticketImpl ticketImpl;

    //查询直达的车辆
    @PostMapping("/find")
    public List<Ticket> findCar_nonStop(@RequestBody Ticket ticket) {
//        System.out.println("已经连接至控制层");
//        System.out.println(ticket);
        return ticketImpl.findCar_nonStop(ticket.getStart(), ticket.getEnd(), ticket.getTicketTime());
    }

    //查询需要转站的车辆
    @PostMapping("/findSecond")
    public List<List<Ticket>> findSecond(@RequestBody Ticket ticket) {
        return ticketImpl.findSecond(ticket.getStart(), ticket.getEnd(), ticket.getTicketTime());
    }

    //购买车票
    @PostMapping("buyTicket")
    public Info buyTickets(@RequestBody List<User> users, @RequestParam("seat_location") String seat_location, @RequestParam("ticketTime") String ticketTime, @RequestParam("car_id") String car_id, @RequestParam("start") String start, @RequestParam("end") String end) throws IOException, URISyntaxException {
//
//        System.out.println(seat_location);
//        System.out.println(ticketTime);
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(car_id);
//        for (User user : users) {
//            System.out.println(user);
//        }
        return ticketImpl.buyTickets(users, seat_location, ticketTime, car_id, start, end, 0);
    }

    @PostMapping("/buyHistory")
    public List<Ticket> buyHistory(@RequestBody Ticket ticket) {
        return ticketImpl.buyHistory(ticket.getUser_id());
    }

    @PostMapping("/getChangeTickets")
    public List<Ticket> getChangeTickets(@RequestBody Ticket ticket) {
        return ticketImpl.getChangeTickets(ticket.getUser_id());
    }

    @PostMapping("/return")
    public String getReturn(@RequestBody Ticket ticket) {
        return ticketImpl.getReturn(ticket);
    }

    @PostMapping("/childChange")
    public List<Ticket> getChildChange(@RequestBody Ticket ticket) {
        return ticketImpl.getChildChange(ticket);
    }

    @PostMapping("/changeMyTicket")
    public String changeMyTicket(@RequestParam("seat_location") String seat_location, @RequestParam("ticketTime") String ticketTime, @RequestParam("end") String end, @RequestParam("seat_type") String seat_type, @RequestBody Ticket ticket, @RequestParam("money") double money) throws IOException, URISyntaxException {
        return ticketImpl.getChange(ticket, end, ticketTime, seat_location, seat_type, money);
    }

    //获得所有的车次信息
    @PostMapping("/cars")
    public List<String> getCars() {
        return ticketImpl.getCars();
    }

    //获得所有的车次信息
    @PostMapping("/year")
    public List<Integer> getCarsByYear(@RequestParam("year") String year, @RequestParam("car_id") String car_id) {
        return ticketImpl.getByYear(year, car_id);
    }


    //进行排序
    @PostMapping("/sort")
    public List<List<Ticket>> getSort(@RequestBody Ticket ticket) {
        List<disSort> disSorts = ticketImpl.getSortSecond(ticket.getStart(), ticket.getEnd(), ticket.getTicketTime());
        List<List<Ticket>> outList = new ArrayList<>();
        for (disSort disSort : disSorts) {
            outList.add(disSort.getTickets());
        }
        return outList;
    }

    @PostMapping("/getSeats")
    public List<Ticket> getAllSeats() {
        return ticketImpl.getAllSeats();
    }

    @PostMapping("/changeSeats")
    public void changeSeats(@RequestBody Ticket ticket) {
        System.out.println(ticket);
        ticketImpl.changeSeat(ticket);
    }

    //获得余票
    @PostMapping("/remainTicket")
    public List<remainTicket> changeSeats(@RequestParam("ticketTime") String ticketTime, @RequestParam("car_id") String car_id) {
        return ticketImpl.getRemainTicket(ticketTime, car_id);
    }

    @PostMapping("/allCars")
    public List<List<Ticket>> getAllCars() {
        return ticketImpl.getAllCars();
    }

    @PostMapping("/getStations")
    public List<String> getAllCars(@RequestParam String car_id)  {
        return ticketImpl.stations(car_id);
    }

    @PostMapping("/changeStations")
    public String changeStatons(@RequestBody Ticket ticket)  {
      return ticketImpl.changeStation(ticket);
    }
    @PostMapping("/changeState")
    public void changeStatons(@RequestParam String car_id)  {
        ticketImpl.changeState(car_id);
    }

    @PostMapping("/stopState")
    public String stopState(@RequestParam String car_id) {
        return ticketImpl.stopState(car_id);
    }

    @PostMapping("/excel")
    public void getExcel(@RequestParam(value = "") MultipartFile file,
                         @RequestParam(defaultValue = "") String user_id) throws IOException {
        System.out.println("连接Excel");
        System.out.println(file);
        System.out.println(file.getBytes());
    }
    @PostMapping("/doExcel")
    public String doExcel(@RequestBody List<Ticket> Tickets) throws IOException {
        System.out.println("连接Excel");
        for(Ticket ticket:Tickets)
        {
            System.out.println(ticket);
        }

        return ticketImpl.addExcel(Tickets);
    }

}
