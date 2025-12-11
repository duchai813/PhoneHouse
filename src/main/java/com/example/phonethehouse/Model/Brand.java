package com.example.phonethehouse.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String Name;

    private String Country;
}