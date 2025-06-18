package com.tiembanhngot.tiem_banh_online.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tiembanhngot.tiem_banh_online.entity.Category;
import com.tiembanhngot.tiem_banh_online.entity.Product;
import com.tiembanhngot.tiem_banh_online.entity.User;
import com.tiembanhngot.tiem_banh_online.repository.CategoryRepository;
import com.tiembanhngot.tiem_banh_online.repository.ProductRepository;
import com.tiembanhngot.tiem_banh_online.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    Product createProductIfNotFound(String name, String description,
                                    BigDecimal defaultPrice, String imageUrl, Category category,
                                    Map<String, BigDecimal> sizeOptions) { 
        Optional<Product> prodOpt = productRepository.findByName(name);
        if (prodOpt.isEmpty()) {
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setDescription(description);
            newProduct.setPrice(defaultPrice); 
            newProduct.setImageUrl(imageUrl);
            newProduct.setCategory(category);
            newProduct.setIsAvailable(true);
            newProduct.setCategory(category);
            newProduct.setSizeOptions(sizeOptions);
            if (sizeOptions != null && !sizeOptions.isEmpty()) {
                newProduct.setSizeOptions(new HashMap<>(sizeOptions)); 
            } else {
                 newProduct.setSizeOptions(new HashMap<>());
            }

            log.info("Creating product (DataLoader): Name='{}', Image='{}', Sizes='{}'",
                     name, newProduct.getName(), imageUrl, newProduct.getSizeOptions());
            return productRepository.save(newProduct);
        } else {
             log.info("Product with name '{}' already exists. Skipping creation.", name);
             return prodOpt.get();
        }
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Loading initial data...");

        createUserIfNotFound("admin@tiembanh.com", "Admin User", "Admin123", "0900000000", User.Role.ADMIN);

        Category banhKem = createCategoryIfNotFound("Bánh Kem", "Các loại bánh kem sinh nhật, lễ kỷ niệm");
        Category pastry = createCategoryIfNotFound("Pastry", "Bánh ngọt kiểu Âu");
        Category banhMi = createCategoryIfNotFound("Bánh Mì Ngọt", "Các loại bánh mì ăn sáng, ăn nhẹ");
        Category cookies = createCategoryIfNotFound("Cookies", "Bánh quy các loại");

        if (banhKem != null) {
            // Sản phẩm có size
            Map<String, BigDecimal> dauSizes = new HashMap<>();
            dauSizes.put("Nhỏ (18cm)", new BigDecimal("350000.00"));
            dauSizes.put("Vừa (22cm)", new BigDecimal("450000.00"));
            dauSizes.put("Lớn (25cm)", new BigDecimal("550000.00"));
            createProductIfNotFound(
                "Bánh Kem Dâu Tươi", 
                "Bánh kem mềm mịn với lớp kem tươi và dâu tây Đà Lạt.",
                new BigDecimal("350000.00"), 
                "/img/products/banhkem_dau.jpg", 
                banhKem,
                dauSizes 
            );

            createProductIfNotFound(
                "Bánh Kem Chocolate",
                "Cốt bánh chocolate ẩm, phủ ganache chocolate đậm đà.",
                new BigDecimal("380000.00"),
                "/img/products/banhkem_socola.jpg", 
                banhKem,
                null 
            );
        }

        if (pastry != null) {
            createProductIfNotFound(
                "Croissant Bơ", 
                "Bánh sừng bò ngàn lớp, thơm lừng mùi bơ Pháp.",
                new BigDecimal("30000.00"),
                "/img/products/croissant.jpg",
                pastry,
                null 
            );

            createProductIfNotFound(
                "Pain au Chocolat", 
                "Bánh mì cuộn socola đen.",
                new BigDecimal("35000.00"),
                "/img/products/PainauChocolat.jpg", 
                pastry,
                null 
            );
        }

        if (banhMi != null) {

             createProductIfNotFound(
                 "Bánh Mì Xúc Xích Phô Mai", 
                 "Bánh mì mềm kẹp xúc xích và phô mai tan chảy.",
                 new BigDecimal("25000.00"),
                 "/img/products/banh_mi_xuc_xich_pho_mai.jpg", 
                 banhMi,
                 null
             );
        }

        if (cookies != null) {
             // Sản phẩm không có size
             createProductIfNotFound(
                 "Cookies Socola Chip", 
                 "Bánh quy bơ giòn rụm với hạt socola.",
                 new BigDecimal("15000.00"),
                 "/img/products/banh_quy_socola.png", 
                 cookies,
                 null 
             );
        }

        log.info("Finished loading initial data.");
    }

    @Transactional
    User createUserIfNotFound(String email, String fullName, String rawPassword, String phone, User.Role role) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setPasswordHash(passwordEncoder.encode(rawPassword)); // Mã hóa mật khẩu
            newUser.setPhoneNumber(phone);
            newUser.setRole(role);
            log.info("Creating user: {}", email);
            return userRepository.save(newUser);
        }
        return userOpt.get();
    }

    
    @Transactional
    Category createCategoryIfNotFound(String name, String description) {
         Optional<Category> catOpt = categoryRepository.findByName(name);
         if (catOpt.isEmpty()) {
             Category newCategory = new Category();
             newCategory.setName(name);
             newCategory.setDescription(description);
             log.info("Creating category: {}", name);
             Category saved = categoryRepository.save(newCategory);
            categoryRepository.flush();
            log.info("Created category with ID = {}", saved.getCategoryId());
             return saved;
         }
         return catOpt.get();
    }
}