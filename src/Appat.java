import javafx.scene.image.Image;


/**
 * Des pecheurs convoitent les ailerons des requins. Et utilisent des poissons comme appat
    Le requin ne doit pas manger cet appat. Sinon Game Over
 */


public class Appat extends Fish {



    public Appat(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/appat.png"));
    }




}



