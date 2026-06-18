package kr.sciencealive.view.judge;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.UserRepository;
import kr.sciencealive.entity.Submission;
import kr.sciencealive.entity.User;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaLabels;
import kr.sciencealive.ui.SaUi;
import kr.sciencealive.view.layout.PublicLayout;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 심사 콘솔 — assigned list + scoring; blocks the judge's own institution. */
@Route(value = "judge", layout = PublicLayout.class)
@PageTitle("심사 콘솔 · PR어워드")
@AnonymousAllowed
public class JudgeView extends Div {

    private final List<Submission> assigned;
    private final String judgeOrg;
    private final Set<String> done = new HashSet<>();
    private String activeId;

    private final Div leftList = new Div();
    private final Div rightHolder = new Div();

    private static final String[][] CRITERIA = {
            {"과학적 정확성", "내용의 사실성과 근거"},
            {"전달력 / 가독성", "대중이 이해하기 쉬운 정도"},
            {"사회적 의의", "연구의 파급 효과"},
    };

    public JudgeView(DataManager dataManager, CurrentAuthentication currentAuthentication,
                     UserRepository userRepository) {
        this.judgeOrg = resolveJudgeOrg(currentAuthentication, userRepository);

        assigned = dataManager.load(Submission.class).all()
                .sort(Sort.by(Sort.Direction.DESC, "code")).list()
                .stream().filter(s -> Integer.valueOf(7).equals(s.getEditionNo())).toList();
        activeId = assigned.stream().filter(s -> !s.getOrg().equals(judgeOrg))
                .map(Submission::getCode).findFirst().orElse(assigned.isEmpty() ? null : assigned.get(0).getCode());

        getStyle().set("max-width", "1200px").set("margin", "0 auto").set("padding", "48px 32px")
                .set("display", "grid").set("grid-template-columns", "340px 1fr")
                .set("gap", "28px").set("align-items", "start");
        addClassName("sa-judge-grid");

        add(buildLeft(), rightHolder);
        renderLeft();
        renderRight();
    }

    private String resolveJudgeOrg(CurrentAuthentication auth, UserRepository repo) {
        try {
            UserDetails u = auth.getUser();
            if (u instanceof User user && user.getInstitution() != null
                    && !user.getUsername().equals(repo.getAnonymousUser().getUsername())) {
                return user.getInstitution();
            }
        } catch (Exception ignored) {
            // fall through to demo default
        }
        return "KAIST 신소재공학과";
    }

