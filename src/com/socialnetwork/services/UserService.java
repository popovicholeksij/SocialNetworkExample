package com.socialnetwork.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.dao.UserDao;
import com.socialnetwork.entities.Country;
import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.entities.User;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;

    public List<Country> getCountriesList() {
        List<Country> countriesList = userDao.getCountriesList();
        return countriesList;
    }

    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    public User getUserByHash(String hash) {
        User user = null;
        if (hash != null) {
            user = userDao.getUser(hash);
        }

        return user;
    }

    public Cookie authorize(String email, String password) {
        boolean isAuthorized = isAuthorized(email, password);

        if (isAuthorized) {
            String hash = UUID.randomUUID().toString();
            userDao.setHash(hash, email, password);

            Cookie cookie = new Cookie("hash", hash);
            // cookie.setMaxAge(60);

            return cookie;
        }

        return null;
    }

    private boolean isAuthorized(String email, String password) {
        return userDao.isAuthorized(email, password);
    }

    public User getUser(int id) {
        User user = userDao.getUser(id);
        return user;
    }

    public void deleteHash(String hashCookie) {
        userDao.deleteHash(hashCookie);
    }

    public List<User> search(User user, Integer fromAge, Integer toAge) {
        List<User> usersList = userDao.search(user);
        List<User> filteredUsersList = new ArrayList<>();

        if (user.getFirstName() == null && user.getSecondName() == null && user.getGender() == 0 && user.getPhoneNumber() == null && user.getEmail() == null
                && user.getSchool() == null && user.getUniversity() == null && user.getCity() == null && user.getCountry() == null && fromAge == null && toAge == null ) {
            return null;
        }
        
        if (fromAge != null || toAge != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            for (User eachUser : usersList) {
                Date birthDate = null;
                try {
                    birthDate = dateFormat.parse(eachUser.getDateOfBirth());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long currentTime = System.currentTimeMillis();
                long difference = currentTime - birthDate.getTime();
                long age = (difference / (1000 * 60 * 60 * 24)) / 365;

                if (fromAge != null && toAge != null) {
                    if (age >= fromAge && age <= toAge) {
                        filteredUsersList.add(eachUser);
                    }
                } else if (fromAge != null && toAge == null) {
                    if (age >= fromAge) {
                        filteredUsersList.add(eachUser);
                    }
                } else if (fromAge == null && toAge != null) {
                    if (age <= toAge) {
                        filteredUsersList.add(eachUser);
                    }
                }
            }

            return filteredUsersList;
        }

        return usersList;
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public User getUser(String email) {
        User user = userDao.getUserByEmail(email);
        return user;
    }

    public void addToFriends(int userId, int friendId) {
        userDao.addToFriends(userId, friendId);
    }

    public List<Integer> getFriendsIdsList(int userId) {
        List<Integer> friendsIdsList = userDao.getFriendsIdsList(userId);
        return friendsIdsList;
    }

    public List<User> getFriendsList(int userId) {
        List<User> friendsList = userDao.getFriendsList(userId);
        return friendsList;
    }

    public void deleteFromFriends(int userId, int friendId) {
        userDao.deleteFromFriends(userId, friendId);
    }

    public List<User> getIgnoreList(int userId) {
        List<User> ignoreList = userDao.getIgnoreList(userId);
        return ignoreList;

    }

    public void addToIgnore(int userId, int ignoreUserId) {
        userDao.addToIgnore(userId, ignoreUserId);

    }

    public void addFriendToIgnore(int userId, int ignoreUserId) {
        userDao.addFriendToIgnore(userId, ignoreUserId);

    }

    public List<Integer> getIgnoreIdsList(int userId) {
        List<Integer> ignoreIdsList = userDao.getIgnoreIdsList(userId);
        return ignoreIdsList;

    }

    public void deleteFromIgnore(int userId, int userIgnoreId) {
        userDao.deleteFromIgnore(userId, userIgnoreId);

    }

    public void updateAvatarPath(int userId, String avatarPath) {
        userDao.updateAvatarPath(userId, avatarPath);
    }

    public List<NewsMessagePrototype> getUserNewsList(int userId) {
        List<NewsMessagePrototype> userNewsPrototypesList = userDao.getUserNews(userId);
        
        return userNewsPrototypesList;
    }

    public List<User> getRequestFriendshipList(int userId) {
        List<User> requestFriendshipList = userDao.getRequestFriendshipList(userId);
        return requestFriendshipList;
    }

    public List<User> getSendRequestFriendshipList(int userId) {
        List<User> sendRequestFriendshipList = userDao.getSendRequestFriendshipList(userId);
        return sendRequestFriendshipList;
    }

    public void acceptFriendship(int userId, int friendId) {
        userDao.acceptFrienship(userId, friendId);
    }

    public void declineFriendship(int userId, int friendId) {
        userDao.declineFriendship(userId, friendId);
    }

    public List<Integer> getRequestFriendshipIdsList(int userId) {
        List<Integer> requestFriendshipIdsList = userDao.getRequestFriendshipIdsList(userId);
        return requestFriendshipIdsList;
    }

    public List<Integer> getSendRequestFriendshipIdsList(int userId) {
        List<Integer> sendRequestFriendshipIdsList = userDao.getSendRequestFriendshipIdsList(userId);
        return sendRequestFriendshipIdsList;
    }

}
