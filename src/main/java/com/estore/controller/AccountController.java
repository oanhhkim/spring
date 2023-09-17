package com.estore.controller;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.estore.bean.MailInfo;
import com.estore.dao.CustomerDAO;
import com.estore.entity.Customer;
import com.estore.service.CookieService;
import com.estore.service.MailService;

@Controller
public class AccountController {
	@Autowired
	CustomerDAO dao;
	@Autowired
	HttpSession session;
	@Autowired
	CookieService cookie;
	@Autowired
	ServletContext app;
	@Autowired
	MailService mailer;
	
	@GetMapping("/account/login")
	public String login(Model model) {
		Cookie ckid = cookie.read("userid");
		Cookie ckpw = cookie.read("pass");
		if(ckid != null) {
			String uid = ckid.getValue();
			String pwd = ckpw.getValue();
			
			model.addAttribute("uid", uid);
			model.addAttribute("pwd", pwd);
		}
		return "account/login";
	}
	
	@PostMapping("/account/login")
	public String login(Model model,
			@RequestParam("id") String id,
			@RequestParam("pw") String pw,
			@RequestParam(value="rm", defaultValue = "false") boolean rm) {
		Customer user = dao.findById(id);
		if(user == null) {
			model.addAttribute("message", "Invalid account");
		}
		else if(!pw.equals(user.getPassword())) {
			model.addAttribute("message", "Invalid password");
		}
		else if(!user.getActivated()) {
			model.addAttribute("message", "Your account is Inactivated!");
		}
		else {//thanh cong
			model.addAttribute("message", "Login Successfully!");
			session.setAttribute("user", user);
			//ghi nho tk bang cookie
			if(rm == true) {
				cookie.create("userid", user.getId(), 30);
				cookie.create("pass", user.getPassword(), 30);
			}
			else {
				cookie.delete("userid");
				cookie.delete("pass");
			}
		}
		return "account/login";
	}
	
	@RequestMapping("/account/logoff")
	public String logoff(Model model) {
		session.removeAttribute("user");
		return "redirect:/home/index";
	}
	
	@GetMapping("/account/register")
	public String register(Model model) {
		Customer user = new Customer();
		model.addAttribute("form", user);
		return "account/register";
	}
	
	@PostMapping("/account/register")
	public String register(Model model, @ModelAttribute("form") Customer user,
			@RequestParam("photo_file") MultipartFile file) throws IllegalStateException, IOException, MessagingException {
		if(file.isEmpty()) {
			user.setPhoto("user.png");
		}
		else {
			String dir = app.getRealPath("/static/images/customers");
			File f = new File(dir, file.getOriginalFilename());
			file.transferTo(f);
			user.setPhoto(f.getName());
		}
		user.setActivated(true);
		user.setAdmin(false);
		dao.create(user);
		model.addAttribute("message", "Register sucessfully");
		
//		String from = "nhom10.ptit@gmail.com";
//		String to = user.getEmail();
//		String subject = "Welcome";
//		String body = "";
//		MailInfo mail = new MailInfo(from, to, subject, body);
//		mailer.send(mail);
		return "account/login";
	}
	
	@GetMapping("/account/change")
	public String change(Model model) {
		return "account/change";
	}
	@PostMapping("/account/change")
	public String change(Model model,
			@RequestParam("id") String id,
			@RequestParam("pw") String pw,
			@RequestParam("pw1") String pw1,
			@RequestParam("pw2") String pw2) {
		if(!pw1.equals(pw2)) {
			model.addAttribute("message", "Password not match");
		}
		else {
			Customer user = dao.findById(id);
			if(user == null) {
				model.addAttribute("message", "Invalid username");
			}
			else if(!pw.equals(user.getPassword())) {
				model.addAttribute("message", "Invalid password");
			}
			else {
				user.setPassword(pw1);
				dao.changePassword(id, pw2);
				model.addAttribute("message", "Change password successfully");
			}
		}
		return "redirect:/account/login";
	}
}
