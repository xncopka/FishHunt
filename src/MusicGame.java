import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicGame {
    private static MediaPlayer mediaPlayer;

    public void playMusic(String musicLocation){
        try{
            File musicPath = new File(musicLocation);
            if (musicPath.exists()){
                Media audioInput = new Media((musicPath).toURI().toString());
                mediaPlayer = new MediaPlayer(audioInput);
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    public void run() {
                        mediaPlayer.seek(Duration.ZERO);
                    }
                });
                mediaPlayer.play();
            } else {
                System.out.println("Can't find files");
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }




}
