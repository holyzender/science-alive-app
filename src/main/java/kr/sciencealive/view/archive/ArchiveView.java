package kr.sciencealive.view.archive;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.view.layout.PublicLayout;

import java.util.List;

/** 행사 아카이브 — edition list with selective detail. */
@Route(value = "archive", layout = PublicLayout.class)
@PageTitle("행사 아카이브 · 사이언스얼라이브")
@AnonymousAllowed
public class ArchiveView extends Div {

    public ArchiveView(DataManager dataManager) {
        getStyle().set("max-width", "1000px").set("margin", "0 auto").set("padding", "56px 32px");

        Div tagRow = new Div(SaUi.tag("Archive", "neutral"));
        tagRow.getStyle().set("margin-bottom", "16px");

        H1 title = new H1("행사 아카이브");
        title.addClassNames("sa-heading", "sa-heading--xl");

        Paragraph lede = new Paragraph("회차별 행사 정보입니다. 초기 회차는 일부 자료가 준비되는 대로 추가됩니다.");
        lede.addClassNames("sa-text", "sa-text--lg", "sa-text--secondary");
        lede.getStyle().set("margin-top", "14px").set("max-width", "620px");

        Div list = new Div();
        list.getStyle().set("margin-top", "40px").set("display", "flex")
                .set("flex-direction", "column").set("gap", "18px");

        List<Edition> editions = dataManager.load(Edition.class).all()
                .sort(Sort.by(Sort.Direction.DESC, "editionNo")).list();
        for (Edition ed : editions) {
            list.add(editionCard(ed));
        }

        add(tagRow, title, lede, list);
    }

    private Component editionCard(Edition ed) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline");
        card.getStyle().set("display", "grid").set("grid-template-columns", "120px 1fr auto")
                .set("gap", "28px").set("align-items", "center");

        // Year column
        Div yearCol = new Div();
        Div year = new Div();
        year.setText(String.valueOf(ed.getYear()));
        year.getStyle().set("font-family", "var(--font-display)").set("font-weight", "700")
                .set("font-size", "40px").set("line-height", "1");
        Div no = new Div();
        no.setText("제 " + ed.getEditionNo() + "회");
        no.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)")
                .set("margin-top", "4px");
        yearCol.add(year, no);

        // Middle column
        Div mid = new Div();
        Div titleRow = new Div();
        titleRow.getStyle().set("display", "flex").set("align-items", "center")
                .set("gap", "8px").set("margin-bottom", "8px");
        H3 theme = new H3(ed.getTheme());
        theme.getStyle().set("margin", "0").set("font-size", "var(--text-xl)").set("font-weight", "600");
        boolean upcoming = "예정".equals(ed.getStatus());
        titleRow.add(theme, SaUi.tagSmall(ed.getStatus(), upcoming ? "info" : "neutral"));
        Div meta = new Div();
        meta.getStyle().set("display", "flex").set("gap", "20px").set("flex-wrap", "wrap")
                .set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)");
        meta.add(metaItem("calendar", ed.getEventDate()),
                metaItem("map-pin", ed.getVenue()),
                metaItem("microscope", ed.getTalks() + "개 세션"));
        mid.add(titleRow, meta);

        // Right column
        Div right = new Div();
        right.getStyle().set("display", "flex").set("flex-direction", "column")
                .set("gap", "8px").set("align-items", "flex-end");
        var eventLink = SaUi.pureLink("행사 페이지", "arrow-right");
        eventLink.addClickListener(e -> navigate("event/" + ed.getEditionNo()));
        var awards = SaUi.pureLink("수상작", "award");
        awards.addClickListener(e -> navigate("award?edition=" + ed.getEditionNo()));
        right.add(eventLink, awards);

        card.add(yearCol, mid, right);
        return card;
    }

    private Component metaItem(String icon, String text) {
        Span s = new Span();
        s.getStyle().set("display", "flex").set("align-items", "center").set("gap", "5px");
        s.add(SaIcons.icon(icon, 15), new Span(text));
        return s;
    }

    private void navigate(String route) {
        UI.getCurrent().navigate(route);
        UI.getCurrent().getPage().executeJs("window.scrollTo(0,0)");
    }
}
