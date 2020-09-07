package com.web.blog.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.blog.model.BasicResponse;
import com.web.blog.model.like.Like;
import com.web.blog.model.post.Post;
import com.web.blog.model.post.PostResponse;
import com.web.blog.model.post.reportPostVo;
import com.web.blog.service.ICommentService;
import com.web.blog.service.IPostService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	IPostService postService;

	@Autowired
	ICommentService commentService;

	/* RRRRRRRRRRRRRRRRRRRRRRRRR */
	@ApiOperation("전체 게시글  정보 반환")
	@GetMapping("/total/{limit}/{userno}")
	public ResponseEntity getTotalPostList(@PathVariable("userno") String userno, @PathVariable("limit") int limit)
			throws Exception {
		List<PostResponse> list = postService.getTotalPost(userno, limit);

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@ApiOperation("조건에 맞는 게시물을 조회한다.")
	@GetMapping("/search/{limit}/{userno}")
	public ResponseEntity<?> searchPost(@PathVariable("userno") int userno, @PathVariable("limit") int limit,
			@RequestParam(value = "tags", required = false) String[] tags,
			@RequestParam(value = "word", required = false) String word) throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		List<Post> list = postService.searchPost(userno, limit, tags, word);
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

	@ApiOperation("조건에 맞는 태그들을 조회한다.")
	@GetMapping("/search/{word}")
	public ResponseEntity<?> searchTags(@PathVariable String word) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<String> list = postService.searchTags(word);
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

	@GetMapping("/latest/{limit}")
	public ResponseEntity getLatestPostList(@PathVariable("limit") int limit) throws Exception {
		List<PostResponse> list = postService.getLatestPostList(limit);

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@GetMapping("/toppost")
	public ResponseEntity getTopLikePostList() throws Exception {

		List<PostResponse> totallist = postService.getTopLikePostList();

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		result.object = totallist;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@ApiOperation("특정 게시물 및 좋아요/싫어요 정보 반환")
	@GetMapping("/detail/{postno}/{userno}")
	public ResponseEntity getPost(@PathVariable("userno") String userno, @PathVariable("postno") int postno)
			throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		PostResponse detail = postService.getPost(userno, postno);
		result.status = true;
		result.object = detail;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	
	@ApiOperation("특정 게시물 및 좋아요/싫어요 정보 반환")
	@GetMapping("/template/{templateno}")
	public ResponseEntity getTemplate(@PathVariable("templateno") int templateno)
			throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String template = postService.getTemplate(templateno);
		result.status = true;
		result.object = template;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	// 내가 등록한 게시글만 가져오기
	@GetMapping("/mine/{userno}/{limit}")
	public ResponseEntity getMyPostList(@PathVariable("userno") int userno, @PathVariable("limit") int limit)
			throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		List<PostResponse> list = postService.getMyPostList(userno, limit);
		result.status = true;
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@GetMapping("/other/{bloguser}/{userno}/{limit}")
	public ResponseEntity getUserPostList(@PathVariable("bloguser") String bloguser, @PathVariable("userno") int userno,
			@PathVariable("limit") int limit) throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		List<PostResponse> list = postService.getUserPostList(bloguser, userno, limit);
		result.status = true;
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@GetMapping("/tags/{tag}")
	public ResponseEntity getPostByTag(@PathVariable("tag") String tag) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<PostResponse> list = postService.getPostByTag(tag);
		result.status = true;
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@GetMapping("/like/{userno}/{limit}")
	public ResponseEntity getLikePostList(@PathVariable("userno") int userno, @PathVariable("limit") int limit)
			throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		List<PostResponse> list = postService.getLikePostList(userno, limit);
		result.status = true;
		result.data = "success";
		result.object = list;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	/* CCCCCCCCCCCCCCCCCCCCCCCCC */
	@ApiOperation("게시글을 작성한다.")
	@PostMapping("")
	public ResponseEntity<?> createPost(@RequestPart("thumbnail") MultipartFile thumbnail,
			@RequestParam("userno") int userno, @RequestParam("title") String title,
			@RequestParam("subtitle") String subtitle, @RequestParam("usetags") String[] tags,
			@RequestParam("content") String content, @RequestParam("save") int save) throws Exception {
		Post post = new Post();
		post.setContent(content);
		post.setSave(save);
		post.setSubtitle(subtitle);
		post.setTags(tags);
		post.setUserno(userno);
		post.setTitle(title);

		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
		String time1 = format1.format(new Date());
//		String filename = "http://localhost:8082/blog/SSAFY/images/" + userno + thumbnail.getOriginalFilename();
		String filename = "http://i3b208.p.ssafy.io:8080/blog/images/thumbnails/" + userno + time1 + thumbnail.getOriginalFilename();
		post.setThumbnail(filename);
		String fileUrl = "/home/ubuntu/images/thumbnails/" + userno + time1
//		String fileUrl = "C:\\SSAFY\\images/" + userno
				+ thumbnail.getOriginalFilename();
		File dest = new File(fileUrl);
		thumbnail.transferTo(dest);

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		postService.createPost(post);
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@ApiOperation("좋아요, 싫어요에 대한 정보를 넣는다")
	@PostMapping("/like")
	public ResponseEntity clickLike(@RequestBody Like likepost) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		postService.clickLike(likepost);
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	/* DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD */
	@ApiOperation("자신이 포스팅한 게시글을 삭제한다.")
	@DeleteMapping("/{postno}")
	public ResponseEntity deletePost(@PathVariable("postno") int postno) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		int deleteRes = postService.deletePost(postno);
		if (deleteRes != 0) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}
		return response;
	}

	@ApiOperation("좋아요, 싫어요를 삭제한다.")
	@DeleteMapping("/like/{userno}/{postno}")
	public ResponseEntity deleteLike(@PathVariable("userno") int userno, @PathVariable("postno") int postno)
			throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		postService.deleteLike(userno, postno);
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	/* UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU */

	@ApiOperation("자신이 작성한 게시글의 내용을 수정한다.")
	@PutMapping("")
	public ResponseEntity modifyPost(@RequestPart("thumbnail") MultipartFile thumbnail,
			@RequestParam("postno") int postno, @RequestParam("userno") int userno, @RequestParam("title") String title,
			@RequestParam("subtitle") String subtitle, @RequestParam("usetags") String[] tags,
			@RequestParam("content") String content, @RequestParam("save") int save) throws Exception {
		Post post = new Post();
		post.setPostno(postno);
		post.setContent(content);
		post.setSave(save);
		post.setSubtitle(subtitle);
		post.setTags(tags);
		post.setUserno(userno);
		post.setTitle(title);

		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
		String time1 = format1.format(new Date());

		String filename = "http://i3b208.p.ssafy.io:8080/blog/images/thumbnails/" + userno + time1 + thumbnail.getOriginalFilename();
//		String filename = "http://localhost:8080/blog/images/" + userno + time1 + thumbnail.getOriginalFilename();
		post.setThumbnail(filename);

		String fileUrl = "/home/ubuntu/images/thumbnails/" + userno + time1
				+ thumbnail.getOriginalFilename();
		
//		String fileUrl = "/C:/images/" + userno + time1
//				+ thumbnail.getOriginalFilename();
		File dest = new File(fileUrl);
		thumbnail.transferTo(dest);

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		int modRes = postService.modifyPost(post, tags , save);

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
		int modifyRes = postService.modifyLike(like);
		if (modifyRes != 0) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}
		return response;
	}

	@ApiOperation("신고된 게시물을 조회한다.")
	@GetMapping("/report/{limit}")
	public ResponseEntity<?> selectReport(@PathVariable int limit) throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		List<reportPostVo> list = postService.searchReport(limit);
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

	@ApiOperation("게시물을 신고한다.")
	@PostMapping("/report")
	public ResponseEntity<?> reportPost(@RequestBody reportPostVo report) throws Exception {
		ResponseEntity response = null;

		final BasicResponse result = new BasicResponse();
		int res = postService.reportPost(report);
		if (res != 0) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		return response;
	}

}
