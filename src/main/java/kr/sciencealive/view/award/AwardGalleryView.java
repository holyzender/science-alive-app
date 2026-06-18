package kr.sciencealive.view.award;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Submission;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaLabels;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.ui.SubmissionDialog;
import kr.sciencealive.view.layout.PublicLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** PR어워드 갤러리 — browse, filter by 부문/회차/수상 여부, open a press-release detail. */
@Route(value = "award", layout = PublicLayout.class)
@PageTitle("보도자료 갤러리 · PR어워드")
@AnonymousAllowed
public class AwardGalleryView extends Div implements BeforeEnterObserver {

    private final List<Submission> all;
    private final Map<Integer, Integer> editionYear = new LinkedHashMap<>();

    private String division = "all";
    private String edition = "all";
    private boolean winnersOnly = false;

    private final Div grid = new Div();
    private final Span count = new Span();
    private final Div tabsBar = new Div();
    private Select<String> editionSelect;

    public AwardGalleryView(DataManager dataManager) {
        getStyle().set("max-width", "1200px").set("margin", "0 auto").set("padding", "56px 32px");

        all = dataManager.load(Submission.class).all()
                .sort(Sort.by(Sort.Direction.DESC, "code")).list();
        List<Edition> editions = dataManager.load(Edition.class).all()
                .sort(Sort.by(Sort.Direction.DESC, "editionNo")).list();
        editions.forEach(e -> editionYear.put(e.getEditionNo(), e.getYear()));

        Div tagRow = new Div(SaUi.tag("PR Award", "gold", "award"));
        tagRow.getStyle().set("margin-bottom", "16px");
        H1 title = new H1("보도자료 갤러리");
        title.addClassNames("sa-heading", "sa-heading--xl");
        Paragraph lede = new Paragraph(
                "심사를 마친 제출작을 누구나 열람·다운로드할 수 있습니다. 회차·부문·수상 여부로 검색해 보세요.");
        lede.addClassNames("sa-text", "sa-text--lg", "sa-text--secondary");
        lede.getStyle().set("margin-top", "14px").set("max-width", "640px");

        add(tagRow, title, lede, buildFilters(editions), grid);
        refresh();
    }

