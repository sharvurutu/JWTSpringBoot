package com.stackroute.springbootjwt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.stackroute.springbootjwt.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	@Query("select u from User u where u.username=(?1) and u.password=(?2)")
	User validate(String username, String password);
}
