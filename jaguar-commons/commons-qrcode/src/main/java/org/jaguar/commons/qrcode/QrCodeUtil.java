package org.jaguar.commons.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lvws
 * @since 2020/6/16
 */
public class QrCodeUtil {

    private static final Map<EncodeHintType, Object> HINTS = new HashMap<>();

    private static final String CHARACTERSET = "UTF-8";

    static {
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        HINTS.put(EncodeHintType.CHARACTER_SET, CHARACTERSET);
    }

    public static void writeToStream(OutputStream out, String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);

        MatrixToImageWriter.writeToStream(bitMatrix, content, out);
    }

}
