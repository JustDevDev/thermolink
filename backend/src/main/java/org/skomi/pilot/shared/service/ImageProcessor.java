package org.skomi.pilot.shared.service;

import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;

@Service
public class ImageProcessor {

    /**
     * Retrieves an avatar image from the specified URL and converts it into a Base64-encoded string
     * with a "data:image/jpeg;base64," prefix.
     *
     * @param imageUrl the URL of the image to be fetched
     * @return the image as a Base64-encoded string with the "data:image/jpeg;base64," prefix
     * @throws Exception if an error occurs while fetching or processing the image
     */
    public String getAvatarFromUrl(String imageUrl) throws Exception {
        // Connect to URL and get input stream
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            // Read bytes into buffer
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Convert to base64
            byte[] imageBytes = outputStream.toByteArray();
            String base64String = Base64.getEncoder().encodeToString(imageBytes);

            // Add JPEG data URI prefix
            return "data:image/jpeg;base64," + base64String;
        }
    }

    /**
     * Processes {@code userAvatar} (a Base64 string) and returns a compressed,
     * centered-square JPEG also wrapped in <pre>data:image/jpeg;base64,</pre>.
     */
    public String processAvatarImage(String userAvatar, int targetSize) throws IOException {

        /* decode ---------------------------------------------------------- */
        String b64 = userAvatar.replaceFirst("^data:image/[^;]+;base64,", "");
        byte[] raw = Base64.getDecoder().decode(b64);

        BufferedImage original = ImageIO.read(new ByteArrayInputStream(raw));

        /* crop to centered square  ----------------------------------- */
        BufferedImage squared = cropToSquare(original);

        /* resize (optional)  ----------------------------------------- */
        BufferedImage resized = resize(squared, targetSize, targetSize);

        /* compress & encode  ----------------------------------------- */
        byte[] jpeg = compress(resized, 0.85f);         // 85 % quality
        return "data:image/jpeg;base64," +
                Base64.getEncoder().encodeToString(jpeg);
    }

    /* --------------------------------------------------------------------- */
    /*  helpers                                                              */
    /* --------------------------------------------------------------------- */

    /**
     * Crops the image to a centered square – maintains aspect ratio,
     * no up-scaling, no distortion.
     */
    private BufferedImage cropToSquare(BufferedImage src) {
        int size = Math.min(src.getWidth(), src.getHeight());
        int x = (src.getWidth() - size) / 2;
        int y = (src.getHeight() - size) / 2;
        return src.getSubimage(x, y, size, size);
    }

    /**
     * Resizes the given image to the specified width and height while maintaining
     * a smooth scaling algorithm for better quality.
     *
     * @param src the source image to be resized
     * @param w   the target width of the resized image
     * @param h   the target height of the resized image
     * @return a new BufferedImage resized to the specified dimensions
     */
    private BufferedImage resize(BufferedImage src, int w, int h) {
        Image tmp = src.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = dst.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(tmp, 0, 0, null);
        g2.dispose();
        return dst;
    }

    /**
     * Compresses a given BufferedImage into a JPEG format with the specified quality.
     *
     * @param img     the BufferedImage to be compressed
     * @param quality the compression quality, where 1.0 represents maximum quality and 0.0 represents maximum compression
     * @return a byte array containing the compressed JPEG image
     * @throws IOException if an error occurs during the compression process
     */
    private byte[] compress(BufferedImage img, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(baos)) {

            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            writer.write(null, new IIOImage(img, null, null), param);
            writer.dispose();
            return baos.toByteArray();
        }
    }

}
