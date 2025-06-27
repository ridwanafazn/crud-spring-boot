package com.api.inventorix.controller;

import com.api.inventorix.model.Barang;
import com.api.inventorix.service.BarangService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/barang")
public class BarangController {

    @Autowired
    private BarangService barangService;

    @GetMapping
    public ResponseEntity<Page<Barang>> getAllBarang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String category) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Barang> barangPage = barangService.getAllBarang(category, pageable);
        return ResponseEntity.ok(barangPage);
    }

    @GetMapping("/by-id")
    public ResponseEntity<Barang> getBarangById(@RequestParam String id) {
        Optional<Barang> barang = barangService.getBarangById(id);
        return barang.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Barang> createBarang(@Valid @RequestBody Barang barang) {
        Barang created = barangService.saveBarang(barang);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping
    public ResponseEntity<Barang> updateBarang(
            @RequestParam String id,
            @Valid @RequestBody Barang barang) {
        Optional<Barang> updated = barangService.updateBarang(id, barang);
        return updated.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteBarang(@RequestParam String id) {
        boolean deleted = barangService.deleteBarang(id);
        Map<String, String> response = new HashMap<>();

        if (deleted) {
            response.put("message", "Barang dengan berhasil dihapus.");
            return ResponseEntity.ok(response); // 200 OK + pesan
        } else {
            response.put("message", "Barang dengan tidak ditemukan.");
            return ResponseEntity.status(404).body(response); // 404 Not Found + pesan
        }
    }

}