package com.web.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blog.dao.CommentDao;
import com.web.blog.model.comment.Comment;
import com.web.blog.model.comment.CommentResponse;
import com.web.blog.model.comment.reportCommentVo;
import com.web.blog.model.like.Like;

@Service
public class CommentService implements ICommentService {

	@Autowired
	CommentDao commentdao;
	
	@Override
	public List<CommentResponse> getCommentsByPostNo(int postno, String userno) throws Exception {
		return commentdao.getCommentsByPostNo(postno, userno);
		
		
	}

	@Override
	public int insertComment(Comment comment) throws Exception {
		return commentdao.insertComment(comment);
	}

	@Override
	public void clickLike(Like like) {
		commentdao.clickLike(like);
		
	}

	@Override
	public void deleteLike(int userno, int commentno) {
		commentdao.deleteLike(userno, commentno);
		
	}

	@Override
	public int modifyLike(Like like) throws Exception {
		return commentdao.modifyLike(like);
	}

	@Override
	public int deleteComment(int commentno) throws Exception {
		return commentdao.deleteComment(commentno);
	}
	@Override
	public int reportComment(reportCommentVo report) throws Exception {
		return commentdao.reportComment(report);
	}

	@Override
	public List<reportCommentVo> searchReport(int limit) throws Exception {
		return commentdao.searchReport(limit);
	}

}
