package llc.redstone.hysentials.hook;

public interface FontRendererAcessor {

void setRandomStyle(boolean randomStyle);

    void setBoldStyle(boolean boldStyle);

    void setStrikethroughStyle(boolean strikethroughStyle);

    void setUnderlineStyle(boolean underlineStyle);

    void setItalicStyle(boolean italicStyle);

    void setColorCode(int[] colorCode);

    void setTextColor(int textColor);


    boolean isRandomStyle();

    boolean isBoldStyle();

    boolean isStrikethroughStyle();

    boolean isUnderlineStyle();

    boolean isItalicStyle();

    boolean isUnicodeFlag();

    int[] getColorCode();

    float red();

    float green();

    void setRed(float red);

    void setGreen(float green);

    void setBlue(float blue);

    float blue();

    void setAlpha(float alpha);

    float alpha();

    int getTextColor();

    float renderDefaultChar(int i, boolean shadow);

    float renderChar(char c, boolean shadow);

    void renderStringAtPosA(String text, boolean shadow);


}
