package com.socialnetwork.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.dao.MessageDao;
import com.socialnetwork.entities.Message;
import com.socialnetwork.entities.NewsMessagePrototype;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    public void saveMessage(Message message) {
        messageDao.saveMessage(message);
    }

    public List<List<Message>> getMessagesGroupList(int ownerId) {
        CopyOnWriteArrayList<Message> messagesList = new CopyOnWriteArrayList<>(messageDao.getMessagesGroupList(ownerId));
        List<List<Message>> groupsList = new ArrayList<>();

        while (messagesList.size() != 0) {
            Message firstMessageInGroup = messagesList.get(0);
            messagesList.remove(firstMessageInGroup);
            List<Message> groupMessagesList = new ArrayList<>();
            groupMessagesList.add(firstMessageInGroup);
            groupsList.add(groupMessagesList);

            for (int j = 0; j < messagesList.size(); j++) {
                Message message2 = messagesList.get(j);
                int message2SenderId = message2.getSender().getId();
                int messageReceiverId = firstMessageInGroup.getReceiver().getId();
                int message2ReceiverId = message2.getReceiver().getId();
                int messageSenderId = firstMessageInGroup.getSender().getId();
                if ((message2SenderId == messageSenderId && message2ReceiverId == messageReceiverId)
                        || (message2SenderId == messageReceiverId && message2ReceiverId == messageSenderId)) {
                    messagesList.remove(message2);
                    j = j - 1;
                    groupMessagesList.add(message2);
                }
            }
        }

        return groupsList;
    }

    public void setMessagesReaded(int userId) {
        messageDao.setMessagesReaded(userId);
    }

    public void saveWallMessage(Message wallMessage) {
        messageDao.saveWallMessage(wallMessage);
    }

    public int getWallMessageId(Message wallMessage) {
        int lastInsertId = messageDao.getWallMessageId(wallMessage);
        return lastInsertId;
    }

    public List<Message> getWallMessagesList(int ownerId) {
        List<Message> wallMessagesList = messageDao.getWallMessagesList(ownerId);
        return wallMessagesList;
    }

    public List<Message> getDialogList(int userId1, int userId2) {
        List<Message> dialogList = messageDao.getDialogList(userId1, userId2);
        return dialogList;
    }

    public void addNews(NewsMessagePrototype newsMessagePrototype) {
        long creationDate = System.currentTimeMillis();
        newsMessagePrototype.setCreationDate(creationDate);
        messageDao.addNews(newsMessagePrototype);
    }

}
