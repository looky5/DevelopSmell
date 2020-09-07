package com.web.blog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.blog.model.comment.Comment;
import com.web.blog.model.comment.CommentResponse;
import com.web.blog.model.comment.reportCommentVo;
import com.web.blog.model.like.Like;

@Mapper
public interface CommentDao {


	public List<CommentResponse> getCommentsByPostNo(@Param("postno") int postno, @Param("userno") String userno) throws Exception;
	public int insertComment(Comment comment) throws Exception;
	
	 public void clickLike(Like like);
	 public void deleteLike(@Param("userno") int userno,@Param("commentno") int commentno);
	 public int modifyLike(Like like) throws Exception;
	 
	 public int deleteComment(int commentno) throws Exception;
 public int reportComment(reportCommentVo report) throws Exception;
	 
	 public List<reportCommentVo> searchReport(int limit) throws Exception;
}
