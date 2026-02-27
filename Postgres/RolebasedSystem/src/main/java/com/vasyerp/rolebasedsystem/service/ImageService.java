package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadImage(Long userId, Long productId, MultipartFile file) throws IOException;

    String updateImage(Long userId, Long productId, MultipartFile file) throws IOException;

    String deleteImage(Long userId, Long productId);

    Image getImage(Long productId);
}
