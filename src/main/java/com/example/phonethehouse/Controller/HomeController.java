package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Repository.BrandRepository;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Service.CartService;
import com.example.phonethehouse.Model.Brand;
import com.example.phonethehouse.Model.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BrandRepository brandRepo;

    @Autowired
    private PhoneRepository phoneRepo;

    @Autowired
    private CartService cart;

    @GetMapping({"/", "/index"})
    public String index(
            Model model,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) { // Đổi thành 12 sản phẩm/trang

        List<Brand> brands = brandRepo.findAll();
        model.addAttribute("brands", brands);

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Phone> phonePage;

        if (keyword != null && !keyword.isBlank()) {
            phonePage = phoneRepo.findByNameContainingIgnoreCase(keyword, pageable);
            model.addAttribute("keyword", keyword);

        } else if (brandId != null) {
            Brand brand = brandRepo.findById(brandId).orElse(null);
            phonePage = (brand != null)
                    ? phoneRepo.findByBrand(brand, pageable)
                    : phoneRepo.findAll(pageable);

            model.addAttribute("brandId", brandId);

        } else {
            phonePage = phoneRepo.findAll(pageable);
        }

        model.addAttribute("phones", phonePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phonePage.getTotalPages());

        model.addAttribute("cartCount", cart.getCount());
        model.addAttribute("cartAmount", cart.getAmount());

        return "index";
    }
}