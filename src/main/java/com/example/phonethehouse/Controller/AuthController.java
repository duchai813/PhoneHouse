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
}
