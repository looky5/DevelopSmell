package com.web.blog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.web.blog.model.like.Like;
import com.web.blog.model.post.Post;
import com.web.blog.model.post.PostResponse;
import com.web.blog.model.post.reportPostVo;

@Mapper
public interface PostDao {
	 public List<PostResponse> getTotalPost(@Param("userno") String userno, @Param("limit") int limit) throws Exception; //전체 게시글 조회
	 public List<PostResponse> getMyPostList(@Param("userno")  int userno, @Param("limit") int limit) throws Exception; //내가등록한 글만 가져오기
	 public List<PostResponse> getLikePostList(@Param("userno")  int userno, @Param("limit") int limit) throws Exception;
	 public List<PostResponse> getLatestPostList(int limit) throws Exception;
	 public List<PostResponse> getTopLikePostList() throws Exception;//좋아요 Top 게시물 조회
	 public List<PostResponse> getUserPostList(@Param("bloguser") String bloguser,@Param("userno") int userno, @Param("limit") int limit) throws Exception;
	 public List<PostResponse> getPostByTag(String tag) throws Exception;//tag로 게시글 조회
	 public List<Post> searchPost(@Param("userno") int userno, @Param("limit") int limit, @Param("tags") String[] tags, @Param("word") String word) throws Exception;
	 public List<String> searchTags(String word) throws Exception;
	 
	 public int createPost(@Param("userno") int userno, @Param("save") int save) throws Exception;
	 public String getTemplate(int templateno) throws Exception;
	 
	 
	 public int getPostNo(int userno) throws Exception;
	 public int deletePost(int postno) throws Exception;
	 public int deleteTags(int postno) throws Exception;
	 public PostResponse getPost(@Param("userno")  String userno, @Param("postno") int postno) throws Exception;
	 
	
	 public void clickLike(Like likepost);
	 public void deleteLike(@Param("userno") int userno, @Param("postno") int postno);

	 public int insertPost(Post post) throws Exception;
	 public int insertTags(@Param("postno") int postno, @Param("tags") String[] tags) throws Exception;
	 
	 public int modifyPost(Post post) throws Exception;
	 public int modifyLike(Like like) throws Exception;
	 public int modifySave(@Param("postno") int postno, @Param("save") int save) throws Exception;

		public int reportPost(reportPostVo report) throws Exception;
		
		public List<reportPostVo> searchReport(int limit) throws Exception;

	 
	 
	 
}
