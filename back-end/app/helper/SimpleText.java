package helper;

/**
 *
 *
 * @author fabiomazzone
 */
public class SimpleText {

    public final String text;

    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_1              =  1;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_2              =  2;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_3              =  3;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_4              =  4;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_SUCCESSFULL    =  5;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_SHEET_SUCCESSFULL  =  6;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_BACK           =  7;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_DUNGEON_FIRST_TRY  =  8;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_BELT               =  9;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_BELT_SUCCESSFULL   = 10;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_TASK               = 11;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_DUNGEON_SECOND_TRY = 12;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_POTION             = 13;
    public static final int SIMPLE_TEXT_PREREQUISITE_TUTORIAL_SUCCESSFULL        = 14;

    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_1  =  91;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_2  =  92;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_3  =  93;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_4  =  94;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_5  =  95;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_6  =  96;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_7  =  97;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_8  =  98;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_9  =  99;
    public static final int SIMPLE_TEXT_PREREQUISITE_BOSSMAP_10 = 100;

    public final int prerequisite;

    public final String sound_url;

    public final int lines;

    public SimpleText(String text, int prerequisite, int lines) {
        this(text, prerequisite, null, lines);
    }

    public SimpleText(String text, int prerequisite, String sound_url, int lines) {
        this.text = text;
        this.prerequisite = prerequisite;
        this.sound_url = sound_url;
        this.lines = lines;
    }
}
