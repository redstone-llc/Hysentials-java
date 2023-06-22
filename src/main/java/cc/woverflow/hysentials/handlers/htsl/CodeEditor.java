package cc.woverflow.hysentials.handlers.htsl;

import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.util.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CodeEditor extends GuiScreen {
    private String[] guiText;
    private int cursorLine = 0;
    private int cursorPos = 0;
    private int cursorBlink = 0;
    private String fileNameSave;
    private int lineLimit = (int) Math.floor((ResolutionUtil.current().getScaledHeight() - 7) / 20);
    private int startIndex = 0;

    public CodeEditor() {
//        gui.registerDraw(this::guiRender);
//        gui.registerKeyTyped(this::guiTextRegister);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Renderer.drawRect(Renderer.color(30, 30, 30, 200),
            Renderer.screen.getWidth() / 4 / 2,
            Renderer.screen.getHeight() / 4 / 2,
            (float) (Renderer.screen.getWidth() / 2 * 1.5),
            (float) (Renderer.screen.getHeight() / 2 * 1.5));

        lineLimit = (int) Math.floor((Renderer.screen.getHeight() - 7) / 20 * 1.5f) + 1;
        for (int i = startIndex; i < lineLimit + startIndex && i < guiText.length; i++) {
            if (guiText[i].length() >= 0) {
                if (guiText[i].startsWith("//")) {
                    Renderer.drawString("&7" + (i + 1) + "| &2" + guiText[i],
                        Renderer.screen.getWidth() / 4 / 2 + 7,
                        Renderer.screen.getHeight() / 4 / 2 + (i - startIndex) * 10 + 7);
                } else {
                    Renderer.drawString("&7" + (i + 1) + "| &f" + guiText[i],
                        Renderer.screen.getWidth() / 4 / 2 + 7,
                        Renderer.screen.getHeight() / 4 / 2 + (i - startIndex) * 10 + 7);
                }
            }
        }

        if (cursorBlink < 100) {
            cursorBlink = cursorBlink + 1;
        } else {
            cursorBlink = 0;
        }

        if (cursorBlink >= 50.0 && cursorBlink < 100.0) {
            Renderer.drawRect(Renderer.color(200, 200, 200, 200),
                getCursorPos(cursorLine) + Renderer.screen.getWidth() / 4 / 2 + 7,
                Renderer.screen.getHeight() / 4 / 2 + (cursorLine) * 10 + 7,
                5,
                10);
        }
    }

    public int getCursorPos(int i) {
        String line = guiText[cursorLine];
        if (cursorPos > guiText[cursorLine].length()) {
            cursorPos = guiText[cursorLine].length();
        }
        return fontRendererObj.getStringWidth(line.substring(0, cursorPos)) + fontRendererObj.getStringWidth(i + ": ");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 28) {
            String[] newGuiText = new String[guiText.length + 1];
            System.arraycopy(guiText, 0, newGuiText, 0, cursorLine + startIndex + 1);
            newGuiText[cursorLine + startIndex + 1] = "";
            System.arraycopy(guiText, cursorLine + startIndex + 1, newGuiText, cursorLine + startIndex + 2, guiText.length - cursorLine - startIndex - 1);
            guiText = newGuiText;

            if (startIndex + lineLimit < guiText.length && cursorLine + 1 == lineLimit) {
                startIndex = startIndex + 1;
            } else if (cursorLine + 1 != lineLimit) {
                cursorLine = cursorLine + 1;
            }
            cursorPos = 0;
            return;
        }

        if (keyCode == 14) {
            if (guiText[cursorLine + startIndex].length() <= 0 && guiText.length > 1) {
                if (startIndex > 0) {
                    startIndex = startIndex - 1;
                } else if (cursorLine < lineLimit && cursorLine > 0) {
                    cursorLine = cursorLine - 1;
                }

                String[] newGuiText = new String[guiText.length - 1];
                System.arraycopy(guiText, 0, newGuiText, 0, cursorLine + startIndex + 1);
                System.arraycopy(guiText, cursorLine + startIndex + 2, newGuiText, cursorLine + startIndex + 1, guiText.length - cursorLine - startIndex - 2);
                cursorPos = guiText[cursorLine + startIndex].length();
                guiText = newGuiText;
                return;
            }

            if (guiText[cursorLine + startIndex].length() <= 0) {
                return;
            }

            //use cursor position
            guiText[cursorLine + startIndex] = guiText[cursorLine + startIndex].substring(0, cursorPos - 1) + guiText[cursorLine + startIndex].substring(cursorPos);

            if (cursorPos > guiText[cursorLine + startIndex].length()) {
                cursorPos = guiText[cursorLine + startIndex].length();
            }
            return;
        }

        if (keyCode == 1) {
            File file = new File("./config/hysentials/htsl/" + fileNameSave + ".htsl");
            try {
                FileUtils.write(file, String.join("\n", guiText), "UTF-8");
                MUtils.chat("&3[HTSL] &fSaved text to " + fileNameSave + ".htsl");
                Minecraft.getMinecraft().thePlayer.closeScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        int[] ignoredKeyCodes = {42, 29, 54, 157, 184, 56, 219, 15, 58, 211, 207, 209, 201, 199, 210, 197, 70, 183, 88, 87, 68, 67, 66, 65, 64, 63, 62, 61, 60, 59};

        for (int ignoredKeyCode : ignoredKeyCodes) {
            if (keyCode == ignoredKeyCode) {
                return;
            }
        }

        if (keyCode == 200) {
            if (cursorLine > 0) {
                cursorLine = cursorLine - 1;
            } else if (startIndex > 0) {
                startIndex = startIndex - 1;
            }
            return;
        }

        if (keyCode == 203) {
            if (cursorPos > 0) {
                cursorPos = cursorPos - 1;
            } else if (cursorLine > 0) {
                cursorLine = cursorLine - 1;
                cursorPos = guiText[cursorLine + startIndex].length();
            } else if (startIndex > 0) {
                startIndex = startIndex - 1;
                cursorLine = lineLimit - 1;
                cursorPos = guiText[cursorLine + startIndex].length();
            }
            return;
        }

        if (keyCode == 205) {
            if (cursorPos < guiText[cursorLine + startIndex].length()) {
                cursorPos = cursorPos + 1;
            } else if (cursorLine + 1 < lineLimit) {
                cursorLine = cursorLine + 1;
                cursorPos = 0;
            } else if (cursorLine + startIndex + 1 < guiText.length) {
                startIndex = startIndex + 1;
                cursorLine = 0;
                cursorPos = 0;
            }
            return;
        }

        if (keyCode == 208) {
            if (cursorLine + 1 < lineLimit && cursorLine + startIndex + 1 < guiText.length) {
                cursorLine = cursorLine + 1;
            } else if (cursorLine + startIndex + 1 < guiText.length) {
                startIndex = startIndex + 1;
            }
            return;
        }

        if (cursorPos > guiText[cursorLine + startIndex].length()) {
            cursorPos = guiText[cursorLine + startIndex].length();
        }
        guiText[cursorLine + startIndex] = guiText[cursorLine + startIndex].substring(0, cursorPos) + typedChar + guiText[cursorLine + startIndex].substring(cursorPos);
        cursorPos += 1;
    }

    public void openGui(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "default";
        }
        File file = new File("./config/hysentials/htsl/" + fileName + ".htsl");
        File file2 = new File("./config/hysentials/htsl" + fileName + ".txt");
        if (!(file.exists() ||
            file2.exists())) {
            MUtils.chat("&3[HTSL] &cCouldn't find the file \"" + fileName + "\", please make sure it exists!");
            return;
        }

        if (!file.exists()) {
            MUtils.chat("&3[HTSL] &fCreated new file \"" + fileName + ".htsl\"");
            try {
                FileUtils.write(file, "", "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            MUtils.chat("&3[HTSL] &fLoading " + fileName + ".htsl . . .");
        }

        this.fileNameSave = fileName;

        String fileContent;

        if (file.exists()) {
            try {
                fileContent = FileUtils.readFileToString(file, "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (file2.exists()) {
            try {
                fileContent = FileUtils.readFileToString(file2, "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MUtils.chat("&3[HTSL] &eThe .txt file extension won't be supported in future updates. Please change your file extensions to be .htsl");
        } else {
            MUtils.chat("&3[HTSL] &cCouldn't find the file \"" + fileName + "\", please make sure it exists!");
            return;
        }

        guiText = fileContent.split("\n");
        lineLimit = (int) Math.floor((Renderer.screen.getHeight() - 7) / 20);

        if (guiText.length > lineLimit) {
            cursorLine = lineLimit - 1;
            startIndex = guiText.length - lineLimit;
        } else {
            cursorLine = guiText.length - 1;
        }

        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(this);
    }
}
