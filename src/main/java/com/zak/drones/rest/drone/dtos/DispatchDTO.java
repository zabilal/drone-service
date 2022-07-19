package com.zak.drones.rest.drone.dtos;

import com.zak.drones.rest.drone.entities.Dispatch;
import com.zak.drones.rest.drone.entities.DispatchStatus;

public class DispatchDTO {

    public DispatchDTO(){}

    public DispatchDTO(Dispatch dispatch){
        this.id = dispatch.getId();
        this.dispatchRef = dispatch.getDispatchRef();
        this.status = dispatch.getStatus();
        this.droneId = dispatch.getDrone().getId();
        this.medicationId = dispatch.getMedication().getId();
    }

    private Long id;
    private String dispatchRef;
    private DispatchStatus status;
    private Long droneId;
    private Long medicationId;
}
