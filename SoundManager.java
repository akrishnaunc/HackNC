package dungeonmaze;

import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {

    public static File killSound;
    public static File toolSound;
    public static File hurt;
    public static File background;
    public static File bossMusic;
    private static float volume = 0.75f;
    private static Clip bgClip;
    private static boolean bossLevel;

    public static void playBackgroundMusic(boolean boss) {
        stopMusic();
        bossLevel = boss;
        if (volume != 0) {
            try {
                bgClip = AudioSystem.getClip();
                if (boss) {
                    bgClip.open(AudioSystem.getAudioInputStream(bossMusic));
                } else {
                    bgClip.open(AudioSystem.getAudioInputStream(background));
                }

                FloatControl volumeControl = (FloatControl) bgClip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = volumeControl.getMaximum() - volumeControl.getMinimum();
                float gain = (range * volume) + volumeControl.getMinimum();
                volumeControl.setValue(gain);

                bgClip.start();
                bgClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void playSound(String s) {
        if (volume != 0) {
            try {
                Clip c = AudioSystem.getClip();
                if (s.equals("KILL")) {
                    c.open(AudioSystem.getAudioInputStream(killSound));
                } else if (s.equals("HURT")) {
                    c.open(AudioSystem.getAudioInputStream(hurt));
                } else if (s.equals("TOOL")) {
                    c.open(AudioSystem.getAudioInputStream(toolSound));
                }

                FloatControl volumeControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                float range = volumeControl.getMaximum() - volumeControl.getMinimum();
                float gain = (range * volume) + volumeControl.getMinimum();
                volumeControl.setValue(gain);

                c.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void toggleMute() {
        if (volume == 0) {
            volume = 0.75f;
            playBackgroundMusic(bossLevel);
        } else {
            volume = 0;
            stopMusic();
        }
    }

    private static void stopMusic() {
        if (bgClip != null) {
            bgClip.stop();
        }
    }
}
