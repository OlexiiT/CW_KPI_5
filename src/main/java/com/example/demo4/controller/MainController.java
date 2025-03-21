package com.example.demo4.controller;

import com.example.demo4.model.Book;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;

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
        if (getUserId(request) == null) return "welcome";
        return "main";
    }

    @GetMapping("/welcome")
    public String welcomePage(HttpServletRequest request) {
        if (getUserId(request) == null) return "welcome";
        return "main";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        if (getUserId(request) == null) return "login";
        return "main";
    }

    @GetMapping("/signup")
    public String signUpPage(HttpServletRequest request) {
        if (getUserId(request) == null) return "signup";
        return "main";
    }

    @GetMapping("/create_book")
    public String bookPage(HttpServletRequest request) {
        if (getUserId(request) == null) return "welcome";
        return "create_book";
    }

    @RequestMapping(value = "/save_book", method = RequestMethod.POST)
    public void saveBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("title");
        String text = request.getParameter("content");
        if (text != null && name != null) {
            Long userId = getUserId(request);
            bookService.saveBook(name, text, userId);
        }
        response.sendRedirect("/personal_cabinet");
    }

    @GetMapping("/book")
    public String book(Model model, HttpServletRequest request) {
        String sId = request.getParameter("id");
        if (sId == null) return "personal_cabinet";
        Long id = Long.valueOf(sId);
        Book book = bookService.getBookById(id);
        model.addAttribute(book);

        Long userId = getUserId(request);
        if (userId!=null) userService.addRecentBook(userId, book);
        return "book";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return "welcome";
        List<Book> books = bookService.getRecomendedBooks();
        model.addAttribute("recomendedBooks", books);
        return "main";
    }

    @GetMapping("/personal_cabinet")
    public String PCPage(Model model, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return "welcome";

        List<Book> books = bookService.getOwnerBooksList(userId);
        List<Book> recentBooks = userService.getRecentBooks(userId);
        model.addAttribute("books", books);
        model.addAttribute("recentBooks", recentBooks);

        return "personal_cabinet";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam String query, Model model, HttpServletRequest request) {
        if (getUserId(request) == null) return "welcome";

        List<Book> searchResults = bookService.getSearchResults(query);

        model.addAttribute("query", query);
        model.addAttribute("books", searchResults);

        return "search";
    }

    private Long getUserId(HttpServletRequest request) {
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
