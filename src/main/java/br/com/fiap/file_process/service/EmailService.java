package br.com.fiap.file_process.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${aws.ses.sender}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendErrorEmail(String to, String videoId, String errorMsg) {

        String subject = "Atualização sobre o processamento do seu vídeo";

        String userFriendlyMessage = "Não foi possível ler o arquivo enviado. Certifique-se de que é um vídeo válido.";

        if (errorMsg.contains("empty") || errorMsg.contains("vazio")) {
            userFriendlyMessage = "O arquivo enviado parece estar vazio.";
        }

        String body = """
                Olá,

                Informamos que houve um problema ao processar o seu vídeo (ID: %s).

                %s

                Por favor, verifique o arquivo e tente novamente. 
                Se o problema persistir, entre em contato com nosso suporte.
                
                Atenciosamente,
                Equipe FIAP X
                """.formatted(videoId, userFriendlyMessage);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
