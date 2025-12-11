package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute("sessionUser")
    public Account sessionUser(HttpSession session) {
        return (Account) session.getAttribute("user");
    }
}
