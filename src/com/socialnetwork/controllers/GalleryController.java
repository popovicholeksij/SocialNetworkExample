package com.socialnetwork.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.socialnetwork.entities.Album;
import com.socialnetwork.entities.Image;
import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.GalleryService;
import com.socialnetwork.services.MessageService;
import com.socialnetwork.services.UserService;

@Controller
public class GalleryController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private UserService userService;
    @Autowired
    private GalleryService galleryService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/gallery.action", method = RequestMethod.GET)
    public ModelAndView getGallery(
            @RequestParam(value = "userid", required = true) int userId, 
            @CookieValue(value = "hash", required = true) String hash) {
        
        User user = userService.getUserByHash(hash);
        
        if (user == null) {
            return new ModelAndView("redirect:./");
        }

        List<Integer> galleryOwnerFriendsIdsList = userService.getFriendsIdsList(userId);
        
        if (!galleryOwnerFriendsIdsList.contains(user.getId()) && user.getId() != userId) {
            return new ModelAndView("redirect:gallery.action?userid=" + user.getId());
        }

        ModelAndView mav = new ModelAndView("gallery");

        User galleryOwner = userService.getUser(userId);
        List<Album> albumsList = galleryService.getAlbumsList(userId);
        
        mav.addObject("galleryOwner", galleryOwner);
        mav.addObject("albumsList", albumsList);
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/gallery.action", method = RequestMethod.POST)
    public ModelAndView postGallery(@RequestParam(value = "albumname", required = true) String albumTitle,
            @CookieValue(value = "hash", required = true) String hash) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }

        Album album = new Album();

        album.setTitle(albumTitle);

        long creationDate = System.currentTimeMillis();
        album.setCreationDate(creationDate);

        album.setOwnerId(user.getId());

        galleryService.saveAlbum(album);

        String appPath = servletContext.getRealPath("");
        String savePath = appPath + File.separator + "galleries" + File.separator + user.getId() + File.separator + albumTitle;
        File file = new File(savePath);
        file.mkdirs();

        return new ModelAndView("redirect:gallery.action?userid=" + user.getId());
    }

    @RequestMapping(value = "/album.action", method = RequestMethod.GET)
    public ModelAndView getAlbum(
    		@RequestParam(value = "id", required = true) int albumId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int pageIndex, 
            @CookieValue(value = "hash", required = true) String hash) {
        
    	ModelAndView mav = new ModelAndView("album");

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        List <Integer> friendsIdsList = userService.getFriendsIdsList(user.getId());
        Album album = galleryService.getAlbum(albumId);
        
        if(!friendsIdsList.contains(album.getOwnerId()) && album.getOwnerId() != user.getId()) {
        	return new ModelAndView("redirect:./");
        }
        
        List<Image> imagesList = galleryService.getImagesList(albumId, pageIndex);

        int imagesCount = album.getPhotosCount();
        int pagesCount = 0;
        if (imagesCount % Album.ALBUM_PHOTOS_COUNT_PER_PAGE == 0) {
            pagesCount = imagesCount / Album.ALBUM_PHOTOS_COUNT_PER_PAGE;
        } else {
            pagesCount = imagesCount / Album.ALBUM_PHOTOS_COUNT_PER_PAGE + 1;
        }

        mav.addObject("pagesCount", pagesCount);
        mav.addObject("album", album);
        mav.addObject("imagesList", imagesList);
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/album.action", method = RequestMethod.POST)
    public ModelAndView postAlbum(
    		@RequestParam(value = "albumid", required = true) int albumId, 
            @CookieValue(value = "hash", required = true) String hash,
            @RequestParam(value = "files[]", required = true) List<MultipartFile> files) {
        User user = userService.getUserByHash(hash);
        
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        Album album = galleryService.getAlbum(albumId);
        if (user.getId() != album.getOwnerId()) {
            return new ModelAndView("redirect:./");
        }

        String appPath = servletContext.getRealPath("");
        String savePath = appPath + File.separator + "galleries" + File.separator + user.getId() + File.separator + album.getTitle() + File.separator;

        File file = new File(savePath);
        file.mkdirs();
        int photosAddedCount = 0;
        for (MultipartFile eachFile : files) {
            if (!eachFile.isEmpty()) {
                if (eachFile.getOriginalFilename() != null && eachFile.getOriginalFilename().length() > 0) {
                    String fileName = UUID.randomUUID().toString();
                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(savePath + fileName + ".jpg"))) {
                        byte[] bytes = eachFile.getBytes();
                        stream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Image image = new Image();
                    image.setAlbumId(albumId);
                    long creationDate = System.currentTimeMillis();
                    image.setCreationDate(creationDate);
                    image.setFilename(fileName + ".jpg");
                    image.setOwnerId(user.getId());
                    image.setTitle(eachFile.getOriginalFilename());

                    galleryService.saveImage(image);

                    photosAddedCount = photosAddedCount + 1;
                }
            }
        }

        album.setPhotosCount(album.getPhotosCount() + photosAddedCount);
        galleryService.updateAlbumPhotosCount(album);
        
        NewsMessagePrototype newsMessagePhotoAdded = new NewsMessagePrototype();
        newsMessagePhotoAdded.setSender(user);
        newsMessagePhotoAdded.setMessageType(NewsMessagePrototype.PHOTOS_ADDED);
        newsMessagePhotoAdded.setObjectId(albumId);
                
        messageService.addNews(newsMessagePhotoAdded);

        return new ModelAndView("redirect:album.action?id=" + album.getId());
    }

    @RequestMapping(value = "/deletealbum.action", method = RequestMethod.GET)
    public void DeleteAlbum(
            @RequestParam(value = "albumid", required = true) int albumId, 
            @CookieValue(value = "hash", required = true) String hash,
            HttpServletResponse response) throws IOException {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        
        Album album = galleryService.getAlbum(albumId);
        if (user.getId() != album.getOwnerId()) {
            response.getWriter().write("ERROR");
            return;
        }
        
        String appPath = servletContext.getRealPath("");
        String fullDeletePath = appPath + File.separator + "galleries" + File.separator + user.getId() + File.separator + album.getTitle();

        File deleteFile = new File(fullDeletePath);

        try {
            FileUtils.deleteDirectory(deleteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        galleryService.deleteAlbum(albumId);
        response.getWriter().write("OK");
    }

    @RequestMapping(value = "/renamealbum.action", method = RequestMethod.POST)
    public ModelAndView postRenameAlbum(
    		@RequestParam(value = "folderpath", required = true) String folderPath,
            @RequestParam(value = "foldername", required = true) String newFolderName, 
            @RequestParam(value = "albumid", required = true) int albumId,
            @CookieValue(value = "hash", required = true) String hash) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        Album album = galleryService.getAlbum(albumId);
        if (user.getId() != album.getOwnerId()) {
            return new ModelAndView("redirect:./");
        }

        String appPath = servletContext.getRealPath("");
        String absoluteFolderPath = appPath + File.separator + folderPath;
        String newAbsoluteFolderPath = appPath + File.separator + "galleries" + File.separator + user.getId() + File.separator + newFolderName;

        File folderToRename = new File(absoluteFolderPath);
        File newName = new File(newAbsoluteFolderPath);
        folderToRename.renameTo(newName);

        galleryService.renameAlbum(albumId, newFolderName);

        return new ModelAndView("redirect:gallery.action?userid=" + user.getId());
    }

    @RequestMapping(value = "/deleteimage.action", method = RequestMethod.GET)
    public void postDeleteImage(
            @RequestParam(value = "albumid", required = true) int albumId, 
            @RequestParam(value = "imageid", required = true) int imageId,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {
    	
    	User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        
        Image image = galleryService.getImage(imageId);
        if (user.getId() != image.getOwnerId()) {
            response.getWriter().write("ERROR");
            return;
        }
        Album album = galleryService.getAlbum(albumId);
        String appPath = servletContext.getRealPath("");
        String deletePath = appPath + File.separator + "galleries" + File.separator + image.getOwnerId() + File.separator + album.getTitle() + File.separator + image.getFilename(); 
        File deleteFile = new File(deletePath);

        if (deleteFile.delete()) {
            galleryService.deleteImage(albumId, image.getFilename());
            album.setPhotosCount(album.getPhotosCount() - 1);
            galleryService.updateAlbumPhotosCount(album);
        }
        response.getWriter().write("OK");
    }

}