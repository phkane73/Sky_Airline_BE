package com.sky.airline.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plane")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plane_id")
    private Integer id;
    private String planeName;
    private Integer seatQuanlity;
    @ManyToOne
    @JoinColumn(name = "airport_id", referencedColumnName = "airport_id")
    @JsonIgnore
    private Airport onAirport;

    public Plane(String planeName, Integer seatQuanlity) {
        this.planeName = planeName;
        this.seatQuanlity = seatQuanlity;
    }
}