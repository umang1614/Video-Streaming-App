package com.stream.app.services;

import com.stream.app.entities.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    //Save video
    Video save(Video video, MultipartFile file);

    //get video by id
    Video get(String videoId);

    //get by title
    Video getByTitle(String title);

    //get All video
    List<Video> getAll();
}
