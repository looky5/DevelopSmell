package com.web.blog.model.post;

import lombok.Data;

@Data
public class searchPostVo {

	private String[] tags;
	//작성자 제목 내용
	private String key;
	//검색 내용
	private String word;
	private int tagflag;
	private String userno;
	private int limit;
}
