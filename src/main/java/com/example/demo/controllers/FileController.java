package com.example.demo.controllers;

import com.example.demo.models.DBFile;
import com.example.demo.services.DBFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final DBFileService dbFileService;

    public FileController(DBFileService dbFileService) {
        this.dbFileService = dbFileService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("image") MultipartFile file) {
        DBFile dbFile = dbFileService.storeFile(file);

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(dbFile.getId())
//                .toUriString();
        return dbFile.getId();
    }

    @PostMapping("/uploadMultipleFiles")
    public List<String> uploadMultipleFiles(@RequestParam("images") MultipartFile[] files) {
        System.out.println(files.length);
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") String id) {
        DBFile dbFile = dbFileService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/base64/{id}")
    public String getBase64File(@PathVariable("id") String id) {
        DBFile dbFile = dbFileService.getFile(id);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+dbFile.getFileName()+"\"")
//                .body("data:" + dbFile.getFileType() + ";base64," + dbFile.getBase64Data());
        return "data:" + dbFile.getFileType() + ";base64," + dbFile.getBase64Data();
    }

}
