package br.com.fiap.file_process;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

class FileProcessApplicationTest {

    @Test
    void shouldStartApplication() {

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

            FileProcessApplication.main(new String[]{});

            mocked.verify(() ->
                    SpringApplication.run(FileProcessApplication.class, new String[]{}));
        }
    }
}
