package com.web.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blog.dao.PostDao;
import com.web.blog.model.like.Like;
import com.web.blog.model.post.Post;
import com.web.blog.model.post.PostResponse;
import com.web.blog.model.post.reportPostVo;

@Service
public class PostServiceImpl implements IPostService {

	
	@Autowired
	PostDao postdao;
	
	
	@Override
	public List<PostResponse> getTotalPost(String userno , int limit) throws Exception {
		return postdao.getTotalPost(userno, limit);
	}
	

	@Override
	public List<PostResponse> getMyPostList(int userno, int limit) throws Exception {
		return postdao.getMyPostList(userno, limit);
	}
	
   @Override
   public List<Post> searchPost(int userno, int limit, String[] tags, String word) throws Exception {
      return postdao.searchPost(userno, limit, tags, word);
   }

	@Override
	public String getTemplate(int templateno) throws Exception {
		return postdao.getTemplate(templateno);
	}


	@Override
	public List<String> searchTags(String word) throws Exception {
		return postdao.searchTags(word);
	}
		

	@Override
	public List<PostResponse> getLatestPostList(int limit) throws Exception {
		return postdao.getLatestPostList(limit);
	}

	@Override
	public List<PostResponse> getTopLikePostList() throws Exception {
		return postdao.getTopLikePostList();
	}
	
	@Override
	public void clickLike(Like like) throws Exception {
		postdao.clickLike(like);

	}
	@Override
	public void deleteLike(int userno, int postno) throws Exception {
		postdao.deleteLike(userno, postno);

		
	}


	@Override
	public int createPost(Post post) throws Exception {
		//1. postReg에 insert
		System.out.println("작성하려는 게시글 번호 가져오기");
		postdao.createPost(post.getUserno(), post.getSave());
		
		int postno = getPostNo(post.getUserno()); 
		//2. 가지고 오는거
		//postno를가지고 post테이블에 삽입
		post.setPostno(postno);
		
		//3. 태그 넣기
		String[] tags = post.getTags();
		insertPost(post); 
		
		int res = 0;
		if(tags != null) {
			res = postdao.insertTags(postno, tags);
		}
		
	
		return res;
		
		
	}


	@Override
	public int getPostNo(int userno) throws Exception {
		return postdao.getPostNo(userno);
	}


	@Override
	public int insertPost(Post post) throws Exception {
		return postdao.insertPost(post);
	}


	@Override
	public int deletePost(int postno) throws Exception {
		return postdao.deletePost( postno);
	}


	@Override
	public int modifyPost(Post post, String[] tags, int save) throws Exception {
		
		postdao.modifySave(post.getPostno(), save);
		postdao.modifyPost(post);
		postdao.deleteTags(post.getPostno());
		
		return postdao.insertTags(post.getPostno(), tags);
	}


	@Override
	public PostResponse getPost(String userno, int postno) throws Exception {
		return postdao.getPost(userno, postno);
	}


	@Override
	public List<PostResponse> getLikePostList(int userno, int limit) throws Exception {
		return postdao.getLikePostList(userno, limit);
	}


	@Override
	public int modifyLike(Like like) throws Exception {
		return postdao.modifyLike(like);
	}


	@Override
	public int reportPost(reportPostVo report) throws Exception {
		return postdao.reportPost(report);
	}

	@Override
	public List<reportPostVo> searchReport(int limit) throws Exception {
		return postdao.searchReport(limit);
	}


	@Override
	public List<PostResponse> getUserPostList(String bloguser, int userno, int limit) throws Exception {
		return postdao.getUserPostList(bloguser, userno, limit);
	}


	@Override
	public List<PostResponse> getPostByTag(String tag) throws Exception {
		return postdao.getPostByTag(tag);
	}







}
