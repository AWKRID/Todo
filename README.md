# Todo 앱 서버
---
 <img src="https://img.shields.io/badge/kotlin-#7F52FF?style=flat&logo=kotlin&logoColor=white"/>  <img src="https://img.shields.io/badge/spring-###6DB33F?style=flat&logo=spring&logoColor=white"/>
 
## 🔍프로젝트 소개 
---

간단한 Todo 리스트와 댓글을 달 수 있는 프로그램을 위한 서버입니다. 
- 할일을 기록하고 수정할 수 있으며 완료 여부를 나타낼 수 있습니다.
- 할일 내용 기록에 카테고리와 태그를 달 수 있습니다.
- 제목, 태그, 완료 여부 등으로 검색할 수 있습니다. 

## ⚙️ 개발 환경
---

- `Java 17` , `Kotiln`
- **Framework**: Spring Boot 3.2.5
- **Database**: PostgreSQL
- **ORM** : JPA / hibernate 

## 프로젝트 설계
### 1. Event Stroming
---

DDD 설계 방식을 통해 아래와 같이 Design 하였습니다.
![Event storming_page-0001](https://github.com/AWKRID/Todo/assets/137989290/34c09760-fb3e-4287-91b5-7386e862bd1a)

### 2. API 명세서
---

본 프로젝트의 API 명세서는 아래 사이트에 정리해두었습니다.

[API 명세서 확인하기](https://leather-antimony-86c.notion.site/Todo-API-4cdba9cd234143569797d2c34137f009)


### 3. 프로젝트 구조
---

```
└─ src
   ├─ main
   │  └─ kotlin
   │     └─ com
   │        └─ awkrid
   │           └─ todo
   │              ├─ TodoApplication.kt
   │              ├─ domain
   │              │  ├─ category
   │              │  │  ├─ model
   │              │  │  │  └─ Category.kt
   │              │  │  └─ repository
   │              │  │     └─ CategoryRepository.kt
   │              │  ├─ comment
   │              │  │  ├─ controller
   │              │  │  │  └─ CommentController.kt
   │              │  │  ├─ dto
   │              │  │  │  ├─ AddCommentRequest.kt
   │              │  │  │  ├─ CommentResponse.kt
   │              │  │  │  └─ UpdateCommentRequest.kt
   │              │  │  ├─ model
   │              │  │  │  └─ Comment.kt
   │              │  │  ├─ repository
   │              │  │  │  └─ CommentRepository.kt
   │              │  │  └─ service
   │              │  │     └─ CommentService.kt
   │              │  ├─ exception
   │              │  │  ├─ GlobalExceptionHandler.kt
   │              │  │  ├─ ModelNotFoundException.kt
   │              │  │  └─ dto
   │              │  │     └─ ErrorResponse.kt
   │              │  ├─ oauth2
   │              │  │  ├─ OAuth2Provider.kt
   │              │  │  ├─ client
   │              │  │  │  ├─ config
   │              │  │  │  │  └─ RestClientConfig.kt
   │              │  │  │  └─ oauth2
   │              │  │  │     ├─ OAuth2Client.kt
   │              │  │  │     ├─ OAuth2ClientService.kt
   │              │  │  │     ├─ OAuth2LoginUserInfo.kt
   │              │  │  │     └─ kakao
   │              │  │  │        ├─ OAuth2KakaoClient.kt
   │              │  │  │        └─ dto
   │              │  │  │           ├─ KakaoLoginUserInfoResponse.kt
   │              │  │  │           ├─ KakaoTokenResponse.kt
   │              │  │  │           └─ KakaoUserPropertiesResponse.kt
   │              │  │  ├─ controller
   │              │  │  │  └─ Oauth2LoginController.kt
   │              │  │  └─ service
   │              │  │     └─ OAuth2LoginService.kt
   │              │  ├─ todo
   │              │  │  ├─ controller
   │              │  │  │  └─ TodoController.kt
   │              │  │  ├─ dto
   │              │  │  │  ├─ CreateTodoRequest.kt
   │              │  │  │  ├─ TodoFilter.kt
   │              │  │  │  ├─ TodoResponse.kt
   │              │  │  │  ├─ TodoResponseWithComments.kt
   │              │  │  │  └─ UpdateTodoRequest.kt
   │              │  │  ├─ model
   │              │  │  │  └─ Todo.kt
   │              │  │  ├─ repository
   │              │  │  │  └─ TodoRepository.kt
   │              │  │  └─ service
   │              │  │     ├─ TodoService.kt
   │              │  │     └─ TodoServiceImpl.kt
   │              │  └─ user
   │              │     ├─ controller
   │              │     │  └─ UserController.kt
   │              │     ├─ dto
   │              │     │  ├─ LoginRequest.kt
   │              │     │  ├─ LoginResponse.kt
   │              │     │  ├─ SignUpRequest.kt
   │              │     │  └─ UserResponse.kt
   │              │     ├─ exception
   │              │     │  └─ InvalidCredentialException.kt
   │              │     ├─ model
   │              │     │  ├─ User.kt
   │              │     │  └─ UserRole.kt
   │              │     ├─ repository
   │              │     │  └─ UserRepository.kt
   │              │     └─ service
   │              │        ├─ S3Service.kt
   │              │        ├─ UserService.kt
   │              │        └─ UserServiceImpl.kt
   │              └─ infra
   │                 ├─ amazons3
   │                 │  └─ S3Config.kt
   │                 ├─ querydsl
   │                 │  └─ QueryDslConfig.kt
   │                 ├─ security
   │                 │  ├─ CustomAuthenticationEntryPoint.kt
   │                 │  ├─ PasswordEncoderConfig.kt
   │                 │  ├─ SecurityConfig.kt
   │                 │  ├─ UserPrincipal.kt
   │                 │  └─ jwt
   │                 │     ├─ JwtAuthenticationFilter.kt
   │                 │     ├─ JwtAuthenticationToken.kt
   │                 │     └─ JwtHelper.kt
   │                 └─ swagger
   │                    └─ SwaggerConfig.kt
   └─ test
      └─ kotlin
         └─ com
            └─ awkrid
               └─ todo
                  └─ domain
                     ├─ comment
                     │  └─ service
                     │     └─ CommentServiceTest.kt
                     ├─ todo
                     │  ├─ controller
                     │  │  └─ TodoControllerTest.kt
                     │  ├─ repository
                     │  │  └─ TodoRepositoryTest.kt
                     │  └─ service
                     │     └─ TodoServiceTest.kt
                     └─ user
                        ├─ entity
                        │  └─ UserTest.kt
                        └─ service
                           ├─ UserServiceIntegrateTest.kt
                           └─ UserServiceTest.kt


```

### 4. 추가 구현 사항
---
- [x] queryDSL 활용하여 검색 기능 만들기
- [x] Pageable 사용해서 페이징 및 정렬 기능 추가
- [x] 동적 쿼리 작성해서 다양한 조건에 대한 필터 기능 추가
- [x] Domain Entity 단위 테스트 추가
- [x] Service 단위테스트 추가
- [x] Controller 단위테스트 추가
- [x] Repository 단위 테스트 추가
- [x] s3 bucket 활용 이미지 업로드 기능 추가      
