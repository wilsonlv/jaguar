package org.jaguar.modules.document.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.exception.CheckedException;
import org.jaguar.modules.document.mapper.DocumentMapper;
import org.jaguar.modules.document.model.Document;
import org.jaguar.modules.document.service.DocumentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文档上传表
 *
 * @author lvws
 * @date 2019-11-01 10:23:09
 */
@Validated
@RestController
@RequestMapping("/document")
@Api(tags = "文档上传表管理")
public class DocumentController extends BaseController<Document, DocumentMapper, DocumentService> {

    @ApiOperation(value = "上传文档")
    @PostMapping("/upload")
    public JsonResult<List<Document>> upload(
            @ApiParam(value = "文件", required = true) @RequestParam("files") @NotEmpty List<MultipartFile> files) {

        List<Document> documentList = service.upload(files);
        return success(documentList);
    }

    @ApiOperation(value = "下载文档")
    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @ApiParam(value = "文档全路径", required = true) @RequestParam @NotBlank String absolutePath,
                         @ApiParam(value = "是否下载") @RequestParam(required = false, defaultValue = "true") Boolean download)
            throws IOException {

        Document document = service.getByAbsolutePath(absolutePath);
        Assert.validateProperty(document, "文档路径", absolutePath);

        this.download(response, document, download);
    }

    @ApiOperation(value = "下载文档")
    @GetMapping("/download/{id}")
    public void download(HttpServletResponse response,
                         @ApiParam(value = "文档ID", required = true) @PathVariable Long id,
                         @ApiParam(value = "是否下载") @RequestParam(required = false, defaultValue = "true") Boolean download)
            throws IOException {

        Document document = service.getById(id);
        Assert.validateId(document, "文档", id);

        this.download(response, document, download);
    }

    private void download(HttpServletResponse response, Document document, boolean download) throws IOException {
        File file = new File(document.getAbsolutePath());
        if (!file.exists()) {
            throw new CheckedException("文件不存在！");
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (download) {
            String encodeFileName = URLEncoder.encode(document.getOriginalName(), StandardCharsets.UTF_8.name());

            response.setContentType("multipart/form-data;charset=" + StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;fileName=" + encodeFileName);
        }

        byte[] buffer = new byte[1024];
        try (OutputStream outputStream = response.getOutputStream();
             BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
    }

}
