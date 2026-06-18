package kr.sciencealive.view.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.UserRepository;
import kr.sciencealive.entity.User;
import kr.sciencealive.ui.SaIcons;
import kr.sciencealive.ui.SaUi;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global chrome for the public Science Alive surfaces — the sticky frosted
 * header (wordmark + horizontal nav + search + login/avatar) and the dark
 * footer. Child {@code @Route} views render between them.
 */
public class PublicLayout extends Div implements RouterLayout {

    private final CurrentAuthentication currentAuthentication;
    private final UserRepository userRepository;

    private final Div main = new Div();
    private final Map<String, Anchor> navLinks = new LinkedHashMap<>();

    private static final String[][] NAV = {
            {"", "소개"},
            {"archive", "행사 아카이브"},
            {"award", "PR어워드"},
            {"submit", "제출하기"},
    };

    public PublicLayout(CurrentAuthentication currentAuthentication, UserRepository userRepository) {
        this.currentAuthentication = currentAuthentication;
        this.userRepository = userRepository;

        addClassName("sa-public");
        add(buildHeader());
        main.addClassName("sa-public__main");
        add(main);
        add(buildFooter());
    }

    private Component buildHeader() {
        Header header = new Header();
        header.addClassName("sa-header");

        Div inner = new Div();
        inner.addClassName("sa-header__inner");

        // Wordmark -> home
        Div brand = new Div(SaUi.wordmark(24, false));
        brand.getStyle().set("cursor", "pointer").set("display", "inline-flex");
        brand.addClickListener(e -> navigate(""));

        // Nav
        Nav nav = new Nav();
        nav.addClassName("sa-nav");
        for (String[] n : NAV) {
            String route = n[0];
            Anchor link = new Anchor();
            link.addClassName("sa-nav__link");
            link.setText(n[1]);
            link.getElement().setAttribute("role", "link");
            link.getElement().addEventListener("click", e -> navigate(route))
                    .addEventData("event.preventDefault()");
            link.setHref("");
            navLinks.put(route, link);
            nav.add(link);
        }

        // Actions: search + login/avatar
        Div actions = new Div();
        actions.addClassName("sa-header__actions");

        NativeButton search = new NativeButton();
        search.addClassName("sa-iconbtn");
        search.getElement().setAttribute("aria-label", "검색");
        search.add(SaIcons.icon("search", 22));
        actions.add(search);

        actions.add(buildUserArea());

        inner.add(brand, nav, actions);
        header.add(inner);
        return header;
    }

    private Component buildUserArea() {
        User user = currentUser();
        if (user != null) {
            Span wrap = new Span();
            wrap.addClassName("sa-user");
            Span avatar = new Span(initial(user));
            avatar.addClassName("sa-avatar");
            Span name = new Span(displayName(user));
            wrap.add(avatar, name);
            wrap.getStyle().set("cursor", "pointer");
            wrap.addClickListener(e -> navigate("admin"));
            return wrap;
        }
        NativeButton login = SaUi.button("로그인", "user", "sa-btn--secondary", false);
        login.addClassName("sa-btn--sm");
        login.addClickListener(e -> navigate("login"));
        return login;
    }

    private Component buildFooter() {
        Footer footer = new Footer();
        footer.addClassName("sa-footer");

        Div inner = new Div();
        inner.addClassName("sa-footer__inner");

        Div about = new Div();
        about.add(SaUi.wordmark(22, true));
        Paragraph desc = new Paragraph(
                "과학계 토론·소통 행사. 과학자·커뮤니케이터·정책입안자·대중이 함께 과학 현장의 주요 이슈를 토론합니다.");
        desc.addClassName("sa-footer__desc");
        about.add(desc);

        Div quick = footerColumn("바로가기", "행사 아카이브", "PR어워드", "수상작 갤러리", "제출 안내");
        Div ops = new Div();
        ops.addClassName("sa-footer__col");
        Span opsH = new Span("운영");
        opsH.addClassName("sa-footer__h");
        Span team = new Span("데일리뉴스 과학팀");
        team.getStyle().set("color", "rgba(255,255,255,0.82)").set("font-size", "var(--text-sm)");
        Span mail = new Span("press@sciencealive.kr");
        mail.getStyle().set("color", "rgba(255,255,255,0.82)").set("font-size", "var(--text-sm)");
        ops.add(opsH, team, mail);

        inner.add(about, quick, ops);

        Div bar = new Div();
        bar.addClassName("sa-footer__bar");
        Div barInner = new Div();
        barInner.addClassName("sa-footer__barinner");
        barInner.add(new Span("© 2026 Science Alive. All rights reserved."),
                new Span("Built on the Porsche Design System v4 · localized for Korean"));
        bar.add(barInner);

        footer.add(inner, bar);
        return footer;
    }

    private Div footerColumn(String heading, String... links) {
        Div col = new Div();
        col.addClassName("sa-footer__col");
        Span h = new Span(heading);
        h.addClassName("sa-footer__h");
        col.add(h);
        for (String l : links) {
            Anchor a = new Anchor("", l);
            a.addClassName("sa-footer__link");
            col.add(a);
        }
        return col;
    }

    private void navigate(String route) {
        UI.getCurrent().navigate(route);
        UI.getCurrent().getPage().executeJs("window.scrollTo(0,0)");
    }

    private User currentUser() {
        try {
            UserDetails u = currentAuthentication.getUser();
            if (u instanceof User user) {
                String anon = userRepository.getAnonymousUser().getUsername();
                if (!user.getUsername().equals(anon)) {
                    return user;
                }
            }
        } catch (Exception ignored) {
            // not authenticated
        }
        return null;
    }

    private String displayName(User u) {
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        String name = (ln + fn).trim().isEmpty() ? (fn + " " + ln).trim() : (ln + fn);
        return name.isEmpty() ? u.getUsername() : name;
    }

    private String initial(User u) {
        String n = displayName(u);
        return n.isEmpty() ? "?" : n.substring(0, 1);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        main.removeAll();
        if (content != null) {
            main.getElement().appendChild(content.getElement());
            updateActiveNav(content);
        }
    }

    @Override
    public void removeRouterLayoutContent(HasElement oldContent) {
        main.removeAll();
    }

    private void updateActiveNav(HasElement content) {
        String key = switch (content.getClass().getSimpleName()) {
            case "HomeView" -> "";
            case "ArchiveView" -> "archive";
            case "AwardGalleryView" -> "award";
            case "SubmitView" -> "submit";
            default -> null;
        };
        navLinks.forEach((route, link) ->
                link.getElement().getClassList().set("sa-nav__link--active", route.equals(key)));
    }
}
