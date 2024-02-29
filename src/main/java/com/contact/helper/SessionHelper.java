package com.contact.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeMessage()
	{
		
		// instead of th block use this direct accessing session is depricated
		// call this instead in the block use this class object in that 
		try {
			
			System.out.println("Removing Session");
			HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
