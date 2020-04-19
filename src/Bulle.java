import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Represente une bulle dans l'océan
 */
public class Bulle extends Entity {


    private double rayon;

    public static double vitesseMax = 450;
    public static double vitesseMin = 350;

    public static double maxRayon = 40;
    public static double minRayon = 10;

    /**
     * Constructeur de bulle
     * @param x coordonné x de la bulle
     * @param y coordonné y de la bulle
     * @param rayon rayon de la bulle
     * @param vx composante x de la vitesse
     * @param vy composante y de la vitesse
     */
    public Bulle(double x, double y, double rayon, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.vx = vx;
        this.vy = vy;

    }


    /**
     * Met à jour la position y de la bulle
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        // mettre a jour la nouvelle position Y
        y -=  dt * vy;
    }

    /**
     * Donne un effet plus réaliste à l'ecran en ajoutant un groupe de bulles
     * @param bulles groupe de bulles
     * @param widthScreen largeur de l'ecran
     * @param heightScreen hauteur de l'ecran
     */
    public static void groupBulles (Bulle[][] bulles, double widthScreen, double heightScreen) {
        Random random= new Random();
        double[] baseX = new double[bulles.length];
        double borne = 20;

        // Pour chaque groupe de bulle
        for (int i = 0; i < bulles.length; i++) {

            // generer aleatoirement une coordonnee baseX
            baseX[i] = random.nextDouble() * (widthScreen + 1);

            for (int j = 0; j < bulles[0].length; j++) {

                // calculer rayon pour chaque bulle
                double rayon = random.nextDouble() * (maxRayon + 1 - minRayon) + minRayon;

                // calculer vitesse pour chaque bulle
                double vitesseY = random.nextDouble() * (Bulle.vitesseMax + 1 - Bulle.vitesseMin) + Bulle.vitesseMin;

                // calculer la position initiale X pour chaque bulle
                double base = baseX[i];
                double initX = random.nextDouble() * (base + borne + 1 - base + borne) + base - borne;

                // mettre a jour les valeurs dans la bulle
                bulles[i][j] = new Bulle(initX, heightScreen, rayon, 0, vitesseY);
            }
        }
    }


    /**
     * Dessine la bulle
     * @param context contexte graphique du canvas
     */
    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(0, 0, 255, 0.4));
        context.fillOval(x, y, rayon*2, rayon*2);
    }
}



