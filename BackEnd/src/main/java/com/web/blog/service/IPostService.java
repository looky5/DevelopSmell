package com.web.blog.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.web.blog.model.like.Like;
import com.web.blog.model.post.Post;
import com.web.blog.model.post.PostResponse;
import com.web.blog.model.post.reportPostVo;

public interface IPostService {

	public List<PostResponse> getTotalPost(String userno, int limit) throws Exception; // 전체 게시글 조회
	public List<PostResponse> getMyPostList(int userno, int limit) throws Exception; // 내가 등록한 게시글 조회
	public List<PostResponse> getUserPostList( String bloguser, int userno,int limit) throws Exception; // 내가 등록한 게시글 조회
	public List<PostResponse> getLikePostList(int userno, int limit) throws Exception; //내가 좋아요한 게시글 조회
	public List<PostResponse> getPostByTag(String tag) throws Exception;//tag로 게시글 조회
	public List<PostResponse> getLatestPostList(int limit) throws Exception;//최신 게시글 조회
	
	public List<Post> searchPost(int userno, int limit, String[] tags, String word) throws Exception;
	public List<String> searchTags(String word) throws Exception;
	public List<PostResponse> getTopLikePostList() throws Exception;//좋아요 Top 게시물 조회
	public String getTemplate(int templateno) throws Exception;
	public PostResponse getPost(String userno, int postno) throws Exception;

	public int createPost(Post post) throws Exception;

	public int getPostNo(int userno) throws Exception;

	public int insertPost(Post post) throws Exception;

	public int deletePost(int postno) throws Exception;

	public int modifyPost(Post post, String[] tags, int save) throws Exception;

	public int modifyLike(Like like) throws Exception;

	public void clickLike(Like like) throws Exception;

	public void deleteLike(int userno, int postno) throws Exception;
	
public int reportPost(reportPostVo report) throws Exception;
	
	public List<reportPostVo> searchReport(int limit) throws Exception;


}
