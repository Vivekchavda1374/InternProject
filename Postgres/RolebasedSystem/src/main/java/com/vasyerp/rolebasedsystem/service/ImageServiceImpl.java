package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Image;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.repository.ImageRepository;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;
    private final ProductRepository productRepository;
    private final UserFrontRepository userFrontRepository;

    public ImageServiceImpl(
            ImageRepository repository,
            ProductRepository productRepository,
            UserFrontRepository userFrontRepository
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.userFrontRepository = userFrontRepository;
    }

    @Override
    public String uploadImage(Long userId, Long productId, MultipartFile file) throws IOException {
        return saveOrUpdateImage(userId, productId, file);
    }

    @Override
    public String updateImage(Long userId, Long productId, MultipartFile file) throws IOException {
        return saveOrUpdateImage(userId, productId, file);
    }

    @Override
    public String deleteImage(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductEditPermission(userId)) {
            throw new RuntimeException("User does not have permission to remove product image");
        }

        if (repository.existsById(productId)) {
            repository.deleteById(productId);
            return "Product image removed successfully";
        }

        return "Product image does not exist";
    }

    @Override
    public Image getImage(Long productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Image not found for product"));
    }

    private String saveOrUpdateImage(Long userId, Long productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductEditPermission(userId)) {
            throw new RuntimeException("User does not have permission to update product image");
        }

        validateImage(file);

        boolean alreadyPresent = repository.existsById(productId);
        Image image = new Image();
        image.setId(productId);
        image.setName(resolveFileName(file, productId));
        image.setType(file.getContentType());
        image.setData(file.getBytes());
        repository.save(image);

        return alreadyPresent
                ? "Product image updated successfully"
                : "Product image uploaded successfully";
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }
    }

    private String resolveFileName(MultipartFile file, Long productId) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            return "product-" + productId + "-image";
        }
        return originalFileName;
    }

    private boolean hasProductEditPermission(Long userId) {
        return isCompany(userId) || isBranch(userId);
    }

    private boolean isCompany(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompany() == null)
                .orElse(false);
    }

    private boolean isBranch(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompany() != null)
                .orElse(false);
    }
}
