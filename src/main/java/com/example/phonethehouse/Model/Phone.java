package com.example.phonethehouse.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Phone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")      // cột Name trong SQL
    private String name;        // field name trong Java

    @Column(name = "Price")
    private Double price;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Image")
    private String image;

    @Column(name = "Description")
    private String description;

    @Column(name = "Rom")
    private String rom;

    @Column(name = "Ram")
    private String ram;

    @Column(name = "Screen")
    private String screen;

    @Column(name = "Battery")
    private String battery;

    @Column(name = "Color")
    private String color;


    @ManyToOne
    @JoinColumn(name = "BrandId")
    private Brand brand;
}
