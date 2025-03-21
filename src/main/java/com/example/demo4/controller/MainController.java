package com.example.demo4.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @RequestMapping(path = "/login", params = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Long userId = userService.login(email, password, getSessionId(request));
        if (userId != null) {
            return "main";
        }
        return "login";
    }

    @RequestMapping(path = "/signup", params = "signup", method = RequestMethod.POST)
    public String signup(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if (password == null || !password.equals(confirmPassword)){
            return "signup";
        }
        if (userService.checkUserByEmail(email)) {
            return "signup";
        }
        //Починаємо процедуру реєстрації
        userService.addUser(email, password);
        //
        Long userId = userService.login(email, password, getSessionId(request));
        return "main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        System.out.println("Logout Method");
        userService.endSession(getSessionId(request));
        return "/welcome";
    }

    @GetMapping("/")
    public String indexPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "welcome";
        return "main";
    }

    @GetMapping("/welcome")
    public String welcomePage(HttpServletRequest request) {
        if (checkUser(request) == null) return "welcome";
        return "main";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "login";
        return "main";
    }

    @GetMapping("/signup")
    public String signUpPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "signup";
        return "main";
    }

    //--------------------------------------------------------------
    @GetMapping("/main")
    public String mainPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "welcome";
        return "main";
    }
    //--------------------------------------------------------------

    @GetMapping("/personal_cabinet")
    public String PCPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "welcome";
        return "personal_cabinet";
    }
    //--------------------------------------------------------------

    @GetMapping("/search")
    public String searchPage(HttpServletRequest request) {
        if (checkUser(request) == null) return "welcome";
        return "main";
    }
    //--------------------------------------------------------------

    private Long checkUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = getSessionId(request);
        Long userId = userService.findUserId(sessionId);
        return userId;
    }

    private String getSessionId(HttpServletRequest request) {
        Cookie [] cookies = request.getCookies();
        String sessionId = null;
        for (Cookie cookie: cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }
        return sessionId;
    }


}
