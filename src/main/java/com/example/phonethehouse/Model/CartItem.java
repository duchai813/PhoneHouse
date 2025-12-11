package com.example.phonethehouse.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Integer phoneId;
    private String name;
    private String image;
    private Double price;
    private Integer quantity;

    public Double getTotal() {
        return price * quantity;
    }
}
