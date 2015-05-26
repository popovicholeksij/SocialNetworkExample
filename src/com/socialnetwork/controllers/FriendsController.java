package com.socialnetwork.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.MessageService;
import com.socialnetwork.services.UserService;

@Controller
public class FriendsController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageService messageService; 

    @RequestMapping(value = "/friends.action", method = RequestMethod.GET)
    public ModelAndView getFriends(@CookieValue(value = "hash") String hash) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        List<User> friendsList = userService.getFriendsList(user.getId());
        ModelAndView mav = new ModelAndView("friends");
        mav.addObject("user", user);
        mav.addObject("friendsList", friendsList);

        return mav;
    }

    @RequestMapping(value = "/addtofriends.action", method = RequestMethod.POST)
    public ModelAndView postAddToFriends(
            @RequestParam(value = "friendid", required = true) int friendId,
            @CookieValue(value = "hash", required = true) String hash) {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        int userId = user.getId();

        userService.addToFriends(userId, friendId);
              
        return new ModelAndView("redirect:profile.action?id=" + friendId);
    }

    @RequestMapping(value = "/deletefromfriends.action", method = RequestMethod.GET)
    public void getDeleteFromFriends(
            @RequestParam(value = "friendid", required = true) int friendId,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        int userId = user.getId();

        userService.deleteFromFriends(userId, friendId);
        
        
        NewsMessagePrototype newsMessageForSender = new NewsMessagePrototype();
        newsMessageForSender.setSender(user);        
        newsMessageForSender.setMessageType(NewsMessagePrototype.FRIEND_DELETED);
        newsMessageForSender.setObjectId(friendId);
        messageService.addNews(newsMessageForSender);
        
        
        NewsMessagePrototype newsMessageForFriends = new NewsMessagePrototype();
        newsMessageForFriends.setSender(userService.getUser(friendId));        
        newsMessageForFriends.setMessageType(NewsMessagePrototype.FRIEND_DELETED);
        newsMessageForFriends.setObjectId(user.getId());
        messageService.addNews(newsMessageForFriends);
        
        response.getWriter().write("OK");
    }

    @RequestMapping(value = "/addtoignore.action", method = RequestMethod.POST)
    public ModelAndView postAddToIgnore(
            @RequestParam(value = "ignoreUserId", required = true) int ignoreUserId,
            @CookieValue(value = "hash", required = true) String hash) {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        userService.addToIgnore(user.getId(), ignoreUserId);

        return new ModelAndView("redirect:profile.action?id=" + ignoreUserId);
    }
    
    @RequestMapping(value = "/addfriendtoignore.action", method = RequestMethod.POST)
    public ModelAndView addFriendToIgnore(
            @RequestParam(value = "ignoreUserId", required = true) int ignoreUserId,
            @CookieValue(value = "hash", required = true) String hash) {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        userService.addFriendToIgnore(user.getId(), ignoreUserId);

        return new ModelAndView("redirect:friends.action");
    }


    @RequestMapping(value = "/ignore.action", method = RequestMethod.GET)
    public ModelAndView getIgnore(
            @CookieValue(value = "hash", required = true) String hash) {
        ModelAndView mav = new ModelAndView("ignoreusers");
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        mav.addObject("user", user);
        
        List<User> ignoreList = userService.getIgnoreList(user.getId());
        mav.addObject("ignoreList", ignoreList);

        return mav;
    }

    @RequestMapping(value = "/deletefromignore.action", method = RequestMethod.GET)
    public void getDeleteFromIgnore(
            @RequestParam(value = "userignoreid", required = true) int userIgnoreId,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        int userId = user.getId();
        userService.deleteFromIgnore(userId, userIgnoreId);

        response.getWriter().write("OK");
    }
    
    @RequestMapping(value = "/incomefrienship.action", method = RequestMethod.GET)
    public ModelAndView getIncomeFrienship(
            @CookieValue(value = "hash", required = true) String hash) {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        List<User> requestFriendshipList = userService.getRequestFriendshipList(user.getId());
        ModelAndView mav = new ModelAndView("incomrequestfriends");
        mav.addObject("user", user);
        mav.addObject("requestFriendshipList", requestFriendshipList);
        return mav;
    }
    
    @RequestMapping(value = "/sendfrienship.action", method = RequestMethod.GET)
    public ModelAndView getSendFrienship(
            @CookieValue(value = "hash", required = true) String hash) {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        List<User> sendRequestFriendshipList = userService.getSendRequestFriendshipList(user.getId());
        ModelAndView mav = new ModelAndView("sendrequestfriends");
        mav.addObject("user", user);
        mav.addObject("sendRequestFriendshipList", sendRequestFriendshipList);
        return mav;
    }
    
    @RequestMapping(value = "/acceptfriendship.action", method = RequestMethod.GET)
    public void getAcceptFriendship(
            @RequestParam(value = "friendid", required = true) int friendId,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        int userId = user.getId();

        userService.acceptFriendship(userId, friendId);
        
        NewsMessagePrototype newsMessagePrototypeForReceiver = new NewsMessagePrototype();
        newsMessagePrototypeForReceiver.setSender(user);        
        newsMessagePrototypeForReceiver.setMessageType(NewsMessagePrototype.FRIEND_ADDED);
        newsMessagePrototypeForReceiver.setObjectId(friendId);
        messageService.addNews(newsMessagePrototypeForReceiver);
        
        NewsMessagePrototype newsMessagePrototypeForSender = new NewsMessagePrototype();
        newsMessagePrototypeForSender.setSender(userService.getUser(friendId));        
        newsMessagePrototypeForSender.setMessageType(NewsMessagePrototype.FRIEND_ADDED);
        newsMessagePrototypeForSender.setObjectId(user.getId());
        messageService.addNews(newsMessagePrototypeForSender);
        
        response.getWriter().write("OK");
    }
    
    @RequestMapping(value = "/declinefriendship.action", method = RequestMethod.GET)
    public void getDeclineFriendships(
            @RequestParam(value = "friendid", required = true) int friendId,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {
        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }
        userService.declineFriendship(user.getId(), friendId);
        
        response.getWriter().write("OK");
    }


}