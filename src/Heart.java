import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;


/**
 *  Objets dans le jeu qui permet de faire gagner 1 vie supplémentaire au requin qui apparait aléatoirement
 *  apres que le requin mange un poisson et apparait au même endroit ou le requin mange ce dernier
 */


public class Heart extends Item {

    /**
     * Classe qui représente un coeur dans le jeu
     */


    public Heart() {
        Random random = new Random();
        this.ay=0;
        this.hauteur = 50;
        this.largeur = 50;
        this.x = random.nextDouble()*(Jeu.WIDTH-largeur);
        this.y = random.nextDouble()*(Jeu.HEIGHT-hauteur);
        int pileOuFace = random.nextInt(2);
        if (pileOuFace ==0) {
            this.image = new Image("items/heart-plus.png");
            this.id = "vie bonus";
        } else {
            this.image = new Image("items/heart-minus.png");
            this.id = "vie malus";
        }
        estAttrape = false;
        firstTimeActivation = false;
        lastTimeActivation = false;
        isUsed = false;
    }

}
