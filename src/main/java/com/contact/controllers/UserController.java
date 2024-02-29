package com.contact.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
//import java.util.List;
//import java.util.List;
import java.util.Optional;

//import javax.lang.model.element.ModuleElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.helper.Message;
import com.contact.repositories.ContactRepository;
import com.contact.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;




	// TO FIRE THE HANDLER USE URL / USER / INDEX  
    // WE WILL PROTECT THIS URL WITH SPRING SECURITY 

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	// method for adding common data to response
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		// principal name will find user name from database
		
				String userName = principal.getName();
				System.out.println(userName); // will return email in the console that is unique identifier
				//in our case our email is our user name logged in user will be retunred below
				User user = this.userRepository.getUserByUserName(userName);
				System.out.println("USER : "+user);
				model.addAttribute("title","DASHBOARD");
				
				model.addAttribute("user", user);
				
		
	}
	
   // home dash board
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		
		
		return "normal/user_dashboard";
	}
	
	// add contact form handler contact
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","ADD CONTACT");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	// process contact data handler
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile multipartFile
			, Principal principal, HttpSession httpSession, Model model)
	{
		try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		// this line is essential otherwise it will not send id of user to database
		contact.setUser(user);
		
		user.getContacts().add(contact);
		
		// image processing by using MULTI part file
		
		if(multipartFile.isEmpty())
		{
			// if file not found
			System.out.println(" IMAGE NOT FOUND");
			contact.setImage("contact.png");
		}
		else {
			// file found process it
			contact.setImage(multipartFile.getOriginalFilename());
			File file = new ClassPathResource("static/img").getFile(); 
			
			Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
			
			Files.copy(multipartFile.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("uploaded");
			
		}
		
		
		
		
		this.userRepository.save(user);
		
		System.out.println(contact);
		
		System.out.println("CONTACT ADDED TO DATABASE");
		//SUCCESS MESSAGE
		
		httpSession.setAttribute("message", new Message("YOUR CONTACT IS ADDED SUCCESSFULLY !!","success"));
		
		
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// MESSAGE SEND ERROR
			httpSession.setAttribute("message", new Message("SOMETHING WENT WRONG !!","danger"));
			
		}
		
		
		
	 return "normal/add_contact_form";
	}
	
	
	// per page 5 contacts 
	//per page 5[n]
	//current page = 0 [page]
	
	//show contact handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model model, Principal principal )
	{
		model.addAttribute("title","SHOW CONTACTS");
		// return contact list here from Database and show on the page
		String name = principal.getName();
		// you can use this method too but there is another way we will create contact repository and will make methods there
	User user = this.userRepository.getUserByUserName(name);
//		List<Contact> contacts = user.getContacts();
		
	// to pass paegable object in the new method
		
	// change here to change the pages
	Pageable pageable = PageRequest.of(page, 6);
	
	
	
	//	this.contactRepository.findAll(); // this will find all the contact but we want only contact of the user that is logged in
		
	//change type list to page after passing page able object
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable );
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		System.out.println(contacts);
		
		return "normal/show_contacts";
	}
	
	
	// showing particular contact handler and there is a bug that one user can access another users contact by changing url
	//to fix that we need some security checks that is by using principal
	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") int cId, Model model, Principal principal)
	{
		System.out.println(cId);
		Optional<Contact> byId = this.contactRepository.findById(cId);
		Contact contact = byId.get();
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		// here we fixed it
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
		}
		
		
		
		
		
		return "normal/contact_detail";
	}
	
	
	//delete contact handler
	
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal)
	{
		Optional<Contact> byId = this.contactRepository.findById(cId);
		Contact contact = byId.get();
		
		//Contact contact2 = this.contactRepository.findById(cId).get();
		//check here so that no one can delete another's contact
		// to unlink the contact because it is of cascade type so it is necessary to unlink it
		// but to unlink is not a good practice got data is still in the database thats why it not showing on the view page
		// 	contact.setUser(null); this is because of cascade
		// to resolve this issue in entity orphan delete true
		
		User user = this.userRepository.getUserByUserName(principal.getName());
	     user.getContacts().remove(contact);
	     this.userRepository.save(user);
		// override equals method in the entity too to match the contact object
		
		
		
		
		// ASSIGNMENT  PHOTO IN IMG FOLDER
		//IMAGE NAME WILL BE ON THE CONTACT.GETiMAGE()
		//COMBINE THEM AND MAKE A PATH TO DELETE THE IMAGE IN THE FOLDER
		
		
		
		//this.contactRepository.delete(contact);
		
		session.setAttribute("message", new Message("CONTACT DELETED SUCCESSFULLY !!!","success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	
	//open update contact handler
	@PostMapping("/update-contact/{cId}")
	public String updateContact(Model model, @PathVariable("cId") Integer cId)
	{
		
		model.addAttribute("title","UPDATE CONTACT");
		Optional<Contact> byId = this.contactRepository.findById(cId);
		Contact contact = byId.get();
		
		model.addAttribute("contact",contact);
	  return "normal/update_form";
	}
	
	//process update contact data form handler
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile, Model model,
			HttpSession session , Principal principal)
	{
		try {
			// old contact detail fetching
			Optional<Contact> byId = this.contactRepository.findById((int) contact.getcId());
			Contact oldContact = byId.get();
			
			
		
			
			//image
			if(!multipartFile.isEmpty())
			{
				//file work //rewrite
				//DELETE OLD PHOTO 
				
				
                File deleteFile = new ClassPathResource("static/img").getFile(); 
				
			//	Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
				File file1 = new File(deleteFile, oldContact.getImage());
				boolean delete = file1.delete();
				System.out.println(" OLD FILE IS DELETED : " +delete);
				
				
				//update new photo 
				File file = new ClassPathResource("static/img").getFile(); 
				
				Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
				
				Files.copy(multipartFile.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(multipartFile.getOriginalFilename());
			}
			else {
				contact.setImage(oldContact.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("YOU CONTACT IS UPDATED SUCCESSFULLY !!!","success") );
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(contact.getName());
		// id will not return right now will will make hided field in the form
		System.out.println(contact.getcId());
		
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	
	// handler to show profile page
	
	@GetMapping("/profile")
	public String showProfile(Model model)
	{
		model.addAttribute("title","PROFILE PAGE");
		return "normal/profile";
	}
	
	
	
	
	
}
