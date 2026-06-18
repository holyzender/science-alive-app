package kr.sciencealive.view.submit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import kr.sciencealive.entity.Submission;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.view.layout.PublicLayout;

import java.util.Objects;

/** 제출하기 — PR Award submission form; persists a real Submission. */
@Route(value = "submit", layout = PublicLayout.class)
@PageTitle("보도자료 제출 · PR어워드")
@AnonymousAllowed
public class SubmitView extends Div {

    private final transient DataManager dataManager;
    private final transient SystemAuthenticator systemAuthenticator;

    private final Div notice = new Div();
    private Select<String> divisionSelect;
    private Input institutionInput;
    private Input titleInput;

    public SubmitView(DataManager dataManager, SystemAuthenticator systemAuthenticator) {
        this.dataManager = dataManager;
        this.systemAuthenticator = systemAuthenticator;

        getStyle().set("max-width", "760px").set("margin", "0 auto").set("padding", "56px 32px");

        Div tagRow = new Div(SaUi.tag("PR Award · 제출", "basic"));
        tagRow.getStyle().set("margin-bottom", "16px");
        H1 title = new H1("보도자료 제출");
        title.addClassNames("sa-heading", "sa-heading--lg");
        Paragraph lede = new Paragraph("기관당 부문별 제출 건수에 제한이 있습니다. 제출 후에는 수정이 제한됩니다.");
        lede.addClassNames("sa-text", "sa-text--lg", "sa-text--secondary");
        lede.getStyle().set("margin-top", "14px");

        add(tagRow, title, lede, notice, buildForm());
    }

    private Component buildForm() {
        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--surface");
        card.getStyle().set("margin-top", "28px").set("display", "flex")
                .set("flex-direction", "column").set("gap", "22px");

        Div grid = new Div();
        grid.getStyle().set("display", "grid").set("grid-template-columns", "1fr 1fr").set("gap", "18px");

        Div divisionField = new Div();
        divisionField.add(fieldLabel("부문", true));
        divisionSelect = new Select<>();
        divisionSelect.setItems("Basic Science (순수과학)", "Applied Science (응용과학)");
        divisionSelect.setPlaceholder("선택하세요");
        divisionSelect.setWidthFull();
        divisionField.add(divisionSelect);

        Div institutionField = new Div();
        institutionField.add(fieldLabel("소속 기관", true));
        institutionInput = new Input();
        institutionInput.addClassName("sa-input");
        institutionInput.setPlaceholder("예) 한국천문연구원");
        institutionField.add(institutionInput);

        grid.add(divisionField, institutionField);

        Div titleField = new Div();
        titleField.add(fieldLabel("보도자료 제목", true));
        titleInput = new Input();
        titleInput.addClassName("sa-input");
        titleInput.setPlaceholder("제출작의 제목을 입력하세요");
        titleField.add(titleInput);

        card.add(grid, titleField,
                labeledTextarea("핵심 요약", "3–5문장으로 요약",
                        "연구의 의의를 대중이 이해하기 쉽게 작성해 주세요."),
                divider());

        Div attachWrap = new Div();
        attachWrap.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "12px");
        Span attachH = new Span("첨부 자료");
        attachH.getStyle().set("font-size", "var(--text-sm)").set("font-weight", "600");
        attachWrap.add(attachH,
                dropzone("file-text", "추천서 파일", "PDF · 최대 10MB"),
                dropzone("file-text", "보도자료 파일", "PDF, DOCX · 최대 20MB"),
                dropzone("image", "사진 (다중 첨부)", "JPG, PNG · 최대 10장"),
                dropzone("video", "영상 (파일 또는 외부 링크)", "MP4 업로드 또는 URL"));
        card.add(attachWrap, divider());

        Checkbox consent = new Checkbox("비수상작도 공개되는 것에 동의합니다.");
        NativeButton submit = SaUi.button("제출하기", "check", "sa-btn--primary", false);
        submit.setEnabled(false);
        consent.addValueChangeListener(e -> submit.setEnabled(e.getValue()));
        submit.addClickListener(e -> onSubmit());

        NativeButton draft = SaUi.button("임시 저장", null, "sa-btn--secondary", false);
        Div actions = new Div(draft, submit);
        actions.getStyle().set("display", "flex").set("justify-content", "flex-end").set("gap", "12px");

