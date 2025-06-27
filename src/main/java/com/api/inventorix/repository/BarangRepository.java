package com.api.inventorix.repository;

import com.api.inventorix.model.Barang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BarangRepository extends JpaRepository<Barang, String> {

    Page<Barang> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
    Optional<Barang> findByNameIgnoreCase(String name);

}