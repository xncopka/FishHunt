import javafx.scene.image.Image;

public class Star extends Fish {

    public Star(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/star.png"));

        //TODO Oscillations


    }

    /**
     * Met à jour la position de l'étoile
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        super.update(dt);


    }


}
