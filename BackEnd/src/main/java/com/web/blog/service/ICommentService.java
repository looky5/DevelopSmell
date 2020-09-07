package com.web.blog.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.blog.model.comment.Comment;
import com.web.blog.model.comment.CommentResponse;
import com.web.blog.model.comment.reportCommentVo;
import com.web.blog.model.like.Like;


public interface ICommentService {
	
	public List<CommentResponse> getCommentsByPostNo(int postno, String userno) throws Exception;
	public int insertComment(Comment comment) throws Exception;
	
	 public void clickLike(Like like);
	 public void deleteLike(int userno, int commentno);
	 public int modifyLike(Like like) throws Exception;
	 
	 public int deleteComment(int commentno) throws Exception;
 public int reportComment(reportCommentVo report) throws Exception;
	 
	 public List<reportCommentVo> searchReport(int limit) throws Exception;
}
