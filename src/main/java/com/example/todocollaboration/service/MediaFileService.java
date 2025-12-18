package com.example.todocollaboration.service;

import com.example.todocollaboration.entity.MediaFile;
import java.util.List;

public interface MediaFileService {
    
    MediaFile uploadMediaFile(MediaFile mediaFile);
    MediaFile getMediaFileById(Long id);
    List<MediaFile> getMediaFilesByUserId(Long userId);
    List<MediaFile> getMediaFilesByStatus(String status);
    MediaFile updateMediaFileStatus(Long id, String status);
    void deleteMediaFile(Long id);

}