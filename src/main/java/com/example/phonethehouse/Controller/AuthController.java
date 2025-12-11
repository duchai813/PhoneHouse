package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.Account;
import com.example.phonethehouse.Repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Account acc = accountRepo.findByUsernameAndPassword(username, password);

        if (acc == null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "login";
        }

        // Lưu user vào session
        session.setAttribute("user", acc);

        // PHÂN QUYỀN
        if ("ADMIN".equalsIgnoreCase(acc.getRole())) {
            return "redirect:/admin";   // chuyển admin vào trang quản trị
        }

        return "redirect:/"; // user thường -> về trang chủ
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "register"; // trỏ tới file register.html
    }

    // Xử lý submit form đăng ký
    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("account") Account account,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        // 1. Kiểm tra trùng username
        if (accountRepo.existsById(account.getUsername())) {
            model.addAttribute("error", "Username đã tồn tại, hãy chọn tên khác.");
            return "register";
        }

        // 2. Kiểm tra password nhập lại
        if (!account.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu nhập lại không khớp.");
            return "register";
        }

        // 3. Set role mặc định cho tài khoản mới
        account.setRole("USER");

        // TODO: sau này bạn có thể mã hóa mật khẩu ở đây

        // 4. Lưu vào DB
        accountRepo.save(account);

        // 5. Chuyển về trang login + hiển thị thông báo
        return "redirect:/login?registered=true";
    }
}
