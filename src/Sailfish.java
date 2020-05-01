import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Le poisson le plus rapide de l'océan. Apparait dans le mode de difficiculte le plus difficile
 */


public class Sailfish extends Fish {

    public boolean isMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(boolean maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    private boolean maxSpeed;



    public Sailfish(int level) {
        super(level, true);
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
