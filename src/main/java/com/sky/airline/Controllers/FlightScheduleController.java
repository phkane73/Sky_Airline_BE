package com.sky.airline.Controllers;

import com.sky.airline.Entities.FlightTime;
import com.sky.airline.Services.IFlightScheduleService;
import com.sky.airline.Services.IFlightTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public/flightschedule")
@RequiredArgsConstructor
public class FlightScheduleController {

    private final IFlightScheduleService flightScheduleService;

    private final IFlightTimeService flightTimeService;


    @GetMapping("/create")
    public ResponseEntity<?> createFlightSchedule() {
        List<FlightTime> flightTimeList = flightTimeService.allFlightTimeList();
        flightScheduleService.handleFlightSchedule(flightTimeList, "2023-12-05 00:00", "2023-12-06 00:00", 0);
        Map<Integer, Object> schedule = flightScheduleService.getAllFlightScheduleWithRedis();
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/getschedule")
    public ResponseEntity<?> getFlightSchedule() {
        flightScheduleService.deleteFlightScheduleWithRedis();
        return new ResponseEntity<>("delete", HttpStatus.OK);
    }
}
