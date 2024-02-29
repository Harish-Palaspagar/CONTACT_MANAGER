package com.contact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	// MAKE A METHOD WHICH ACCEPCT EMAIL AND RETURN USER ACC TO IT
	//MAKE CUSTOMER USER DETAIL IN CONFIG PACKAGE
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByUserName(@RequestParam("email") String email);

	
	

}
