# 사이언스얼라이브 · Science Alive — Jmix app

A production-style implementation of the **Science Alive** design system (Korean
science‑communication conference + **PR어워드** platform) built on **Jmix 2.8.2**
(Spring Boot 3.5 + Vaadin Flow 24), recreated from the `science-alive-design-system`
handoff bundle.

## Stack

- **Java 21** (Temurin), **Gradle 8.14.4** (wrapper)
- **Jmix 2.8.2** — data (EclipseLink + HSQLDB), security, Flow UI
- **Vaadin Flow 24** for the UI, custom `science-alive` theme
- Korean font stack bundled in the theme: Pretendard (UI/body), Gmarket Sans
  (display), Noto Sans KR (fallback)

## Run

```bat
REM Java is auto-set by the wrapper script:
run-dev.bat
REM …or manually:
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot
gradlew.bat bootRun
```

Then open **http://localhost:8080**. (First run builds the Vaadin frontend bundle via
Node — give it ~1 minute.) Set a `PORT` env var to change the port.

> Tip: validate a build with `gradlew clean classes` or `gradlew clean bootRun` —
> running `gradlew compileJava` on its own leaves entity *enhancement* skipped and
> the app fails to boot ("MetaClass not found").

## Production build & deploy

Build an executable, self-contained jar with the Vaadin frontend compiled in
production mode (no Node needed at runtime):

```bat
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot
gradlew.bat clean bootJar -Pvaadin.productionMode=true
```

Output: `build/libs/science-alive-0.0.1-SNAPSHOT.jar`. Run it anywhere with a JRE 21:

```bat
java -jar build/libs/science-alive-0.0.1-SNAPSHOT.jar
REM change port:  java -DPORT=9090 -jar ...   (server.port=${PORT:8080})
```

The jar starts an embedded Tomcat and creates/uses the HSQLDB file under `.jmix/`
in the working directory. For a real deployment, point `main.datasource.*` in
`application.properties` (or via env/`--main.datasource.url=...`) at PostgreSQL/MySQL
and the Liquibase changelogs will create the schema on first start. A WAR for an
external servlet container is also possible by adding the `war` plugin + `bootWar`.

## Surfaces (routes)

| Route | Screen | Access |
|---|---|---|
| `/` | 소개 / Home — hero, next edition, about, PR Award teaser | public |
| `/archive` | 행사 아카이브 — edition list | public |
| `/event/{n}` | 회차별 행사 페이지 — poster, program (talks/speakers), articles, link to that edition's winners | public |
| `/award` | PR어워드 보도자료 갤러리 — division/edition/winner filters (supports `?edition=N`) + detail dialog | public |
| `/submit` | 제출하기 — submission form (persists a real Submission) | public |
| `/judge` | 심사 콘솔 — scoring + own‑institution block | public demo |
| `/admin` | Jmix back‑office (entity inspector, roles) | login required |
| `/login` | sign in | — |

The public surfaces are plain Vaadin routes (`@AnonymousAllowed`) hosted in a custom
`PublicLayout` (the frosted header + dark footer). The admin back‑office is Jmix's
`StandardMainView`.

## Demo logins

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | full access (back‑office) |
| `judge` | `judge` | PR Award judge — institution **KAIST 신소재공학과** |
| `submitter` | `submitter` | submitter — 한국생명공학연구원 |

The **own‑institution rule** (a judge cannot view/evaluate their own institution's
submission) is enforced in the Judge console. Logged out, it uses KAIST as the demo
judge institution (matching the prototype); logged in as `judge`, it uses that user's
institution.

## Layout

- `entity/` — `Edition`, `Speaker`, `Submission`, `Talk`, `Article`, `User`
- `security/` — roles (Full Access, Anonymous, UI‑minimal, Submitter, Judge),
  user repository, security config
- `ui/` — `SaIcons` (inline SVG icon set), `SaUi` (wordmark/tags/buttons/links),
  `SaLabels`
- `view/` — `home`, `archive`, `edition` (event pages), `award`, `submit`, `judge`,
  `layout/PublicLayout`, `login`, `main`; shared `ui/SubmissionDialog` (press detail popup)
- `src/main/frontend/themes/science-alive/` — tokens → Lumo mapping + fonts
- `resources/.../liquibase/changelog/` — schema + seed data (mirrors `data.js`)

## Notes / substitutions (carried over from the design bundle)

- Porsche Next → **Pretendard**; PDS icon font → curated inline‑SVG subset.
- Award/press images are CSS gradient placeholders — swap for real media.
- Hero background is a real image (`themes/science-alive/images/hero_bg.webp`, WebP-optimized
  ~170 KB) under a left→right dark scrim so the white headline stays legible. Replace the file
  (same name) to change it.
- Submission upload, file storage, and admin CRUD are cosmetic click‑throughs in the
  UI kit; the data model + security are real and ready to wire up.
