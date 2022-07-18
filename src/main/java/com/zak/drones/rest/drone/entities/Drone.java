package com.zak.drones.rest.drone.entities;

import com.zak.drones.rest.users.entities.Gender;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="drones")
@Data
public class Drone implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    @Column(name="serial_number", nullable = false)
    private String serialNumber;

    @Enumerated
    @Column(columnDefinition = "tinyint")
    private DroneModel model;

    @Column(name="weight_limit", nullable = false)
    private int weightLimit;

    @Column(name="battery_capacity", nullable = false)
    private int batteryCapacity;

    @Enumerated
    @Column(columnDefinition = "tinyint")
    private DroneState state;
}
