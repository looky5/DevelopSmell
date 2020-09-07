package com.web.blog.model.post;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	private int postno;
	private int userno;
	private String uid;
	private String title;
	private String subtitle;
	private String content;
	private String[] tags;
	private String thumbnail;
	private int save;
	


	
	
}
