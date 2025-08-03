package com.stream.app.controller;
import com.stream.app.entities.Video;
import com.stream.app.payload.CustomMessage;
import com.stream.app.services.VideoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/videos")
@CrossOrigin("*")
public class VideoController {

    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<?> saveVideo(
            @RequestParam("file")MultipartFile file,
            @RequestParam("title")String title,
            @RequestParam("description")String description
            ){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());
       Video savedVideo =  videoService.save(video, file);
       if(savedVideo != null){
           return ResponseEntity.status(HttpStatus.OK)
                   .body(video);
       }
       else{
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomMessage.builder().message("Video uploading failed")
                           .success(false)
                           .build());
       }
    }

    //get all video
    @GetMapping
    public List<Video> getAllVideo(){
        return videoService.getAll();
    }

    //get by id
    @GetMapping("stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String videoId
    ){
        Video video = videoService.get(videoId);
        String contentType = video.getContentType();
        String filePath = video.getFilePath();
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    //get the video in chunks or byte
    @GetMapping("stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoInChunks(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        try {
            Video video = videoService.get(videoId);
            Path path = Paths.get(video.getFilePath());
            String contentType = video.getContentType() != null ? video.getContentType() : "application/octet-stream";
            long fileLength = Files.size(path);

            // If no range header, return entire file
            if (range == null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .contentLength(fileLength)
                        .body(new FileSystemResource(path));
            }

            // Parse the Range header
            String[] ranges = range.trim().replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = (ranges.length > 1 && !ranges[1].isEmpty()) ? Long.parseLong(ranges[1]) : fileLength - 1;
            if (end > fileLength - 1) end = fileLength - 1;

            long contentLength = end - start + 1;
            byte[] data = new byte[(int) contentLength];

            // Read only the requested bytes
            try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
                raf.seek(start);
                raf.readFully(data);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            headers.add("Accept-Ranges", "bytes");
            headers.setContentLength(contentLength);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
