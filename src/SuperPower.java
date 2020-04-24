import javafx.scene.image.Image;


/**
 *
 * Objet dans le jeu qui rend le requin plus puissant en etant plus vifs dans ses attaques
 * La vitesse de toutes les autres entites du jeu va baisser le temps du super-pouvoir
 */

public class SuperPower extends Fish {  // n'est pas un poisson mais reprend les méthodes du poisson


    public SuperPower(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/superPower.png"));
    }









}
