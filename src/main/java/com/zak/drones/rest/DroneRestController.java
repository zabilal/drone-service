package com.zak.drones.rest;

import com.zak.drones.rest.drone.dtos.DispatchListDTO;
import com.zak.drones.rest.drone.dtos.DroneDTO;
import com.zak.drones.rest.drone.dtos.DroneListDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateDroneDTO;
import com.zak.drones.rest.drone.dtos.requests.LoadDroneDTO;
import com.zak.drones.rest.drone.entities.Dispatch;
import com.zak.drones.rest.drone.entities.Medication;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/drones")
public class DroneRestController {

    @Autowired
    private DroneService droneService;

    @GetMapping
    public ResponseEntity<List<DroneDTO>> getAllDrones() {
        List<DroneDTO> droneList = droneService.getAllDrones();
        return ResponseEntity.ok(droneList);
    }

    @PostMapping
    public ResponseEntity<DroneDTO> createDrone(@RequestBody CreateOrUpdateDroneDTO droneDTO) {
        return new ResponseEntity<>(new DroneDTO(droneService.createDrone(droneDTO)), null, HttpStatus.CREATED);
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

    @PostMapping("/load")
    public ResponseEntity<?> loadDrone(@RequestBody LoadDroneDTO loadDroneDTO){
        Map<String, String> response = new HashMap<>();
        String ref = droneService.loadDrone(loadDroneDTO);

        response.put("message", "Drone loaded successfully");
        response.put("dispatchRef" , ref);
        return new ResponseEntity<>(response, null, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDronesForLoading(){ return new ResponseEntity<>(droneService.getAvailableDronesForLoading() , null, HttpStatus.OK);}

    @GetMapping("/loaded")
    public ResponseEntity<DispatchListDTO> getLoadedItemsFromDrone(@RequestParam(value = "droneId") Long droneId, @RequestParam(value = "String dispatchRef")String dispatchRef){
        return new ResponseEntity<>(droneService.getLoadedItemsFromDrone(droneId, dispatchRef), null, HttpStatus.OK);
    }

    @PostMapping("/dispatch")
    public ResponseEntity<String> dispatchDrone(@RequestParam(value = "dispatchRef") String dispatchRef){
        return new ResponseEntity<>(droneService.dispatchDrone(dispatchRef), null, HttpStatus.OK);
    }
}
