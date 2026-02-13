package br.com.fiap.file_process.messaging;

import br.com.fiap.file_process.messaging.dto.VideoUploadMessage;
import br.com.fiap.file_process.service.VideoProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class VideoUploadConsumer {

    private final VideoProcessingService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VideoUploadConsumer(VideoProcessingService service) {
        this.service = service;
    }

    @RabbitListener(queues = "${rabbitmq.queue.upload}")
    public void consume(String message) throws Exception {

        VideoUploadMessage payload =
                objectMapper.readValue(message, VideoUploadMessage.class);

        byte[] videoBytes = Base64.getDecoder().decode(payload.getVideoBase64());

        service.processVideoAndUploadRabbit(
                videoBytes,
                payload.getEmail(),
                payload.getVideoId()
        );
    }
}
