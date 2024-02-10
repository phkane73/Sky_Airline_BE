package com.sky.airline.Services;

import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.Plane;

import java.util.List;

public interface IPlaneService {
    void addPlane(Plane plane);

    void addPlaneOnAirport(Plane plane, Airport airport);

    Plane findPlaneById(int id);

    List<Plane> listAllPlaneOnAirport(Airport airport);

    List<Plane> allPlane();

    int currentQuanlityPlaneOnAirport(Airport airport);

    void updatePlaneOnAirport(String planeName, Airport airport);
}
