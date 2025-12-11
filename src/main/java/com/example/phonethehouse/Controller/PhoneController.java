package com.example.phonethehouse.Controller;

import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Repository.PhoneRepository;
import com.example.phonethehouse.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PhoneController {
    @Autowired
    PhoneRepository phoneRepo;

    @Autowired
    CartService cart;

    @GetMapping("/phone/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {

        Phone phone = phoneRepo.findById(id).orElse(null);
        if (phone == null) {
            return "redirect:/";    // hoặc 404
        }

        // Lấy tất cả biến thể cùng model: cùng tên + ROM + RAM (khác màu)
        List<Phone> allVariants = phoneRepo.findByNameContainingAndRomAndRam(
                phone.getName(),
                phone.getRom(),
                phone.getRam()
        );

        // Gộp theo màu, mỗi màu chọn 1 bản "tốt nhất"
        Map<String, Phone> variantsByColor = allVariants.stream()
                .collect(Collectors.toMap(
                        Phone::getColor,
                        p -> p,
                        (existing, replacement) -> {
                            // Ưu tiên: còn hàng -> giá thấp hơn
                            if (existing.getQuantity() == 0 && replacement.getQuantity() > 0) {
                                return replacement;
                            }
                            if (existing.getQuantity() > 0 && replacement.getQuantity() == 0) {
                                return existing;
                            }
                            if (replacement.getPrice() < existing.getPrice()) {
                                return replacement;
                            }
                            return existing;
                        }
                ));

        // Đưa ra view dạng List cho dễ lặp
        List<Phone> variantList = new ArrayList<>(variantsByColor.values());

        model.addAttribute("phone", phone);
        model.addAttribute("variants", variantList);   // list các màu

        return "phone-detail";
    }


}
//    @Autowired
//    PhoneRepository phoneRepo;
//
//    @Autowired
//    CartService cart;
//
//    @GetMapping("/phone/{id}")
//    public String productDetail(@PathVariable Integer id, Model model) {
//
//        Phone phone = phoneRepo.findById(id).orElse(null);
//
//        if (phone == null) return "redirect:/";
//
//        // Lấy tất cả phiên bản màu có cùng tên
//        List<Phone> variants = phoneRepo.findByName(phone.getName());
//
//        model.addAttribute("phone", phone);
//        model.addAttribute("variants", variants);
//        List<Phone> romVariants = variants.stream()
//                .filter(p -> p.getRom() != null)
//                .collect(Collectors.toMap(
//                        Phone::getRom,          // key: ROM (64GB, 128GB…)
//                        p -> p,                 // value: Phone
//                        (p1, p2) -> p1,         // nếu trùng ROM thì giữ cái đầu
//                        LinkedHashMap::new      // giữ đúng thứ tự
//                ))
//                .values()
//                .stream()
//                .toList();
//
//        model.addAttribute("romVariants", romVariants);
//        List<Phone> ramVariants = variants.stream()
//                .filter(p -> p.getRam() != null)
//                .collect(Collectors.toMap(
//                        Phone::getRam,      // key: RAM (4GB, 8GB...)
//                        p -> p,             // value: Phone
//                        (p1, p2) -> p1,     // trùng RAM thì giữ cái đầu
//                        LinkedHashMap::new  // giữ thứ tự
//                ))
//                .values()
//                .stream()
//                .toList();
//
//        model.addAttribute("ramVariants", ramVariants);
//
//        return "phone-detail";
//    }