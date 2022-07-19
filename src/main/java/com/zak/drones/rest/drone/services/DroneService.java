package com.zak.drones.rest.drone.services;

import com.zak.drones.rest.drone.dtos.DispatchListDTO;
import com.zak.drones.rest.drone.dtos.DroneDTO;
import com.zak.drones.rest.drone.dtos.requests.LoadDroneDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateDroneDTO;
import com.zak.drones.rest.drone.entities.Dispatch;
import com.zak.drones.rest.drone.entities.DispatchStatus;
import com.zak.drones.rest.drone.entities.Drone;
import com.zak.drones.rest.drone.entities.DroneModel;
import com.zak.drones.rest.drone.entities.DroneState;
import com.zak.drones.rest.drone.entities.Medication;
import com.zak.drones.rest.drone.exceptions.DroneException;
import com.zak.drones.rest.drone.repositories.DispatchRepository;
import com.zak.drones.rest.drone.repositories.DroneRepository;
import com.zak.drones.rest.drone.services.validation.SerialNumberValidator;
import com.zak.drones.rest.drone.services.validation.WeightValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DispatchRepository dispatchRepository;

    @Autowired
    private MedicationService medicationService;

    private WeightValidator weightValidator;
    private SerialNumberValidator serialNumberValidator;

    public DroneService() {
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

    public List<DroneDTO> getAllDrones() {
        ArrayList<DroneDTO> listDto = new ArrayList<>();
        Iterable<Drone> list = getDroneList();
        list.forEach(drone -> listDto.add(new DroneDTO(drone)));
        return listDto;
    }

    public Iterable<Drone> getDroneList() {
        return droneRepository.findAll();
    }

    public Drone getDroneById(Long id) {
        Optional<Drone> droneOptional = droneRepository.findById(id);
        if (!droneOptional.isPresent()) {
            throw new DroneException(String.format("The drone with id %s id not found", id));
        }
        return droneOptional.get();
    }

    public Drone getDroneBySerialNumber(String serialNumber) {
        Optional<Drone> droneOptional = droneRepository.findBySerialNumber(serialNumber);
        if (!droneOptional.isPresent()) {
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
        if (Objects.nonNull(updateDroneDTO.getSerialNumber())) {
            drone.setSerialNumber(updateDroneDTO.getSerialNumber());
        }

        if (Objects.nonNull(updateDroneDTO.getModel())) {
            DroneModel model = DroneModel.getValidDroneModel(updateDroneDTO.getModel());
            drone.setModel(model);
        }

        if (updateDroneDTO.getWeightLimit() != drone.getWeightLimit()) {
            drone.setWeightLimit(updateDroneDTO.getWeightLimit());
        }

        if (updateDroneDTO.getBatteryCapacity() == 0) {
            drone.setBatteryCapacity(updateDroneDTO.getBatteryCapacity());
        }

        if (Objects.nonNull(updateDroneDTO.getState())) {
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

    @Transactional
    public String loadDrone(LoadDroneDTO loadDroneDTO) {

        int totalMedicationWeight = 0;
        Drone droneToLoad = getDroneById(loadDroneDTO.getDroneId());
        droneToLoad.setState(DroneState.LOADING);

        Integer batteryLevel = checkDroneBatteryLevel(droneToLoad.getId());
        if (batteryLevel < 25) {
            droneToLoad.setState(DroneState.IDLE);
        }

        //load the drone
        Dispatch dispatch = new Dispatch();
        dispatch.setDispatchRef(UUID.randomUUID().toString());
        List<Medication> list = new ArrayList<>();
        Medication medication;
        for (String code : loadDroneDTO.getMedicationCodes()) {
            medication = medicationService.getMedicationByCode(code);
            totalMedicationWeight += medication.getWeight();
            if (totalMedicationWeight > droneToLoad.getWeightLimit()) {
                droneToLoad.setState(DroneState.IDLE);
                throw new DroneException("Drone weight limit exceeded, please reduce weight");
            }
            list.add(medication);
        }

        for (Medication med : list) {
            dispatch.setDrone(droneToLoad);
            dispatch.setMedication(med);
            dispatchRepository.save(dispatch);
        }
        droneToLoad.setState(DroneState.LOADED);
        droneRepository.save(droneToLoad);
        return dispatch.getDispatchRef();
    }

    public DispatchListDTO getLoadedItemsFromDrone(Long droneId, String dispatchRef) {
        List<Dispatch> loadedItems = dispatchRepository.findLoadedItems(droneId, dispatchRef);

        DispatchListDTO listDTO = new DispatchListDTO();
        listDTO.setDrone(loadedItems.get(0).getDrone());
        loadedItems.forEach((dispatch -> {
            listDTO.getMedications().add(dispatch.getMedication());
        }));

        return listDTO;
    }

    public List<Drone> getAvailableDronesForLoading() {
        return droneRepository.findAvailableDrones(DroneState.IDLE);
    }

    public Integer checkDroneBatteryLevel(Long droneId) {
        Drone drone = getDroneById(droneId);
        if (Objects.isNull(drone)) {
            throw new DroneException(String.format("Drone with id %s not found", droneId));
        }
        return drone.getBatteryCapacity();
    }

    public String dispatchDrone(String dispatchRef){
        Dispatch dispatch = dispatchRepository.findByDispatchRef(dispatchRef);
        if (Objects.isNull(dispatch)){
            throw new DroneException(String.format("No dispatch with this Ref : %s", dispatchRef));
        }
        Drone drone = dispatch.getDrone();
        drone.setState(DroneState.DELIVERING);
        droneRepository.save(drone);

        dispatch.setStatus(DispatchStatus.START);
        dispatchRepository.save(dispatch);

        return "Drone Dispatch done";
    }

    //Cron job to simulate a dispatch journey
    //of a drone. Each run updates the state
    //of the drone. after a complete journey
    //the drone batteryCapacity drops by 25%
    //and dispatch is set to COMPLETE
    @Scheduled(cron = "3 * * * * ?")
    public void droneCheckerCron() {
        System.out.println("Checking up dispatched drones");
        Iterable<Drone> drones = droneRepository.findAll();
        drones.forEach((drone -> {
            switch (drone.getState()) {
                case DELIVERING: drone.setState(DroneState.DELIVERED); break;
                case DELIVERED: drone.setState(DroneState.RETURNING); break;
                case RETURNING: drone.setState(DroneState.IDLE);
                                drone.setBatteryCapacity(drone.getBatteryCapacity() - 25); break;
                default: drone.setState(DroneState.IDLE);break;
            }
            droneRepository.save(drone);
        }));
    }

}
