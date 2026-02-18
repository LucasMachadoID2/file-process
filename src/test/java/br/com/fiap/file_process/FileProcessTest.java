package br.com.fiap.file_process;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

class FileProcessTest {

    @Test
    void shouldInvokeSpringApplicationRun() {

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

            String[] args = {};

            FileProcessApplication.main(args);

            mocked.verify(() ->
                    SpringApplication.run(FileProcessApplication.class, args));
        }
    }
}
