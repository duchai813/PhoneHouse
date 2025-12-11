package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.*;
import com.example.phonethehouse.Repository.AccountRepository;
import com.example.phonethehouse.Repository.OrderDetailRepository;
import com.example.phonethehouse.Repository.OrdersRepository;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class OderController {

    @Autowired
    CartService cart;

    @Autowired
    OrdersRepository ordersRepo;

    @Autowired
    OrderDetailRepository orderDetailRepo;

    @Autowired
    PhoneRepository phoneRepo;

    @Autowired
    AccountRepository accountRepo;

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getAmount());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String doCheckout(@RequestParam String address,
                             HttpSession session,
                             Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (cart.getCount() == 0) {
            model.addAttribute("message", "Giỏ hàng đang trống!");
            return "redirect:/cart";
        }

        // Tạo order
        Orders order = new Orders();
        order.setAccount(user);
        order.setAddress(address);
        order.setCreateDate(Date.valueOf(LocalDate.now()));
        order.setStatus("Pending");
        Orders savedOrder = ordersRepo.save(order);

        // Lưu order detail
        for (CartItem item : cart.getItems()) {
            Phone phone = phoneRepo.findById(item.getPhoneId()).orElse(null);
            if (phone == null) continue;

            OrderDetail od = new OrderDetail();
            od.setOrder(savedOrder);
            od.setPhone(phone);
            od.setPrice(item.getPrice());
            od.setQuantity(item.getQuantity());
            orderDetailRepo.save(od);

            // trừ tồn
            phone.setQuantity(phone.getQuantity() - item.getQuantity());
            phoneRepo.save(phone);
        }

        cart.clear();
        model.addAttribute("orderId", savedOrder.getId());
        return "order-success";
    }
    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy tất cả đơn hàng theo user
        List<Orders> orders = ordersRepo.findByAccount(user);
        model.addAttribute("orders", orders);

        return "order-history";
    }
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable("id") Integer id,
                              HttpSession session,
                              Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Orders order = ordersRepo.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/orders"; // hoặc trang 404 custom
        }

        // Không cho xem đơn của người khác
        if (!order.getAccount().getUsername().equals(user.getUsername())) {
            return "redirect:/orders";
        }

        // Lấy danh sách chi tiết đơn
        java.util.List<OrderDetail> items = orderDetailRepo.findByOrder(order);

        model.addAttribute("order", order);
        model.addAttribute("items", items);

        return "order-detail"; // trỏ tới file order-detail.html
    }

}
