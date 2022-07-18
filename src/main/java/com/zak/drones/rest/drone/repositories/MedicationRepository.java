package com.zak.drones.rest.drone.repositories;

import com.zak.drones.rest.drone.entities.Medication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MedicationRepository extends CrudRepository<Medication, Long> {
    Optional<Medication> findByCode(String medicationCode);
}
