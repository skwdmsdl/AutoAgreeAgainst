1. 포털 사이트와 신문사의 댓글에 대한 추천, 비추천 기능이 자동화 툴로 조작 가능한지 확인

2. 대상 
포털 : 다음(www.daum.net)
신문 : 동아일보 (www.donga.com) , 조선일보(www.chosun.com)

3. 도구
selenium 

4. resources 안

   ChosunIdPwList.txt.
   DaumIdPwList.txt.
   DongaIdPwList.txt

파일 안에 아이디와 비번을 tab 문자를 구분자로 넣음

5. 실행

조선일보 /AutoAgreeAgainst/src/main/java/chosun/RunChosun.java,
다음  /AutoAgreeAgainst/src/main/java/daum/RunDaum.java,
동아 /AutoAgreeAgainst/src/main/java/donga/RunDonga.java
