package com.halfacode.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.halfacode.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class S3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;
    private static String bucketName = "halfa-store-s3";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getImageUrl(String imageName) {
        try {
            // Set S3 object key based on the desired path and file name in the bucket
            String s3ObjectKey = "images/" + imageName;

            // Generate a pre-signed URL to access the object directly from S3
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 5; // Set the expiration time for the pre-signed URL to 5 minutes

            // Construct the pre-signed URL manually
            String bucketEndpoint = "https://" + bucketName + ".s3.amazonaws.com"; // Change to your bucket endpoint
            String presignedUrl = bucketEndpoint + "/" + s3ObjectKey + "?X-Amz-Expires=300&X-Amz-Date=" + expTimeMillis;

            return presignedUrl;
        } catch (AmazonServiceException e) {
            // Handle exceptions as needed
            throw new RuntimeException("An error occurred while generating the pre-signed URL for the image from S3", e);
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    public ApiResponse<String> uploadFile(String folderName, String fileName, InputStream inputStream) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folderName + "/" + fileName)
                    .build();

            int contentLength = inputStream.available();
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));

            // Return the S3 object key for further usage, e.g., storing it in the database
            String s3ObjectKey = folderName + "/" + fileName;
            return new ApiResponse<>(HttpStatus.OK.value(), s3ObjectKey, null, LocalDateTime.now());
        } catch (IOException ex) {
            LOGGER.error("Could not read the image file", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while reading the image file", LocalDateTime.now());
        } catch (AmazonServiceException ex) {
            LOGGER.error("Could not upload file to Amazon S3", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while saving the image", LocalDateTime.now());
        }
    }

    public static void deleteFile(String fileName) {
        S3Client client = S3Client.builder().build();

        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName)
                .key(fileName).build();
        client.deleteObject(request);
    }

    public static void removeFolder(String folderName) {
        S3Client client = S3Client.builder().build();
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName).prefix(folderName + "/").build();

        ListObjectsResponse response = client.listObjects(listRequest);

        List<S3Object> contents = response.contents();

        ListIterator<S3Object> listIterator = contents.listIterator();

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName)
                    .key(object.key()).build();
            client.deleteObject(request);
            System.out.println("Deleted " + object.key());
        }
    }
}
