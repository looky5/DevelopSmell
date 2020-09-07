package com.web.blog.model.post;

import lombok.Data;

@Data
public class reportPostVo {

	private int reportno;
	private int userno;
	private int postno;
	private int reportcount;
	private String content;
	private String uid;
	private String title;
}
