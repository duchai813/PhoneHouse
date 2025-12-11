package com.example.phonethehouse.Service;

import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    public List<Phone> findAllVariantsByModel(String name, String rom, String ram) {
        String baseName = extractBaseName(name);
        return phoneRepository.findByNameContainingAndRomAndRam(baseName, rom, ram);
    }

    private String extractBaseName(String name) {
        // Ví dụ: "iPhone 15 Pro Max Xanh" -> "iPhone 15 Pro Max"
        // Bạn tự xử lý theo rule của bạn, tạm thời return name luôn:
        return name;
    }
}
