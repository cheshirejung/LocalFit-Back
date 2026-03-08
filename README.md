# LocalFit 엘리스 3차 프로젝트 - Backend

Spring Boot 기반 백엔드 프로젝트입니다.

## 기술 스택

- Java 21
- Spring Boot 3.4
- MariaDB / Redis / MongoDB
- Kafka / Elasticsearch / Kibana
- Docker Compose

## 실행 전 준비

- Java 21 설치
- Docker Desktop 실행
- MariaDB 실행

## 환경 변수 설정

```bash
cd /Users/yookyung/desktop/localfit
cp .env.example .env
```

`.env`에서 아래 항목을 본인 환경에 맞게 설정하세요.

- `DB_URL` (기본: `jdbc:mariadb://localhost:3306/1Team`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET_KEY`
- (선택) `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `AWS_*`

## 데이터베이스 준비

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS 1Team;"
```

## 인프라 실행 (Docker)

```bash
docker compose up -d
```

## 애플리케이션 실행

```bash
set -a; source .env; set +a
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

## 테스트 실행

```bash
./gradlew test
```

## 주요 포트

- App: `8080`
- Redis: `6379`
- MariaDB: `3306`
- MongoDB: `27017`
- Kafka: `9092`
- Elasticsearch: `9200`
- Kibana: `5601`

## 종료

```bash
docker compose down
```

볼륨까지 삭제하려면:

```bash
docker compose down -v
```
