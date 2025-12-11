package com.example.phonethehouse.Service;

import com.example.phonethehouse.Model.CartItem;

import java.util.Collection;

public interface CartService {

    void add(CartItem item);

    void update(Integer phoneId, Integer quantity);

    void remove(Integer phoneId);

    void clear();

    Collection<CartItem> getItems();

    int getCount();

    double getAmount();
}
