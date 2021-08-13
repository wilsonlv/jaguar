package top.wilsonlv.jaguar.commons.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

/**
 * @author lvws
 * @since 2021/8/13
 */
@Slf4j
public class PdfUtil {

    private static final String FONT_PATH;

    static {
        String fontPath = "font/sims.ttf";
        ClassPathResource resource = new ClassPathResource(fontPath);
        File file = new File("lib/" + fontPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new Error("创建字体文件夹失败");
        }

        try (FileOutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = resource.getInputStream()) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new Error(e);
        }

        FONT_PATH = file.getAbsolutePath();
    }

    /**
     * 支持html转pdf，如果文档中存在中文字符，必须body标签中添加如下样式
     * <body style="font-family:SimSun">
     */
    public static void documentStr2Pdf(String document, OutputStream out) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();

        //添加中文字体库
        ITextFontResolver font = renderer.getFontResolver();
        font.addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        renderer.setDocumentFromString(document);
        renderer.layout();
        renderer.createPDF(out);
        renderer.finishPDF();
    }

}
