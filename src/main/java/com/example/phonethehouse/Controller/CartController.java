package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.CartItem;
import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cart;

    @Autowired
    PhoneRepository phoneRepo;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getAmount());
        model.addAttribute("count", cart.getCount());
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") Integer id) {
        Phone phone = phoneRepo.findById(id).orElse(null);
        if (phone != null) {
            CartItem item = new CartItem(
                    phone.getId(),
                    phone.getName(),
                    phone.getImage(),
                    phone.getPrice(),
                    1
            );
            cart.add(item);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Integer id,
                         @RequestParam("qty") Integer qty) {
        cart.update(id, qty);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id) {
        cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clear() {
        cart.clear();
        return "redirect:/cart";
    }
}
