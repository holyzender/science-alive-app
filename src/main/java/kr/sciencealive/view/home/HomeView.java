package kr.sciencealive.view.home;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Speaker;
import kr.sciencealive.entity.Submission;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaLabels;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.ui.SubmissionDialog;
import kr.sciencealive.view.layout.PublicLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 소개 / landing — hero, next-edition strip, about + speakers, PR Award teaser. */
@Route(value = "", layout = PublicLayout.class)
@PageTitle("사이언스얼라이브 · Science Alive")
@AnonymousAllowed
public class HomeView extends Div {

    private final transient DataManager dataManager;
    private final Map<Integer, Integer> editionYear = new HashMap<>();

    public HomeView(DataManager dataManager) {
        this.dataManager = dataManager;

        List<Edition> editions = dataManager.load(Edition.class).all()
                .sort(Sort.by(Sort.Direction.DESC, "editionNo")).list();
        editions.forEach(e -> editionYear.put(e.getEditionNo(), e.getYear()));
        Edition next = editions.isEmpty() ? null : editions.get(0);
        List<Speaker> speakers = dataManager.load(Speaker.class).all()
                .sort(Sort.by(Sort.Direction.ASC, "sortOrder")).list();
        List<Submission> winners = dataManager.load(Submission.class).all().list().stream()
                .filter(s -> Integer.valueOf(7).equals(s.getEditionNo()) && s.getAward() != null)
                .limit(3).toList();

        add(hero());

        Div container = new Div();
        container.addClassName("sa-container");
        container.getStyle().set("position", "relative");
        if (next != null) {
            container.add(nextEditionStrip(next));
        }
        container.add(aboutSection(speakers), teaserSection(winners));
        add(container);
    }

    private Component hero() {
        Section hero = new Section();
        hero.addClassName("sa-hero");
        Div glow = new Div();
        glow.addClassName("sa-hero__glow");

        Div inner = new Div();
        inner.addClassName("sa-hero__inner");

        Div tagRow = new Div(SaUi.tag("제 7회 · 2026", "info"));
        tagRow.getStyle().set("margin-bottom", "24px");

        H1 title = new H1();
        title.addClassName("sa-hero__title");
        title.getElement().setProperty("innerHTML", "과학으로<br>세상을 잇다");

        Paragraph lede = new Paragraph(
                "과학자·커뮤니케이터·정책입안자·대중이 한자리에 모여 과학 현장의 주요 이슈를 토론하는 연례 행사.");
        lede.addClassName("sa-hero__lede");

        Div cta = new Div();
        cta.addClassName("sa-hero__cta");
        NativeButton seeAwards = SaUi.button("PR어워드 수상작 보기", "arrow-right", "sa-btn--on-dark-primary", true);
        seeAwards.addClickListener(e -> navigate("award"));
        NativeButton archive = SaUi.button("행사 아카이브", null, "sa-btn--on-dark-secondary", false);
        archive.addClickListener(e -> navigate("archive"));
        cta.add(seeAwards, archive);

        Div metrics = new Div();
        metrics.addClassName("sa-hero__metrics");
        metrics.add(SaUi.metric("7회", "누적 개최"),
                SaUi.metric("340+", "강연·세션"),
                SaUi.metric("2024–", "PR어워드 운영"));

        inner.add(tagRow, title, lede, cta, metrics);
        hero.add(glow, inner);
        return hero;
    }

    private Component nextEditionStrip(Edition next) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--elevated");
        card.getStyle().set("margin-top", "-48px").set("position", "relative")
                .set("display", "flex").set("align-items", "center").set("gap", "32px")
                .set("flex-wrap", "wrap").set("justify-content", "space-between");

        Div fields = new Div();
        fields.getStyle().set("display", "flex").set("gap", "40px").set("flex-wrap", "wrap");
        fields.add(infoField("calendar", "일시", next.getEventDate()),
                infoField("map-pin", "장소", next.getVenue()),
                infoField("microscope", "프로그램", next.getTalks() + "개 세션"));

