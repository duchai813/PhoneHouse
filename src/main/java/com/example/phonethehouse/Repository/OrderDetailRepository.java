package com.example.phonethehouse.Repository;

import com.example.phonethehouse.Model.OrderDetail;
import com.example.phonethehouse.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findByOrder(Orders order);
}
