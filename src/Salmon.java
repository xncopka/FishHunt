import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Classe qui représente le poisson le plus rapide de l'océan. Apparait dans le niveau du jeu le plus difficile
 */


public class Salmon extends Fish {

    public boolean isMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(boolean maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    private boolean maxSpeed;

    /**
     * Constructeur du saulmon
     * @param level niveau du jeu
     */
    public Salmon(int level) {
        super(level);
        setImage(new Image("fish/sailfish.png"));
        this.color = new Color(Math.random(), Math.random(), Math.random(), 1);
        this.image = ImageHelpers.colorize(image, color);
        this.maxSpeed = false;
        this.hauteur = 125;
        this.largeur = 125;
        if(!leftOfScreen){
            this.image = ImageHelpers.flop(image);
        }
    }

}
