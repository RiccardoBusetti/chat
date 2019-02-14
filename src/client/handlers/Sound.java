package client.handlers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import server.constants.Constants;

import java.io.File;

public class Sound {

    public static final Media oof = new Media(new File(Constants.OOF_SFX).toURI().toString());
    public static final Media boot = new Media(new File(Constants.CHAT_BOOT_SFX).toURI().toString());

    public static void play(String path, int mode) {
        switch (mode) {
            case 1:
                boolean exists;
                try {
                    File tmpDir = new File(path);
                    exists = tmpDir.exists();
                } catch (Exception ex) {
                    exists = false;
                }

                if (exists) {
                    Media media = new Media(new File(path).toURI().toString());
                    new Thread() {
                        public void start() {
                            MediaPlayer mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.play();
                        }
                    }.start();
                } else {
                    System.out.println(path + " not found");
                    return;
                }
                break;
            case 2:
                new Thread() {
                    public void start() {
                        MediaPlayer mediaPlayer = new MediaPlayer(oof);
                        mediaPlayer.play();
                    }
                }.start();
                break;
            case 3:
                new Thread() {
                    public void start() {
                        MediaPlayer mediaPlayer = new MediaPlayer(boot);
                        mediaPlayer.play();
                    }
                }.start();
                break;
            default:
                return;
        }
    }

    public static void playOof() {
        try {
            play("", 2);
        } catch (Exception exc) {
            System.out.println("Cannot play sound");
        }
    }

    public static void playBoot() {
        try {
            play("", 3);
        } catch (Exception exc) {
            System.out.println("Cannot play sound");
        }
    }

}
