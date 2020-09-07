package com.web.blog.model.like;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

	@Id
	private int userno;
	private int targetno;
	private int kind;

}
