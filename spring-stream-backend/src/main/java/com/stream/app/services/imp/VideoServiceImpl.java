package com.stream.app.services.imp;

import com.stream.app.entities.Video;
import com.stream.app.repositories.VideoRepository;
import com.stream.app.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Value("${folderPath}")
    String DIR;
    @PostConstruct
    public void init(){
        File file = new File(DIR);
        if(!file.exists()){
            file.mkdir();
            System.out.println("Directory is created");
        }
            System.out.println("Directory is already existed");
    }
    @Override
    public Video save(Video video, MultipartFile file) {
        try {
            String videoFileName = file.getOriginalFilename();
            String videoContentType = file.getContentType();
            InputStream videoInputStream = file.getInputStream();

            String cleanFolderPath = StringUtils.cleanPath(DIR);
            String cleanFileName = StringUtils.cleanPath(videoFileName);
            Path videoFilePath = Paths.get(cleanFolderPath, cleanFileName);
            Files.copy(videoInputStream, videoFilePath, StandardCopyOption.REPLACE_EXISTING);
            video.setFilePath(videoFilePath.toString());
            video.setContentType(videoContentType);
            return videoRepository.save(video);
        }
        catch (IOException e){
            e.printStackTrace();
        return null;
        }
    }

    @Override
    public Video get(String videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }
}
