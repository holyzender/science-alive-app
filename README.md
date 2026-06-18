# 사이언스얼라이브 · Science Alive

과학 커뮤니케이션 컨퍼런스 + **PR어워드** 플랫폼.
**Jmix 2.8.2** (Spring Boot 3.5 + Vaadin Flow 24) 기반.

---

## 🚀 빠른 시작 (각자 PC에서 실행)

### 1. 준비물 — JDK 17 이상
- 없으면 [Temurin JDK](https://adoptium.net/temurin/releases/?version=21) 설치 (21 권장)
- 설치 확인: 터미널에서 `java -version` → `17` 이상이면 OK

### 2. 내려받기
```bash
git clone https://github.com/holyzender/science-alive-app.git
cd science-alive-app
```

### 3. 실행 — 아래 (A) 또는 (B) 중 하나

**(A) 운영 jar 빌드 후 실행 — 권장**
```bash
# Windows
gradlew.bat clean bootJar -Pvaadin.productionMode=true
java -jar build\libs\science-alive-0.0.1-SNAPSHOT.jar

# macOS / Linux
./gradlew clean bootJar -Pvaadin.productionMode=true
java -jar build/libs/science-alive-0.0.1-SNAPSHOT.jar
```

**(B) 개발 모드 한 줄 실행 (최초 1회 Node 자동 다운로드)**
```bash
# Windows
gradlew.bat clean bootRun
# macOS / Linux
./gradlew clean bootRun
```

### 4. 접속
브라우저에서 **http://localhost:8080**

> ⚠️ **반드시 `clean` 을 붙이세요.** Jmix 엔티티 *enhancement* 가 스킵되면 부팅에 실패합니다
> ("MetaClass not found"). `gradlew compileJava` 만 단독 실행하지 마세요.
> 🌐 최초 빌드는 의존성·프론트엔드 다운로드로 **몇 분** 걸릴 수 있습니다 (인터넷 필요).
> 🔌 포트 변경: `java -DPORT=9090 -jar ...` (기본 8080).
> 🪟 Windows 사용자는 폴더의 **`run-dev.bat`** 더블클릭으로 (A) 과정을 한 번에 실행할 수도 있어요.

---

## 데모 로그인

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | 전체 권한 (백오피스) |
| `judge` | `judge` | PR어워드 심사위원 — 소속 **KAIST 신소재공학과** |
| `submitter` | `submitter` | 제출자 — 한국생명공학연구원 |

공개 페이지(소개·아카이브·PR어워드·제출)는 **로그인 없이** 열람 가능합니다.
**소속 회피 규칙**(심사위원은 자기 소속 기관의 보도자료를 평가할 수 없음)은 심사 콘솔에 구현돼 있어요.

## 화면 (routes)

| Route | 화면 | 접근 |
|---|---|---|
| `/` | 소개 / Home — 히어로, 다음 회차, 소개, PR어워드 티저 | 공개 |
| `/archive` | 행사 아카이브 — 회차 목록 | 공개 |
| `/event/{n}` | 회차별 행사 페이지 — 포스터, 강연·강연자, 기사, 해당 회차 수상작 링크 | 공개 |
| `/award` | PR어워드 보도자료 갤러리 — 분야/회차/수상 필터(`?edition=N`) + 상세 | 공개 |
| `/submit` | 제출하기 — 보도자료 제출 폼 (실제 저장됨) | 공개 |
| `/judge` | 심사 콘솔 — 채점 + 소속 회피 규칙 | 공개 데모 |
| `/admin` | Jmix 백오피스 (엔티티 인스펙터, 역할 관리) | 로그인 필요 |
| `/login` | 로그인 | — |

## 스택

- **JDK 17 이상** (Temurin 권장 · 17 또는 21). 빌드 산출물은 Java 17 호환.
- **Gradle 8.14.4** — wrapper 포함이라 별도 설치 불필요
- **Jmix 2.8.2** — data (EclipseLink + **HSQLDB 파일 DB**), security, Flow UI
- **Vaadin Flow 24** — 커스텀 `science-alive` 테마
- 한글 폰트 번들: Pretendard(본문), Gmarket Sans(디스플레이), Noto Sans KR(폴백)

DB는 최초 실행 시 작업 폴더의 `.jmix/` 아래 **HSQLDB 파일**로 자동 생성·시드됩니다
(별도 DB 설치 불필요). 실서비스용으로는 `application.properties` 의 `main.datasource.*`
를 PostgreSQL/MySQL로 바꾸면 Liquibase가 스키마를 생성합니다.

## 폴더 구조

- `entity/` — `Edition`, `Speaker`, `Submission`, `Talk`, `Article`, `User`
- `security/` — 역할(Full Access, Anonymous, UI-minimal, Submitter, Judge), 사용자 저장소, 보안 설정
- `ui/` — `SaIcons`(인라인 SVG 아이콘), `SaUi`(워드마크/태그/버튼/링크), `SaLabels`
- `view/` — `home`, `archive`, `edition`(행사 페이지), `award`, `submit`, `judge`,
  `layout/PublicLayout`, `login`, `main`; 공용 `ui/SubmissionDialog`(보도자료 상세 팝업)
- `src/main/frontend/themes/science-alive/` — 디자인 토큰 → Lumo 매핑 + 폰트
- `resources/.../liquibase/changelog/` — 스키마 + 시드 데이터

## 참고

- **`.replit`, `replit.nix`** 는 Replit 배포용 파일 — **로컬 실행과 무관**하니 무시하세요.
- Porsche Next → **Pretendard**, PDS 아이콘 폰트 → 인라인 SVG 서브셋으로 대체.
- 히어로 배경은 실제 이미지(`themes/science-alive/images/hero_bg.webp`), PR어워드 카드 이미지는
  테마에 번들된 mRNA/배터리/중력파 이미지 사용.
- 제출 파일 업로드/스토리지/관리자 CRUD 일부는 UI 클릭스루(시안) — 데이터 모델·보안은 실제 동작.
