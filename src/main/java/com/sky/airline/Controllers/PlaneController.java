package com.sky.airline.Controllers;

import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.Plane;
import com.sky.airline.Services.IAirportService;
import com.sky.airline.Services.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public/plane")
@RequiredArgsConstructor
public class PlaneController {

    private final IPlaneService planeService;

    private final IAirportService airportService;
    @PostMapping("/add")
    public ResponseEntity<?> addPlane(@RequestBody Plane plane){
        planeService.addPlane(plane);
        return new ResponseEntity<>("Add plane success!", HttpStatus.OK);
    }

    @GetMapping("/addtoairport")
    public ResponseEntity<?> addPlaneToAirport(@RequestParam("plane_id") int plane_id,
                                               @RequestParam("airport_id") int airport_id){
        Airport airport = airportService.findAirportById(airport_id);
        Plane plane = planeService.findPlaneById(plane_id);
        planeService.addPlaneOnAirport(plane, airport);
        return new ResponseEntity<>("Add "+plane.getPlaneName()+" on "+airport.getAirportName(), HttpStatus.OK);
    }
}
