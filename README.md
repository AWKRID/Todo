# Todo 앱 서버
---
## 🔍프로젝트 소개 
---

간단한 Todo 리스트와 댓글을 달 수 있는 프로그램을 위한 서버입니다.
할일을 기록하고 수정할 수 있으며 완료 여부를 나타낼 수 있습니다.

## 프로젝트 설계
### 1. Event Stroming
---

DDD 설계 방식을 통해 아래와 같이 Design 하였습니다.
![Event storming_page-0001](https://github.com/AWKRID/Todo/assets/137989290/34c09760-fb3e-4287-91b5-7386e862bd1a)

### 2. ERD
---

본 프로젝트에 사용된 테이블은 총 세 개로 다음과 같이 관계를 가지고 있습니다.
![image](https://github.com/AWKRID/Todo/assets/137989290/a47d4741-ef0f-4ed1-8c7d-196a16c2a3f9)

### 3. API 명세서
---

본 프로젝트의 API 명세서는 아래 사이트에 정리해두었습니다.

[API 명세서 확인하기](https://leather-antimony-86c.notion.site/Todo-API-4cdba9cd234143569797d2c34137f009)


### 4. 프로젝트 구조
---

```
└── todo
    ├── TodoApplication.kt
    ├── domain
    │   ├── comment
    │   │   ├── controller
    │   │   │   └── CommentController.kt
    │   │   ├── dto
    │   │   │   ├── AddCommentRequest.kt
    │   │   │   ├── CommentResponse.kt
    │   │   │   └── UpdateCommentRequest.kt
    │   │   ├── model
    │   │   │   └── Comment.kt
    │   │   ├── repository
    │   │   │   └── CommentRepository.kt
    │   │   └── service
    │   │       └── CommentService.kt
    │   ├── exception
    │   │   ├── GlobalExceptionHandler.kt
    │   │   ├── ModelNotFoundException.kt
    │   │   └── dto
    │   │       └── ErrorResponse.kt
    │   ├── todo
    │   │   ├── controller
    │   │   │   └── TodoController.kt
    │   │   ├── dto
    │   │   │   ├── CreateTodoRequest.kt
    │   │   │   ├── TodoResponse.kt
    │   │   │   └── UpdateTodoRequest.kt
    │   │   ├── model
    │   │   │   └── Todo.kt
    │   │   ├── repository
    │   │   │   └── TodoRepository.kt
    │   │   └── service
    │   │       ├── TodoService.kt
    │   │       └── TodoServiceImpl.kt
    │   └── user
    │       ├── controller
    │       │   └── UserController.kt
    │       ├── dto
    │       │   ├── SignUpRequest.kt
    │       │   └── UserResponse.kt
    │       ├── model
    │       │   └── User.kt
    │       ├── repository
    │       │   └── UserRepository.kt
    │       └── service
    │           ├── UserService.kt
    │           └── UserServiceImpl.kt
    └── infra
        └── swagger
            └── SwaggerConfig.kt


```