        NativeButton apply = SaUi.button("참가 신청", "arrow-right", "sa-btn--primary", true);
        card.add(fields, apply);
        return card;
    }

    private Component infoField(String icon, String label, String value) {
        Div wrap = new Div();
        Div labelRow = new Div();
        labelRow.addClassName("sa-eyebrow");
        labelRow.getStyle().set("display", "flex").set("align-items", "center").set("gap", "6px");
        labelRow.add(SaIcons.icon(icon, 15), new Span(label));
        Div val = new Div();
        val.setText(value);
        val.getStyle().set("margin-top", "6px").set("font-size", "var(--text-lg)").set("font-weight", "600");
        wrap.add(labelRow, val);
        return wrap;
    }

    private Component aboutSection(List<Speaker> speakers) {
        Section section = new Section();
        section.getStyle().set("margin-top", "88px").set("display", "grid")
                .set("grid-template-columns", "1fr 1fr").set("gap", "56px").set("align-items", "center");

        Div left = new Div();
        Div tagRow = new Div(SaUi.tag("About", "basic"));
        tagRow.getStyle().set("margin-bottom", "16px");
        H2 h = new H2();
        h.addClassNames("sa-heading", "sa-heading--lg");
        h.getElement().setProperty("innerHTML", "현장의 과학을<br>대화의 테이블로");
        Paragraph p = new Paragraph(
                "사이언스얼라이브는 2020년부터 매년 열리는 과학계 토론·소통 행사입니다. 연구 성과를 넘어, 과학이 사회와 만나는 방식을 함께 고민합니다.");
        p.addClassNames("sa-text", "sa-text--lg", "sa-text--secondary");
        p.getStyle().set("margin-top", "20px");
        Span link = SaUi.arrowLink("역대 행사 보기");
        link.addClickListener(e -> navigate("archive"));
        Div linkRow = new Div(link);
        linkRow.getStyle().set("margin-top", "24px");
        left.add(tagRow, h, p, linkRow);

        Div right = new Div();
        right.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "14px");
        for (Speaker s : speakers) {
            right.add(speakerCard(s));
        }

        section.add(left, right);
        return section;
    }

    private Component speakerCard(Speaker s) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline", "sa-card--pad-md");
        card.getStyle().set("display", "flex").set("gap", "16px").set("align-items", "center");

        Div avatar = new Div(SaIcons.icon("user", 24));
        avatar.getStyle().set("width", "52px").set("height", "52px").set("border-radius", "50%")
                .set("flex", "0 0 auto").set("background", "var(--color-surface)")
                .set("display", "grid").set("place-items", "center").set("color", "var(--color-contrast-medium)");

        Div body = new Div();
        body.getStyle().set("min-width", "0");
        Div nameRow = new Div();
        Span name = new Span(s.getName());
        name.getStyle().set("font-weight", "600");
        Span role = new Span(" · " + s.getRole());
        role.getStyle().set("color", "var(--color-contrast-medium)").set("font-weight", "400")
                .set("font-size", "var(--text-sm)");
        nameRow.add(name, role);
        Div topic = new Div();
        topic.setText(s.getTopic());
        topic.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)")
                .set("margin-top", "2px");
        body.add(nameRow, topic);

        card.add(avatar, body);
        return card;
    }

    private Component teaserSection(List<Submission> winners) {
        Section section = new Section();
        section.getStyle().set("margin-top", "96px");

        Div header = new Div();
        header.getStyle().set("display", "flex").set("justify-content", "space-between")
                .set("align-items", "flex-end").set("margin-bottom", "28px");
        Div headLeft = new Div();
        Div tagRow = new Div(SaUi.tag("PR Award 2026", "gold", "award"));
        tagRow.getStyle().set("margin-bottom", "14px");
        H2 h = new H2("올해의 과학 보도자료");
        h.addClassNames("sa-heading", "sa-heading--lg");
        headLeft.add(tagRow, h);
        Span all = SaUi.arrowLink("전체 수상작");
        all.addClickListener(e -> navigate("award"));
        header.add(headLeft, all);

        Div grid = new Div();
        grid.addClassName("sa-grid-3");
        for (Submission s : winners) {
            grid.add(teaserCard(s));
        }

        section.add(header, grid);
        return section;
    }

    private Component teaserCard(Submission s) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline", "sa-card--pad-none", "sa-card--interactive");
        card.getStyle().set("overflow", "hidden");
        card.addClickListener(e -> SubmissionDialog.open(s, editionYear.get(s.getEditionNo())));

        Div media = new Div();
        media.addClassName("sa-media");
        media.getStyle().set("height", "150px");
        SaUi.applyMedia(media, s.getMediaClass(), s.getImageGradient());
        Div badge = new Div(SaUi.tag(SaLabels.awardLabel(s.getAward()), SaLabels.awardTone(s.getAward()), "award"));
        badge.addClassName("sa-media__badge");
        media.add(badge);

        Div body = new Div();
        body.getStyle().set("padding", "20px");
        body.add(SaUi.tagSmall(SaLabels.division(s.getDivision()), SaLabels.divisionTone(s.getDivision())));
        H3 title = new H3(s.getTitle());
        title.getStyle().set("margin", "12px 0 6px").set("font-size", "var(--text-lg)")
                .set("font-weight", "600").set("line-height", "1.3");
        Div org = new Div();
        org.setText(s.getOrg());
        org.addClassNames("sa-text", "sa-text--sm", "sa-text--muted");
        body.add(title, org);

        card.add(media, body);
        return card;
    }

    private void navigate(String route) {
        UI.getCurrent().navigate(route);
        UI.getCurrent().getPage().executeJs("window.scrollTo(0,0)");
    }
}
