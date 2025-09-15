package com.tnovel.demo;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class PageController {
    @GetMapping("/list")
    @ResponseBody
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = " - Logged User : " + authentication;
        log.info(message);
        return "[LogIn Success] " + message;
    }

    @GetMapping("/login")
    @ResponseBody
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = " - Annonymous User : " + authentication;
        log.info(message);
        return "[LogIn Required] " + message;
    }

}