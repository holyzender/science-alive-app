package kr.sciencealive.ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import kr.sciencealive.entity.Submission;

/** Shared press-release detail dialog, used by the home teaser and the PR Award gallery. */
public final class SubmissionDialog {

    private SubmissionDialog() {
    }

    public static void open(Submission s, Integer year) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(s.getTitle());
        dialog.setWidth("680px");

        Div content = new Div();

        Div media = new Div();
        media.getStyle().set("height", "220px").set("border-radius", "var(--radius-md)").set("margin-bottom", "20px");
        media.getElement().getStyle().set("overflow", "hidden");
        SaUi.applyMedia(media, s.getMediaClass(), s.getImageGradient());

        Div tags = new Div();
        tags.getStyle().set("display", "flex").set("gap", "6px").set("margin-bottom", "16px").set("flex-wrap", "wrap");
        tags.add(SaUi.tag(SaLabels.division(s.getDivision()), SaLabels.divisionTone(s.getDivision())));
        tags.add(SaUi.tag("제" + s.getEditionNo() + "회 · " + (year != null ? year : ""), "neutral"));
        if (s.getAward() != null) {
            tags.add(SaUi.tag(SaLabels.awardLabel(s.getAward()), SaLabels.awardTone(s.getAward()), "award"));
        } else {
            tags.add(SaUi.tag("비수상작", "neutral"));
        }

        Div orgRow = new Div();
        orgRow.getStyle().set("display", "flex").set("justify-content", "space-between")
                .set("align-items", "center").set("margin-bottom", "16px");
        Span org = new Span(s.getOrg());
        org.getStyle().set("font-size", "var(--text-md)").set("font-weight", "600");
        Span views = new Span();
        views.getStyle().set("display", "flex").set("align-items", "center").set("gap", "5px")
                .set("color", "var(--color-contrast-medium)").set("font-size", "var(--text-sm)");
        views.add(SaIcons.icon("eye", 15),
                new Span(String.format("%,d", s.getViews()) + " 조회 · 접수번호 " + s.getCode()));
        orgRow.add(org, views);

        Hr divider = new Hr();
        divider.addClassName("sa-divider");

        Paragraph desc = new Paragraph("본 보도자료는 " + s.getOrg()
                + "이(가) 제출하여 사이언스얼라이브 PR어워드 심사를 거쳐 공개되었습니다. 연구의 배경과 의의, 실험 방법, 기대 효과를 일반 대중이 이해하기 쉽게 정리한 과학 커뮤니케이션 자료입니다.");
        desc.getStyle().set("margin-top", "16px").set("color", "var(--color-contrast-high)").set("line-height", "1.7");

        content.add(media, tags, orgRow, divider, desc);
        dialog.add(content);

        NativeButton photos = SaUi.button("사진 6", "image", "sa-btn--secondary", false);
        photos.addClassName("sa-btn--sm");
        NativeButton download = SaUi.button("보도자료 다운로드", "download", "sa-btn--primary", false);
        download.addClassName("sa-btn--sm");
        dialog.getFooter().add(photos, download);

        dialog.open();
    }
}
