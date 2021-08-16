package com.emilyread.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.emilyread.authentication.models.User;
import com.emilyread.authentication.services.UserService;

@Controller
public class MainController {
	
	private final UserService userService;
	
	public MainController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/home")
	public String home(@ModelAttribute("user") User user, Model model,  HttpSession session) {
		model.addAttribute(user);
		session.getAttribute("user");
		return "index.jsp";
	}

	@RequestMapping("/registration")
	public String registrationForm(@ModelAttribute("user") User user) {
		return "register.jsp";
	}
	
	@RequestMapping(value="/registration",  method=RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		
		if(result.hasErrors()) {
			return "/register.jsp";
		}
		else {
			User u = userService.registerUser(user);
			session.setAttribute("user", u.getEmail());
			return "redirect:/home";
		}
	}
	
	@RequestMapping("/login")
	public String loginForm(@ModelAttribute("user") User user) {
		return "login.jsp";
	}
	
	@RequestMapping(value="/login",  method=RequestMethod.POST)
	public String login(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("email") String email, @RequestParam("password") String password){
		
		if(result.hasErrors()) {
			return "/login.jsp";
		}
		else {
			userService.authenticateUser(email, password);
			return "redirect:/home";
		}
	}
	
	
	@RequestMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
	
	
}