    private Component buildLeft() {
        Div left = new Div();
        Div tagRow = new Div(SaUi.tag("심사위원 콘솔", "info"));
        tagRow.getStyle().set("margin-bottom", "12px");
        H1 h = new H1("배정 제출작");
        h.addClassNames("sa-heading", "sa-heading--sm");
        Div sub = new Div();
        sub.setText(judgeOrg + " · " + assigned.size() + "건");
        sub.addClassNames("sa-text", "sa-text--sm", "sa-text--muted");
        sub.getStyle().set("margin-top", "6px").set("margin-bottom", "18px");

        leftList.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "10px");
        left.add(tagRow, h, sub, leftList);
        return left;
    }

    private void renderLeft() {
        leftList.removeAll();
        for (Submission s : assigned) {
            boolean own = s.getOrg().equals(judgeOrg);
            boolean on = s.getCode().equals(activeId);

            NativeButton item = new NativeButton();
            item.addClassName("sa-judge-item");
            if (on) {
                item.addClassName("sa-judge-item--on");
            }

            Div top = new Div();
            top.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center");
            top.add(SaUi.tagSmall(SaLabels.division(s.getDivision()), SaLabels.divisionTone(s.getDivision())));
            if (own) {
                top.add(SaUi.tagSmall("본인 기관", "warning"));
            } else if (done.contains(s.getCode())) {
                top.add(SaUi.tagSmall("완료", "success", "check"));
            } else {
                Span pending = new Span("미평가");
                pending.getStyle().set("font-size", "var(--text-xs)").set("color", "var(--color-contrast-medium)");
                top.add(pending);
            }

            Span title = new Span(s.getTitle());
            title.getStyle().set("font-weight", "600").set("font-size", "var(--text-sm)").set("line-height", "1.35");
            Span org = new Span(s.getOrg());
            org.getStyle().set("font-size", "var(--text-xs)").set("color", "var(--color-contrast-medium)");

            item.add(top, title, org);
            item.addClickListener(e -> {
                activeId = s.getCode();
                renderLeft();
                renderRight();
            });
            leftList.add(item);
        }
    }

    private void renderRight() {
        rightHolder.removeAll();
        Submission active = assigned.stream().filter(s -> s.getCode().equals(activeId)).findFirst().orElse(null);
        if (active == null) {
            return;
        }
        boolean blocked = active.getOrg().equals(judgeOrg);

        Div card = new Div();
        card.addClassNames("sa-card", "sa-card--outline");

        Div tags = new Div();
        tags.getStyle().set("display", "flex").set("gap", "6px").set("margin-bottom", "14px");
        tags.add(SaUi.tag(SaLabels.division(active.getDivision()), SaLabels.divisionTone(active.getDivision())),
                SaUi.tag(active.getCode(), "neutral"));
        H2 title = new H2(active.getTitle());
        title.addClassNames("sa-heading", "sa-heading--sm");
        Div org = new Div();
        org.setText(active.getOrg());
        org.addClassNames("sa-text", "sa-text--sm", "sa-text--muted");
        org.getStyle().set("margin-top", "6px");

        Hr divider = new Hr();
        divider.addClassName("sa-divider");
        divider.getStyle().set("margin", "22px 0");

        card.add(tags, title, org, divider);

        if (blocked) {
            card.add(blockNote());
        } else {
            card.add(scoringForm(active));
        }
        rightHolder.add(card);
    }

    private Component blockNote() {
        Div note = new Div();
        note.addClassNames("sa-note", "sa-note--warning");
        Div icon = new Div(SaIcons.icon("warning", 20));
        icon.addClassName("sa-note__icon");
        Div body = new Div();
        Div h = new Div("평가할 수 없는 제출작입니다");
        h.addClassName("sa-note__heading");
        Div b = new Div("소속 기관(" + judgeOrg + ")의 제출작은 기관 기준으로 열람·평가가 차단됩니다.");
        b.addClassName("sa-note__body");
        body.add(h, b);
        note.add(icon, body);
        return note;
    }

    private Component scoringForm(Submission active) {
        Div form = new Div();
        form.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "26px");

        for (int i = 0; i < CRITERIA.length; i++) {
            Div crit = new Div();
            Div q = new Div();
            q.setText((i + 1) + ". " + CRITERIA[i][0]);
            q.getStyle().set("font-weight", "600").set("margin-bottom", "4px");
            Div hint = new Div();
            hint.setText(CRITERIA[i][1]);
            hint.getStyle().set("font-size", "var(--text-xs)").set("color", "var(--color-contrast-medium)")
                    .set("margin-bottom", "12px");
            crit.add(q, hint, scoreGroup());
            form.add(crit);
        }

        Div commentWrap = new Div();
        Div cl = new Div("총평 (주관식)");
        cl.getStyle().set("font-weight", "600").set("margin-bottom", "10px");
        Element ta = new Element("textarea");
        ta.setAttribute("rows", "4");
        ta.setAttribute("placeholder", "제출작에 대한 종합 의견을 작성해 주세요.");
        ta.getClassList().add("sa-textarea");
        commentWrap.add(cl);
        commentWrap.getElement().appendChild(ta);
        form.add(commentWrap);

        NativeButton draft = SaUi.button("임시 저장", null, "sa-btn--secondary", false);
        NativeButton submit = SaUi.button("평가 제출", "check", "sa-btn--primary", false);
        submit.addClickListener(e -> {
            done.add(active.getCode());
            renderLeft();
        });
        Div actions = new Div(draft, submit);
        actions.getStyle().set("display", "flex").set("justify-content", "flex-end").set("gap", "12px");
        form.add(actions);
        return form;
    }

    /** 5–1점 radio-style score selector (default 4점). */
    private Component scoreGroup() {
        Div group = new Div();
        group.addClassName("sa-scores");
        int[] scores = {5, 4, 3, 2, 1};
        for (int n : scores) {
            NativeButton b = new NativeButton(n + "점");
            b.addClassName("sa-score");
            if (n == 4) {
                b.addClassName("sa-score--on");
            }
            b.addClickListener(e -> group.getChildren().forEach(c ->
                    c.getElement().getClassList().set("sa-score--on", c.equals(b))));
            group.add(b);
        }
        return group;
    }
}
