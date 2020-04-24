import javafx.scene.image.Image;


/**
 *  Objets dans le jeu qui permet de faire gagner 1 vie supplémentaire au requin qui apparait aléatoirement
 *  apres que le requin mange un poisson et apparait au même endroit ou le requin mange ce dernier
 */


public class Heart extends Fish {         // n'est pas un poisson mais reprend les méthodes du poisson


    public Heart(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/heart.png"));
    }




}
