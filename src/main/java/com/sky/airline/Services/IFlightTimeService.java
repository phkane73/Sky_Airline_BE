package com.sky.airline.Services;

import com.sky.airline.Dto.FlightTimeDTO;
import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.FlightTime;

import java.util.List;

public interface IFlightTimeService {

    void addFlightTime(FlightTimeDTO flightTimeDTO);

    List<FlightTime> allFlightTimeList();

    List<FlightTime> getFlightTimeByAirport(Airport airport, Airport airport1);
}
