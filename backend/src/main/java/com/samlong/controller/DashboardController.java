package com.samlong.controller;

import com.samlong.model.Booking;
import com.samlong.repository.*;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
  private final ProductRepository products; private final NewsRepository news; private final SlideRepository slides; private final BookingRepository bookings;
  public DashboardController(ProductRepository p,NewsRepository n,SlideRepository s,BookingRepository b){products=p;news=n;slides=s;bookings=b;}

  @GetMapping("/stats") public Map<String,Long> stats(){return buildStats();}

  @GetMapping("/overview") public Map<String,Object> overview(){
    Map<String,Object> result=new LinkedHashMap<String,Object>();
    result.put("stats",buildStats());
    result.put("trend",buildTrend());
    result.put("statuses",buildStatuses());
    result.put("cities",buildCities());
    result.put("recent",buildRecent());
    return result;
  }

  private Map<String,Long> buildStats(){LocalDate today=LocalDate.now();LocalDate monday=today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));Map<String,Long> m=new LinkedHashMap<String,Long>();m.put("products",products.count());m.put("news",news.count());m.put("slides",slides.count());m.put("bookings",bookings.count());m.put("todayBookings",bookings.countByCreatedAtGreaterThanEqual(today.atStartOfDay()));m.put("weekBookings",bookings.countByCreatedAtGreaterThanEqual(monday.atStartOfDay()));m.put("newBookings",bookings.countByStatus("NEW"));return m;}

  private List<Map<String,Object>> buildTrend(){LocalDate first=LocalDate.now().minusDays(6);Map<LocalDate,Long> counts=new HashMap<LocalDate,Long>();for(Booking booking:bookings.findByCreatedAtGreaterThanEqualOrderByCreatedAtAsc(first.atStartOfDay())){LocalDate day=booking.getCreatedAt().toLocalDate();counts.put(day,counts.containsKey(day)?counts.get(day)+1L:1L);}List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();DateTimeFormatter label=DateTimeFormatter.ofPattern("MM/dd");for(int i=0;i<7;i++){LocalDate day=first.plusDays(i);Map<String,Object> item=new LinkedHashMap<String,Object>();item.put("date",day.toString());item.put("label",day.format(label));item.put("count",counts.containsKey(day)?counts.get(day):0L);result.add(item);}return result;}

  private List<Map<String,Object>> buildStatuses(){String[][] values={{"NEW","待联系"},{"CONTACTED","已联系"},{"VISITED","已到店"},{"COMPLETED","已完成"},{"CANCELLED","已取消"}};List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();for(String[] value:values){Map<String,Object> item=new LinkedHashMap<String,Object>();item.put("status",value[0]);item.put("label",value[1]);item.put("count",bookings.countByStatus(value[0]));result.add(item);}return result;}

  private List<Map<String,Object>> buildCities(){List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();List<Object[]> rows=bookings.countBookingsByCity();for(int i=0;i<Math.min(rows.size(),5);i++){Map<String,Object> item=new LinkedHashMap<String,Object>();String city=rows.get(i)[0]==null||String.valueOf(rows.get(i)[0]).trim().isEmpty()?"未填写":String.valueOf(rows.get(i)[0]);item.put("city",city);item.put("count",rows.get(i)[1]);result.add(item);}return result;}

  private List<Map<String,Object>> buildRecent(){List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();for(Booking booking:bookings.findTop5ByStatusOrderByCreatedAtDesc("NEW")){Map<String,Object> item=new LinkedHashMap<String,Object>();item.put("id",booking.getId());item.put("name",booking.getName());item.put("phone",booking.getPhone());item.put("city",booking.getCity());item.put("district",booking.getDistrict());item.put("createdAt",booking.getCreatedAt());item.put("unread",booking.getUnread());result.add(item);}return result;}
}
