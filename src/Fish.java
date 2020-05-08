import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class Fish extends Entity{

    protected Image[] frames;
    protected Image image;
    protected Color color;
    protected boolean leftOfScreen;
    protected boolean estAttrape;
    protected boolean isFood;




    public boolean isFood() {
        return isFood;
    }






    public Fish(int level) {

        this.ay = 100;
        this.vx = 100*Math.pow(level, 1.0/3) + 200;
        Random random= new Random();
        this.vy = (random.nextDouble()*(200-100) + 100)*(-1);
        this.y = random.nextDouble()*(4.0*Jeu.HEIGHT/5 - 1.0*Jeu.HEIGHT/5) + 1.0*Jeu.HEIGHT/5;
        this.estAttrape = false;
        this.largeur=100;
        this.hauteur=100;
        this.color = new Color(Math.random(), Math.random(), Math.random(), 1);
        this.isFood = true;
       




            this.frames = new Image[]{
                    new Image("fish/00.png"),
                    new Image("fish/01.png"),
                    new Image("fish/02.png"),
                    new Image("fish/03.png"),
                    new Image("fish/04.png"),
                    new Image("fish/05.png"),
                    new Image("fish/06.png"),
                    new Image("fish/07.png")
            };
            this.image = frames[random.nextInt(8)];

        this.image = ImageHelpers.colorize(image, color);

        int valeurRandom = random.nextInt(2);
        if (valeurRandom == 0) {    // 0: va à droite
            this.x = -this.largeur; // poisson est à gauche de l'écran
            this.leftOfScreen = true;

        } else {                    // 1: va à gauche
            this.x = Jeu.WIDTH;     // poisson est à droite de l'écran
            this.vx *= -1;
            this.leftOfScreen = false;
            this.image = ImageHelpers.flop(image);



        }













    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isLeftOfScreen() {
        return leftOfScreen;
    }

    public void setLeftOfScreen(boolean leftOfScreen) {
        this.leftOfScreen = leftOfScreen;
    }



    public boolean intersects(Balle other) {
        return other.intersects(this);
    }


    /**
     * Methode qui set estAttrape selon si la balle a touché le poisson
     * @param other balle
     */
    public void testCollision(Balle other) {
        if (intersects(other)) {
            estAttrape = true;
        }

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
        context.drawImage(image, x, y, largeur, hauteur);
    }

    public boolean estAttrape() {
        return estAttrape;
    }


}
