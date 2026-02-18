package br.com.fiap.file_process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FileProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessApplication.class, args);
	}

}
