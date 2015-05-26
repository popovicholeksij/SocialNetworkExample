package com.socialnetwork.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.socialnetwork.entities.Country;
import com.socialnetwork.entities.Message;
import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.GalleryService;
import com.socialnetwork.services.MessageService;
import com.socialnetwork.services.UserService;

@Controller
public class ProfileController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GalleryService galleryService;
    
    @RequestMapping(value = "/profile.action", method = RequestMethod.GET)
    public ModelAndView getProfile(
            @RequestParam(value = "id", required = true) int id, 
            @CookieValue(value = "hash", required = true) String hash
            ) {
        
        ModelAndView mav = new ModelAndView("profile");

        User thisUser = userService.getUser(id);
        mav.addObject("thisUser", thisUser);

        User user = userService.getUserByHash(hash);
        mav.addObject("user", user);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }

        List<Message> wallMessageList = messageService.getWallMessagesList(id);
        mav.addObject("wallMessageList", wallMessageList);

        List<Integer> friendsIdsList = userService.getFriendsIdsList(user.getId());
        mav.addObject("friendsIdsList", friendsIdsList);

        List<Integer> ignoreIdsList = userService.getIgnoreIdsList(user.getId());
        mav.addObject("ignoreIdsList", ignoreIdsList);

        List<Integer> thisUserIgnoreIdsList = userService.getIgnoreIdsList(thisUser.getId());
        mav.addObject("thisUserIgnoreIdsList", thisUserIgnoreIdsList);

        String appPath = servletContext.getRealPath("");
        String avatarPath = appPath + File.separator + "galleries" + File.separator + "avatars" + File.separator + thisUser.getId() + ".jpg";

        Boolean haveAvatar = galleryService.haveAvatar(avatarPath);
        mav.addObject("haveAvatar", haveAvatar);
        
        List<Integer> requestFriendshipIdsList = userService.getRequestFriendshipIdsList(user.getId());
        mav.addObject("requestFriendshipIdsList", requestFriendshipIdsList);
        
        List<Integer> sendRequestFriendshipIdsList = userService.getSendRequestFriendshipIdsList(user.getId());
        mav.addObject("sendRequestFriendshipIdsList", sendRequestFriendshipIdsList);

        return mav;
    }

    @RequestMapping(value = "/profile.action", method = RequestMethod.POST)
    public ModelAndView postProfile(
            @RequestParam(value = "receiverid", required = true) int receiverId,
            @RequestParam(value = "wallmessage", required = true) String wallMessageText, 
            @CookieValue(value = "hash", required = true) String hash) {
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        List <Integer> receiverFriendsIdsList = userService.getFriendsIdsList(receiverId);
    	if(!receiverFriendsIdsList.contains(user.getId()) && user.getId() != receiverId) {
    		return new ModelAndView("redirect:./");
    	}
    	
        long creationDate = System.currentTimeMillis();
        System.out.println(creationDate);
        Date date = new Date(creationDate);
        System.out.println(date.getTime());

        Message wallMessage = new Message();
        wallMessage.setReceiver(userService.getUser(receiverId));
        wallMessage.setSender(user);
        wallMessage.setMessage(wallMessageText);
        wallMessage.setCreationDate(date);

        messageService.saveWallMessage(wallMessage);
        if (user.getId() == receiverId) {
            int lastInsertId = messageService.getWallMessageId(wallMessage);
            NewsMessagePrototype newsMessageWallMessageAdded = new NewsMessagePrototype();
            newsMessageWallMessageAdded.setSender(user);
            newsMessageWallMessageAdded.setMessageType(NewsMessagePrototype.WALL_MESSAGE_ADDED);
            newsMessageWallMessageAdded.setObjectId(lastInsertId);
            messageService.addNews(newsMessageWallMessageAdded);
        }
        
        return new ModelAndView("redirect:profile.action?id=" + receiverId);
    }

    @RequestMapping(value = "/avatar.action", method = RequestMethod.POST)
    public ModelAndView postAvatar(
            @CookieValue(value = "hash", required = true) String hash, 
            @RequestParam(value = "file", required = true) MultipartFile file) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }

        String appPath = servletContext.getRealPath("");
        String avatarPath = "galleries" + "/" + "avatars" + "/" + user.getId() + ".jpg";
        String savePath = appPath + File.separator + avatarPath;
        File savefile = new File(appPath + "galleries" + "/" + "avatars" + "/" + user.getId());
        savefile.mkdirs();
       
        if (file.getOriginalFilename() != null && file.getOriginalFilename().length() > 0) {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(savePath))) {
                byte[] bytes = file.getBytes();
                savefile.mkdirs();
                stream.write(bytes);
                userService.updateAvatarPath(user.getId(), avatarPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ModelAndView("redirect:profile.action?id=" + user.getId());
    }

    @RequestMapping(value = "/deleteavatar.action", method = RequestMethod.POST)
    public ModelAndView postDeleteAvatar(
    		@RequestParam(value = "userid", required = true) int userId, 
            @CookieValue(value = "hash", required = true) String hash) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        if (userId != user.getId()) {
            return new ModelAndView("redirect:./");
        }

        String appPath = servletContext.getRealPath("");
        String avatarPath = "galleries" + "/" + "avatars" + "/"+ userId + ".jpg";
        String saveAvatarPath = appPath + File.separator + avatarPath;  
        
        File file = new File(saveAvatarPath);
        if (file.delete()) {
            userService.updateAvatarPath(user.getId(), User.DEFAULT_AVATAR_PATH);
        }

        return new ModelAndView("redirect:profile.action?id=" + user.getId());
    }

    @RequestMapping(value = "/editprofile.action", method = RequestMethod.GET)
    public ModelAndView getEditProfile(
    		@CookieValue(value = "hash", required = true) String hash) {
    	
        ModelAndView mav = new ModelAndView("editprofile");
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
        	return new ModelAndView("redirect:./");
        }

        List<Country> countriesList = userService.getCountriesList();
        mav.addObject("countriesList", countriesList);
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/editprofile.action", method = RequestMethod.POST)
    public ModelAndView postEditProfile(
    		@RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "username", required = true) String username, @RequestParam(value = "secondname", required = true) String secondName,
            @RequestParam(value = "gender", required = true) int gender, @RequestParam(value = "country", required = true) int countryId,
            @RequestParam(value = "phonenumber", required = true) String phoneNumber, @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "school", required = true) String school, @RequestParam(value = "university", required = true) String university,
            @RequestParam(value = "dateofbirth", required = true) String dateOfBirth, @RequestParam(value = "city", required = false) String city) {

        User user = new User();
        
        if (id != user.getId()) {
            return new ModelAndView("redirect:./");
        }
        
        user.setId(id);
        user.setFirstName(username);
        user.setSecondName(secondName);
        user.setGender(gender);

        Country country = new Country();
        country.setId(countryId);
        user.setCountry(country);

        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setSchool(school);
        user.setUniversity(university);
        user.setCity(city);
        user.setDateOfBirth(dateOfBirth);

        userService.updateUser(user);
        return new ModelAndView("redirect:profile.action?id=" + user.getId());
    }

    @RequestMapping(value = "/mynews.action", method = RequestMethod.GET)
    public ModelAndView getMyNews (
            @CookieValue(value = "hash", required = true) String hash) {
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        List<NewsMessagePrototype> userNewsList = userService.getUserNewsList(user.getId());
        ModelAndView mav = new ModelAndView("news");
        mav.addObject("userNewsList", userNewsList);
        mav.addObject("user", user);
      
        return mav;
    }
    
}