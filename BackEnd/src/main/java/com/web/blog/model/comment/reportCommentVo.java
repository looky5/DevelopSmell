package com.web.blog.model.comment;

import lombok.Data;

@Data
public class reportCommentVo {

	private int creportno;
	private int userno;
	private int postno;
	private int commentno;
	private String uid;
	private String reply;
	private String content;
	private int reportcount;
}
