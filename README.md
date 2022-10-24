# 개요

- 프로젝트 명 : MyBoard
- 개발 인원 : 김민경 (1인)
- 프로젝트 기한 : 2022.4
- 프로젝트 목적
    - 첫 프로젝트 (bokduck) 개발 후 제 실력의 부족함과 처음부터 끝까지 제가 직접 만든 프로젝트가 있다면 좋을것 같아서 시작하게 되었습니다.
    - 전 프로젝트에서 하지 못했던 TestCode를 작성하였고 CRUD 기반 프로젝트를 완성하였습니다.

# 개발 환경

- IDE : ItelliJ
- Framework : SpringBoot
- Project Type : Gradle
- JDK 11

### **Depencies**

- h2
- Web
- JPA
- Security
- Thymeleaf
- OAuth2
- Lombok

# 구현 기능

- 로그인
    - 일반로그인
    - 네이버 로그인
    - 구글 로그인
    - 카카오 로그인

- 회원가입
    - 핸드폰 번호 인증
    - 닉네임 중복 확인
    - 이메일 중복 확인
- 자유 게시판
    - 글 등록
    - 글 수정
    - 글 삭제
    - 좋아요 기능
    - 신고
- 로그아웃
- 댓글
    - 댓글 등록
    - 댓글 수정
    - 댓글 삭제
    - 대댓글
- 마이페이지
    - 닉네임 수정
    - 핸드폰번호 수정
    - 회원 탈퇴
- 관리자 페이지
    - 신고 내용 확인
    - 게시물 강제 삭제
    
    # ERD
![ERD](https://user-images.githubusercontent.com/54256348/197440940-68ff70df-3d5c-487c-aff3-5265966dcf3f.JPG)
