package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.Account;
import com.example.phonethehouse.Model.Brand;
import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Model.Orders;
import com.example.phonethehouse.Repository.BrandRepository;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Repository.OrdersRepository;
import com.example.phonethehouse.Repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    PhoneRepository phoneRepo;

    @Autowired
    OrdersRepository ordersRepo;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    BrandRepository brandRepo;

    // ==================== HELPER METHODS ====================

    // Kiểm tra có phải admin không
    private boolean isAdmin(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    // Nếu không phải admin -> redirect về login
    private String requireAdmin(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return null;
    }

    // ==================== DASHBOARD ====================

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        long totalPhones = phoneRepo.count();
        long totalOrders = ordersRepo.count();
        long totalUsers = accountRepo.count();

        model.addAttribute("totalPhones", totalPhones);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);

        return "admin-dashboard";
    }

    // ==================== QUẢN LÝ SẢN PHẨM ====================

    // Trang danh sách sản phẩm
    @GetMapping("/phones")
    public String phones(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        Phone phone = new Phone();
        List<Phone> phones = phoneRepo.findAll();
        List<Brand> brands = brandRepo.findAll();

        model.addAttribute("phone", phone);
        model.addAttribute("phones", phones);
        model.addAttribute("brands", brands);

        return "admin-phones";
    }

    // Sửa sản phẩm
    @GetMapping("/phones/edit/{id}")
    public String editPhone(HttpSession session, @PathVariable Integer id, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        Phone phone = phoneRepo.findById(id).orElse(new Phone());
        List<Phone> phones = phoneRepo.findAll();
        List<Brand> brands = brandRepo.findAll();

        model.addAttribute("phone", phone);
        model.addAttribute("phones", phones);
        model.addAttribute("brands", brands);

        return "admin-phones";
    }

    // Lưu sản phẩm (thêm mới hoặc cập nhật)
    @PostMapping("/phones/save")
    public String savePhone(HttpSession session, @ModelAttribute("phone") Phone phone) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        // Lấy brand theo id user chọn
        if (phone.getBrand() != null && phone.getBrand().getId() != null) {
            Brand brand = brandRepo.findById(phone.getBrand().getId()).orElse(null);
            phone.setBrand(brand);
        }

        phoneRepo.save(phone);
        return "redirect:/admin/phones";
    }

    // Xóa sản phẩm
    @GetMapping("/phones/delete/{id}")
    public String deletePhone(@PathVariable("id") Integer id, HttpSession session) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        phoneRepo.deleteById(id);
        return "redirect:/admin/phones";
    }

    // ==================== QUẢN LÝ ĐƠN HÀNG ====================

    // Trang danh sách đơn hàng
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        List<Orders> list = ordersRepo.findAll();
        model.addAttribute("orders", list);
        return "admin-orders";
    }

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/orders/status")
    public String updateOrderStatus(@RequestParam("id") Integer id,
                                    @RequestParam("status") String status,
                                    HttpSession session) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        Orders order = ordersRepo.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            ordersRepo.save(order);
        }
        return "redirect:/admin/orders";
    }
}