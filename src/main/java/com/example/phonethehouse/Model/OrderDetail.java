package com.example.phonethehouse.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Double Price;
    private Integer Quantity;

    @ManyToOne
    @JoinColumn(name = "OrderId")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "PhoneId")
    private Phone phone;
}
