package com.example.demo.user;

import com.example.demo.file.FileStorageService;
import com.example.demo.file.dto.DownloadProfilePictureDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;

    public UserEntity createAUser(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @SneakyThrows
    public UserEntity uploadProfileImage(Long id, MultipartFile image) {
        UserEntity user = getSingleUser(id);
        if (user.getProfilePicture() != null) {
            fileStorageService.remove(user.getProfilePicture());
        }
        String fileName = fileStorageService.upload(image);
        user.setProfilePicture(fileName);
        return userRepository.saveAndFlush(user);
    }

    @SneakyThrows
    public DownloadProfilePictureDTO downloadProfileImage(Long id) {
        UserEntity user = getSingleUser(id);

        DownloadProfilePictureDTO dto = new DownloadProfilePictureDTO();
        dto.setUserEntity(user);

        if (user.getProfilePicture() == null) return dto;

        byte[] imageBytes = fileStorageService.download(user.getProfilePicture());
        dto.setImage(imageBytes);
        return dto;
    }

    @SneakyThrows
    public Boolean deleteProfilePicture(Long id) {
        UserEntity user = getSingleUser(id);
        if (user.getProfilePicture() != null) throw new Exception("Profile picture not found");
        return fileStorageService.remove(user.getProfilePicture());

    }

    @SneakyThrows
    public UserEntity getSingleUser(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (!user.isPresent()) throw new Exception("User not found");
        return user.get();
    }

    public Boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }
}
