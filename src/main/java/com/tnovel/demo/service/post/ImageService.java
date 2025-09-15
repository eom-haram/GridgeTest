package com.tnovel.demo.service.post;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final UserService userService;
    private final Bucket bucket;

    @Transactional
    protected List<String> uploadImages(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        String username = userService.getLoggedUser().getUsername();
        Integer loginId = userService.getLoggedUser().getId();
        String path = String.format("%s/%d/", username, loginId);

        for (MultipartFile image : images) {

            long uploadTime = System.currentTimeMillis();
            String fileExtension = this.getFileExtension(image.getOriginalFilename());
            String filePath = path + uploadTime + "." + fileExtension;

            BlobId blobId = BlobId.of(bucket.getName(), filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(image.getContentType())
                    .build();

            bucket.create(filePath, image.getInputStream());
            imageUrls.add(String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), filePath));
        }

        return imageUrls;
    }

    private String getFileExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < originalFilename.length() - 1) {
            return originalFilename.substring(dotIndex + 1); // '.' 다음 문자부터 추출
        }
        throw new CustomException(ExceptionType.INVALID_FILE);
    }

}