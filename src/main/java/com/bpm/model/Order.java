package com.bpm.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String externalId;
    private String status;
    private String actionOrderStatus;

}
