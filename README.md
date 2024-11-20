# **UAM Control System Backend**

UAM Control System Backend는 도시 항공 교통(Urban Air Mobility, UAM) 제어 시스템을 위한 **Spring Boot 기반 백엔드 애플리케이션**입니다.  
이 애플리케이션은 드론 및 항공기의 객체 정보 및 경로 관리, 시뮬레이터와 프론트엔드 간의 통신 기능을 제공하여 도시 항공 교통 환경에서의 **원활한 운영**을 지원합니다.

---

## **✨ 주요 기능**
- **드론 경로 관리**  
  MySQL 데이터베이스에 저장된 드론 경로 데이터를 저장 및 검색합니다.
  
- **경로 추천**  
  현재 항공 교통 데이터를 기반으로 최적화된 비행 경로를 추천합니다.
  
- **실시간 모니터링**  
  시뮬레이터와 연동하여 경로 정보를 실시간으로 업데이트합니다.
  
- **REST API 제공**  
  프론트엔드와의 상호작용을 위한 위치 정보 업데이트 및 경로 데이터 검색 API를 제공합니다.

---

## **🛠️ 사용 기술**
- **Java 17**  
  백엔드 로직 개발 언어.
  
- **Spring Boot 3**  
  RESTful API 개발을 위한 프레임워크.
  
- **MySQL**  
  드론 경로 및 관련 데이터를 저장하기 위한 데이터베이스.
  
- **Hibernate**  
  데이터베이스와의 상호작용을 관리하는 ORM(Object-Relational Mapping) 도구.
  
- **HikariCP**  
  데이터베이스 연결 풀 관리.
  
- **SLF4J & Logback**  
  애플리케이션 로깅.
  
- **GitHub Actions**  
  CI/CD 파이프라인 설정.
  
- **Postman**  
  API 테스트 도구.

---

## **⚙️ 설치 및 실행 방법**

### **1. 환경 설정**
- **Java**: JDK 17 이상 설치
- **MySQL**: MySQL 8.0 이상 설치
- **Gradle**: 빌드 도구

### **2. 소스 코드 클론**
```bash
git clone https://github.com/your-repository/uam-control-system-be.git
cd uam-control-system-be
```

**3. MySQL 데이터베이스 설정**

**4. 애플리케이션 실행**
```bash
./gradlew bootRun
```
