# 부스트캠프 사전 과제


### 구현 내용
기본 요구사항에 대한 부분은 전부 구현했으며, 다음과 같은 추가 내용을 포함하였습니다.
- Infinity scroll
- DataBinding
- RxJava (부분적으로 사용)
- onBackpress timer : 뒤로가기 두번눌린경우 종료
- Fragment 구현
- 유효하지 않은 검색결과 처리
- customtab 

### 구현과정에서 있었던 애로사항
##### 영화 정보를 클릭했을 때 customtab을 이용해 Web-page를 띄우는 화면이 예시에 나온 화면과 다르게 출력되는 문제가 있었습니다. 
##### 확인결과 버전에 따른 차이였습니다. 
##### 버전 9에서 동작하는 customtab라이브러리는 translate 애니메이션을 나가는 화면과 들어오는화면에 각각 추가해주면 상권님이 올려주신 예시에서와 같이 동작하지만, 제가 테스트했던 버전 8에서의 customtab은 예시와 같이 출력되지 않았습니다. 
##### (버전9에서 테스트하면 예시에서와 같이 동작하도록 프로그래밍해놨습니다.)
