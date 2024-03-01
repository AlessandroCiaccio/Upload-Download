package com.example.demo.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${fileRepo}")
    private String fileRepo;

    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;

        File finalFolder = new File(fileRepo);
        if (!finalFolder.exists()) throw new IOException("Final folder doesn't exist");
        if (!finalFolder.isDirectory()) throw new IOException(("Final folder isn't a directory"));

        File destination = new File(fileRepo + "/" + completeFileName);
        if (destination.exists()) throw new IOException("File conflict");

        file.transferTo(destination);
        return completeFileName;
    }
    public byte[] download(String fileName) throws IOException{
        File fileToDownload = new File(fileRepo+"/"+fileName);
        if (!fileToDownload.exists()) throw new IOException("File doesn't exist");
        return IOUtils.toByteArray(new FileInputStream(fileToDownload));
    }
    public Boolean remove(String fileName) throws IOException{
        File fileToRemove = new File(fileRepo+"/"+fileName);
        return fileToRemove.delete();
    }
}
