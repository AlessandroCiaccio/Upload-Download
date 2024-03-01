package com.example.demo.file;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception {
        return fileStorageService.upload(file);
    }

    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) throws Exception {
        System.out.println("Downloading " + fileName);
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension){
            case "jpg":
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
        return fileStorageService.download(fileName);
    }

}
