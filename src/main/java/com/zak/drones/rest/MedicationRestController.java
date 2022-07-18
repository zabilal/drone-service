package com.zak.drones.rest;

import com.zak.drones.rest.drone.dtos.MedicationDTO;
import com.zak.drones.rest.drone.dtos.MedicationListDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateMedicationDTO;
import com.zak.drones.rest.drone.services.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/medications")
public class MedicationRestController {

    @Autowired
    private MedicationService medicationService;

    @GetMapping
    public ResponseEntity<MedicationListDTO> getAllMedications() {
        List<MedicationDTO> list = medicationService.getAllMedications();
        MedicationListDTO listDTO = new MedicationListDTO();
        list.forEach(e -> listDTO.getMedicationListDTO().add(e));
        return ResponseEntity.ok(listDTO);
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody CreateOrUpdateMedicationDTO medicationDTO) {
        return new ResponseEntity(new MedicationDTO(medicationService.createMedication(medicationDTO)), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public MedicationDTO getMedicationById(@PathVariable("id") Long id) {
        return new MedicationDTO(medicationService.getMedicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(@PathVariable("id") Long id, @RequestBody CreateOrUpdateMedicationDTO updateMedicationDTO) {
        return new ResponseEntity(new MedicationDTO(medicationService.updateMedication(id, updateMedicationDTO)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        medicationService.deleteMedicationById(id);
        return ResponseEntity.noContent().build();
    }

}
