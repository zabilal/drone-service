package com.zak.drones.rest.drone.dtos.requests;

import lombok.Data;

@Data

public class LoadDroneDTO {

    private Long droneId;
    private String [] medicationCodes;
}
