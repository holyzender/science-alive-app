package kr.sciencealive.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

import java.util.Map;

/**
 * PDS-style geometric icon set (24×24, 1.6px stroke, round caps) ported from
 * the Science Alive design system's curated SVG subset. Rendered as inline SVG
 * via the element's innerHTML so the browser parses the SVG namespace natively
 * (preserves viewBox) and the stroke inherits {@code currentColor}.
 */
public final class SaIcons {

    private SaIcons() {
    }

    public static final Map<String, String> PATHS = Map.ofEntries(
            Map.entry("arrow-right", "M5 12h14M13 6l6 6-6 6"),
            Map.entry("arrow-left", "M19 12H5M11 6l-6 6 6 6"),
            Map.entry("chevron-right", "M9 6l6 6-6 6"),
            Map.entry("chevron-down", "M6 9l6 6 6-6"),
            Map.entry("close", "M6 6l12 12M18 6 6 18"),
            Map.entry("plus", "M12 5v14M5 12h14"),
            Map.entry("check", "M5 13l4 4L19 7"),
            Map.entry("search", "M11 19a8 8 0 1 1 0-16 8 8 0 0 1 0 16zM21 21l-4.3-4.3"),
            Map.entry("filter", "M3 5h18M6 12h12M10 19h4"),
            Map.entry("user", "M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM5 20a7 7 0 0 1 14 0"),
            Map.entry("calendar", "M7 3v3M17 3v3M4 8h16M5 6h14v14H5zM4 11h16"),
            Map.entry("map-pin", "M12 21s7-5.4 7-11a7 7 0 1 0-14 0c0 5.6 7 11 7 11zM12 12a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5z"),
            Map.entry("clock", "M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18zM12 7v5l3 2"),
            Map.entry("download", "M12 4v11M7 11l5 5 5-5M5 20h14"),
            Map.entry("upload", "M12 20V9M7 9l5-5 5 5M5 20h14"),
            Map.entry("file-text", "M7 3h7l5 5v13H7zM14 3v5h5M10 13h6M10 17h6"),
            Map.entry("award", "M12 14a5 5 0 1 0 0-10 5 5 0 0 0 0 10zM9 13l-1.5 8L12 19l4.5 2L15 13"),
            Map.entry("mail", "M4 6h16v12H4zM4 7l8 6 8-6"),
            Map.entry("info", "M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18zM12 11v5M12 8h.01"),
            Map.entry("warning", "M12 4l9 16H3zM12 10v4M12 17h.01"),
            Map.entry("check-circle", "M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18zM8.5 12l2.5 2.5L16 9"),
            Map.entry("eye", "M2 12s4-7 10-7 10 7 10 7-4 7-10 7-10-7-10-7zM12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"),
            Map.entry("link", "M9 15l6-6M10 6l1-1a4 4 0 0 1 6 6l-1 1M14 18l-1 1a4 4 0 0 1-6-6l1-1"),
            Map.entry("image", "M4 5h16v14H4zM4 16l4-4 3 3 4-4 5 5M9 10a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"),
            Map.entry("video", "M4 6h11v12H4zM15 10l5-3v10l-5-3z"),
            Map.entry("microscope", "M9 4l4 4-3 3-4-4zM10 12l-4 4M4 20h10M12 20a6 6 0 0 0 6-6"),
            Map.entry("flask", "M9 3h6M10 3v6l-5 9a2 2 0 0 0 2 3h10a2 2 0 0 0 2-3l-5-9V3M7.5 15h9"),
            Map.entry("atom", "M12 13a1 1 0 1 0 0-2 1 1 0 0 0 0 2zM12 12c5-3 9-3 9 0s-4 3-9 0-9-3-9 0 4 3 9 0zM12 12c3 5 3 9 0 9s-3-4 0-9 3-9 0-9-3 4 0 9z")
    );

    /** Icon inheriting {@code currentColor}, sized in px. */
    public static Component icon(String name, int size) {
        String d = PATHS.getOrDefault(name, "");
        String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + size + "\" height=\"" + size
                + "\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"1.6\""
                + " stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"" + d + "\"/></svg>";
        Span span = new Span();
        span.getElement().setProperty("innerHTML", svg);
        span.getElement().getStyle()
                .set("display", "inline-flex")
                .set("align-items", "center")
                .set("line-height", "0")
                .set("flex", "0 0 auto");
        return span;
    }
}
