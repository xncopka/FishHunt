import javafx.scene.image.Image;

public class Star extends Fish {

    public Star(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/star.png"));

        //TODO Oscillations


    }

    @Override
    public void update(double dt) {

        if ((dt - tempsZero) >= ((long)1e+9)){
            count +=1;
        }
        if ( count %3 == 0){
            y -=  dt * vy;
        } else {
            y +=  dt * vy;
        }
    }


}
