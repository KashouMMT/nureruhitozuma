package com.dev.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    private String name;

    @NotBlank(message = "Name is required")
//  @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String oldPassword;
    
    public UserDto() {
    	
    }

	public UserDto(String name, @NotBlank(message = "Name is required") String email,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@Size(min = 6, message = "Password must be at least 6 characters") String oldPassword) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.oldPassword = oldPassword;
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
