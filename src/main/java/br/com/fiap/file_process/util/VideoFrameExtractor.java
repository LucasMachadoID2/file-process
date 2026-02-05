package br.com.fiap.file_process.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class VideoFrameExtractor {

    public static void extractFrames(File video, File outputDir) throws Exception {

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(video);
        grabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        int frameNumber = 0;
        int frameRate = (int) grabber.getFrameRate();

        Frame imageFrame;
        while ((imageFrame = grabber.grabImage()) != null) {

            if (frameNumber % frameRate == 0) {
                BufferedImage image = converter.convert(imageFrame);
                File output = new File(outputDir, "frame_" + frameNumber + ".jpg");
                ImageIO.write(image, "jpg", output);
            }

            frameNumber++;
        }

        grabber.stop();
    }
}
