package com.project.auction.converter;

import com.project.auction.model.Category;
import com.project.auction.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToCategoryConverter implements Converter<String, Category> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public Category convert(String source) {
        return categoryService.findById(Long.parseLong(source));
    }
}
