import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FishBone extends Entity {

    private Image image;
    private boolean estAttrape;
    private double nowTime;
    private boolean enable;
    private Fish fish;



    public boolean isEnabled() {
        return enable;
    }




    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }





    public FishBone(Fish fish) {
        this.x = fish.getX();
        this.y = fish.getY();
        this.hauteur = 100;
        this.largeur = 100;
        ay = 100;
        this.image = new Image("fish/fishbone.png");
        this.estAttrape = false;
        this.enable = false;
        this.nowTime = 0;

        if(!fish.isLeftOfScreen()) {
            this.image = ImageHelpers.flop(image);
        }


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


    @Override
    public void update(double dt) {
        super.update(dt);

        nowTime+=dt;

        if(nowTime>0.25) {
            enable = true;
        }

    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }

    public boolean estAttrape() {
        return estAttrape;
    }


}
