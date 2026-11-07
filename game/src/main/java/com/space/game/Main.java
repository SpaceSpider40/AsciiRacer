package com.space.game;

import com.space.engine.core.Launcher;

import javax.sound.sampled.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {

//        File wavFile = new File("assets/raw/music0_sample.wav");
//
//        try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile)) {
//            AudioFormat sourceFormat = audioInputStream.getFormat();
//
//            AudioFormat targetFormat = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    sourceFormat.getSampleRate() > 0 ? sourceFormat.getSampleRate() : 44100,
//                    16,
//                    sourceFormat.getChannels(),
//                    sourceFormat.getChannels() * (16/8),
//                    sourceFormat.getSampleRate() > 0 ? sourceFormat.getSampleRate() : 44100,
//                    false
//            );
//
//            try (AudioInputStream covertedStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream)) {
//                DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
//
//                if (!AudioSystem.isLineSupported(info)) {
//                    System.err.println("Ten format audio nie jest wspierany przez Twój system operacyjny.");
//                    return;
//                }
//
//                Clip clip = (Clip) AudioSystem.getLine(info);
//                clip.open(covertedStream);
//
//                System.out.println("Odtwarzanie dźwięku...");
//                clip.start();
//
//                // Blokada wątku, aby aplikacja konsolowa się nie wyłączyła w trakcie grania
//                Thread.sleep(clip.getMicrosecondLength() / 1000);
//            }
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }

        Launcher.launch(new Racer());
    }
}