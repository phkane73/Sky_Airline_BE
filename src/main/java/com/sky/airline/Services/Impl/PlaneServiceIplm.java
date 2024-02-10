package com.sky.airline.Services.Impl;

import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.Plane;
import com.sky.airline.Repositories.IPlaneRepository;
import com.sky.airline.Services.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaneServiceIplm implements IPlaneService {

    private final IPlaneRepository planeRepository;

    @Override
    public void addPlane(Plane plane) {
        planeRepository.save(plane);
    }

    @Override
    public void addPlaneOnAirport(Plane plane, Airport airport) {
        plane.setOnAirport(airport);
        planeRepository.save(plane);
    }

    @Override
    public Plane findPlaneById(int id) {
        return planeRepository.findById(id).get();
    }

    @Override
    public List<Plane> listAllPlaneOnAirport(Airport airport) {
        return planeRepository.findAllByOnAirport(airport);
    }

    @Override
    public List<Plane> allPlane() {
        return planeRepository.findAll();
    }

    @Override
    public int currentQuanlityPlaneOnAirport(Airport airport) {
        return planeRepository.countByOnAirport(airport);
    }

    @Override
    public void updatePlaneOnAirport(String planeName, Airport airport) {
        Plane plane = planeRepository.findByPlaneName(planeName);
        plane.setOnAirport(airport);
        planeRepository.save(plane);
    }
}
