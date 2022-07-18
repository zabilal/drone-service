package com.zak.drones.rest.drone.dtos;

import com.zak.drones.rest.drone.entities.Medication;

import java.io.Serializable;

public class MedicationDTO implements Serializable {

    public MedicationDTO(){}

    public MedicationDTO(Medication medication){
        this.name = medication.getName();
        this.code = medication.getCode();
        this.weight = medication.getWeight();
        this.imageUrl = medication.getImageUrl();
    }

    private String name;
    private int weight;
    private String code;
    private String imageUrl;
}
