package llc.redstone.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import llc.redstone.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.client.Minecraft.getMinecraft;

public class HousingScoreboard {
    Field footerField;
    List<String> fonts = Arrays.asList(
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()-_+={}][|\\`,./?;:'\"<> ",
        "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ０１２３４５６７８９~！＠＃＄％^＆＊（）－_＋＝{}][|\\`，．／？；：＇\"<> ",
        "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ⓪①②③④⑤⑥⑦⑧⑨~!@#$%^&⊛()⊖_⊕⊜{}][⦶⦸`,⨀⊘?;:'\"⧀⧁ ",
        "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()-_+={}][|\\`,./?;:'\"<> ",
        "ᵃᵇᶜᵈᵉᶠᵍʰᶦʲᵏˡᵐⁿᵒᵖᵠʳˢᵗᵘᵛʷˣʸᶻᴬᴮᶜᴰᴱᶠᴳᴴᴵᴶᴷᴸᴹᴺᴼᴾᵠᴿˢᵀᵁⱽᵂˣʸᶻ⁰¹²³⁴⁵⁶⁷⁸⁹~ᵎ@#$%^&*⁽⁾⁻_⁺⁼{}][|\\`,./ˀ;:'\"<> ",
        "ɐqɔpǝɟɓɥıɾʞlɯuodbɹsʇnʌʍxʎz∀ᙠƆᗡƎℲ⅁HIſ⋊˥WNOԀΌᴚS⊥∩ΛMX⅄Z0⇂ᄅƐㄣގ9ㄥ86~¡@#$%ˇ⅋*)(-¯+=}{[]|\\̖ '˙/¿؛:,„>< "
    );

    public HousingScoreboard() {
        {
            try {
                footerField = ReflectionHelper.findField(GuiPlayerTabOverlay.class, "footer", "field_175255_h");
                footerField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public String getHousingName() {
        if (getMinecraft().ingameGUI == null || getMinecraft().ingameGUI.getTabList() == null) return null;
        if (!HypixelUtils.INSTANCE.isHypixel()) return null;
        try {
            GuiPlayerTabOverlay tab = getMinecraft().ingameGUI.getTabList();
            String footer = ((IChatComponent) footerField.get(tab)).getFormattedText();
            footer = footer.replaceAll("§r", "");
            String[] split = footer.split("\n");
            if (split.length < 2) return null;
            if (split[1].startsWith("§fYou are in ")) {
                Pattern pattern = Pattern.compile("(§fYou are in )(.+)(§f, by)");
                Matcher matcher = pattern.matcher(split[1]);
                if (matcher.find()) {
                    return matcher.group(2);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public String getHousingCreator() {
        if (getMinecraft().ingameGUI == null || getMinecraft().ingameGUI.getTabList() == null) return null;
        if (!HypixelUtils.INSTANCE.isHypixel()) return null;
        try {
            GuiPlayerTabOverlay tab = getMinecraft().ingameGUI.getTabList();
            String footer = ((IChatComponent) footerField.get(tab)).getFormattedText();
            footer = footer.replaceAll("§r", "");
            String[] split = footer.split("\n");
            if (split.length < 2) return null;
            if (split[1].startsWith("§fYou are in ")) {
                Pattern pattern = Pattern.compile("(§fYou are in )(.+)(§f, by)(.+)");
                Matcher matcher = pattern.matcher(split[1]);
                if (matcher.find()) {
                    return removePrefix(C.removeColor(matcher.group(4)));
                }
            }
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
        return null;
    }

    public String removeFormatting(String text) {
        String newText = "";
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            char newCharacter = character;
            for (String font : fonts) {
                if (font.indexOf(character) != -1) {
                    newCharacter = fonts.get(0).charAt(font.indexOf(character));
                    break;
                }
            }
            newText += newCharacter;
        }
        return C.removeColor(newText);
    }

    public String removePrefix(String text) {
        Pattern pattern = Pattern.compile("\\[[A-Za-z§0-9+]+] ");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return text.substring(matcher.end());
        }
        return text;
    }

}
