package com.zak.drones.rest.drone.repositories;

import com.zak.drones.rest.drone.entities.Drone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends CrudRepository<Drone, Long> {

    @Query(value = "SELECT u FROM Drone u WHERE u.serialNumber = :serialNumber")
    Optional<Drone> findBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT u FROM Drone u WHERE u.state = :state")
    List<Drone> findAvailableDrones(@Param("state") String state);
}