        card.add(consent, actions);
        return card;
    }

    private void onSubmit() {
        String division = mapDivision(divisionSelect.getValue());
        String org = value(institutionInput.getValue());
        String title = value(titleInput.getValue());

        if (division == null || org.isEmpty() || title.isEmpty()) {
            showNote("error", "warning", "필수 항목을 확인해 주세요",
                    "부문 · 소속 기관 · 제목은 반드시 입력해야 합니다.");
            return;
        }

        Submission s = dataManager.create(Submission.class);
        String code = nextCode();
        s.setCode(code);
        s.setTitle(title);
        s.setOrg(org);
        s.setDivision(division);
        s.setEditionNo(7);
        s.setAward(null);
        s.setViews(0);
        s.setImageGradient("linear-gradient(135deg,#1b2a4a,#0e1422)");
        // Persist as a system action so the cosmetic-demo works for any visitor.
        systemAuthenticator.withSystem(() -> dataManager.save(s));

        showNote("success", "check-circle", "제출이 완료되었습니다",
                "접수 번호 #" + code + "으로 확인하실 수 있습니다. 심사 일정은 등록하신 이메일로 안내드립니다. 갤러리에서 바로 확인할 수 있습니다.");
    }

    private String nextCode() {
        int max = systemAuthenticator.withSystem(() ->
                dataManager.load(Submission.class).all().list().stream()
                        .map(Submission::getCode).filter(Objects::nonNull)
                        .map(c -> c.replaceAll("[^0-9]", "")).filter(n -> !n.isEmpty())
                        .mapToInt(Integer::parseInt).max().orElse(142));
        return String.format("PRA-%04d", max + 1);
    }

    private String mapDivision(String label) {
        if (label == null) return null;
        if (label.startsWith("Basic")) return "basic";
        if (label.startsWith("Applied")) return "applied";
        return null;
    }

    private String value(String v) {
        return v == null ? "" : v.trim();
    }

    private void showNote(String variant, String icon, String heading, String body) {
        notice.removeAll();
        Div note = new Div();
        note.addClassNames("sa-note", "sa-note--" + variant);
        note.getStyle().set("margin-top", "28px");
        Div iconEl = new Div(SaIcons.icon(icon, 20));
        iconEl.addClassName("sa-note__icon");
        Div bodyWrap = new Div();
        Div h = new Div(heading);
        h.addClassName("sa-note__heading");
        Div b = new Div(body);
        b.addClassName("sa-note__body");
        bodyWrap.add(h, b);
        note.add(iconEl, bodyWrap);
        notice.add(note);
        notice.getElement().executeJs("this.scrollIntoView({behavior:'smooth',block:'center'})");
    }

    private Component fieldLabel(String text, boolean required) {
        Span label = new Span(text);
        label.addClassName("sa-field-label");
        if (required) {
            Span star = new Span(" *");
            star.addClassName("sa-field-required");
            label.add(star);
        }
        return label;
    }

    private Component labeledTextarea(String label, String placeholder, String hint) {
        Div field = new Div();
        field.add(fieldLabel(label, false));
        Element ta = new Element("textarea");
        ta.setAttribute("rows", "4");
        ta.setAttribute("placeholder", placeholder);
        ta.getClassList().add("sa-textarea");
        field.getElement().appendChild(ta);
        if (hint != null) {
            Div hintEl = new Div();
            hintEl.setText(hint);
            hintEl.getStyle().set("color", "var(--color-contrast-medium)")
                    .set("font-size", "var(--text-xs)").set("margin-top", "6px");
            field.add(hintEl);
        }
        return field;
    }

    private Component dropzone(String icon, String label, String hint) {
        Div zone = new Div();
        zone.addClassName("sa-dropzone");
        Div iconEl = new Div(SaIcons.icon(icon, 24));
        iconEl.getStyle().set("color", "var(--color-contrast-medium)");
        Div text = new Div();
        Div l = new Div();
        l.setText(label);
        l.getStyle().set("font-weight", "600").set("font-size", "var(--text-sm)");
        Div h = new Div();
        h.setText(hint);
        h.getStyle().set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-xs)")
                .set("margin-top", "2px");
        text.add(l, h);
        NativeButton pick = SaUi.button("선택", "upload", "sa-btn--secondary", false);
        pick.addClassName("sa-btn--sm");
        Div pickWrap = new Div(pick);
        pickWrap.getStyle().set("margin-left", "auto");
        zone.add(iconEl, text, pickWrap);
        return zone;
    }

    private Component divider() {
        Hr hr = new Hr();
        hr.addClassName("sa-divider");
        return hr;
    }
}
