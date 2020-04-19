import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;


/**
 * Classe reprentant le jeu et la logique interne de l’application (modèle)
 */
public class Jeu {

    // Largeur, hauteur du niveau
    public static final int WIDTH = 640, HEIGHT = 480;

    // Entités dans le jeu
    private Bulle[][] bulles;


    /**
     * Constructeur de Jeu
     */
    public Jeu() {

        // pas de bulles au debut du jeu
        bulles = new Bulle[0][0];

    }


    /**
     * Initialise un groupe de bulles
     */
    public void groupBulles() {
        bulles = new Bulle[3][5];
        Bulle.groupBulles(bulles, WIDTH, HEIGHT);
    }


    /**
     * Met a jour les donnes du jeu
     *
     * @param dt Temps écoulé depuis le dernier update()
     */
    public void update(double dt) {
        // Pour chaque groupe de bulle
        for (int i = 0; i < bulles.length; i++) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                // mettre a jour la vitesse de la bulle
                Bulle bulle = bulles[i][j];
                bulle.update(dt);
            }
        }
    }


    /**
     * Dessine toutes les formes de l'animation dans le contexte
     * graphique du canvas
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {

        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);


        // Pour chaque groupe de bulle
        for (int i = 0; i < bulles.length; i++) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = bulles[i][j];
                // dessiner la bulle
                bulle.draw(context);
            }
        }
    }


}
