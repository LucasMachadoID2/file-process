package br.com.fiap.file_process.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.File;

import org.bytedeco.javacv.Frame;


import java.awt.image.BufferedImage;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class VideoFrameExtractorTest {

    @Test
    void shouldThrowWhenVideoIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                VideoFrameExtractor.extractFrames(null, new File("out"))
        );

        assertTrue(ex.getMessage().contains("não existe"));
    }

    @Test
    void shouldThrowWhenVideoDoesNotExist() {
        File fakeVideo = new File("video-inexistente.mp4");

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                VideoFrameExtractor.extractFrames(fakeVideo, new File("out"))
        );

        assertTrue(ex.getMessage().contains("não existe"));
    }

    @Test
    void shouldThrowWhenVideoIsEmpty() throws Exception {
        File emptyVideo = File.createTempFile("empty", ".mp4");

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                VideoFrameExtractor.extractFrames(emptyVideo, new File("out"))
        );

        assertTrue(ex.getMessage().contains("vazio"));
    }

    @Test
    void shouldExecuteFrameExtractionFlow() throws Exception {

        // arquivo fake não vazio
        File video = File.createTempFile("video", ".mp4");
        video.deleteOnExit();

        // força tamanho > 0
        try (var fos = new java.io.FileOutputStream(video)) {
            fos.write(new byte[]{1});
        }

        File outputDir = new File(System.getProperty("java.io.tmpdir"), "frames_test_dir");
        if (outputDir.exists()) {
            outputDir.delete();
        }

        Frame fakeFrame = mock(Frame.class);
        BufferedImage fakeImage = mock(BufferedImage.class);

        try (MockedConstruction<FFmpegFrameGrabber> grabberMock =
                     mockConstruction(FFmpegFrameGrabber.class, (grabber, context) -> {

                         when(grabber.getFrameRate()).thenReturn(30.0);

                         // Simula 2 frames e depois fim
                         when(grabber.grabImage())
                                 .thenReturn(fakeFrame)
                                 .thenReturn(null);
                     });

             MockedConstruction<Java2DFrameConverter> converterMock =
                     mockConstruction(Java2DFrameConverter.class, (converter, context) -> {
                         when(converter.convert(any(Frame.class))).thenReturn(fakeImage);

                     });

             MockedStatic<javax.imageio.ImageIO> imageIoMock =
                     mockStatic(javax.imageio.ImageIO.class)) {

            VideoFrameExtractor.extractFrames(video, outputDir);

            FFmpegFrameGrabber grabber = grabberMock.constructed().get(0);

            verify(grabber).setOption("analyzeduration", "10000000");
            verify(grabber).setOption("probesize", "10000000");
            verify(grabber).start();
            verify(grabber).stop();
            verify(grabber).release();

            // garante que tentou escrever imagem
            imageIoMock.verify(() ->
                    javax.imageio.ImageIO.write(any(), eq("jpg"), any(File.class))
            );
        }
    }
}
