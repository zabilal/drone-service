package com.zak.drones.rest.drone.repositories;

import com.zak.drones.rest.drone.entities.Dispatch;
import com.zak.drones.rest.drone.entities.Medication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DispatchRepository extends CrudRepository<Dispatch, Long> {
    @Query(value = "SELECT u from Dispatch u where u.drone.id=:droneId AND u.dispatchRef=:dispatchRef")
    List<Dispatch> findLoadedItems(@Param("droneId")Long droneId, @Param("dispatchRef")String dispatchRef);

    @Query(value = "SELECT DISTINCT u FROM Dispatch u WHERE u.dispatchRef=:dispatchRef")
    Dispatch findByDispatchRef(@Param("dispatchRef")String dispatchRef);
}
