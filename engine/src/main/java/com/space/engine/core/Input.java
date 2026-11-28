package com.space.engine.core;

import com.space.engine.core.actors.FreeCameraActor;
import com.space.engine.core.graphics.TerminalWindow;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


//todo: move to background thread
public final class Input {

    public static final int KEY_A = 97;
    public static final int KEY_B = 98;
    public static final int KEY_C = 99;
    public static final int KEY_D = 100;
    public static final int KEY_E = 101;
    public static final int KEY_F = 102;
    public static final int KEY_G = 103;
    public static final int KEY_H = 104;
    public static final int KEY_I = 105;
    public static final int KEY_J = 106;
    public static final int KEY_K = 107;
    public static final int KEY_L = 108;
    public static final int KEY_M = 109;
    public static final int KEY_N = 110;
    public static final int KEY_O = 111;
    public static final int KEY_P = 112;
    public static final int KEY_Q = 113;
    public static final int KEY_R = 114;
    public static final int KEY_S = 115;
    public static final int KEY_T = 116;
    public static final int KEY_U = 117;
    public static final int KEY_V = 118;
    public static final int KEY_W = 119;
    public static final int KEY_X = 120;
    public static final int KEY_Y = 121;
    public static final int KEY_Z = 122;

    private static final byte MAX_KEY_EVENTS_PER_FRAME = 25;

    private static final Set<Integer> keysDown = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> keysJustDown = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> keysJustUp = ConcurrentHashMap.newKeySet();
    private static final Queue<KittyEvent> kittyEventQueue = new ConcurrentLinkedQueue<>();

    private static final Thread poolingThread = new Thread(Input::pool);

    private static NonBlockingReader reader;

    public static boolean IsKeyDown(int keycode) {
        return keysDown.contains(keycode);
    }

    public static boolean IsKeyJustDown(int keycode) {
        return keysJustDown.contains(keycode);
    }

    public static boolean IsKeyJustUp(int keycode) {
        return keysJustDown.contains(keycode);
    }

    static void init() throws IOException, InterruptedException {
        var term = TerminalWindow.get().getTerminal();

        term.enterRawMode();

        reader = term.reader();

        term.writer().print("\033[>27u");
        term.writer().flush();

        poolingThread.start();
    }

    static void update() {
        //clear just arrays
        keysJustDown.clear();
        keysJustUp.clear();

        for (int i = 0; i < MAX_KEY_EVENTS_PER_FRAME && !kittyEventQueue.isEmpty(); i++) {
            KittyEvent event = kittyEventQueue.poll();

            if (event == null) break;

            if (event.eventType == 3) {
                if (keysDown.remove(event.keyCode)) {
                    keysJustUp.add(event.keyCode);
                }
            }
            if (event.eventType == 1) {
                boolean isInitialPress = keysDown.add(event.keyCode);

                if (isInitialPress) {
                    keysJustDown.add(event.keyCode);
                }
            }
        }
    }

    static void clean() {
        poolingThread.interrupt();
        try {
            reader.close();
        } catch (IOException e) {
            Debug.err(e.getMessage());
        }

        TerminalWindow.get().moveCursor(1, 1).resetColors().clear();
    }

    /**
     * !Runs on separate thread!
     */
    private static void pool() {

        final StringBuilder sb = new StringBuilder();

        while (Thread.currentThread().isAlive()) {
            try {

                int first = reader.read();

                sb.append((char) first);

                Thread.sleep(1); //waits 1ms to allow for kitty to write input events

                while (reader.available() > 0) {
                    sb.append((char) reader.read());
                }

                String[] sequences = sb.toString().split("(?=\033)");
                for (String seq : sequences) {
                    KittyEvent event = parseKittyEvent(seq);

                    if (event == null) continue;

                    kittyEventQueue.offer(event);
                }

                sb.setLength(0);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static KittyEvent parseKittyEvent(String seq) {
        if (!seq.startsWith("\033[") || !seq.endsWith("u")) return null;

        String body = seq.substring(2, seq.length() - 1); // strip \033[ and u

        // Format: key;mods:event OR key:event OR key
        String[] parts = body.split(":");

        int eventType = 1;
        if (parts.length > 1) {
            try {
                eventType = Integer.parseInt(parts[1].split(";")[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        if (eventType == 2) return null;

        String keyAndMods = parts[0];
        String[] keyDetails = keyAndMods.split(";");

        int keyCode;
        try {
            keyCode = Integer.parseInt(keyDetails[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        return new KittyEvent(keyCode, eventType);
    }

    private record KittyEvent(int keyCode, int eventType) {
    }
}
