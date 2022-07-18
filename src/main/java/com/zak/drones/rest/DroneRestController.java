package com.zak.drones.rest;

import com.zak.drones.rest.drone.dtos.DroneDTO;
import com.zak.drones.rest.drone.dtos.DroneListDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateDroneDTO;
import com.zak.drones.rest.drone.services.DroneService;
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
@RequestMapping(value = "/drones")
public class DroneRestController {

    @Autowired
    private DroneService droneService;

    @GetMapping
    public ResponseEntity<DroneListDTO> getAllDrones() {
        List<DroneDTO> droneList = droneService.getAllDrones();
        DroneListDTO listDTO = new DroneListDTO();
        droneList.forEach(e -> listDTO.getDroneList().add(e));
        return ResponseEntity.ok(listDTO);
    }

    @PostMapping
    public ResponseEntity<DroneDTO> createMedication(@RequestBody CreateOrUpdateDroneDTO droneDTO) {
        return new ResponseEntity(new DroneDTO(droneService.createDrone(droneDTO)), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public DroneDTO getDroneById(@PathVariable("id") Long id) {
        return new DroneDTO(droneService.getDroneById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DroneDTO> updateDrone(@PathVariable("id") Long id, @RequestBody CreateOrUpdateDroneDTO updateDroneDTO) {
        return new ResponseEntity(new DroneDTO(droneService.updateDrone(id, updateDroneDTO)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        droneService.deleteDroneById(id);
        return ResponseEntity.noContent().build();
    }
}
