package com.api.inventorix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @Column(length = 36)
    private String id;

    @Size(max = 100, message = "Nama barang maksimal 100 karakter")
    private String name;

    @Size(max = 50, message = "Kategori maksimal 50 karakter")
    private String category;

    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock;

    @Column(name = "date_of_entry")
    private LocalDate dateOfEntry;

    public Barang() {
    }

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(LocalDate dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }
}