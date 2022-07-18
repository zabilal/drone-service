package com.zak.drones.rest.drone.services;

import com.zak.drones.rest.drone.dtos.MedicationDTO;
import com.zak.drones.rest.drone.dtos.requests.CreateOrUpdateMedicationDTO;
import com.zak.drones.rest.drone.entities.Medication;
import com.zak.drones.rest.drone.exceptions.DroneException;
import com.zak.drones.rest.drone.exceptions.MedicationException;
import com.zak.drones.rest.drone.repositories.MedicationRepository;
import com.zak.drones.rest.drone.services.validation.MedicationNameValidator;
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
public class MedicationService {

    @Autowired
    MedicationRepository medicationRepository;

    MedicationNameValidator nameValidator;

    public MedicationService(){
        nameValidator = new MedicationNameValidator();
    }

    @Transactional
    public Medication createMedication(CreateOrUpdateMedicationDTO medicationDTO) {
        if (medicationDTO == null) {
            throw new MedicationException("Medication data cannot be null");
        }

        nameValidator.checkName(medicationDTO.getName());
        // create the Drone
        Medication medication = new Medication();
        medication.setName(medicationDTO.getName());
        medication.setCode(medication.getCode());
        medication.setWeight(medication.getWeight());
        medication.setImageUrl(medication.getImageUrl());

        Medication createdMedication = medicationRepository.save(medication);

        log.info(String.format("Medication %s has been created.", medication.getId()));
        return createdMedication;
    }

    public List<MedicationDTO> getAllMedications() {
        ArrayList<MedicationDTO> listDto = new ArrayList<>();
        Iterable<Medication> list = getMedicationList();
        list.forEach(medication -> listDto.add(new MedicationDTO(medication)));
        return listDto;
    }

    public Iterable<Medication> getMedicationList() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(Long id){
        Optional<Medication> optional = medicationRepository.findById(id);
        if (!optional.isPresent()){
            throw new MedicationException(String.format("Medication with id %s id not found", id));
        }
        return optional.get();
    }

    public Medication getMedicationByCode(String medicationCode){
        Optional<Medication> optional = medicationRepository.findByCode(medicationCode);
        if (!optional.isPresent()){
            throw new DroneException(String.format("Medication with code %s not found", medicationCode));
        }
        return optional.get();
    }

    @Transactional
    public Medication updateMedication(Long id, CreateOrUpdateMedicationDTO updateMedicationDTO) {
        if (id == null) {
            throw new MedicationException("Medication Id cannot be null");
        }
        if (Objects.isNull(updateMedicationDTO)) {
            throw new MedicationException("Medication data cannot be null");
        }

        if (Objects.nonNull(updateMedicationDTO.getName())){
            nameValidator.checkName(updateMedicationDTO.getName());
        }

        Medication medication = getMedicationById(id);

        // update the Drone
        medication.setName(updateMedicationDTO.getName());

        if (Objects.nonNull(updateMedicationDTO.getCode())){
            medication.setCode(updateMedicationDTO.getCode());
        }

        if (updateMedicationDTO.getWeight() != 0 && updateMedicationDTO.getWeight() != medication.getWeight()){
            medication.setWeight(updateMedicationDTO.getWeight());
        }

        if (Objects.nonNull(updateMedicationDTO.getImageUrl())){
            medication.setImageUrl(updateMedicationDTO.getImageUrl());
        }
        Medication updatedMedication = medicationRepository.save(medication);

        log.info(String.format("Medication with Id %s has been updated.", updatedMedication.getId()));
        return updatedMedication;
    }

    @Transactional
    public void deleteMedicationById(Long id) {
        if (id == null) {
            throw new MedicationException("Medication Id cannot be null");
        }

        Medication medication = getMedicationById(id);
        if (Objects.isNull(medication)) {
            throw new MedicationException(String.format("Medication with Id %s not found", id));
        }

        medicationRepository.deleteById(id);
        log.info(String.format("Medication with Id %s has been deleted.", id));
    }
}
