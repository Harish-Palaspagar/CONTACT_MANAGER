package com.contact.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.entities.User;
import com.contact.repositories.UserRepository;

public class UserDetailServiceImpl implements UserDetailsService {

	
	// YOU HAVE TO FETCH THE USER FROM DATABASE USE  USER REPOSITORY HERE
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userByUserName = userRepository.getUserByUserName(username);
		if(userByUserName==null)
		{
			throw new UsernameNotFoundException("USER NOT FOUND");
		}
		
		CustomerUserDetails cDetails = new CustomerUserDetails(userByUserName);
		
		
		return cDetails;
	}

}
