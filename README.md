
# Develop Smell

## Member

##### Team 4Men
* Front-End
  * 전병규 :crown:
  * 이석기
* Back-End
  * 윤영현
  * 장진우

## Project introduction

* 개발자들이 만드는 트렌디한 IT 블로그

## Manual

* :link: 주소 : [Smell](http://i3b208.p.ssafy.io)
* 포스트 조회 등의 기능은 비로그인시 사용가능.
* 포스트 작성, :thumbsup: 좋아요 등의 기능은 로그인시 사용가능.
* 로그인은 일반 회원 로그인, 카카오로그인 두 가지가 있습니다.
* 카카오로그인시 이메일 동의가 필수입니다.


> 아래 설명의 ▶ 버튼 클릭 시 추가 설명이 나타납니다.


## Function & Description

<details>
<summary> :couple: 유저관리</summary>

* 유저에 대한 전반적인 기능입니다.
  * 회원가입 기능
  * 로그인 기능
  * 정보 수정 기능
</details>

<details>
<summary> :pencil: 포스트 작성</summary>

* 자신의 블로그에 포스트를 작성하는 기능입니다.
  * Toast UI Editor를 사용하여 Markdown과 WYSIWYG 편집기를 사용.
</details>

<details>
<summary> :mag: 포스트 조회</summary>

* 어떤 사람이 쓴 글이던지 조회가 가능합니다.
  * 포스트의 리스트는 인피니트 스크롤을 적용하여 페이징 처리.
</details>

<details>
<summary> :bar_chart: 랭킹</summary>

* 게시글과 유저에 랭킹을 부여하는 기능입니다.
  * 게시글에는 보이지않는 점수가 있고, 이 점수로 유저들이 보다 질 좋은 포스트를 볼 수 있도록 합니다.
  * 유저에게 포스팅을 하는 동기부여가 될 수 있도록 랭킹시스템을 도입하여 포스팅의 재미를 더해줍니다.
</details>

# Backend

## User API

<details>
<summary>회원 정보 수정</summary>

### 회원 정보 수정
---
- path : .../user
- method : PUT
- input data : userno, uid, password, introduce
</details>

<details>
<summary>회원 정보 조회</summary>

### 회원 정보 조회
---
- path : .../user/{userno}
- method : GET
- input data : userno
</details>

<details>
<summary>회원 정보 삭제</summary>

### 회원 정보 삭제
---
- path : .../user/{userno}
- method : DELETE
- input data : userno
</details>

<details>
<summary>메일 인증</summary>

### 메일 인증
---
- path : .../account/certification/{flag}
- method : POST
- input data : email, flag
- output data : code
</details>

<details>
<summary>로그인</summary>

### 로그인
---
- path : .../account/login
- method : POST
- input data : email, password
- output data : userno, uid, email
</details>

<details>
<summary>로그인 여부 확인</summary>

### 로그인 여부 확인 
---
- path : .../account/user/{userno}
- method : GET
- input data : userno
- ouput data : uid, email
</details>

<details>
<summary>비밀번호 변경</summary>

### 비밀번호 변경 
---
- path : .../account/changepw
- method : PUT
- input data : email, code, password
</details>

<details>
<summary>전체 유저 랭크 조회</summary>

### 전체 유저 랭크 조회
---
- path : .../user/rank/{limit}
- method : GET
- input data : limit
- output date : userrating, userrank, userscore, userno, uid, profilename
</details>

<details>
<summary>프로필 업로드</summary>

### 프로필 업로드
---
- path : .../user/profile
- method : POST
- input data : file, userno
</details>

<details>
<summary>카카오 소셜 로그인</summary>

### 카카오 소셜 로그인
---
- path : .../account/kakaologin
- method : GET
- input data : access_token
- output date : token, userno, uid
</details>

## Comment API

<details>
<summary>댓글 조회</summary>

###  댓글 조회
---
- path : .../comment/{postno}/{userno}
- method : GET
- input data : postno, userno
- output data : commentno, userno, postno, uid, reply, reply_date, likecount, dislikecount, kind, profilename 
</details>

<details>
<summary>댓글 작성</summary>

### 댓글 작성
---
- path : .../comment
- method : POST
- input data : userno, reply
</details>

<details>
<summary>좋아요, 싫어요 추가</summary>

### 좋아요, 싫어요 추가
---
- path : .../comment/like
- method : POST
- input data : userno, targetno, kind
</details>

<details>
<summary>좋아요, 싫어요 삭제</summary>

### 좋아요, 싫어요 삭제
---
- path : .../comment/like/{userno}/{commentno}
- method : DELETE
- input data : userno, commentno
</details>

<details>
<summary>댓글 삭제</summary>

### 댓글 삭제
---
- path : .../comment/{commentno}
- method : DELETE
- input data : commentno
</details>

<details>
<summary>좋아요, 싫어요 수정</summary>

### 좋아요, 싫어요 수정
---
- path : .../comment/like
- method : PUT
- input data : userno, targetno, kind
</details>

<details>
<summary>댓글 신고</summary>

### 댓글 신고
---
- path : .../comment/report
- method : POST
- input data : commentno, userno, content
</details>

<details>
<summary>신고 댓글 조회</summary>

### 신고 댓글 조회
---
- path : .../comment/report/{limit}
- method : GET
- input data : limit
- output data : commentno, reportcount, creportno, userno, uid, reply, postno, content
</details>

## Post API

<details>
<summary>게시글 작성</summary>

### 게시글 작성
---
- path : .../post
- method : POST
- input data : userno, postno, title, subtitle, content, save
</details>

<details>
<summary>게시글 수정</summary>

### 게시글 수정
---
- path : .../post
- method : PUT
- input data : postno, title, content
</details>

<details>
<summary>게시글 삭제</summary>

### 게시글 삭제
---
- path : .../post/{postno}
- method : DELETE
- input data : postno
</details>

<details>
<summary>게시글 조회</summary>

### 게시글 조회(검색 기능 포함)
---
- path : /post/search/{limit}/{userno}
- method : GET
- input data : limit, userno, tags, word
- output data : userrating, postrating, ranking, score, kind, likecount, dislikecount, tags, userno, uid, postno, title, subtitle, content, profilename, thumbnail, create_date
</details>

<details>
<summary>게시글 좋아요, 싫어요</summary>

### 게시글 좋아요, 싫어요
---
- path : .../post/like
- method : POST
- input data : userno, targetno, kind
</details>

<details>
<summary>게시글 좋아요, 싫어요 수정</summary>

### 게시글 좋아요, 싫어요 수정
---
- path : .../post/like
- method : PUT
- input data : userno, targetno, kind
</details>

<details>
<summary>게시글 좋아요, 싫어요 삭제</summary>

### 게시글 좋아요, 싫어요 삭제
---
- path : .../post/like/{userno}/{postno}
- method : DELETE
- input data : userno, postno
</details>

<details>
<summary>최근 게시물 조회</summary>

### 최근 게시물 조회
---
- path : .../post/latest/{limit}
- method : GET
- input data : limit
- output data : likeusers, dislikeusers, tags, userno, uid, postno, title, subtitle, thumbnail, content, create_date, profilename
</details>

<details>
<summary>유저 게시물 조회</summary>

### 유저 게시물 조회
---
- path : .../post/other/{bloguser}/{userno}/{limit}
- method : GET
- input data : bloguser, userno, limit
- output data : userrating, postrating, ranking, score, kind, likecount, dislikecount, tags, userno, uid, profilename, postno, titile, subtitle, content, create_date, thumbnail
</details>

<details>
<summary>게시물 신고</summary>

### 게시물 신고
---
- path : .../post/report
- method : POST
- input data : postno, userno, content
</details>

<details>
<summary>신고 게시물 조회</summary>

### 신고 게시물 조회
---
- path : .../post/report/{limit}
- method : GET
- input data : limit
- output data : postno, reportcount, reportno, userno, uid, postno, title, content
</details>

<details>
<summary>태그별 게시물 조회(태그 클릭시)</summary>

### 태그별 게시물 조회(태그 클릭시)
---
- path : .../post/tags/{tag}
- method : GET
- input data : tag
- output data : tags, postno, uid, profilename, title, subtitle, create_date
</details>

<details>
<summary>좋아요 한 게시물 조회</summary>

### 좋아요 한 게시물 조회
---
- path : .../post/like/{userno}/{limit}
- method : GET
- input data : userno, limit
- output data : userrating, postrating, ranking, score, kind, likecoutn, dislikecount, tags, userno, uid, postno, title, subtitle, thumbnail, content, create_date, profilename, 
</details>