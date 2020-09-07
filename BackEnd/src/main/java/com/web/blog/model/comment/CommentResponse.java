package com.web.blog.model.comment;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.web.blog.model.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
	  @Id
	   private int commentno;
	   private int userno;
	   private int postno;
	   private String uid;
	   private String reply;
	   private String reply_date;
	   private int likecount;
	   private int dislikecount;
	   private int kind;
	   private String profilename;
	
}
