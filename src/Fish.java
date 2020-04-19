import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class Fish extends Entity{

    private Image[] frames;
    private Image image;
    private Color color;


    public Fish(int level) {

        this.ay = 100;
        this.vx = 100*Math.pow(level, 1.0/3) + 200;
        Random random= new Random();
        this.vy = random.nextDouble()*(200-100) + 100;
        this.y = random.nextDouble()*(4.0/5 - 1.0/5) + 1.0/5;

        frames = new Image[]{
                new Image("fish/00.png"),
                new Image("fish/01.png"),
                new Image("fish/02.png"),
                new Image("fish/03.png"),
                new Image("fish/04.png"),
                new Image("fish/05.png"),
                new Image("fish/06.png"),
                new Image("fish/07.png")
        };
        image = frames[0];

        //TODO Inverser image






        //TODO Couleur aléatoire









    }


    /**
     * Met à jour la position du poisson
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        super.update(dt);


    }


    @Override
    public void draw(GraphicsContext context) {

    }
}
