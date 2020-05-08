import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Classe qui représente un prédateur qui attrape des poissons dans l'océan
 */
public class Predator extends Fish {

    private boolean aAttrape;

    /**
     * Constructeur de prédateur. Si celui-ci mange des poissons, cela enlève des vies au joueur
     * @param level niveau du jeu
     */
    public Predator(int level) {
        super(level);
        this.hauteur = 200;
        this.largeur = 200;
        setImage(new Image("fish/predator.png"));
        if(!leftOfScreen){
            this.image = ImageHelpers.flop(image);
        }

        this.aAttrape = false;
    }


    /**
     * Méthode qui vérifie si le prédateur intercepte un poisson
     * @param other poisson dans le jeu
     * @return un boolean, vrai ou faux, dépendemment de si le prédateur intercepte ou non le poisson
     */

    public boolean intersects(Fish other) {
        return !( // Un des carrés est à gauche de l’autre
                x + largeur < other.x || other.x + other.largeur < this.x
                        // Un des carrés est en haut de l’autre
                        || y + hauteur < other.y || other.y + other.hauteur < this.y);
    }
}
