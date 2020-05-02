import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Predator extends Fish {



    private boolean aAttrape;

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


    public boolean aAttrape() {
        return aAttrape;
    }



    public boolean intersects(Fish other) {
        return !( // Un des carrés est à gauche de l’autre
                x + largeur < other.x || other.x + other.largeur < this.x
                        // Un des carrés est en haut de l’autre
                        || y + hauteur < other.y || other.y + other.hauteur < this.y);
    }



    public void testCollision(Fish other) {
        if (intersects(other) && other.isLeftOfScreen() != isLeftOfScreen() ) {
            aAttrape = true;
        }
    }






}
