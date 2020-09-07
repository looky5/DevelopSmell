package com.web.blog.service;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blog.dao.UserDao;
import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.model.user.UserRank;
import com.web.blog.util.SHA256Util;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	UserDao userDao;

	@Transactional
	@Override
	public int loginUser(User user) throws Exception {
		User u = userDao.getUserByEmail(user.getEmail());
		String salt = u.getSalt();
		String npw = SHA256Util.getEncrypt(user.getPassword(), salt);
		if (u.getPassword().equals(npw)) {
			u.setPassword(null);
			u.setSalt(null);
			return u.getUserno();
		} else {
			return -1;
		}
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		return userDao.getUserByEmail(email);
	}

	@Transactional
	@Override
	public int joinUser(SignupRequest user) throws Exception {
		return userDao.joinUser(user);
	}

	@Transactional
	@Override
	public int updateUser(User user) throws Exception {
		return userDao.updateUser(user);
	}

	@Override
	public User infoUser(int userno) throws Exception {
		return userDao.infoUser(userno);
	}

	@Transactional
	@Override
	public int deleteUser(int userno) throws Exception {
		return userDao.deleteUser(userno);
	}

	@Transactional
	@Override
	public int insProfileUrl(int userno, String profilename) throws Exception {
		return userDao.insProfileUrl(userno, profilename);
	}

	@Override
	public User getUserbyUserNo(int userno) throws Exception {
		return userDao.getUserbyUserNo(userno);
	}

	@Override
	public List<UserRank> getUsersRank(int limit) throws Exception {
		return userDao.getUsersRank(limit);
	}

	@Transactional
	@Override
	public int createAuthCode(String email, String code) throws Exception {
		return userDao.createAuthCode(email, code);
	}

	@Override
	public String getAuthcode(String email) throws Exception {
		return userDao.getAuthcode(email);
	}

	@Transactional
	@Override
	public int deleteAuthCode(String email) throws Exception {
		return userDao.deleteAuthCode(email);
	}

	@Transactional
	@Override
	public int UserPwChange(SignupRequest user) throws Exception, SQLSyntaxErrorException {
		return userDao.UserPwChange(user);
	}
	
	@Override
	public User getUserbyUid(String bloguser) throws Exception {
		return userDao.getUserbyUid(bloguser);
	}
	

}
