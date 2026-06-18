package kr.sciencealive.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;

/**
 * Factory helpers for the recurring Science Alive design primitives
 * (typographic wordmark, tags, PDS-style buttons, "pure" arrow links,
 * hero metrics). Visual styling lives in the {@code science-alive} theme
 * ({@code sa-*} classes); these helpers assemble the markup.
 */
public final class SaUi {

    private SaUi() {
    }

    /** Typographic wordmark: "Science" + boxed "Alive". */
    public static Component wordmark(int size, boolean inverted) {
        Span root = new Span();
        root.addClassName("sa-wordmark");
        if (inverted) {
            root.addClassName("sa-wordmark--inverted");
        }
        root.getStyle().set("font-size", size + "px");

        Span science = new Span("Science");
        science.addClassName("sa-wordmark__science");

        Span alive = new Span("Alive");
        alive.addClassName("sa-wordmark__alive");

        root.add(science, alive);
        return root;
    }

    /** Status / category tag. tone ∈ neutral|info|success|warning|error|basic|applied|gold|solid */
    public static Span tag(String text, String tone) {
        return buildTag(text, tone, null, false);
    }

    public static Span tag(String text, String tone, String iconName) {
        return buildTag(text, tone, iconName, false);
    }

    public static Span tagSmall(String text, String tone) {
        return buildTag(text, tone, null, true);
    }

    public static Span tagSmall(String text, String tone, String iconName) {
        return buildTag(text, tone, iconName, true);
    }

    private static Span buildTag(String text, String tone, String iconName, boolean small) {
        Span tag = new Span();
        tag.addClassNames("sa-tag", "sa-tag--" + tone);
        if (small) {
            tag.addClassName("sa-tag--sm");
        }
        if (iconName != null) {
            tag.add(SaIcons.icon(iconName, small ? 13 : 15));
        }
        tag.add(new Span(text));
        return tag;
    }

    /** Solid near-black primary button (optionally inverted to white-on-dark for the hero). */
    public static NativeButton primaryButton(String text, String iconName) {
        return button(text, iconName, "sa-btn--primary", false);
    }

    /** Outlined secondary button (2px border). */
    public static NativeButton secondaryButton(String text, String iconName) {
        return button(text, iconName, "sa-btn--secondary", false);
    }

    public static NativeButton button(String text, String iconName, String variantClass, boolean iconEnd) {
        NativeButton btn = new NativeButton();
        btn.addClassNames("sa-btn", variantClass);
        if (iconName != null && !iconEnd) {
            btn.add(SaIcons.icon(iconName, 20));
        }
        if (text != null) {
            btn.add(new Span(text));
        }
        if (iconName != null && iconEnd) {
            btn.add(SaIcons.icon(iconName, 20));
        }
        return btn;
    }

    /** "Pure" text link with a trailing arrow (PDS quiet link). Clickable. */
    public static Span arrowLink(String text) {
        Span a = new Span();
        a.addClassName("sa-arrow-link");
        a.add(new Span(text), SaIcons.icon("arrow-right", 18));
        a.getElement().setAttribute("role", "button");
        a.getStyle().set("cursor", "pointer");
        return a;
    }

    /** "Pure" text link with a leading icon. Clickable. */
    public static Span pureLink(String text, String iconName) {
        Span a = new Span();
        a.addClassName("sa-arrow-link");
        a.add(SaIcons.icon(iconName, 18), new Span(text));
        a.getElement().setAttribute("role", "button");
        a.getStyle().set("cursor", "pointer");
        return a;
    }

    /** Hero metric: big display number + caption. */
    public static Div metric(String number, String label) {
        Div wrap = new Div();
        Div n = new Div();
        n.setText(number);
        n.addClassName("sa-metric__num");
        Div l = new Div();
        l.setText(label);
        l.addClassName("sa-metric__label");
        wrap.add(n, l);
        return wrap;
    }

    /**
     * Sets a submission's media background. Prefers a bundled CSS class
     * (e.g. {@code sa-media--mrna}) so the image survives Vaadin's production
     * bundling; falls back to an inline gradient.
     */
    public static void applyMedia(Component media, String mediaClass, String gradient) {
        if (mediaClass != null && !mediaClass.isBlank()) {
            media.getElement().getClassList().add(mediaClass);
        } else if (gradient != null) {
            media.getElement().getStyle().set("background", gradient);
        }
    }
}
