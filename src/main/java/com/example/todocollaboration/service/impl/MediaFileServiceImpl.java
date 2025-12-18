package com.example.todocollaboration.service.impl;

import com.example.todocollaboration.entity.MediaFile;
import com.example.todocollaboration.mapper.MediaFileMapper;
import com.example.todocollaboration.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFileMapper mediaFileMapper;

    @Override
    @Transactional
    public MediaFile uploadMediaFile(MediaFile mediaFile) {
        Date now = new Date();
        mediaFile.setCreatedAt(now);
        mediaFile.setUpdatedAt(now);
        mediaFile.setStatus("uploading");
        
        mediaFileMapper.insert(mediaFile);
        return mediaFile;
    }

    @Override
    public MediaFile getMediaFileById(Long id) {
        return mediaFileMapper.selectById(id);
    }

    @Override
    public List<MediaFile> getMediaFilesByUserId(Long userId) {
        return mediaFileMapper.findByUserId(userId);
    }

    @Override
    public List<MediaFile> getMediaFilesByStatus(String status) {
        return mediaFileMapper.findByStatus(status);
    }

    @Override
    @Transactional
    public MediaFile updateMediaFileStatus(Long id, String status) {
        MediaFile mediaFile = mediaFileMapper.selectById(id);
        if (mediaFile != null) {
            mediaFile.setStatus(status);
            mediaFile.setUpdatedAt(new Date());
            mediaFileMapper.updateById(mediaFile);
            return mediaFile;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteMediaFile(Long id) {
        mediaFileMapper.deleteById(id);
    }

}