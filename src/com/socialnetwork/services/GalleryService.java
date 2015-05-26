package com.socialnetwork.services;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.dao.GalleryDao;
import com.socialnetwork.entities.Album;
import com.socialnetwork.entities.Image;

@Service
public class GalleryService {

    @Autowired
    private GalleryDao galleryDao;

    public void saveAlbum(Album album) {
        galleryDao.saveAlbum(album);
    }

    public void saveImage(Image image) {
        galleryDao.saveImage(image);
    }

    public List<Album> getAlbumsList(int userId) {
        List<Album> albumsList = galleryDao.getAlbumsList(userId);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Album eachAlbum : albumsList) {
            Date creationDate = new Date(eachAlbum.getCreationDate());
            String formatedCreationDate = dateFormat.format(creationDate).toString();
            eachAlbum.setFormatedDate(formatedCreationDate);
        }
        
        return albumsList;
    }

    public List<Image> getImagesList(int albumId, int index) {
        List<Image> imagesList = galleryDao.getImagesList(albumId, index);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Image eachImage : imagesList) {
            Date creationDate = new Date(eachImage.getCreationDate());
            String formatedCreationDate = dateFormat.format(creationDate).toString();
            eachImage.setFormatedDate(formatedCreationDate);
        }
        
        return imagesList;
    }

    public Album getAlbum(int albumId) {
        Album album = galleryDao.getAlbum(albumId);
        return album;
    }

    public void updateAlbumPhotosCount(Album album) {
        galleryDao.updateAlbumPhotosCount(album);

    }

    public void deleteImage(int albumId, String filename) {
        galleryDao.deleteImage(albumId, filename);

    }

    public void deleteAlbum(int albumId) {
        galleryDao.deleteAlbum(albumId);
    }

    public void renameAlbum(int albumId, String newFolderName) {
        galleryDao.renameAlbum(albumId, newFolderName);

    }

    public boolean haveAvatar(String avatarPath) {
        File file = new File(avatarPath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

	public Image getImage(int imageId) {
		Image image = galleryDao.getImage(imageId);
		return image;
	}

}
