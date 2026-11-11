package pt.iscte.poo.AudioPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip audioClip;

    public void load(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(filePath);
        AudioInputStream originalStream = AudioSystem.getAudioInputStream(audioFile);

        // Target format (16-bit, stereo, little-endian)
        AudioFormat originalFormat = originalStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                originalFormat.getSampleRate(),
                16, // 16-bit depth
                originalFormat.getChannels(),
                originalFormat.getChannels() * 2, // Frame size (2 bytes per channel)
                originalFormat.getSampleRate(),
                false // little-endian
        );

        // Convert audio to target format
        AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);

        audioClip = AudioSystem.getClip();
        audioClip.open(convertedStream);

        // Close streams to free resources
        originalStream.close();
        convertedStream.close();
    }

    public void play() {
        if (audioClip != null) {
            audioClip.start();
        }
    }

    public void stop() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setMicrosecondPosition(0);
        }
    }

    public void close() {
        if (audioClip != null) {
            audioClip.close();
        }
    }

    public void loop(boolean continuous) {
        if (audioClip != null) {
            if (continuous) {
                audioClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop indefinitely
            } else {
                audioClip.loop(1); // Loop once (play twice in total)
            }
        }
    }
}
