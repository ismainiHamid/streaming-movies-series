package ma.streaming.upload.shared.storage;

import io.minio.*;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Value("${minio.config.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        io.minio.MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Bucket '{}' created successfully.", bucketName);
            } else {
                log.info("Bucket '{}' already exists.", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to ensure bucket '{}': {}", bucketName, e.getMessage(), e);
            throw new RuntimeException("Could not initialize bucket: " + bucketName, e);
        }
    }

    @Override
    public void saveObject(UUID id, String objectName, InputStream inputStream, String contentType) throws Exception {
        this.minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .headers(Map.of("X-Amz-Meta-FileId", id.toString()))
                        .build()
        );
    }

    @Override
    public InputStream getObject(String segment) throws Exception {
        return this.minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(segment)
                .build());
    }

    @Override
    public InputStream getObjectWithRange(String segment, long start, long end) throws Exception {
        StatObjectResponse statObjectResponse = this.minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(segment)
                .build()
        );
        if (start < 0 || start >= statObjectResponse.size() || end < start || end >= statObjectResponse.size())
            throw new BadRequestException("Invalid range");
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(segment)
                .offset(start)
                .length(end - start + 1)
                .build()
        );
    }

    @Override
    public Map<String, byte[]> getObjectsWithPrefix(String prefix) throws Exception {
        Map<String, byte[]> streams = new HashMap<>();
        Iterable<Result<Item>> objects = this.minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(this.bucketName)
                .prefix(prefix)
                .build());
        for (Result<Item> object : objects) {
            String objectName = object.get().objectName();
            streams.put(objectName, this.getObject(objectName).readAllBytes());
        }
        return streams;
    }

    @Override
    public long getObjectSize(String segment) throws Exception {
        return this.minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(segment)
                .build()
        ).size();
    }
}
