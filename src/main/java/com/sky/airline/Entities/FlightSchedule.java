package com.sky.airline.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_schedule")
public class FlightSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "departure_airport", referencedColumnName = "airport_id")
    private Airport departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrival_airport", referencedColumnName = "airport_id")
    private Airport arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String planeName;
    private String flightCode;
}
