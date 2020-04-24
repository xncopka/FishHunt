import javafx.scene.image.Image;


/**
Le requin ne doit pas manger un piranha sinon il perd de la vie
 */


public class Piranha extends Fish {



    public Piranha(int level) {
        super(level);
        this.ay=0;
        setImage(new Image("/piranha.png"));
    }




}



