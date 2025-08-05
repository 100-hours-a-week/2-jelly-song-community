# DevTalk - 개발자 커뮤니티 플랫폼

> **카카오테크 부트캠프 판교 2기 - 개인 프로젝트**  
> IT 개발자들을 위한 소통 공간을 제공하는 Spring Boot 기반 커뮤니티 서비스

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptopenjdk.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 관련 블로그 글

1. [프로젝트 회고 - 멘토링 피드백을 통한 성장 과정](https://aole.tistory.com/83)

2. [ERD 설계 - NULL과 정규화 사이의 고민들](https://aole.tistory.com/61)

3. [웹 개발 - HTML부터 REST API까지](https://aole.tistory.com/55)

4. [<개인 프로젝트> HTML을 재사용 가능하게 만들기](https://aole.tistory.com/52)

## 📖 프로젝트 개요

- **서비스 설명**: IT 개발자들에게 소통 공간을 제공하는 커뮤니티 서비스
- **개발 기간**: 2025.03 ~ 2025.03 (1개월)
- **개발 인원**: 1명 (풀스택)
- **담당 역할**: 풀스택 개발
- **GitHub**: https://github.com/100-hours-a-week/2-jelly-song-community

## 🛠️ 기술 스택

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.4.2
- **Security**: Spring Security + JWT 
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle

### Infrastructure
- **Cloud**: AWS (EC2, RDS, ELB, ECR, CloudFront, CodeDeploy, CloudWatch)
- **Container**: Docker
- **CI/CD**: GitHub Actions
- **Storage**: AWS S3

## 🏗️ 주요 기능 및 기술적 성과

### 🔐 1. 보안성을 고려한 JWT 인증 시스템

**Refresh Token Rotation (RTR) 구현**
```java
@Scheduled(fixedRate = 3600000)
public void deleteExpiredTokens() {
    refreshTokenService.deleteByExpirationBefore(LocalDateTime.now());
}
```

**핵심 특징:**
- RTR 방식으로 토큰 탈취 시 피해 범위 최소화
- Access Token (로컬스토리지) + Refresh Token (HttpOnly 쿠키) 차별화 저장
- 스케줄링 기반 만료 토큰 자동 정리로 메모리 최적화

### 📊 2. 성능 최적화를 위한 선택적 반정규화

**문제 상황**: 게시물 목록 조회 시 좋아요 수를 위한 반복적인 JOIN 쿼리로 DB 부하 발생

**해결 방안**: Board 엔티티에 `likeCount` 필드 추가
```java
@Entity
public class Board {
    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long likeCount;  
    
    public void plusLikeCount() {
        this.likeCount += 1;
        this.updateDate = LocalDateTime.now();
    }
    
    public void minusLikeCount() {
        this.likeCount -= 1;
        this.updateDate = LocalDateTime.now();
    }
}
```

**데이터 무결성 보장**: 좋아요 생성/삭제 시 트랜잭션 기반 동기화
```java 
@Service
@Transactional
public class LikeService {
    public void addLike(Long boardId) {
        Board board = getBoard(boardId);
        board.plusLikeCount();  // 원자적 업데이트
        Like like = new Like(user, board);
        likeRepository.save(like);
    }
}
```

**성과**: JOIN 쿼리 제거로 게시물 목록 조회 성능 대폭 향상

### 🚀 3. CI/CD 파이프라인 구축

**기술 스택**: GitHub Actions + Docker + AWS (S3, ECR, CodeDeploy)

**주요 구성요소:**
- **Docker 컨테이너화**: 환경 일관성 보장
- **ECR 이미지 관리**: 중앙 집중식 이미지 저장소
- **S3 파일 배포**: 고성능 정적 리소스 제공
- **CodeDeploy 자동화**: 무중단 배포 구현

**성과:**
- 배포 자동화로 인적 오류 제거
- 트래픽 급증 시 빠른 확장 가능
- 안정적인 서비스 운영 환경 구축

## 🏗️ 시스템 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Spring Boot   │    │    MariaDB      │
│   (JavaScript)  │◄──►│   Application   │◄──►│   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                       ┌──────┴──────┐
                       │   JWT Auth  │
                       │   + RTR      │
                       └─────────────┘
```

### 📁 프로젝트 구조
```
src/main/java/io/github/jeli01/kakao_bootcamp_community/
├── 🔐 auth/           # JWT 인증 & RTR
│   ├── jwt/          # JWT 유틸리티
│   ├── scheduler/    # 토큰 정리 스케줄러
│   └── service/      # 인증 서비스
├── 📝 board/          # 게시판 (반정규화 적용)
├── 💬 comment/        # 댓글 시스템
├── ❤️ like/           # 좋아요 (트랜잭션 관리)
├── 👤 user/           # 사용자 관리
├── ☁️ cloud/          # AWS 연동
└── 🛠️ common/         # 공통 컴포넌트
```

## ✅ 구현 완료 기능

### 🔐 인증/인가
- [x] JWT 기반 로그인 (Access Token + Refresh Token)
- [x] Refresh Token Rotation 구현
- [x] 회원가입, 정보수정, 탈퇴, 비밀번호 변경
- [x] 만료 토큰 자동 정리 스케줄링

### 📝 게시판
- [x] 게시물 CRUD (생성, 조회, 수정, 삭제)
- [x] 조회수 자동 증가
- [x] 성능 최적화 (반정규화 적용)

### 💬 소셜 기능
- [x] 댓글 CRUD
- [x] 좋아요/취소 (트랜잭션 기반 동기화)

## 🔧 기술적 도전과 해결

### 1. 보안 강화
- **문제**: 일반적인 JWT의 보안 취약점
- **해결**: RTR + 토큰별 차별화 저장 전략
- **결과**: XSS/CSRF 공격 원천 차단

### 2. 성능 최적화
- **문제**: 게시물 목록 조회 시 JOIN 쿼리 부하
- **해결**: 선택적 반정규화 + 트랜잭션 동기화
- **결과**: 쿼리 성능 대폭 향상

### 3. 배포 자동화
- **문제**: 수동 배포의 비효율성과 오류 가능성
- **해결**: GitHub Actions + AWS 기반 CI/CD
- **결과**: 안정적이고 확장 가능한 배포 환경

## 📊 해결된 이슈

- [x] **보안**: 서버 에러 401 응답 개선
- [x] **API**: 로그아웃/로그인 응답 JSON 통일화
- [x] **개발 편의성**: 포스트맨 액세스 토큰 자동 공유

## 🔄 진행 중인 작업

## 📈 성과 및 학습

### 🎯 핵심 성과
1. **보안**: RTR 도입으로 JWT 보안성 대폭 향상
2. **성능**: 반정규화로 DB 쿼리 성능 최적화
3. **운영**: CI/CD 파이프라인으로 배포 자동화
4. **확장성**: AWS 기반 클라우드 아키텍처 구축

### 📚 기술 학습
- Spring Boot 3.x + Spring Security 6.x 실무 적용
- JWT 기반 인증 시스템 심화 구현
- 데이터베이스 정규화/반정규화 전략 수립
- AWS 기반 클라우드 인프라 설계 및 운영
- Docker 컨테이너화 및 CI/CD 파이프라인 구축
