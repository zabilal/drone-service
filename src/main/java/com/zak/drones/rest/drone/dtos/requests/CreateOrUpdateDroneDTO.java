package com.zak.drones.rest.drone.dtos.requests;

import com.zak.drones.rest.drone.entities.DroneModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateDroneDTO implements Serializable {
    private String serialNumber;
    private String model;
    private int weightLimit;
    private int batteryCapacity;
    private String state;
}
