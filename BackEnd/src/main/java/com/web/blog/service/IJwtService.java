package com.web.blog.service;

import java.util.Map;

public interface IJwtService {
	
	public String createToken(int userno) throws Exception;
	
	public boolean isUsable(String token);
	
	public Map<String, Object> get(String key);

}
