package com.space.engine.core.graphics;

import com.github.stephengold.joltjni.StateRecorder;
import lombok.Getter;
import lombok.ToString;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TerminalWindow {

    public record Size(int width, int height) {
    }

    public static final String CODE_SHOW_CURSOR = "\033[?25h";
    public static final String CODE_HIDE_CURSOR = "\033[?25l";
    public static final String CODE_CLEAR = "\033[0J";
    public static final String CODE_RESET_COLORS = "\033[0m";

    private static volatile TerminalWindow instance;
    @Getter
    private final Terminal terminal;

    private TerminalWindow() throws IOException {
        terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .build();
    }

    public static void init() throws IOException {
        if (instance == null) {
            synchronized (TerminalWindow.class) {
                if (instance == null) {
                    instance = new TerminalWindow();
                } else {
                    throw new IllegalStateException("TerminalWindow already initialized");
                }
            }
        }
    }

    public static void shutdown() throws IOException {
        if (instance == null) return;

        synchronized (TerminalWindow.class) {
            if (instance == null) return;

            instance.showCursor()
                    .moveCursor(1, 1)
                    .clear()
                    .resetColors()
                    .terminal.close();
        }
    }

    public static TerminalWindow get() {
        if (instance == null) {
            throw new IllegalStateException("TerminalWindow not initialized");
        }

        return instance;
    }

    public Size size() {
        return new Size(
                terminal.getSize().getColumns(),
                terminal.getSize().getRows()
        );
    }

    public TerminalWindow showCursor() {
        check();

        terminal.writer().print(CODE_SHOW_CURSOR);
        terminal.writer().flush();

        return this;
    }

    public TerminalWindow hideCursor() {
        check();

        terminal.writer().print(CODE_HIDE_CURSOR);
        terminal.writer().flush();

        return this;
    }

    public TerminalWindow moveCursor(int x, int y) {
        check();

        terminal.writer().print("\033[" + y + ";" + x + "H");
        terminal.writer().flush();

        return this;
    }

    public TerminalWindow clear() {
        check();

        terminal.writer().print(CODE_CLEAR);
        terminal.writer().flush();

        return this;
    }

    public TerminalWindow resetColors() {
        check();

        terminal.writer().print(CODE_RESET_COLORS);
        terminal.writer().flush();

        return this;
    }

    private void check() {
        if (instance == null) {
            throw new IllegalStateException("TerminalWindow not initialized");
        }
        if (terminal == null) {
            throw new IllegalStateException("Terminal not initialized");
        }
    }
}
