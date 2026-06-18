package kr.sciencealive.view.edition;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import kr.sciencealive.entity.Article;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Talk;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.view.layout.PublicLayout;

import java.util.List;

/** 회차별 행사 페이지 — 일시·장소, 포스터, 강연 프로그램, 기사, 해당 회차 PR어워드 링크. */
@Route(value = "event", layout = PublicLayout.class)
@AnonymousAllowed
public class EditionView extends Div implements HasUrlParameter<Integer> {

    private final transient DataManager dataManager;

    public EditionView(DataManager dataManager) {
        this.dataManager = dataManager;
        getStyle().set("max-width", "1000px").set("margin", "0 auto").set("padding", "56px 32px");
    }

    @Override
    public void setParameter(BeforeEvent event, Integer editionNo) {
        removeAll();
        Edition ed = dataManager.load(Edition.class).all().list().stream()
                .filter(e -> editionNo != null && editionNo.equals(e.getEditionNo()))
                .findFirst().orElse(null);

        if (ed == null) {
            add(notFound());
            return;
        }
        UI.getCurrent().getPage().setTitle("제 " + ed.getEditionNo() + "회 · 사이언스얼라이브");

        List<Talk> talks = dataManager.load(Talk.class).all().sort(Sort.by(Sort.Direction.ASC, "sortOrder")).list()
                .stream().filter(t -> ed.getEditionNo().equals(t.getEditionNo())).toList();
        List<Article> articles = dataManager.load(Article.class).all().sort(Sort.by(Sort.Direction.ASC, "sortOrder")).list()
                .stream().filter(a -> ed.getEditionNo().equals(a.getEditionNo())).toList();

        add(backLink(), headerSection(ed), talksSection(talks), articlesSection(articles));
    }

    private Component backLink() {
        var back = SaUi.pureLink("행사 아카이브", "arrow-left");
        back.addClickListener(e -> navigate("archive"));
        Div row = new Div(back);
        row.getStyle().set("margin-bottom", "20px");
        return row;
    }

    private Component headerSection(Edition ed) {
        Section section = new Section();
        section.getStyle().set("display", "grid").set("grid-template-columns", "300px 1fr")
                .set("gap", "40px").set("align-items", "start");
        section.addClassName("sa-edition-header");

        // Poster
        Div poster = new Div();
        poster.addClassName("sa-poster");
        if (ed.getPosterImage() != null) {
            poster.getStyle().set("background", ed.getPosterImage());
        }
        Div posterInner = new Div();
        posterInner.addClassName("sa-poster__inner");
        Span eyebrow = new Span("제 " + ed.getEditionNo() + "회 · " + ed.getYear());
        eyebrow.addClassName("sa-poster__eyebrow");
        Div ptitle = new Div();
        ptitle.setText(ed.getTheme());
        ptitle.addClassName("sa-poster__title");
        Span pdate = new Span(ed.getEventDate());
        pdate.addClassName("sa-poster__date");
        posterInner.add(eyebrow, ptitle, pdate);
        poster.add(posterInner);

        // Info
        Div info = new Div();
        boolean upcoming = "예정".equals(ed.getStatus());
        Div tagRow = new Div(SaUi.tag(ed.getStatus(), upcoming ? "info" : "neutral"));
        tagRow.getStyle().set("margin-bottom", "14px");
        H1 title = new H1(ed.getTheme());
        title.addClassNames("sa-heading", "sa-heading--lg");

        Div meta = new Div();
        meta.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "14px")
                .set("margin-top", "24px");
        meta.add(metaItem("calendar", "일시", ed.getEventDate()),
                metaItem("map-pin", "장소", ed.getVenue()),
                metaItem("microscope", "프로그램", ed.getTalks() + "개 세션"));

        NativeButton awardBtn = SaUi.button("이 회차 PR어워드 수상작 보기", "award", "sa-btn--primary", false);
        awardBtn.getStyle().set("margin-top", "28px");
        awardBtn.addClickListener(e -> navigate("award?edition=" + ed.getEditionNo()));

