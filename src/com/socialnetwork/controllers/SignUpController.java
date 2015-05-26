package com.socialnetwork.controllers;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
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
public class SignUpController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signup.action", method = RequestMethod.GET)
    public ModelAndView getSignup() {
        ModelAndView mav = new ModelAndView("signup");
        List<Country> countriesList = userService.getCountriesList();
        mav.addObject("countriesList", countriesList);

        return mav;
    }

    @RequestMapping(value = "/signup.action", method = RequestMethod.POST)
    public ModelAndView postSignup(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "secondname", required = true) String secondName, 
            @RequestParam(value = "gender", required = true) int gender,
            @RequestParam(value = "dateofbirth", required = true) String dateOfBirth, 
            @RequestParam(value = "country", required = true) int countryId,
            @RequestParam(value = "phonenumber", required = true) String phoneNumber, 
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "school", required = true) String school, 
            @RequestParam(value = "university", required = true) String university,
            @RequestParam(value = "password", required = true) String password) {

        User user = new User();

        user.setFirstName(username);
        user.setSecondName(secondName);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);

        Country country = new Country();
        country.setId(countryId);
        user.setCountry(country);

        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setSchool(school);
        user.setUniversity(university);
        user.setPassword(password);
        user.setAvatarPath(User.DEFAULT_AVATAR_PATH);
        userService.saveUser(user);

        User user2 = userService.getUser(user.getEmail());

        String appPath = servletContext.getRealPath("");
        String savePath = appPath + File.separator + "galleries" + File.separator + user2.getId();
        File file = new File(savePath);
        file.mkdir();

        return new ModelAndView("redirect:signin.action");
    }

    @RequestMapping(value = "signin.action", method = RequestMethod.GET)
    public ModelAndView getSignIn(@CookieValue(value = "hash", required = false) String hash) {
        ModelAndView mav = new ModelAndView("signin");
        User user = userService.getUserByHash(hash);

        if (user != null) {
            return new ModelAndView("redirect:profile.action?id=" + user.getId());
        }

        return mav;
    }

    @RequestMapping(value = "signin.action", method = RequestMethod.POST)
    public ModelAndView postSignin(HttpServletResponse response, 
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password) {

        Cookie cookie = userService.authorize(email, password);
        if (cookie != null) {
            response.addCookie(cookie);
        }

        return new ModelAndView("redirect:signin.action");
    }

    @RequestMapping(value = "/signout.action", method = RequestMethod.GET)
    public ModelAndView getSignout(@CookieValue(value = "hash", required = true) String hash) {

        userService.deleteHash(hash);

        return new ModelAndView("redirect:signin.action");
    }

}
