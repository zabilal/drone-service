package com.zak.drones.rest.drone.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateMedicationDTO implements Serializable {

    private String name;
    private int weight;
    private String code;
    private String imageUrl;
}
