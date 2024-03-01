package com.example.demo.file.dto;

import com.example.demo.user.UserEntity;
import lombok.Data;

@Data
public class DownloadProfilePictureDTO {
    private UserEntity userEntity;
    private byte[] image;
}
