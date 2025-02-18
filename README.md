# ToDo API (Spring Boot)

이 프로젝트는 할 일(ToDo) API를 구현한 것으로, 사용자는 할 일을 추가하고, 완료 여부를 체크하며, 삭제할 수 있는 기능을 제공합니다. 이 API는 MySQL 데이터베이스와 연동되어 있으며, Swagger를 통해 API 명세를 제공합니다.

## 💡 기능
- 할 일 추가: 사용자가 할 일을 입력하고 추가할 수 있습니다. 
- 할 일 수정: 기존의 할 일을 수정할 수 있습니다.
- 할 일 완료 여부 변경: 완료된 일로 표시하거나 완료 취소가 가능합니다. 
- 할 일 삭제 여부 변경: 삭제된 일로 표시하거나 다시 복구할 수 있습니다.
- 할 일 목록 조회: 진행 중, 완료된 일, 삭제된 일로 구분된 할 일 목록을 조회할 수 있습니다.

## 🛠️ 사용 기술
- Spring Boot (v3.x): 백엔드 프레임워크 
- Spring Data JPA: 데이터베이스와의 연동 
- MySQL: 데이터베이스 
- Swagger: API 명세 및 테스트
- JUnit: 테스트 케이스 작성

## 🚀 실행 방법

### 1. 환경 설정
   - JDK 17 이상 설치
   - MySQL 데이터베이스 설치

### 2. 프로젝트 클론
   ```shell
   git clone https://github.com/KimSooHa/todo-api.git
   cd todo-api
   ```
### 3. MYSQL 설정
#### 1. MySQL에서 데이터베이스 생성
```shell
CREATE DATABASE todo_db;
```

#### 2. MySQL에서 dump 파일 import 하기
- dump 파일 위치: 'db/todo-dump.sql'

```shell
mysql -u root -p todo_db < db/todo-dump.sql
```
> password : 12341234 (설정한 MySQL 비밀번호 입력)

### 4. 빌드 및 실행
   ```shell
   ./gradlew build
   ./gradlew bootRun
   ```
   앱은 http://localhost:8080에서 실행됩니다.

> FrontEnd와 함께 연동하여 확인하려면 해당 링크([Github](https://github.com/KimSooHa/todo-app.git))의 가이드를 따라 실행하면 됩니다.

## 🧩 주력으로 사용한 라이브러리 설명

### 1. Spring Data JPA

Spring Data JPA를 사용하여 MySQL 데이터베이스와의 연동을 간편하게 구현하였습니다. JpaRepository를 활용하여 CRUD 작업을 수행하고, SQL 쿼리 작성 없이 데이터를 쉽게 처리할 수 있습니다.

### 2. Swagger

Swagger를 사용하여 API 명세서를 자동으로 생성하고, 이를 통해 API 테스트와 문서화 작업을 효율적으로 진행하였습니다. Swagger UI는 어플리케이션 실행 후 http://localhost:8080/swagger-ui/에서 확인할 수 있습니다.

### 3. JUnit

JUnit을 사용하여 테스트 케이스를 작성했습니다. 각 API에 대한 기본적인 단위 테스트를 포함하고 있습니다.

## 🔧 추가된 기능
1.	할 일 자동 삭제 기능 : 완료/삭제된 할 일이 3일이 지난 후 자동으로 삭제됩니다.
2.	할 일 완료 취소 기능 : 사용자가 할 일을 완료한 후 취소할 수 있는 기능을 추가했습니다.
3.  할 일 삭제 취소 기능 : 사용자가 할 일을 삭제한 후 취소할 수 있는 기능을 추가했습니다.