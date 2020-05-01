import javafx.scene.image.Image;

import java.util.Random;

public class Appat extends Fish {

    private boolean sensDirect;


    public Appat(int level) {
        super(level);
        isFood = false;
        Random random = new Random();
        vx = 0;
        vy = (random.nextDouble()*(200-100) + 100);
        y = -hauteur;
        x = random.nextDouble() * (Jeu.WIDTH - largeur);
        this.image = new Image("fish/appat.png");
        this.image = ImageHelpers.colorize(image, color);
        this.sensDirect = true;

    }


    @Override
    public void update(double dt) {
        super.update(dt);

        if(y>=Jeu.HEIGHT-hauteur && sensDirect){
            vy *= -1;
            sensDirect = false;
        }


    }

}