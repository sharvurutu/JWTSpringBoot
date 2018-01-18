package com.stackroute.springbootjwt.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.springbootjwt.model.User;
import com.stackroute.springbootjwt.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserAuthController {

	Map<String, String> map = new HashMap<>();

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String serverStarted() {
		return "AUTHENTICATION SERVER STARTED..!!!";
	}

	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody User user) {

		User userObj = userService.get(user.getUsername());

		if (userObj != null) {
			return new ResponseEntity<String>("USER ALREADY EXIST..!!!", HttpStatus.CONFLICT);
		}

		userService.save(user);
		return new ResponseEntity<String>("USER CREATED SUCCESSFULLY..!!!", HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) throws ServletException {

		String jwtToken = "";

		try {

			jwtToken = getToken(user.getUsername(), user.getPassword());
			map.clear();
			map.put("message", "user successfully logged in");
			map.put("token", jwtToken);
		} catch (Exception e) {
			String exceptionMessage = e.getMessage();
			map.clear();
			map.put("token", null);
			map.put("message", exceptionMessage);
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	public String getToken(String username, String password) throws Exception {

		String jwtToken = "";
		String role = "admin";

		if (username == null || password == null) {
			throw new ServletException("Please fill in username and password");
		}

		boolean flag = userService.validate(username, password);

		if (!flag) {
			throw new ServletException("Invalid credentials.");
		}

		jwtToken = Jwts.builder().setSubject(username).setIssuedAt(new Date()).claim("role", role)
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

		return jwtToken;

	}
}
