import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle extends Entity  {

    private double rayon;

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
}
