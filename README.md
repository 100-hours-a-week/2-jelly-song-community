# 💬 DevTalk - 개발자 커뮤니티 플랫폼
<img src="https://github.com/user-attachments/assets/c1f078b0-45e8-4ccc-a8f0-95834fbd4de9" alt="DevTalk 로고" width="300"/>

> **카카오테크 부트캠프 판교 2기 - 개인 프로젝트**

> **개발자를 위한 소통 플랫폼**
> *혼자 끙끙 앓지 말고, 함께 나누고 성장하세요.*
> *지식은 공유될 때 더 빛이 납니다.*

---

## 📌 프로젝트 개요

**DevTalk**는 개발자들이 **진짜 필요한 소통**을 할 수 있는 커뮤니티 플랫폼입니다.  

현재 개발 생태계는 빠르게 변화하고 있지만, 개발자들이 마음 편히 질문하고 경험을 나눌 수 있는 공간은 부족합니다.  
기존의 개발 커뮤니티들은 너무 형식적이거나, 초보자에게는 진입장벽이 높고, 실무진에게는 깊이가 부족한 경우가 많습니다.

DevTalk은 이런 문제의식에서 출발했습니다:

- **"이런 상황, 다른 사람들은 어떻게 했을까?"** - 혼자만의 고민에서 벗어나고 싶은 개발자들
- **"내 개발 과정을 누군가와 나누고 싶은데..."** - 성장 여정을 함께할 동료가 그리운 순간들  
- **"이 문제, 말로는 설명이 어려운데..."** - 복잡한 상황을 시각적으로 보여주고 싶을 때
- **"진짜 현실적인 조언이 듣고 싶어요"** - 이론이 아닌 경험담이 필요한 순간들

**DevTalk**에서 개발자들은 단순히 질문하고 답하는 것을 넘어서, **사진과 진솔한 이야기로 실제 경험을 바탕으로 한 깊이 있는 소통**을 나눌 수 있습니다.

개발 과정의 생생한 순간들을 이미지로 공유하고, 댓글을 통해 같은 고민을 했던 개발자들과 연결되며, 서로의 경험담을 나누는 **개발자를 위한, 개발자에 의한 진짜 소통 공간**입니다.

혼자서는 막막했던 개발 여정이 **함께라면 더 즐거운 성장**이 될 수 있도록, DevTalk가 여러분의 든든한 개발 동반자가 되겠습니다.

---

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptopenjdk.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 관련 블로그 글

1. [<개인 프로젝트> HTML을 재사용 가능하게 만들기](https://aole.tistory.com/52)

2. [웹 개발 - HTML부터 REST API까지](https://aole.tistory.com/55)

3. [ERD 설계 - NULL과 정규화 사이의 고민들](https://aole.tistory.com/61)
 
4. [프로젝트 회고 - 멘토링 피드백을 통한 성장 과정](https://aole.tistory.com/83)


## 📖 프로젝트 개요

- **서비스 설명**: IT 개발자들에게 소통 공간을 제공하는 커뮤니티 서비스
- **개발 기간**: 2025.03 ~ 2025.03 (1개월)
- **개발 인원**: 1명 (풀스택)
- **담당 역할**: 풀스택 개발
- **GitHub**: https://github.com/100-hours-a-week/2-jelly-song-community

### 📁 프로젝트 구조
```
src/main/java/io/github/jeli01/kakao_bootcamp_community/
├── 🔐 auth/           # JWT 인증 & RTR
├── 📝 board/          # 게시판 (반정규화 적용)
├── 💬 comment/        # 댓글 시스템
├── ❤️ like/           # 좋아요 (트랜잭션 관리)
├── 👤 user/           # 사용자 관리
├── ☁️ cloud/          # AWS 연동
└── 🛠️ common/         # 공통 컴포넌트
```

## 🏗️ 주요 기능 및 기술적 성과

### 🔐 1. 보안성을 고려한 JWT 인증 시스템

**핵심 특징:**
- RTR 방식으로 토큰 탈취 시 피해 범위 최소화
- Access Token (로컬스토리지) + Refresh Token (HttpOnly 쿠키) 차별화 저장
- 스케줄링 기반 만료 토큰 자동 정리로 메모리 최적화

---

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

**고려사항** : ddd를 이용한 원자적 처리도 고려하였지만, 연관관계 주인을 변경하거나 양방향 매핑을 도입할 경우 유지보수성이 저하될 위험이 있어 트랜잭션 기반 접근 방식을 채택


**성과**: JOIN 쿼리 제거로 게시물 목록 조회 성능 향상

---

### 🚀 3. CI/CD 파이프라인 구축

**기술 스택**: GitHub Actions + Docker + AWS (S3, ECR, CodeDeploy, CloudWatch)

**주요 구성요소:**
- **Docker 컨테이너화**: 환경 일관성 보장
- **CodeDeploy 자동화**: 무중단 배포 구현
- **Cloud Watch 모니터링**: 오류 시 알림으로 개발자가 인지

**성과:**
- 배포 자동화로 인적 오류 제거
- 트래픽 급증 시 빠른 확장 가능
- 안정적인 서비스 운영 환경 구축


## 🏗️ 클라우드 아키텍처

<img width="684" height="952" alt="image" src="https://github.com/user-attachments/assets/a86779e8-dd46-4911-9c5d-74dc78296c5d" />
