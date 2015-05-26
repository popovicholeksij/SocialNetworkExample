package com.socialnetwork.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.socialnetwork.entities.Message;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.MessageService;
import com.socialnetwork.services.UserService;

@Controller
public class MyMessagesController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/mymessages.action", method = RequestMethod.GET)
    public ModelAndView getMyMessages(
            @CookieValue(value = "hash", required = true) String hash,
            @RequestParam(value = "receiverid", required = false) Integer receiverId) {
        ModelAndView mav = new ModelAndView("mymessages");
        User user = userService.getUserByHash(hash);
        
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        if(receiverId != null) {
        	List <Integer> receiverIgnoreIdsList = userService.getIgnoreIdsList(receiverId);
        	if(receiverIgnoreIdsList.contains(user.getId()) || user.getId() == receiverId) {
        		return new ModelAndView("redirect:./");
        	}
        }
        
        List<List<Message>> messagesGroupList = messageService.getMessagesGroupList(user.getId());
        mav.addObject("messagesGroupList", messagesGroupList);

        messageService.setMessagesReaded(user.getId());
        if (receiverId != null) {
            mav.addObject("receiverId", receiverId);
        }
        List<User> usersFriendsList = userService.getFriendsList(user.getId());
        List<Integer> userIdsFriendsList = userService.getFriendsIdsList(user.getId());
        mav.addObject("userIdsFriendsList", userIdsFriendsList);
        mav.addObject("usersFriendsList", usersFriendsList);
        mav.addObject("user", user);
        
        return mav;
    }

    @RequestMapping(value = "/mymessages.action", method = RequestMethod.POST)
    public ModelAndView postMyMessages(
            @RequestParam(value = "receiverid", required = true) int receiverId,
            @RequestParam(value = "message", required = true) String messageText, 
            @CookieValue(value = "hash", required = true) String hash) {
        
        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        List <Integer> receiverIgnoreIdsList = userService.getIgnoreIdsList(receiverId);
    	if(receiverIgnoreIdsList.contains(user.getId()) || user.getId() == receiverId) {
    		return new ModelAndView("redirect:./");
    	}
    	
        long creationDate = System.currentTimeMillis();
        Date date = new Date(creationDate);

        Message message = new Message();

        message.setReceiver(userService.getUser(receiverId));
        message.setSender(user);
        message.setMessage(messageText);
        message.setCreationDate(date);

        messageService.saveMessage(message);

        return new ModelAndView("redirect:dialog.action?senderid=" + user.getId() + "&receiverid=" + receiverId);
    }

    @RequestMapping(value = "/dialog.action", method = RequestMethod.GET)
    public ModelAndView getDialog(
            @RequestParam(value = "senderid", required = true) int senderId,
            @RequestParam(value = "receiverid", required = true) int receiverId, 
            @CookieValue(value = "hash", required = true) String hash) {

        User user = userService.getUserByHash(hash);
        
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        if(user.getId() != senderId && user.getId() != receiverId) {
        	return new ModelAndView("redirect:./");
        }
        List <Integer> receiverIgnoreIdsList = userService.getIgnoreIdsList(receiverId);
        ModelAndView mav = new ModelAndView("dialog");

        List<Message> dialogList = messageService.getDialogList(senderId, receiverId);
        mav.addObject("dialogList", dialogList);
        mav.addObject("user", user);
        mav.addObject("receiverIgnoreIdsList", receiverIgnoreIdsList);
        messageService.setMessagesReaded(user.getId());

        return mav;
    }

    @RequestMapping(value = "/dialog.action", method = RequestMethod.POST)
    public ModelAndView postDialog(@CookieValue(value = "hash", required = true) String hash, 
            @RequestParam(value = "message", required = true) String messageText,
            @RequestParam(value = "receiverid", required = true) int receiverId) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        
        List <Integer> receiverIgnoreIdsList = userService.getIgnoreIdsList(receiverId);
    	if(receiverIgnoreIdsList.contains(user.getId())) {
    		return new ModelAndView("redirect:./");
    	}

        long creationDate = System.currentTimeMillis();
        Date date = new Date(creationDate);

        Message message = new Message();
        message.setReceiver(userService.getUser(receiverId));
        message.setSender(user);
        message.setMessage(messageText);
        message.setCreationDate(date);

        messageService.saveMessage(message);

        return new ModelAndView("redirect:dialog.action?senderid=" + user.getId() + "&receiverid=" + receiverId);
    }

}