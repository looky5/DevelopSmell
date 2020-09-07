package com.web.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.blog.model.BasicResponse;
import com.web.blog.model.comment.Comment;
import com.web.blog.model.comment.CommentResponse;
import com.web.blog.model.comment.reportCommentVo;
import com.web.blog.model.like.Like;
import com.web.blog.service.ICommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	ICommentService commentService;

	@ApiOperation(value = "특정 게시글에 있는 댓글들을 가져온다.")
	@GetMapping("/get/{postno}/{userno}")
	public ResponseEntity<?> getTotalPostList(@PathVariable("postno") int postno, @PathVariable("userno") String userno)
			throws Exception {
		ResponseEntity response = null;

		List<CommentResponse> cr = commentService.getCommentsByPostNo(postno, userno);
		BasicResponse result = new BasicResponse();
		if (cr != null) {
			result.status = true;
			result.data = "success";
			result.object = cr;
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@ApiOperation(value = "댓글을 삭제한다")
	@DeleteMapping("/{commentno}")
	public ResponseEntity deleteComment(@PathVariable("commentno") int commentno) throws Exception {

		ResponseEntity response = null;
		int deletetRes = commentService.deleteComment(commentno);
		BasicResponse result = new BasicResponse();
		if (deletetRes == 1) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.status = true;
			result.data = "fail";
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	@ApiOperation(value = "특정 게시글에 작성한다.")
	@PostMapping("")
	public ResponseEntity insertComment(@RequestBody Comment comment) throws Exception {
		ResponseEntity response = null;

		int insertRes = commentService.insertComment(comment);
		BasicResponse result = new BasicResponse();
		if (insertRes == 1) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.status = true;
			result.data = "fail";

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	@ApiOperation("좋아요, 싫어요에 대한 정보를 넣는다")
	@PostMapping("/like")
	public ResponseEntity clickLike(@RequestBody Like like) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		commentService.clickLike(like);
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@ApiOperation("좋아요, 싫어요를 삭제한다.")
	@DeleteMapping("/like/{userno}/{commentno}")
	public ResponseEntity deleteLike(@PathVariable("userno") int userno, @PathVariable("commentno") int commentno)
			throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		commentService.deleteLike(userno, commentno);
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@ApiOperation("좋아요, 싫어요를 수정한다.")
	@PutMapping("/like")
	public ResponseEntity modifyLike(@RequestBody Like like) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		int modifyRes = commentService.modifyLike(like);
		if (modifyRes != 0) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}
		return response;
	}

	@ApiOperation("댓글을 신고한다.")
	@PostMapping("/report")
	public ResponseEntity reportComment(@RequestBody reportCommentVo report) throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		int res = commentService.reportComment(report);
		if (res != 0) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		return response;
	}

	@ApiOperation("신고된 댓글을 조회한다.")
	@GetMapping("/report/{limit}")
	public ResponseEntity<?> selectReport(@PathVariable int limit) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<reportCommentVo> list = commentService.searchReport(limit);
		if (list != null) {
			result.status = true;
			result.data = "success";
			result.object = list;
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		return response;
	}

}
