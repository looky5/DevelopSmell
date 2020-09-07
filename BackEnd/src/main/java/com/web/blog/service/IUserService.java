package com.web.blog.service;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.model.user.UserRank;

public interface IUserService {
	public int loginUser(User user) throws Exception; // 로그인

	public User getUserByEmail(String email) throws Exception; // email로 중복체크

	public int joinUser(SignupRequest user) throws Exception; // 회원가입

	public int updateUser(User user) throws Exception; // 회원정보수정

	public User getUserbyUserNo(int userno) throws Exception;
	
	public User getUserbyUid(String bloguser) throws Exception;

	public User infoUser(int userno) throws Exception;

	public int deleteUser(int userno) throws Exception;

	public int insProfileUrl(int userno, String profilename) throws Exception;

	public List<UserRank> getUsersRank(int limit) throws Exception;

	public int createAuthCode(String email, String code) throws Exception;

	public String getAuthcode(String email) throws Exception;

	public int deleteAuthCode(String email) throws Exception;

	public int UserPwChange(SignupRequest user) throws Exception, SQLSyntaxErrorException;
}
