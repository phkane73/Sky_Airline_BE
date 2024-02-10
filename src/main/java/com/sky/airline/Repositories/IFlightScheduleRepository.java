package com.sky.airline.Repositories;

import com.sky.airline.Entities.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFlightScheduleRepository extends JpaRepository<FlightSchedule, Integer> {
}
