package cc.woverflow.hysentials.util;/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.asm.FMLSanityChecker;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SharedDrawable;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;


public class SplashProgress {
    private static Drawable d;

    // Max amount of progress updates
    private static final int DEFAULT_MAX = 11;

    // Current progress
    private static int PROGRESS;

    // Currently displayed progress text
    private static String CURRENT = "";

    // Background texture
    private static ResourceLocation splash;
    private static boolean done = false;
    private static boolean introDone = false;
    private static Texture fontTexture;
    private static Texture logoTexture;
    private static Texture forgeTexture;
    private static Texture introTexture;
    private static Thread thread;
    private static Throwable threadError;
    private static int angle = 0;

    private static boolean freeze = false;

    private static SplashFontRenderer fontRenderer;

    static final Semaphore mutex = new Semaphore(1);


    /**
     * Update the splash text
     */
    public static void update() {
        drawSplash();
    }

    /**
     * Update the splash progress
     *
     * @param givenProgress Stage displayed on the splash
     * @param givenSplash   Text displayed on the splash
     */
    public static void setProgress(int givenProgress, String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    /**
     * Render the splash screen background
     */
    public static void drawSplash() {
        boolean defaultEnabled = !System.getProperty("os.name").toLowerCase().contains("mac");

        if (!defaultEnabled) return;
        // Get the users screen width and height to apply
        try {
            d = new SharedDrawable(Display.getDrawable());
            Display.getDrawable().releaseContext();
            d.makeCurrent();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        //Call this ASAP if splash is enabled so that threading doesn't cause issues later
        getMaxTextureSize();

        thread = new Thread(new Runnable() {
            private final int barWidth = 400;
            private final int barHeight = 20;
            private final int textHeight2 = 20;
            private int actualPercent = 0;

            @Override
            public void run() {
                setGL();
                final ResourceLocation fontLoc = new ResourceLocation("textures/font/ascii.png");
                final ResourceLocation logoLoc = new ResourceLocation("textures/BWSLogo.png");
                final ResourceLocation forgeLoc = new ResourceLocation("textures/BWSLoading.gif");

                fontTexture = new Texture(fontLoc);
                logoTexture = new Texture(logoLoc);
                forgeTexture = new Texture(forgeLoc);

                glEnable(GL_TEXTURE_2D);
                fontRenderer = new SplashFontRenderer();
                glDisable(GL_TEXTURE_2D);

//                glClearColor((float) ((0x000000 >> 16) & 0xFF) / 0xFF, (float) ((0x000000 >> 8) & 0xFF) / 0xFF, (float) (0x000000 & 0xFF) / 0xFF, 1);
//                new Thread(() -> play("Hysentials/intro.wav")).start();
//                angle = 4;
//                while (!introDone) {
//                    glClear(GL_COLOR_BUFFER_BIT);
//
//                    // matrix setup
//                    int w = Display.getWidth();
//                    int h = Display.getHeight();
//                    int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
//                    glViewport(0, 0, w, h);
//                    glMatrixMode(GL_PROJECTION);
//                    glLoadIdentity();
//                    glOrtho(320 - w / 2, 320 + w / 2, 240 + h / 2, 240 - h / 2, -1, 1);
//                    glMatrixMode(GL_MODELVIEW);
//                    glLoadIdentity();
//
//                    if (!freeze) {
//                        angle += 1;
//                    }
//
//                    // forge logo
//                    setColor(0xFFFFFF);
//                    float fw = (introTexture.getWidth() * 1.5f) / scaleFactor;
//                    float fh = (introTexture.getHeight() * 1.5f) / scaleFactor;
//                    System.out.println("Display w: " + w + " h: " + h + "\nScale factor: " + scaleFactor + "\nTexture w: " + fw + " h: " + fh);
//                    glTranslatef(w / 2 - fw/(2*scaleFactor), h / 2, 0);
//                    //x:267.0 y:270.0
//                    int f = (angle / 2 / 2) % introTexture.getFrames();
//                    if (f == 0) {
//                        freeze = true;
//                        introDone = true;
//                    }
//                    glEnable(GL_TEXTURE_2D);
//                    introTexture.bind();
//                    glBegin(GL_QUADS);
//                    introTexture.texCoord(f, 0, 0);
//                    glVertex2f(-fw, -fh);
//                    introTexture.texCoord(f, 0, 1);
//                    glVertex2f(-fw, fh);
//                    introTexture.texCoord(f, 1, 1);
//                    glVertex2f(fw, fh);
//                    introTexture.texCoord(f, 1, 0);
//                    glVertex2f(fw, -fh);
//                    glEnd();
//                    glDisable(GL_TEXTURE_2D);
//
//
//                    mutex.acquireUninterruptibly();
//                    Display.update();
//                    // As soon as we're done, we release the mutex. The other thread can now ping the processmessages
//                    // call as often as it wants until we get get back here again
//                    mutex.release();
//                    Display.sync(100);
//                }
                angle = 0;
                glClearColor((float) ((0xFFFFFF >> 16) & 0xFF) / 0xFF, (float) ((0xFFFFFF >> 8) & 0xFF) / 0xFF, (float) (0xFFFFFF & 0xFF) / 0xFF, 1);
                while (!done) {
                    ProgressManager.ProgressBar first = null, penult = null, last = null;
                    Iterator<ProgressManager.ProgressBar> i = ProgressManager.barIterator();
                    float percentComplete = 0;
                    while (i.hasNext()) {
                        if (first == null) {
                            first = i.next();
                            if ((actualPercent < first.getStep() * 100 / first.getSteps())) {
                                percentComplete = first.getStep() * 100 / first.getSteps();
                                if (Math.round(percentComplete) == actualPercent && actualPercent < (first.getStep() + 1) * 100 / first.getSteps()) {
                                    percentComplete += 1;
                                }
                            }

                        } else {
                            penult = last;
                            last = i.next();
                            if (actualPercent < (actualPercent + (last.getStep() * 100 / last.getSteps()) / 14.285)) {
                                percentComplete += (last.getStep() * 100 / last.getSteps()) / 14.285;
                            }
                        }
                        if (actualPercent < percentComplete) {
                            actualPercent = (int) percentComplete;
                        }
                    }

                    glClear(GL_COLOR_BUFFER_BIT);

                    // matrix setup
                    int w = Display.getWidth();
                    int h = Display.getHeight();
                    float scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
                    glViewport(0, 0, w, h);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    glOrtho(320 - w / 2, 320 + w / 2, 240 + h / 2, 240 - h / 2, -1, 1);
                    glMatrixMode(GL_MODELVIEW);
                    glLoadIdentity();
                    //854 Height : 480
                    // mojang logo
                    setColor(0xFFFFFF);
                    glTranslatef(0, -50 * scale, 0);
                    glEnable(GL_TEXTURE_2D);
                    logoTexture.bind();
                    glBegin(GL_QUADS);
                    logoTexture.texCoord(0, 0, 0);
                    glVertex2f(320 - 256, 240 - 256);
                    logoTexture.texCoord(0, 0, 1);
                    glVertex2f(320 - 256, 240 + 256);
                    logoTexture.texCoord(0, 1, 1);
                    glVertex2f(320 + 256, 240 + 256);
                    logoTexture.texCoord(0, 1, 0);
                    glVertex2f(320 + 256, 240 - 256);
                    glEnd();
                    glDisable(GL_TEXTURE_2D);

                    //bars
                    if (first != null) {
                        glPushMatrix();
                        glTranslatef(320 - (float) barWidth / 2, 360, 0);
                        drawBar(first);
                    }

                    angle += 1;
                    // forge logo
                    setColor(0xFFFFFF);
                    float fw = (float) forgeTexture.getWidth() / 2 / 1.5f;
                    float fh = (float) forgeTexture.getHeight() / 2 / 1.5f;

                    glTranslatef(0, h/2, 0);

                    int f = (angle / 2) % forgeTexture.getFrames();
                    glEnable(GL_TEXTURE_2D);
                    forgeTexture.bind();
                    glBegin(GL_QUADS);
                    forgeTexture.texCoord(f, 0, 0);
                    glVertex2f(320 - fw, 240 - fh);
                    forgeTexture.texCoord(f, 0, 1);
                    glVertex2f(320 - fw, 240 + fh);
                    forgeTexture.texCoord(f, 1, 1);
                    glVertex2f(320 + fw, 240 + fh);
                    forgeTexture.texCoord(f, 1, 0);
                    glVertex2f(320 + fw, 240 - fh);
                    glEnd();
                    glDisable(GL_TEXTURE_2D);

                    mutex.acquireUninterruptibly();
                    Display.update();
                    // As soon as we're done, we release the mutex. The other thread can now ping the processmessages
                    // call as often as it wants until we get get back here again
                    mutex.release();
                    Display.sync(100);
                }
                clearGL();
            }

            private void clearGL() {
                Minecraft mc = Minecraft.getMinecraft();
                mc.displayWidth = Display.getWidth();
                mc.displayHeight = Display.getHeight();
                mc.resize(mc.displayWidth, mc.displayHeight);
                glClearColor(1, 1, 1, 1);
                glEnable(GL_DEPTH_TEST);
                glDepthFunc(GL_LEQUAL);
                glEnable(GL_ALPHA_TEST);
                glAlphaFunc(GL_GREATER, .1f);
                try {
                    Display.getDrawable().releaseContext();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

            private void setColor(int color) {
                glColor3ub((byte) ((color >> 16) & 0xFF), (byte) ((color >> 8) & 0xFF), (byte) (color & 0xFF));
            }

            private void drawBox(int w, int h) {
                glBegin(GL_QUADS);
                glVertex2f(0, 0);
                glVertex2f(0, h);
                glVertex2f(w, h);
                glVertex2f(w, 0);
                glEnd();
            }

            private void drawBar(ProgressManager.ProgressBar b) {
                glPushMatrix();
                // border
                glPushMatrix();
                glTranslatef(0, textHeight2, 0);
                setColor(0xC0C0C0);
                drawBox(barWidth, barHeight + 2);

                // slidy part
                glTranslatef(2, 2, 0);
                setColor(0x1E1E1E);
                drawBox((barWidth - 4) * (b.getStep() + 1) / (b.getSteps() + 1), barHeight - 2); // Step can sometimes be 0.
                // progress text
                String progress = actualPercent + "%";
                glTranslatef(((float) barWidth - 2) / 2 - fontRenderer.getStringWidth(progress), 40, 0);
                setColor(0x000000);
                glScalef(2, 2, 1);
                glEnable(GL_TEXTURE_2D);
                fontRenderer.drawString(progress, 0, 0, 0x000000);
                glPopMatrix();
            }

            private void setGL() {
                try {
                    Display.getDrawable().makeCurrent();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                glClearColor((float) ((0xFFFFFF >> 16) & 0xFF) / 0xFF, (float) ((0xFFFFFF >> 8) & 0xFF) / 0xFF, (float) (0xFFFFFF & 0xFF) / 0xFF, 1);
                glDisable(GL_LIGHTING);
                glDisable(GL_DEPTH_TEST);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
        });
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                FMLLog.log(Level.ERROR, e, "Splash thread Exception");
                threadError = e;
                done = true;
                introDone = true;
            }
        });
        thread.start();

        checkThreadState();
    }

    private static void checkThreadState() {
        if (thread.getState() == Thread.State.TERMINATED || threadError != null) {
            throw new IllegalStateException("Splash thread", threadError);
        }
    }

    public static void finish() {
        try {
            System.out.println("Finishing splash");
            done = true;
            introDone = true;
            checkThreadState();
            done = true;
            introDone = true;
            thread.join();
            d.releaseContext();
            Display.getDrawable().makeCurrent();
            fontTexture.delete();
            logoTexture.delete();
            forgeTexture.delete();
            introTexture.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final IntBuffer buf = BufferUtils.createIntBuffer(50 * 1024 * 1024);
    private static final ArrayList<String> lines = new ArrayList<>();
    private static final IResourcePack mcPack = Minecraft.getMinecraft().mcDefaultResourcePack;
    private static final IResourcePack fmlPack = createResourcePack(FMLSanityChecker.fmlLocation);

    private static class Texture {
        private final ResourceLocation location;
        private final int name;
        private final int width;
        private final int height;
        private final int frames;
        private final int size;

        public Texture(ResourceLocation location) {
            InputStream s = null;
            try {
                this.location = location;
                s = open(location);
                ImageInputStream stream = ImageIO.createImageInputStream(s);
                Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
                if (!readers.hasNext()) throw new IOException("No suitable reader found for image" + location);
                ImageReader reader = null;
                while (readers.hasNext()) reader = readers.next();
                reader.setInput(stream);
                frames = reader.getNumImages(true);
                BufferedImage[] images = new BufferedImage[frames];
                try {
                    for (int i = 0; i < frames; i++) {
                        images[i] = reader.read(i);
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                reader.dispose();
                int size = 1;
                width = images[0].getWidth();
                height = images[0].getHeight();
                while ((size / width) * (size / height) < frames) size *= 2;
                this.size = size;
                glEnable(GL_TEXTURE_2D);
                synchronized (SplashProgress.class) {
                    name = glGenTextures();
                    glBindTexture(GL_TEXTURE_2D, name);
                }
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer) null);
                checkGLError("Texture creation");
                for (int i = 0; i * (size / width) < frames; i++) {
                    for (int j = 0; i * (size / width) + j < frames && j < size / width; j++) {
                        ((Buffer) buf).clear();
                        BufferedImage image = images[i * (size / width) + j];
                        for (int k = 0; k < height; k++) {
                            for (int l = 0; l < width; l++) {
                                try {
                                    buf.put(image.getRGB(l, k));
                                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                                    buf.put(0);
                                }
                            }
                        }
                        ((Buffer) buf).position(0).limit(width * height);
                        glTexSubImage2D(GL_TEXTURE_2D, 0, j * width, i * height, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buf);
                        checkGLError("Texture uploading");
                    }
                }
                glBindTexture(GL_TEXTURE_2D, 0);
                glDisable(GL_TEXTURE_2D);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(s);
            }
        }

        public ResourceLocation getLocation() {
            return location;
        }

        public int getName() {
            return name;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getFrames() {
            return frames;
        }

        public int getSize() {
            return size;
        }

        public void bind() {
            glBindTexture(GL_TEXTURE_2D, name);
        }

        public void delete() {
            glDeleteTextures(name);
        }

        public float getU(int frame, float u) {
            return width * (frame % (size / width) + u) / size;
        }

        public float getV(int frame, float v) {
            return height * (frame / (size / width) + v) / size;
        }

        public void texCoord(int frame, float u, float v) {
            glTexCoord2f(getU(frame, u), getV(frame, v));
        }
    }

    private static class SplashFontRenderer extends FontRenderer {
        public SplashFontRenderer() {
            super(Minecraft.getMinecraft().gameSettings, fontTexture.getLocation(), null, false);
            super.onResourceManagerReload(null);
        }

        @Override
        protected void bindTexture(ResourceLocation location) {
            if (location != locationFontTexture) throw new IllegalArgumentException();
            fontTexture.bind();
        }

        @Override
        protected InputStream getResourceInputStream(ResourceLocation location) throws IOException {
            return Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(location);
        }
    }

    private static IResourcePack createResourcePack(File file) {
        if (file.isDirectory()) {
            return new FolderResourcePack(file);
        } else {
            return new FileResourcePack(file);
        }
    }

    private static InputStream open(ResourceLocation loc) throws IOException {
        if (fmlPack.resourceExists(loc)) {
            return fmlPack.getInputStream(loc);
        }
        return mcPack.getInputStream(loc);
    }

    private static int max_texture_size = -1;

    public static int getMaxTextureSize() {
        if (max_texture_size != -1) return max_texture_size;
        for (int i = 0x4000; i > 0; i >>= 1) {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            if (GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) != 0) {
                max_texture_size = i;
                return i;
            }
        }
        return -1;
    }

    public static void checkGLError(String where) {
        int err = GL11.glGetError();
        if (err != 0) {
            throw new IllegalStateException(where + ": " + GLU.gluErrorString(err) + " (" + err + ")");
        }
    }

    public static void play(String filePath) {
        final File file = new File(filePath);

        try {
            final AudioInputStream in = getAudioInputStream(file);
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line =
                         (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    AudioInputStream inputMystream = getAudioInputStream(outFormat, in);
                    stream(inputMystream, line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException
                 | LineUnavailableException
                 | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private static void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}
