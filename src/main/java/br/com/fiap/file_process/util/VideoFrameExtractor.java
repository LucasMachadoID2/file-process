package br.com.fiap.file_process.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class VideoFrameExtractor {

    public static void extractFrames(File video, File outputDir) throws Exception {

        if (video == null || !video.exists()) {
            throw new IllegalArgumentException("Arquivo de vídeo não existe");
        }

        if (video.length() == 0) {
            throw new IllegalArgumentException("Arquivo de vídeo está vazio");
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(video);
        grabber.setOption("analyzeduration", "10000000");
        grabber.setOption("probesize", "10000000");
        grabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        int frameNumber = 0;
        int frameRate = Math.max(1, (int) grabber.getFrameRate());

        Frame imageFrame;
        while ((imageFrame = grabber.grabImage()) != null) {

            if (frameNumber % frameRate == 0) {
                BufferedImage image = converter.convert(imageFrame);

                if (image != null) {
                    File output =
                            new File(outputDir, "frame_" + frameNumber + ".jpg");

                    ImageIO.write(image, "jpg", output);
                }
            }

            frameNumber++;
        }

        grabber.stop();
        grabber.release();
    }
}
