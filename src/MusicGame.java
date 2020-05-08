import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Classe qui représente la musique dans le jeu
 */
public class MusicGame {
    private static MediaPlayer mediaPlayer;

    /**
     * Méthode qui joue de la musique
     * @param musicLocation String qui contient le path de la chanson
     */
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

    /**
     * Methode qui arrete la musique
     */
    public void stopMusic() {
        mediaPlayer.stop();
    }
}
