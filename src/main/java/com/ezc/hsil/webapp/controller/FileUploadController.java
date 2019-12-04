package com.ezc.hsil.webapp.controller;

import com.ezc.hsil.webapp.model.DBFile;
import com.ezc.hsil.webapp.model.UploadFileResponse;
import com.ezc.hsil.webapp.service.DBFileStorageService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value="/uploads")
@Slf4j
@Controller
public class FileUploadController {

    

    @Autowired
    private DBFileStorageService dbFileStorageService;
    
  
    @RequestMapping(value = "/doUpload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String upload(@RequestParam MultipartFile file) {
    	log.debug(file+"::file:12:"); 
    	DBFile dbFile=dbFileStorageService.uploadFile(file);
        return "redirect:/success.html";


       
    }
       
    @PostMapping(value = { "/uploadFile" }, consumes = { "multipart/form-data" })
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
    	log.debug(file+"::file::"); 
    	
        DBFile dbFile = dbFileStorageService.storeFile(file);
        int id=dbFile.getId();
        String idStr=id+"";
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(idStr) 
                .toUriString();

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file)) 
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DBFile dbFile = dbFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

}