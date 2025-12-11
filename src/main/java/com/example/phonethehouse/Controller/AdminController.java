package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.Account;
import com.example.phonethehouse.Model.Brand;
import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Model.Orders;
import com.example.phonethehouse.Repository.BrandRepository;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Repository.OrdersRepository;
import com.example.phonethehouse.Repository.AccountRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/users")
    public String usersPage(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        List<Account> users = accountRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("activePage", "users");
        return "admin-users";
    }

    // Trang thêm tài khoản
    @GetMapping("/users/add")
    public String addUserPage(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        model.addAttribute("user", new Account());
        model.addAttribute("mode", "add"); // để view biết là đang thêm
        return "admin-users-add";
    }

    // Lưu tài khoản (thêm mới hoặc cập nhật)
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") Account user,
                           HttpSession session,
                           Model model) {

        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        // Nếu là thêm mới mà username đã tồn tại
        if (!accountRepo.existsById(user.getUsername())) {
            // ok, tạo mới
            accountRepo.save(user);
        } else {
            // update: lấy account cũ rồi update field cho an toàn
            Account existing = accountRepo.findById(user.getUsername()).orElse(null);
            if (existing != null) {
                existing.setFullname(user.getFullname());
                existing.setEmail(user.getEmail());
                existing.setPhone(user.getPhone());
                existing.setRole(user.getRole());
                // nếu bạn muốn cho sửa password luôn:
                existing.setPassword(user.getPassword());
                accountRepo.save(existing);
            }
        }

        return "redirect:/admin/users";
    }

    // Sửa tài khoản
    @GetMapping("/users/edit/{username}")
    public String editUser(@PathVariable("username") String username,
                           HttpSession session,
                           Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        Account user = accountRepo.findById(username).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("mode", "edit"); // để view đổi tiêu đề/nút
        return "admin-users-add";
    }

    // Xóa tài khoản
    @GetMapping("/users/delete/{username}")
    public String deleteUser(@PathVariable("username") String username,
                             HttpSession session) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        // Có thể chặn không cho xóa chính mình, nếu muốn:
        Account current = (Account) session.getAttribute("user");
        if (current != null && current.getUsername().equals(username)) {
            // không cho xóa chính mình
            return "redirect:/admin/users";
        }

        accountRepo.deleteById(username);
        return "redirect:/admin/users";
    }

    // Đặt lại mật khẩu về mặc định
    @GetMapping("/users/reset/{username}")
    public String resetPassword(@PathVariable("username") String username,
                                HttpSession session) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        Account user = accountRepo.findById(username).orElse(null);
        if (user != null) {
            user.setPassword("123456"); // TODO: sau này nhớ mã hoá nhé
            accountRepo.save(user);
        }

        return "redirect:/admin/users";
    }
    @GetMapping("/users/export/csv")
    public void exportUsersToCSV(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        List<Account> users = accountRepo.findAll();

        PrintWriter writer = response.getWriter();
        writer.println("Username,Fullname,Email,Phone,Role");

        for (Account u : users) {
            writer.println(
                    u.getUsername() + "," +
                            u.getFullname() + "," +
                            u.getEmail() + "," +
                            u.getPhone() + "," +
                            u.getRole()
            );
        }

        writer.flush();
        writer.close();
    }
    @GetMapping("/users/export/pdf")
    public void exportUsersToPDF(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        List<Account> users = accountRepo.findAll();

        com.lowagie.text.Document pdf = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(pdf, response.getOutputStream());

        pdf.open();
        pdf.add(new com.lowagie.text.Paragraph("DANH SÁCH TÀI KHOẢN"));
        pdf.add(new com.lowagie.text.Paragraph(" ")); // dòng trống

        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(5);
        table.addCell("Username");
        table.addCell("Fullname");
        table.addCell("Email");
        table.addCell("Phone");
        table.addCell("Role");

        for (Account u : users) {
            table.addCell(u.getUsername());
            table.addCell(u.getFullname());
            table.addCell(u.getEmail());
            table.addCell(u.getPhone());
            table.addCell(u.getRole());
        }

        pdf.add(table);
        pdf.close();
    }
    private double calculateOrderTotal(Orders order) {
        if (order.getOrderDetails() == null) {
            return 0.0;
        }

        return order.getOrderDetails().stream()
                .mapToDouble(d -> {
                    Double price = d.getPrice() != null ? d.getPrice() : 0.0;
                    Integer qty = d.getQuantity() != null ? d.getQuantity() : 0;
                    return price * qty;
                })
                .sum();
    }
    @GetMapping("/revenue")
    public String revenuePage(HttpSession session, Model model) {
        String redirect = requireAdmin(session);
        if (redirect != null) return redirect;

        List<Orders> allOrders = ordersRepo.findAll();

        List<Orders> completed = allOrders.stream()
                .filter(o -> o.getStatus() != null)
                .filter(o -> o.getStatus().equalsIgnoreCase("COMPLETED")
                        || o.getStatus().equalsIgnoreCase("SUCCESS")
                        || o.getStatus().equalsIgnoreCase("ĐÃ GIAO"))
                .toList();

        double totalRevenue = completed.stream()
                .mapToDouble(this::calculateOrderTotal)
                .sum();

        Map<java.time.LocalDate, Double> revenueByDate = new java.util.TreeMap<>();
        Map<Integer, Double> orderTotalMap = new java.util.HashMap<>();

        for (Orders o : completed) {
            double orderTotal = calculateOrderTotal(o);
            orderTotalMap.put(o.getId(), orderTotal);

            if (o.getCreateDate() != null) {
                java.time.LocalDate day = o.getCreateDate().toLocalDate();
                revenueByDate.merge(day, orderTotal, Double::sum);
            }
        }

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("revenueByDate", revenueByDate);
        model.addAttribute("orders", completed);
        model.addAttribute("orderTotalMap", orderTotalMap);
        model.addAttribute("activePage", "revenue");

        return "admin-revenue";
    }
}