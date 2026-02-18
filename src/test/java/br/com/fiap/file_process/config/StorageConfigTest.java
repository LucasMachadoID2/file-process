package br.com.fiap.file_process.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;

class StorageConfigTest {

    @Test
    void shouldCreateS3ClientWithCorrectRegion() {

        StorageConfig config = new StorageConfig();

        S3Client client = config.s3Client();

        assertNotNull(client);
        assertEquals(Region.US_EAST_1, client.serviceClientConfiguration().region());
    }
}
