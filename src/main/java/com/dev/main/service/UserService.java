package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.UserDto;
import com.dev.main.model.User;

public interface UserService {
	List<User>getAllUsers();
	List<User> getAllUsersByName(String name);
	User getUserByEmail(String email);
	User getUserById(Long id);
	boolean isUsernameAlreadyExist(String username);
	void createUser(UserDto userDto,boolean isAdminRole);
	boolean editUserInfo(Long id, String username, String oldPassword, String newPassword);
	void initializeDefaultAdminUser(String name);
	void disableUser(Long id);
	void enableUser(Long id);
}
