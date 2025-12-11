package com.example.phonethehouse.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String Address;

    private Date CreateDate;

    private String Status;

    // Many orders belong to 1 account
    @ManyToOne
    @JoinColumn(name = "Username")
    private Account account;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
