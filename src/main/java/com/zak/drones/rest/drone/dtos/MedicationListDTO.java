package com.zak.drones.rest.drone.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class MedicationListDTO implements Serializable {
    private ArrayList<MedicationDTO> medicationListDTO;

    public MedicationListDTO() {
        medicationListDTO = new ArrayList<>();
    }
}
