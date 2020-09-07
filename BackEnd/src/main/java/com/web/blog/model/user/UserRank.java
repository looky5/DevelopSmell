package com.web.blog.model.user;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
public class UserRank {
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int userno;
	   private int userrank;
	   private String uid;
	   private int userscore;
	   private String profilename;
	   private String userrating;
	
}
