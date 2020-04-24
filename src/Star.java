import javafx.scene.image.Image;

public class Star extends Fish {

    public Star(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/star.png"));

        //TODO Oscillations


    }




}
