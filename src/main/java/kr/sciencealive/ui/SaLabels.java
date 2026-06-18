package kr.sciencealive.ui;

/** Shared label/colour mappings for PR Award divisions and awards (mirrors data.js). */
public final class SaLabels {

    private SaLabels() {
    }

    public static String division(String code) {
        if ("basic".equals(code)) return "Basic Science";
        if ("applied".equals(code)) return "Applied Science";
        return code;
    }

    /** Tone for a division tag: basic = blue, applied = green. */
    public static String divisionTone(String code) {
        return code == null ? "neutral" : code;
    }

    /** Korean award label, or null for a non-winning submission. */
    public static String awardLabel(String code) {
        if ("gold".equals(code)) return "대상";
        if ("silver".equals(code)) return "최우수상";
        if ("bronze".equals(code)) return "우수상";
        return null;
    }

    /** Tone for an award tag: 대상 = gold, others = neutral. */
    public static String awardTone(String code) {
        return "gold".equals(code) ? "gold" : "neutral";
    }
}
