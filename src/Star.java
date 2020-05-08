import javafx.scene.image.Image;
import java.util.Random;

/**
 * Étoile de mer qui oscille verticalement
 */
public class Star extends Fish {
    private double nowTime;
    private double randomY;

    /**
     * Constructeur de l'étoile de mer
     * @param level niveau du jeu
     */
    public Star(int level) {
        super(level);
        Random random= new Random();
        this.ay = 0;
        setImage(new Image("fish/star.png"));
        this.nowTime = 0;
        randomY= random.nextDouble()*(4.0*Jeu.HEIGHT/5 - 1.0*Jeu.HEIGHT/5) + 1.0*Jeu.HEIGHT/5;
    }

    /**
     * Met a jour les donnees du jeu
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        nowTime+=dt;
        this.y =  randomY + 50*Math.sin((2 * Math.PI * nowTime));
        super.update(dt);
    }
}
