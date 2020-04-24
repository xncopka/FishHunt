import javafx.scene.image.Image;

public class Crab extends Fish {

    public Crab(int level) {
        super(level);
        this.vx *= 1.3;
        this.ay = 0;
        setImage(new Image("/crabe.png"));

        //TODO Oscillations

    }




}
