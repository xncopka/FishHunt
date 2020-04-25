import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Item extends Entity {

    protected Image image;
    protected boolean estAttrape;
    protected String id;
    protected boolean lastTimeActivation;
    protected long firstTime;
    protected boolean firstTimeActivation;
    protected boolean isUsed;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void setFirstTimeActivation(boolean firstTimeActivation) {
        this.firstTimeActivation = firstTimeActivation;
    }
    public boolean getFirstTimeActivation() {
        return firstTimeActivation;
    }

    public boolean getLastTimeActivation() {
        return this.lastTimeActivation;
    }

    public void setLastTimeActivation(boolean lastTimeActivation) {
        this.lastTimeActivation = lastTimeActivation;
    }

    public Image getImage() {
        return image;
    }

    public String getId() {
        return this.id;
    }

    public long getFirstTime() {
        return this.firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public boolean intersects(Balle other) {
        return other.intersects(this);
    }

    public void testCollision(Balle other) {
        if (intersects(other)) {
            estAttrape = true;
        }
    }

    public boolean estAttrape() {
        return this.estAttrape;
    }

    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }



}