package com.tiembanhngot.tiem_banh_online.controller;

import com.tiembanhngot.tiem_banh_online.entity.Product;
import com.tiembanhngot.tiem_banh_online.exception.ProductNotFoundException; // Import exception
import com.tiembanhngot.tiem_banh_online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(Model model) { // Thêm Model
        model.addAttribute("currentPage", "products"); // Đặt tên trang
        List<Product> products = productService.findAllAvailableProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{name}")
    public String productDetail(@PathVariable String name, Model model) { // Thêm Model
         model.addAttribute("currentPage", "products"); // Trang chi tiết vẫn thuộc mục sản phẩm
        Product product = productService.findByName(name)
                // Ném lỗi 404 nếu không tìm thấy
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm với ten: " + name));
        model.addAttribute("product", product);
        return "product/detail";
    }
}