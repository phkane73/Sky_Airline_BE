package com.sky.airline.Services;

import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.Plane;

import java.util.List;

public interface IAirportService {
    boolean addAirport(Airport airport);

    Airport findAirportById(int id);

    List<Plane> listPlaneOnAirport(Airport airport);

    List<Airport> allAirport();

    Airport findAirportByAirportName(String airportName);

    List<Airport> listAirportNoFlightTime(int id);

    void deactiveAirport(int id);

    boolean activeAirport(int id);
}