    /** Pre-select an edition from a {@code ?edition=N} query parameter (e.g. from an event page). */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> vals = event.getLocation().getQueryParameters().getParameters().get("edition");
        if (vals == null || vals.isEmpty() || editionSelect == null) {
            return;
        }
        String v = vals.get(0);
        try {
            if ("all".equals(v) || editionYear.containsKey(Integer.valueOf(v))) {
                editionSelect.setValue(v);
            }
        } catch (NumberFormatException ignored) {
            // ignore invalid edition param
        }
    }

    private Component buildFilters(List<Edition> editions) {
        Div wrap = new Div();

        // Division tabs
        tabsBar.addClassName("sa-tabs");
        tabsBar.getStyle().set("margin-top", "32px").set("margin-bottom", "8px");
        addTab("all", "전체 부문");
        addTab("basic", "Basic Science");
        addTab("applied", "Applied Science");

        // Edition select + winners toggle + count
        Div row = new Div();
        row.getStyle().set("display", "flex").set("gap", "16px").set("align-items", "flex-end")
                .set("flex-wrap", "wrap").set("padding", "20px 0 28px");

        editionSelect = new Select<>();
        editionSelect.setWidth("200px");
        java.util.List<String> opts = new java.util.ArrayList<>();
        opts.add("all");
        editions.forEach(e -> opts.add(String.valueOf(e.getEditionNo())));
        editionSelect.setItems(opts);
        editionSelect.setItemLabelGenerator(v -> "all".equals(v)
                ? "전체 회차" : "제 " + v + "회 (" + editionYear.get(Integer.valueOf(v)) + ")");
        editionSelect.setValue("all");
        editionSelect.addValueChangeListener(e -> {
            edition = e.getValue();
            refresh();
        });

        NativeButton toggle = new NativeButton();
        toggle.addClassName("sa-toggle");
        Runnable paintToggle = () -> {
            toggle.removeAll();
            toggle.add(SaIcons.icon(winnersOnly ? "check" : "award", 18), new Span("수상작만"));
            toggle.getElement().getClassList().set("sa-toggle--on", winnersOnly);
        };
        paintToggle.run();
        toggle.addClickListener(e -> {
            winnersOnly = !winnersOnly;
            paintToggle.run();
            refresh();
        });

        count.getStyle().set("margin-left", "auto").set("color", "var(--color-contrast-medium)")
                .set("font-size", "var(--text-sm)");

        row.add(editionSelect, toggle, count);
        wrap.add(tabsBar, row);
        return wrap;
    }

    private void addTab(String key, String label) {
        NativeButton tab = new NativeButton(label);
        tab.addClassName("sa-tab");
        tab.getElement().setAttribute("data-key", key);
        if (key.equals(division)) {
            tab.addClassName("sa-tab--active");
        }
        tab.addClickListener(e -> {
            division = key;
            tabsBar.getChildren().forEach(c -> c.getElement().getClassList()
                    .set("sa-tab--active", key.equals(c.getElement().getAttribute("data-key"))));
            refresh();
        });
        tabsBar.add(tab);
    }

    private void refresh() {
        List<Submission> items = all.stream()
                .filter(s -> "all".equals(division) || division.equals(s.getDivision()))
                .filter(s -> "all".equals(edition) || edition.equals(String.valueOf(s.getEditionNo())))
                .filter(s -> !winnersOnly || s.getAward() != null)
                .toList();

        count.setText(items.size() + "건");
        grid.removeAll();
        grid.removeClassName("sa-grid-3");

        if (items.isEmpty()) {
            grid.add(emptyNote());
            return;
        }
        grid.addClassName("sa-grid-3");
        grid.getStyle().set("gap", "22px");
        for (Submission s : items) {
            grid.add(galleryCard(s));
        }
    }

    private Component emptyNote() {
        Div note = new Div();
        note.addClassNames("sa-note", "sa-note--info");
        Div icon = new Div(SaIcons.icon("info", 20));
        icon.addClassName("sa-note__icon");
        Div body = new Div();
        Div h = new Div("결과가 없습니다");
        h.addClassName("sa-note__heading");
        Div b = new Div("선택한 조건에 해당하는 제출작이 없습니다.");
        b.addClassName("sa-note__body");
        body.add(h, b);
        note.add(icon, body);
        return note;
    }

    private Component galleryCard(Submission s) {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline", "sa-card--pad-none", "sa-card--interactive");
        card.getStyle().set("overflow", "hidden");
        card.addClickListener(e -> openDetail(s));

        Div media = new Div();
        media.addClassName("sa-media");
        media.getStyle().set("height", "168px");
        SaUi.applyMedia(media, s.getMediaClass(), s.getImageGradient());
        if (s.getAward() != null) {
            Div badge = new Div(SaUi.tag(SaLabels.awardLabel(s.getAward()), SaLabels.awardTone(s.getAward()), "award"));
            badge.addClassName("sa-media__badge");
            media.add(badge);
        }
        Div views = new Div();
        views.addClassName("sa-media__views");
        views.add(SaIcons.icon("eye", 13), new Span(String.format("%,d", s.getViews())));
        media.add(views);

        Div body = new Div();
        body.getStyle().set("padding", "20px");
        Div tags = new Div();
        tags.getStyle().set("display", "flex").set("gap", "6px").set("margin-bottom", "10px");
        tags.add(SaUi.tagSmall(SaLabels.division(s.getDivision()), SaLabels.divisionTone(s.getDivision())),
                SaUi.tagSmall("제" + s.getEditionNo() + "회", "neutral"));
        H3 title = new H3(s.getTitle());
        title.getStyle().set("margin", "0 0 8px").set("font-size", "var(--text-lg)")
                .set("font-weight", "600").set("line-height", "1.3");
        Div org = new Div();
        org.setText(s.getOrg());
        org.addClassNames("sa-text", "sa-text--sm", "sa-text--muted");
        body.add(tags, title, org);

        card.add(media, body);
        return card;
    }

    private void openDetail(Submission s) {
        SubmissionDialog.open(s, editionYear.get(s.getEditionNo()));
    }
}
