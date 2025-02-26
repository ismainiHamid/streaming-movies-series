package ma.streaming.upload.shared.storage;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public interface MinioService {
    void saveObject(UUID id, String objectName, InputStream inputStream, String contentType) throws Exception;

    InputStream getObject(String segment) throws Exception;

    InputStream getObjectWithRange(String segment, long start, long end) throws Exception;

    Map<String, byte[]> getObjectsWithPrefix(String prefix) throws Exception;

    long getObjectSize(String segment) throws Exception;
}
