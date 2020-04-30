import javafx.scene.image.Image;

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
        super(level);
        this.ay=0;
        setImage(new Image("fish/sailfish.png"));
        this.hauteur=150;
        this.largeur=150;
        this.maxSpeed = false;
    }





}
