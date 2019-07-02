package com.agencybanking.core.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@CrossOrigin("*")
@Slf4j
public class StorageController {

	@Autowired
	private ResourceProvider resourceProvider;
	
	@GetMapping("/storage/{key}")
	public void download(@PathVariable("key") String key, HttpServletResponse response) {
        try {
            InputStream inputStream = resourceProvider.stream(key);
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(inputStream, response.getOutputStream());
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @GetMapping("/storage/view/{key}")
	public void view(@PathVariable("key") String key, HttpServletResponse response) {
        try {
            InputStream inputStream = resourceProvider.stream(key);
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader("Content-Disposition", "inline; filename=\""+key+"\"");
            IOUtils.copy(inputStream, response.getOutputStream());
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @GetMapping("/sample/storage/{key}")
    public void downloadSample(@PathVariable("key") String key, HttpServletResponse response) {
        try {
            InputStream inputStream = resourceProvider.sampleStream(key);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(inputStream, response.getOutputStream());
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PostMapping("/storage/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) {
	    String key = "";
	    if (!ObjectUtils.isEmpty(multipartFile)) {
            key = this.resourceProvider.put(multipartFile,  multipartFile.getOriginalFilename());
        }
	    return ResponseEntity.ok(key);
    }
}
