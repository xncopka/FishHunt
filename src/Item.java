import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Classe abstraite avec les methodes que l'on peut utiliser
 */
public abstract class Item extends Entity {

    protected Image image;
    protected boolean estAttrape;
    protected String id;
    protected boolean lastTimeActivation;
    protected long firstTime;
    protected boolean firstTimeActivation;
    protected boolean isUsed;

    /**
     * Renvoit si l'item est utilise ou non
     * @return vrai ou faux
     */
    public boolean isUsed() {
        return isUsed;
    }
    /**
     * Mutateur qui définit si l'item est utilisé ou non
     * @return vrai ou faux
     */
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * Mutateur pour indiquer si cela est la premiere fois que l'item est activé
     * @param firstTimeActivation vrai ou faux
     */
    public void setFirstTimeActivation(boolean firstTimeActivation) {
        this.firstTimeActivation = firstTimeActivation;
    }

    /**
     *Renvoit si cela est la premiere fois que l'item est activé
     * @return vrai ou faux
     */
    public boolean getFirstTimeActivation() {
        return firstTimeActivation;
    }

    public boolean getLastTimeActivation() {
        return this.lastTimeActivation;
    }

    public void setLastTimeActivation(boolean lastTimeActivation) {
        this.lastTimeActivation = lastTimeActivation;
    }

    /**
     * Méthode qui va chercher l'image de l'item
     * @return image
     */
    public Image getImage() {
        return image;
    }

    /**
     * ID qui renvoit quel genre d'item
     * @return un string qui identifie l'item
     */
    public String getId() {
        return this.id;
    }

    /**
     * Renvoit le premier temps ou l'item est apparu pour la premiere fois dans le jeu
     * @return temps
     */
    public long getFirstTime() {
        return this.firstTime;
    }

    /**
     * Mutateur du premier temps ou l'item est apparu pour la premiere fois dans le jeu
     * @param firstTime temps
     */
    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    /**
     * Renvoit is l'item est en intercepte la balle
     * @param other balle
     * @return vrai ou faux
     */

    public boolean intersects(Balle other) {
        return other.intersects(this);
    }

    /**
     * Methode qui renvoit si litem est touche par la balle ou non
     * @param other balle
     */
    public void testCollision(Balle other) {
        if (intersects(other)) {
            estAttrape = true;
        }
    }

    /**
     * Booleen qui determine si l'objet est attrape ou non
     * @return
     */
    public boolean estAttrape() {
        return this.estAttrape;
    }

    /**
     * Methode qui dessine litem selon le contexte graphique
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }

}