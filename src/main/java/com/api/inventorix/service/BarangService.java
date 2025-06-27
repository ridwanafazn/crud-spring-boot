package com.api.inventorix.service;

import com.api.inventorix.model.Barang;
import com.api.inventorix.repository.BarangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BarangService {

    @Autowired
    private BarangRepository barangRepository;

    public Page<Barang> getAllBarang(String category, Pageable pageable) {
        if (category == null || category.isBlank()) {
            return barangRepository.findAll(pageable);
        } else {
            return barangRepository.findByCategoryContainingIgnoreCase(category, pageable);
        }
    }

    public Optional<Barang> getBarangById(String id) {
        return barangRepository.findById(id);
    }

    public Barang saveBarang(Barang barang) {
    // Cek apakah nama barang sudah ada (case insensitive)
    Optional<Barang> existing = barangRepository.findByNameIgnoreCase(barang.getName());
    if (existing.isPresent()) {
        // Jika sudah ada, lempar exception agar bisa ditangani di controller/global handler
        throw new IllegalArgumentException("Barang dengan nama '" + barang.getName() + "' sudah tersedia.");
    }
    if (barang.getName() == null || barang.getName().isBlank()) {
    throw new IllegalArgumentException("Nama barang wajib diisi");
    }
    if (barang.getCategory() == null || barang.getCategory().isBlank()) {
        throw new IllegalArgumentException("Kategori barang wajib diisi");
    }

    // Isi otomatis tanggal hari ini jika belum diisi
    if (barang.getDateOfEntry() == null) {
        barang.setDateOfEntry(LocalDate.now());
    }

    // Simpan barang ke repository
    return barangRepository.save(barang);
}


    public Optional<Barang> updateBarang(String id, Barang barangDetails) {
    return barangRepository.findById(id).map(barang -> {
        // Update name hanya jika tidak null dan tidak blank
        if (barangDetails.getName() != null && !barangDetails.getName().isBlank()) {
            barang.setName(barangDetails.getName());
        }

        // Update category hanya jika tidak null dan tidak blank
        if (barangDetails.getCategory() != null && !barangDetails.getCategory().isBlank()) {
            barang.setCategory(barangDetails.getCategory());
        }

        // Update stock jika tidak null (boleh 0)
        if (barangDetails.getStock() != null) {
            barang.setStock(barangDetails.getStock());
        }

        // Update dateOfEntry jika tidak null
        if (barangDetails.getDateOfEntry() != null) {
            barang.setDateOfEntry(barangDetails.getDateOfEntry());
        }

        return barangRepository.save(barang);
    });
}



    public boolean deleteBarang(String id) {
        return barangRepository.findById(id).map(barang -> {
            barangRepository.delete(barang);
            return true;
        }).orElse(false);
    }
}