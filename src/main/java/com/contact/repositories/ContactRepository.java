package com.contact.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entities.Contact;
//import java.util.List;


public interface ContactRepository extends JpaRepository<Contact, Integer>
{
	// pagination we will use page which is subset of the list we can 
	// traverse through the list by suing page it is a sublist of the list object

	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);
	
	//instead of list use page
	
}
