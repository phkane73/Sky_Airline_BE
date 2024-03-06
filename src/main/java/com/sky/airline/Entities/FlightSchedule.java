package com.sky.airline.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_schedule")
public class FlightSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "departure_airport", referencedColumnName = "airport_id")
    private Airport departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrival_airport", referencedColumnName = "airport_id")
    private Airport arrivalAirport;

    @OneToMany(mappedBy = "id.flight_schedule_id")
    private Set<SeatDetail> seatDetails;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String planeName;
    private String flightCode;
    private Long price;
}
