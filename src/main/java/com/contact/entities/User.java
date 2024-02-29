package com.contact.entities;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "NAME IS REQUIRED !!")
	@Size(min = 2,max = 20, message = "MINIMUM 2 AND MAXIMUM 20 CHARACHTER ARE ALLOWED !!")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "EMAIL IS REQUIRED !!")
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "INVALID EMAIL FORMAT !!!")
	private String email;
	
	@NotBlank(message = "PASSWORD IS REQUIRED !!")
    @Size(min = 8, message = "PASSWORD MUST BE AT LEAST 8 CHARACTERS LONG !!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "PASSWORD MUST CONTAIN AT LEAST ONE UPPERCASE LETTER," + "ONE LOWERCASE LETTER, ONE DIGIT, ONE SPECIAL CHARACTER " + "AND NO WHITESPACE !!")
	private String password;
	
	private String role;
	
	private boolean enabled;
	
	private String imageUrl; 
	
	@NotBlank(message = "WRITE SOMETHING HERE !!")
	@Column(length = 500)
	private String about;
	
	// BECAUSE ONE USER HAS MANY CONTACTS 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)// use orphan removal true to fix this delete bug issue
	private List<Contact> contacts = new ArrayList<>();

	public User() {
		
	}
	

	public User(int id, String name, String email, String password, String role, boolean enabled, String imageUrl, String about) 
	{
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	

	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}

}
