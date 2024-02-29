package com.sky.airline.Services.Impl;

import com.sky.airline.Dto.FlightTimeDTO;
import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.FlightTime;
import com.sky.airline.Repositories.IFlightTimeRepository;
import com.sky.airline.Services.IAirportService;
import com.sky.airline.Services.IFlightTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightTimeServiceImpl implements IFlightTimeService {

    private final IFlightTimeRepository flightTimeRepository;

    private final IAirportService airportService;

    @Override
    public void addFlightTime(FlightTimeDTO flightTimeDTO) {
        Airport form = airportService.findAirportById(flightTimeDTO.getFrom());
        Airport to  = airportService.findAirportById(flightTimeDTO.getTo());
        FlightTime flightTime = new FlightTime();
        flightTime.setFrom(form);
        flightTime.setTo(to);
        flightTime.setEstimateTime(flightTimeDTO.getEstimateTime());
        flightTime.setPrice(flightTimeDTO.getPrice());
        flightTimeRepository.save(flightTime);
    }

    @Override
    public List<FlightTime> allFlightTimeList() {
        return flightTimeRepository.findAll();
    }

    @Override
    public List<FlightTime> getFlightTimeByAirport(Airport airport, Airport airport1) {
        List<FlightTime> flightTimeList = flightTimeRepository.getFlightTimeByFromOrTo(airport, airport1);
        if(flightTimeList.isEmpty()){
            return null;
        }
        return flightTimeList;
    }

}
