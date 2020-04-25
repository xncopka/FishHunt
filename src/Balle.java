import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle extends Entity  {


    private double rayon;

    public double getRayon() {
        return rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }




    public Balle(double x, double y) {
        this.x = x;
        this.y = y;
        this.rayon = 50;

    }

    /**
     * Met à jour la position y de la balle
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        // met a jour le nouveau rayon
        rayon -= dt * 300;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.BLACK);

        context.fillOval(x-rayon, y-rayon, rayon*2, rayon*2);

    }





    /**
     * Methode qui permet de verifier si la balle intersecte le poisson
     * Trouve le point (x, y) à l'intérieur du carré le plus proche du centre du cercle
     * et vérifie s'il se trouve dans le rayon du cercle
     * @param other le poisson
     * @return vrai ou faux, selon si le poisson intersecte le rayon du cercle
     */
    public boolean intersects(Entity other) {

        double deltaX = x - Math.max(other.x - other.largeur / 2, Math.min(x, other.x + other.largeur / 2));
        double deltaY = y - Math.max(other.y - other.hauteur / 2, Math.min(y, other.y + other.largeur / 2));

        return deltaX * deltaX + deltaY * deltaY < rayon * rayon;
    }










}
