import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Classe qui représente les poissons dans le jeu
 * */
public class Fish extends Entity{

    protected Image[] frames;
    protected Image image;
    protected Color color;
    protected boolean leftOfScreen;
    protected boolean estAttrape;
    protected boolean isFood;


    /**
     * Constructeur de poisson
     * @param level niveau du jeu
     */


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
/**
 * Méthode qui va mettre en place l'image du poisson
 */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Méthode qui vérifie si le poisson est à gauche de l'écran
     * @return booléen qui renvoit si cela est vrai ou faux
     */
    public boolean isLeftOfScreen() {
        return leftOfScreen;
    }

    /**
     * Méthode qui vérifie si la balle intercepte un poisson
     * @param other balle
     * @return un booléen vrai ou faux
     */
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
     * Verifie si le poisson est un appat ou non
     * @return booléen qui retourne vrai ou faux
     */
    public boolean isFood() {
        return isFood;
    }
    /**
     * Met à jour la position du poisson
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        super.update(dt);
    }

    /**
     * Une méthode qui dessine un poisson selon le contexte du jeu
     * @param context contexte graphique du canvas
     */
    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }

    /**
     * Méthode qui vérifie si le poisson est attrapé par le prédateur ou un requin
     * @return booléen vrai ou faux
     */
    public boolean estAttrape() {
        return estAttrape;
    }

}
