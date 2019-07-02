package com.agencybanking.core.storage;

import com.agencybanking.core.utils.Utils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static org.springframework.util.Assert.notNull;

@Service
//@Profile("dev")
public class LocalResourceProvider implements ResourceProvider {
    private final String SLASH = File.separator;

    @Value("${resource.storage.path:${user.home}/mbcp}")
    private String defaultDirectory;
    @Value("${resource.samplestorage.path:src\\main\\resources\\sample}")
    private String defaultSampleDirectory;

    public LocalResourceProvider() {
    }

    @SneakyThrows()
    @Override
    public String put(MultipartFile uploadedFile) {
        notNull(uploadedFile, "Uploaded file cannot be null");
        String key = uniqueKey(uploadedFile.getName());
        return put(uploadedFile, key);
    }


    @SneakyThrows()
    @Override
    public String put(MultipartFile uploadedFile, String key) {
        if (ObjectUtils.isEmpty(key)) {
            key = uniqueKey(null);
        }
        File file = buildFile(key, uploadedFile.getInputStream());
        System.out.println("New file:" + file.getPath());
        return key;
    }

    @SneakyThrows()
    @Override
    public String putBase64(String base64, String key) {
        if (StringUtils.isEmpty(key)) {
            key = uniqueKey(null);
        }
        byte[] bytes = Base64Utils.decodeFromString(Utils.nullSafeString(base64));
        File file = buildFile(key, new ByteArrayInputStream(bytes));
        System.out.println("New Base file:" + file.getPath());
        return key;
    }

    @Override
    public String putBase64(String base64) {
        return putBase64(base64, null);
    }

    private File buildFile(String relativePath, InputStream inputStream) throws IOException {
        notNull(inputStream, "Inputstream must not be null recipients build file");
        File newfile = new File(defaultDirectory + SLASH + relativePath);
        OutputStream outputStream = FileUtils.openOutputStream(newfile);
        IOUtils.copyLarge(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        return newfile;
    }

    @SneakyThrows
    @Override
    public String put(String bucket, MultipartFile uploadedFile, String key) {
        if (StringUtils.isEmpty(key)) {
            key = uniqueKey(null);
        }
        File file = buildFile(bucket + SLASH + key, uploadedFile.getInputStream());
        return key;
    }

    @SneakyThrows
    @Override
    public String put(InputStream inputStream) {
        String key = uniqueKey(null);
        buildFile(key, inputStream);
        return key;
    }

    @SneakyThrows()
    @Override
    public String putToPdf(String text, String key) {
        ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
        File file = buildFile(key, bais);
        Document document = new Document();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PdfWriter.getInstance(document, fileOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
        document.add(new Paragraph(text, font));
        document.close();
        IOUtils.closeQuietly(fileOutputStream);
        return key;
    }

    @Override
    public void remove(String key) {
        File file = new File(defaultDirectory + SLASH + key);
        FileUtils.deleteQuietly(file);
    }

    @SneakyThrows
    @Override
    public byte[] read(String key) {
        File file = new File(defaultDirectory + SLASH + key);
        return FileUtils.readFileToByteArray(file);
    }

    @SneakyThrows
    @Override
    public String readBase64(String key) {
        File file = new File(defaultDirectory + SLASH + key);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return Base64Utils.encodeToString(bytes);
    }

    @SneakyThrows
    @Override
    public InputStream stream(String key) {
        File file = new File(defaultDirectory + SLASH + key);
        return new FileInputStream(file);
    }

    @SneakyThrows
    @Override
    public File streamFile(String key) {
        return new File(defaultDirectory + SLASH + key);
    }

    @Override
    public InputStream stream(String bucket, String key) {
        return stream(bucket + SLASH + key);
    }

    @Override
    public Resource resource(String key) {
        File file = new File(defaultDirectory + SLASH + key);
        return new FileSystemResource(file);
    }

    @SneakyThrows
    @Override
    public InputStream sampleStream(String key) {
        File file = new File(defaultSampleDirectory + SLASH + key);
        return new FileInputStream(file);
    }
}
