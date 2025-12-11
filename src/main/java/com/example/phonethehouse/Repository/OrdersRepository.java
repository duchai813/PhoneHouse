package com.example.phonethehouse.Repository;

import com.example.phonethehouse.Model.Account;
import com.example.phonethehouse.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    List<Orders> findByAccount(Account account);

}
