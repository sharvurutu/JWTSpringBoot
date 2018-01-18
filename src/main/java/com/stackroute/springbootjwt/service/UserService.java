package com.stackroute.springbootjwt.service;

import java.util.List;

import com.stackroute.springbootjwt.model.User;

public interface UserService {

	public boolean save(User user);

	public boolean update(User user);

	public boolean delete(User user);

	public boolean validate(String username, String password);

	public User get(String username);

	public List<User> list();

}
