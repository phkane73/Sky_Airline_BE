package com.sky.airline.Repositories;

import com.sky.airline.Entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAirportRepository extends JpaRepository<Airport, Integer> {

    Airport findByAirportName(String AirportName);

}