        info.add(tagRow, title, meta, awardBtn);
        section.add(poster, info);
        return section;
    }

    private Component metaItem(String icon, String label, String value) {
        Div wrap = new Div();
        wrap.getStyle().set("display", "flex").set("align-items", "center").set("gap", "10px");
        Div ic = new Div(SaIcons.icon(icon, 18));
        ic.getStyle().set("color", "var(--color-contrast-medium)");
        Span l = new Span(label);
        l.getStyle().set("width", "64px").set("color", "var(--color-contrast-medium)")
                .set("font-size", "var(--text-sm)");
        Span v = new Span(value);
        v.getStyle().set("font-weight", "600");
        wrap.add(ic, l, v);
        return wrap;
    }

    private Component talksSection(List<Talk> talks) {
        Section section = new Section();
        section.getStyle().set("margin-top", "72px");
        Div tagRow = new Div(SaUi.tag("Program", "basic"));
        tagRow.getStyle().set("margin-bottom", "12px");
        H2 h = new H2("강연 프로그램");
        h.addClassNames("sa-heading", "sa-heading--md");
        Div list = new Div();
        list.getStyle().set("margin-top", "24px").set("display", "flex")
                .set("flex-direction", "column").set("gap", "12px");
        if (talks.isEmpty()) {
            list.add(emptyText("프로그램이 곧 공개됩니다."));
        }
        for (Talk t : talks) {
            list.add(talkRow(t));
        }
        section.add(tagRow, h, list);
        return section;
    }

    private Component talkRow(Talk t) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline");
        card.getStyle().set("display", "grid").set("grid-template-columns", "120px 1fr")
                .set("gap", "20px").set("align-items", "center").set("padding", "20px 24px");
        Div slot = new Div();
        slot.setText(t.getTimeSlot() != null ? t.getTimeSlot() : "");
        slot.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)")
                .set("font-weight", "600");
        Div body = new Div();
        Div title = new Div();
        title.setText(t.getTitle());
        title.getStyle().set("font-weight", "600").set("font-size", "var(--text-lg)").set("margin-bottom", "4px");
        Div speaker = new Div();
        Span name = new Span(t.getSpeakerName());
        name.getStyle().set("font-weight", "600").set("font-size", "var(--text-sm)");
        Span role = new Span(t.getSpeakerRole() != null ? "  ·  " + t.getSpeakerRole() : "");
        role.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)");
        speaker.add(name, role);
        body.add(title, speaker);
        card.add(slot, body);
        return card;
    }

    private Component articlesSection(List<Article> articles) {
        Section section = new Section();
        section.getStyle().set("margin-top", "72px");
        Div tagRow = new Div(SaUi.tag("Press", "neutral"));
        tagRow.getStyle().set("margin-bottom", "12px");
        H2 h = new H2("관련 기사");
        h.addClassNames("sa-heading", "sa-heading--md");
        Div list = new Div();
        list.getStyle().set("margin-top", "20px").set("display", "flex")
                .set("flex-direction", "column").set("gap", "2px");
        if (articles.isEmpty()) {
            list.add(emptyText("등록된 기사가 없습니다."));
        }
        for (Article a : articles) {
            list.add(articleRow(a));
        }
        section.add(tagRow, h, list);
        return section;
    }

    private Component articleRow(Article a) {
        Anchor row = new Anchor(a.getUrl() != null ? a.getUrl() : "#", "");
        row.addClassName("sa-article");
        Div left = new Div();
        Div title = new Div();
        title.setText(a.getTitle());
        title.addClassName("sa-article__title");
        Span meta = new Span(a.getPublisher() + "  ·  " + a.getPublishedDate());
        meta.addClassName("sa-article__meta");
        left.add(title, meta);
        row.add(left, SaIcons.icon("arrow-right", 18));
        return row;
    }

    private Component emptyText(String text) {
        Div d = new Div();
        d.setText(text);
        d.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)").set("padding", "8px 0");
        return d;
    }

    private Component notFound() {
        Div d = new Div();
        d.getStyle().set("padding", "40px 0");
        H1 h = new H1("행사를 찾을 수 없습니다");
        h.addClassNames("sa-heading", "sa-heading--lg");
        var link = SaUi.pureLink("행사 아카이브로 돌아가기", "arrow-left");
        link.addClickListener(e -> navigate("archive"));
        d.add(h, link);
        return d;
    }

    private void navigate(String route) {
        UI.getCurrent().navigate(route);
        UI.getCurrent().getPage().executeJs("window.scrollTo(0,0)");
    }
}
