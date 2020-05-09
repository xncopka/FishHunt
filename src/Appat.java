import javafx.scene.image.Image;

import java.util.Random;

/**
 * Classe qui représente un appat dans l'océan
 */
public class Appat extends Fish {

    private boolean sensDirect;

    /**
     * Constructeur de l'appat qui enlève des vies au joueur si celui-ci lance une balle sur l'appat
     * @param level niveau du jeu
     */

    public Appat(int level) {
        super(level);
        Random random = new Random();
        vx = 0;
        vy = (random.nextDouble()*(200-100) + 100);
        y = -hauteur;
        x = random.nextDouble() * (Jeu.WIDTH - largeur);
        this.image = new Image("fish/appat.png");
        this.image = ImageHelpers.colorize(image, color);
        this.sensDirect = true;

        int valeurRandom = random.nextInt(2);
        if (valeurRandom == 0){
            this.image = ImageHelpers.flop(image);
        }
    }

    /**
     * Met a jour les donnes du jeu
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        super.update(dt);

        if(y>=Jeu.HEIGHT-hauteur && sensDirect){
            vy *= -1;
            sensDirect = false;
        }

    }

}