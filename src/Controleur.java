import javafx.scene.canvas.GraphicsContext;

/**
 *  Classe qui relie la vue au modèle, HighSeaTower à Jeu
 */
public class Controleur {

    private Jeu jeu;

    /**
     * Constructeur de Controleur
     */
    public Controleur() {
        jeu = new Jeu();
    }


    /**
     * Demande au jeu de dessiner les formes du dessin
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {
        jeu.draw(context);
    }


    /**
     * Demande au jeu de mettre à jour les données du jeu
     * @param deltaTime Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double deltaTime) {
        jeu.update(deltaTime);
    }


    /**
     * Demande au modele de mettre à jour l'état des bulles
     * Methode pour grouper les bulles a l'arriere plan
     */
    public void groupBulles() { jeu.groupBulles();}

    public void newBall(double x, double y) {jeu.newBall(x,y);}




}
