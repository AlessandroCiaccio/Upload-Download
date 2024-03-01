package com.example.demo.user;

import com.example.demo.file.dto.DownloadProfilePictureDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public UserEntity createUser(@RequestBody UserEntity userEntity) {
        return userService.createAUser(userEntity);
    }

    @PostMapping("/upload-picture/{id}")
    public UserEntity uploadProfileImage(@PathVariable Long id, @RequestParam MultipartFile image) {
        return userService.uploadProfileImage(id, image);
    }

    @GetMapping("/download-picture/{id}")
    public @ResponseBody byte[] getProfileImage(@PathVariable Long id, HttpServletResponse response) throws Exception {
        DownloadProfilePictureDTO downloadProfileDTO = userService.downloadProfileImage(id);
        String fileName = downloadProfileDTO.getUserEntity().getProfilePicture();
        if(fileName==null) throw new Exception("User doesn't have profile picture");
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension) {
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
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return downloadProfileDTO.getImage();
    }

    @DeleteMapping("/download-picture/{id}")
    public Boolean deleteProfilePicture(@PathVariable Long id) {
        return userService.deleteProfilePicture(id);
    }

    @GetMapping("/user/{id}")
    public UserEntity getSingleUser(@PathVariable Long id) {
        return userService.getSingleUser(id);
    }

    @DeleteMapping("/user/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
