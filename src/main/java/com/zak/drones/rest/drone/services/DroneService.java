package com.zak.drones.rest.drone.services;

import com.zak.drones.rest.drone.dtos.DroneDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateDroneDTO;
import com.zak.drones.rest.drone.entities.Drone;
import com.zak.drones.rest.drone.entities.DroneModel;
import com.zak.drones.rest.drone.entities.DroneState;
import com.zak.drones.rest.drone.exceptions.DroneException;
import com.zak.drones.rest.drone.repositories.DroneRepository;
import com.zak.drones.rest.drone.services.validation.SerialNumberValidator;
import com.zak.drones.rest.drone.services.validation.WeightValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    private WeightValidator weightValidator;
    private SerialNumberValidator serialNumberValidator;

    public DroneService(){
        this.serialNumberValidator = new SerialNumberValidator();
        this.weightValidator = new WeightValidator();
    }

    @Transactional
    public Drone createDrone(CreateOrUpdateDroneDTO createDroneDTO) {
        if (createDroneDTO == null) {
            throw new DroneException("Drone data cannot be null");
        }

        weightValidator.checkWeight(createDroneDTO.getWeightLimit());
        serialNumberValidator.checkSerialNumber(createDroneDTO.getSerialNumber());

        // create the Drone
        Drone drone = new Drone();
        drone.setSerialNumber(createDroneDTO.getSerialNumber());

        DroneModel model = DroneModel.getValidDroneModel(createDroneDTO.getModel());
        drone.setModel(model);

        drone.setWeightLimit(createDroneDTO.getWeightLimit());
        drone.setBatteryCapacity(createDroneDTO.getBatteryCapacity());

        DroneState state = DroneState.getValidDroneState(createDroneDTO.getState());
        drone.setState(state);

        Drone createdDrone = droneRepository.save(drone);

        log.info(String.format("Drone %s has been created.", createdDrone.getId()));
        return createdDrone;
    }

    public List<DroneDTO> getDronePresentationList() {
        ArrayList<DroneDTO> listDto = new ArrayList<>();
        Iterable<Drone> list = getDroneList();
        list.forEach(drone -> listDto.add(new DroneDTO(drone)));
        return listDto;
    }

    public Iterable<Drone> getDroneList() {
        return droneRepository.findAll();
    }

    public Drone getDroneById(Long id){
        Optional<Drone> droneOptional = droneRepository.findById(id);
        if (!droneOptional.isPresent()){
            throw new DroneException(String.format("The drone with id %s id not found", id));
        }
        return droneOptional.get();
    }

    public Drone getDroneBySerialNumber(String serialNumber){
        Optional<Drone> droneOptional = droneRepository.findBySerialNumber(serialNumber);
        if (!droneOptional.isPresent()){
            throw new DroneException(String.format("The drone with id %s serial number not found", serialNumber));
        }
        return droneOptional.get();
    }

    @Transactional
    public Drone updateDrone(Long id, CreateOrUpdateDroneDTO updateDroneDTO) {
        if (id == null) {
            throw new DroneException("Id cannot be null");
        }
        if (updateDroneDTO == null) {
            throw new DroneException("Drone data cannot be null");
        }

        weightValidator.checkWeight(updateDroneDTO.getWeightLimit());
        serialNumberValidator.checkSerialNumber(updateDroneDTO.getSerialNumber());

        Drone drone = getDroneById(id);

        // update the Drone
        if (Objects.nonNull(updateDroneDTO.getSerialNumber())){
            drone.setSerialNumber(updateDroneDTO.getSerialNumber());
        }

        if (Objects.nonNull(updateDroneDTO.getModel())){
            DroneModel model = DroneModel.getValidDroneModel(updateDroneDTO.getModel());
            drone.setModel(model);
        }

        if (updateDroneDTO.getWeightLimit() != drone.getWeightLimit()){
            drone.setWeightLimit(updateDroneDTO.getWeightLimit());
        }

        if (updateDroneDTO.getBatteryCapacity() == 0 ){
            drone.setBatteryCapacity(updateDroneDTO.getBatteryCapacity());
        }

        if (Objects.nonNull(updateDroneDTO.getState())){
            DroneState state = DroneState.getValidDroneState(updateDroneDTO.getState());
            drone.setState(state);
        }

        Drone updatedDrone = droneRepository.save(drone);

        log.info(String.format("Drone %s has been updated.", updatedDrone.getId()));
        return updatedDrone;
    }

    @Transactional
    public void deleteDroneById(Long id) {
        if (id == null) {
            throw new DroneException("Drone Id cannot be null");
        }

        Drone drone = getDroneById(id);
        if (Objects.isNull(drone)) {
            throw new DroneException(String.format("Drone with Id = %s not found", id));
        }

        droneRepository.deleteById(id);
        log.info(String.format("Drone %s has been deleted.", id));
    }

    public void loadDrone(){
        Drone droneToLoad;
        List<Drone> availableDronesForLoading = getAvailableDronesForLoading();
        for (Drone drone : availableDronesForLoading) {
            Integer batteryLevel = checkDroneBatteryLevel(drone.getId());
            if (batteryLevel < 25) continue;
            droneToLoad = drone;
            break;
        }
        //load the drone

    }

    public void getLoadedItemsFromDrone(Long droneId){}

    public List<Drone> getAvailableDronesForLoading(){
        return droneRepository.findAvailableDrones(DroneState.IDLE.name());
    }

    public Integer checkDroneBatteryLevel(Long droneId){
        Drone drone = getDroneById(droneId);
        if (Objects.isNull(drone)){
            throw new DroneException(String.format("Drone with id %s not found", droneId));
        }
        return drone.getBatteryCapacity();
    }

}
