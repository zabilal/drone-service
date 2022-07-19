package com.zak.drones.rest.drone.dtos;

import com.zak.drones.rest.drone.entities.Medication;
import lombok.Data;

import java.io.Serializable;

@Data
public class MedicationDTO implements Serializable {

    public MedicationDTO(){}

    public MedicationDTO(Medication medication){
        this.id = medication.getId();
        this.name = medication.getName();
        this.code = medication.getCode();
        this.weight = medication.getWeight();
        this.imageUrl = medication.getImageUrl();
    }

    private Long id;
    private String name;
    private int weight;
    private String code;
    private String imageUrl;
}
