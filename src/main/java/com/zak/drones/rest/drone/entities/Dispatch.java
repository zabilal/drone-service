package com.zak.drones.rest.drone.entities;

import com.zak.drones.rest.users.entities.Address;
import com.zak.drones.rest.users.entities.Gender;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="dispatch")
@Data
public class Dispatch implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Drone drone;

    @ManyToOne(cascade = CascadeType.ALL)
    private Medication medication;

    @Column(name = "dispatch_ref")
    private String dispatchRef;

    @Enumerated
    @Column(name = "status")
    private DispatchStatus status;


}
