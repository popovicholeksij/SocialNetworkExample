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

import com.socialnetwork.entities.Country;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.UserService;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/search.action", method = RequestMethod.GET)
    public ModelAndView getSearch(@CookieValue(value = "hash", required = true) String hash) {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            return new ModelAndView("redirect:./");
        }
        ModelAndView mav = new ModelAndView("search");
        mav.addObject("user", user);

        List<Country> countriesList = userService.getCountriesList();
        mav.addObject("countriesList", countriesList);

        return mav;
    }

    @RequestMapping(value = "/search-ajax.action", method = RequestMethod.GET)
    public void getSearch(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "secondname", required = false) String secondName, 
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "fromage", required = false) Integer fromAge, 
            @RequestParam(value = "toage", required = false) Integer toAge,
            @RequestParam(value = "country", required = false) Integer countryId, 
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "phonenumber", required = false) String phoneNumber, 
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "school", required = false) String school, 
            @RequestParam(value = "university", required = false) String university,
            @CookieValue(value = "hash", required = true) String hash, 
            HttpServletResponse response) throws IOException {

        User user = userService.getUserByHash(hash);
        if (user == null) {
            response.getWriter().write("ERROR");
            return;
        }

        User searchUser = new User();
        if (!username.equals("")) {
            searchUser.setFirstName(username);
        }
        if (!secondName.equals("")) {
            searchUser.setSecondName(secondName);
        }

        if (gender != null) {
            searchUser.setGender(gender);
        }

        if (countryId != null) {
            Country country = new Country();
            country.setId(countryId);
            searchUser.setCountry(country);
        }

        if (city != null) {
            searchUser.setCity(city);
        }
        if (phoneNumber != null) {
            searchUser.setPhoneNumber(phoneNumber);
        }
        if (email != null) {
            searchUser.setEmail(email);
        }
        if (school != null) {
            searchUser.setSchool(school);
        }
        if (university != null) {
            searchUser.setUniversity(university);
        }

        List<User> usersList = userService.search(searchUser, fromAge, toAge);

        String html = "";
        for (User each : usersList) {
            //html = html + each.getFirstName() + " " + each.getSecondName() + "<br>";
            html = html + "<div style=\"width: 210px; float: left;\"> \n"
                    + "<a href=\"profile.action?id=" + each.getId() + "\">\n"
                            + "<img width=\"100\" height=\"100\" src=" + each.getAvatarPath() + "><br>" +
                                        each.getFirstName() + " " + each.getSecondName() + "\n </a> \n </div> \n";
        }

        response.getWriter().write(html);
    }

}
