package com.project.auction.service;

import com.project.auction.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
}
