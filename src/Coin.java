import javafx.scene.image.Image;

import java.util.Random;

public class Coin extends Item {




    public Coin() {
        Random random = new Random();
        this.ay=0;
        this.hauteur = 50;
        this.largeur = 50;
        this.x = random.nextDouble()*(Jeu.WIDTH-largeur);
        this.y = random.nextDouble()*(Jeu.HEIGHT-hauteur);
        int pileOuFace = random.nextInt(2);
        this.image = new Image("items/coin.png");
        estAttrape = false;
        firstTimeActivation = false;
        lastTimeActivation = false;
        isUsed = false;
    }

}
