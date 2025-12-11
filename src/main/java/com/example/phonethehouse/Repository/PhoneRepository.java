package com.example.phonethehouse.Repository;

import com.example.phonethehouse.Model.Phone;
import com.example.phonethehouse.Model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {

    // Lấy theo brand (không phân trang)
    List<Phone> findByBrand(Brand brand);

    // Lấy theo brand (có phân trang)
    Page<Phone> findByBrand(Brand brand, Pageable pageable);

    // Tìm theo tên (chính xác)
    List<Phone> findByName(String name);

    // Tìm theo tên gần đúng + ROM + RAM
    List<Phone> findByNameContainingAndRomAndRam(
            String name,
            String rom,
            String ram
    );

    // Search theo keyword (phân trang, không phân biệt hoa thường)
    Page<Phone> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Lấy tất cả có phân trang
    Page<Phone> findAll(Pageable pageable);

    // Tìm các biến thể cùng model (bỏ màu sắc), cùng ROM + RAM
    @Query("SELECT p FROM Phone p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :baseName, '%')) " +
            "AND p.rom = :rom AND p.ram = :ram")
    List<Phone> findVariantsByModel(
            @Param("baseName") String baseName,
            @Param("rom") String rom,
            @Param("ram") String ram
    );
}
