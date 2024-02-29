package com.contact.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.entities.User;
import com.contact.helper.Message;
import com.contact.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	//BRINGING REPOSITTORY HERE TO SEND DATA IN THE DATABASE AND AUTOWIRE IT
	@Autowired
	private UserRepository userRepository;
	
	
	
	// HOME PAGE HANDLER
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","HOME PAGE");
		return "home";
	}
	
	// PLANS PAGE HANDLER
	@GetMapping("/plans")
	public String plans(Model model)
	{
		model.addAttribute("title","ABOUT PAGE");
		return "plans";
	}
	// ABOUT PAGE HANDLER
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","PLANS PAGE");
		return "about";
	}
	
	// REGISTRATION PAGE HANDLER
	@GetMapping("/register")
	public String signUp(Model model)
	{
		//RETURN EMPTY INSTANCE OF THE USER TO FETCH THE VALUE IN THE FORM
		model.addAttribute("title","REGISTRATION PAGE"); 
		//BLANK FIELDS WILL BE GONE FROM THE FORM AND THE DATA YOU FILLED WILL COME HERE 
		model.addAttribute("user", new User());
		return "register";
	}
	
	// FORM AFTER SUBMIT HANDLER TAKE MODEL AND FOR CHECKBOX USE REQUEST PARAM COZ WE DIDNT TAKE IT IN THE ENTITY AND MODEL TO SEND DATA TO TEH ANOTHER PAGE
	//MODEL ATTRIBUTE TO TAKE VALUES FROM THE PAGE TO HANDLER
	// TO FIRE VALIDATION USE @VALID BEFORE MODEL ATTRIBUTE AND STIRE IT IN A BINDING RESULT OBJECT
	
	@PostMapping("/do_register")
	public String afterRegister(@Valid @ModelAttribute("user") User user, BindingResult result  , @RequestParam(value = "aggrement", defaultValue = "false") boolean agreement, Model model  , HttpSession session )
	{
		
		try {
			model.addAttribute("title","REGISTRATION PAGE");
			
			if(!agreement)
			{
				System.out.println("PLEASE ACCEPT TERMS AND CONDITIONS");
				throw new Exception("PLEASE ACCEPT TERMS AND CONDITIONS");
				
			}
			
			if(result.hasErrors())
			{
				System.out.println("ERROR"+result.toString());
				model.addAttribute("user",user);
				return "register";
			}
			
			
			
			// JUST TO TEST THE DATABASE
			
			user.setImageUrl("default.png");
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("AGGREMENT : "+agreement);
			System.out.println("USER : "+user);
			
			//SAVING IN THE DATABASE
			User user1 = this.userRepository.save(user);
			System.out.println(user1);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("SUCCESSFULLY REGISTERED","alert-success") );
			return "login";
			
			
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("SOMETHING WENT WRONG !! "+e.getMessage(),"alert-danger") );
			return "register";
			
		}
		
		
	}
	
	//CUSTOM LOGIN HANDLER
	
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","CUSTOM LOGIN PAGE");
		return "login";
	}
	
	//ERROR PAGE
	@GetMapping("/login-fail")
	public String errorPage(Model model)
	{
		model.addAttribute("title","LOGIN PAGE");
		return "login-fail";
	}
	
	
	
	
}
