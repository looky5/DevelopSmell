
package com.web.blog.dao;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.model.user.UserRank;

@Mapper
public interface UserDao {
	public User getUserByEmail(String email) throws Exception;

	public int joinUser(SignupRequest user) throws Exception;

	public User loginUser(User user) throws Exception;

	public int updateUser(User user) throws Exception;

	public User infoUser(int userno) throws Exception;

	public int deleteUser(int userno) throws Exception;

	public User getUserbyUserNo(int userno) throws Exception;
	
	public User getUserbyUid(String bloguser) throws Exception;

	public int insProfileUrl(@Param(value = "userno") int userno, @Param(value = "file") String filename)
			throws Exception;

	public List<UserRank> getUsersRank(int limit) throws Exception;

	public int createAuthCode(@Param(value = "email") String email, @Param(value = "code") String code)
			throws Exception;

	public String getAuthcode(String email) throws Exception;

	public int deleteAuthCode(String email) throws Exception;

	public int UserPwChange(SignupRequest user) throws Exception, SQLSyntaxErrorException;
}
