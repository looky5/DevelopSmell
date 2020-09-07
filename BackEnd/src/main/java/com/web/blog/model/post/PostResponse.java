package com.web.blog.model.post;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
	
	@Id
	private int postno;
	private int likecount;
	private int dislikecount;
	private int userno;
	private String uid;
	private int score;
	private String title;
	private String subtitle;
	private String content;
	private String create_date;
	private int kind;
	private String tags;
	private int save;
	private String thumbnail;
	private String userrating;
	private String postrating;
	private String profilename;

}
