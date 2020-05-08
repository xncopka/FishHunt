import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * Classe reprentant le jeu et la logique interne de l’application (modèle)
 */
public class Jeu {

    /**
     *  Attributs
     */

    // Largeur, hauteur du niveau
    public static final int WIDTH = 640, HEIGHT = 480;

    // Entités dans le jeu
    private Bulle[][] bulles;
    private  ArrayList<Balle> balles = new ArrayList<>();
    private ArrayList<Fish> fishes = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    // Chanson du jeu
    private MusicGame chanson;

    // joueurs dans le jeu
    private Player player;

    // niveau du jeu
    private int level;

    // si on a changé de niveau
    private boolean firstChangeLevel;

    // si la partie est terminée
    private boolean gameOver;

    // palier de changement de niveau
    private int palier;

    // si le niveau doit être affiché
    private boolean afficherLevel;

    // si on est invincible
    private boolean modeInvicible;

    // si on arrête de faire apparaitre des poissons
    private boolean stopNewFish = false;

    // si on est dans le mode spécial
    private boolean sniperGame;

    // si les objets sont activés
    private boolean itemsEnabled;

    // Si on a attrapé une série de poissons
    private boolean serieActivated;

// --------------------------------------------------------------------------------------------------------------------

    /**
     * Getters & Setters
     */

    public void setSerieActivated(boolean serieActivated) {
        this.serieActivated = serieActivated;
    }

    public MusicGame getChanson() {
        return chanson;
    }


    public boolean getSniperGame() {
        return sniperGame;
    }

    public int getBalles() {
        return player.getBalles();
    }

    public void setBalles(int balles) {
        if(player.getBalles()!=0) {
            player.setBalles(balles);
        }
    }


    public int getSerie() {
        return player.getSerie();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    
    public boolean getGameOver() {
        return this.gameOver;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return player.getPoints();
    }

    public void setScore(int score) {

        player.setPoints(score);

    }

    public void setLife(int life) {
        if(getLife()<3) {
            player.setNbVies(life);
        }
    }

    public int getLife() {
        return player.getNbVies();
    }


    public boolean getModeInvicible() {
        return modeInvicible;
    }

    public void setModeInvicible(boolean modeInvicible) {
        this.modeInvicible = modeInvicible;
    }



    public boolean isInvicible(){
        return player.isInvicible();
    }
    public void setInvicible (boolean isInvicible){
        player.setInvicible(isInvicible);
    }


    public void setSerie(int serie) {
        player.setSerie(serie);
    }

    public ArrayList<Item> getItem(){
        return this.items;
    }

    public int getLevel() {
        return level;
    }
    
    public boolean getAfficherLevel() {
        return afficherLevel;
    }

    public void setAfficherLevel(boolean afficherLevel) {
        this.afficherLevel = afficherLevel;
    }

    public boolean getStopNewFish() {
        return stopNewFish;
    }

    public void setStopNewFish(boolean stopNewFish) {
        this.stopNewFish = stopNewFish;
    }



    public void enableChanson(boolean on) {
        if(on) {
            String filepath = "src/music/Noisestorm - Crab Rave [Monstercat Release].mp3";
            chanson = new MusicGame();
            chanson.playMusic(filepath);
        }  else {
            chanson.stopMusic();
        }

    }
    

    public void enableItems() {
        itemsEnabled = true;

    }

  



    /**
     * Constructeur de Jeu
     */
    public Jeu(boolean modeSpecial, boolean speakerOn) {

        

        // Test du mode sniper
        //sniperGame = true;
        sniperGame = modeSpecial;
        itemsEnabled = false;
        gameOver = false;



        modeInvicible = false;

        serieActivated = false;


        level = 0;
        firstChangeLevel=false;
        afficherLevel = false;
        palier = 0;


        // pas de bulles au debut du jeu
        bulles = new Bulle[0][0];

      

        player = new Player();




        if(speakerOn) {
            String filepath = "src/music/Noisestorm - Crab Rave [Monstercat Release].mp3";
            chanson = new MusicGame();
            chanson.playMusic(filepath);
        }

    }


    /**
     * Initialise un groupe de bulles
     */
    public void groupBulles() {
        bulles = new Bulle[3][5];
        Bulle.groupBulles(bulles, WIDTH, HEIGHT);
    }

    public void newBall(double x, double y) {
        Balle balle = new Balle(x, y, true);
        balles.add(balle);
    }

    public void newBallSniper(double x, double y) {
        Balle balle = new Balle(x, y, false);
        items.add(balle);
    }

    public void newFish(int level) {
        Fish fish = new Fish(level);
        fishes.add(fish);
    }

    public void newSpecialFish(int level) {
        Fish fish;
        Random random = new Random();
        int valeurRandom = random.nextInt(2);
        if (valeurRandom == 0){
            fish = new Crab(level);
            fishes.add(fish);
        } else{
            fish = new Star(level);
            fishes.add(fish);
        }

    }

    public void newBonusFish(int level) {
        Fish fish;
        Random random = new Random();
        int valeurRandom = random.nextInt(3);
        if (valeurRandom == 0){
            fish = new Appat(level);
            fishes.add(fish);
        } else if (valeurRandom == 1){
            fish = new Salmon(level);
            fishes.add(fish);
        } else {
            fish = new Predator(level);
            fishes.add(fish);
        }



    }
    


    public void newItem(){
      /*  Heart heart = new Heart();
        items.add(heart);*/

        if(sniperGame){
            Random random = new Random();
            int valeurRandom = random.nextInt(6);
            if(valeurRandom == 0){
                Heart heart = new Heart();
                items.add(heart);
            } else {
                newBallSniper(random.nextDouble()*(Jeu.WIDTH-20), random.nextDouble()*(Jeu.HEIGHT-20));
            }
        } else {
            Heart heart = new Heart();
            items.add(heart);
        }
    }







    public boolean checkNewScore (int score, ArrayList<String> meilleursScores) {
        if(meilleursScores.size()<10 || score > Integer.parseInt(meilleursScores.get(9).split(" - ", 0)[meilleursScores.get(9).split(" - ", 0).length-1]) ) {
            return true;
        } else {
            return false;
        }
    }

    public int trierScore (int score, ArrayList<String> meilleursScores, String name) {
        int index = meilleursScores.size();

        for (int i = 0; i < meilleursScores.size(); i++) {
            if(score > Integer.parseInt(meilleursScores.get(i).split(" - ", 0)[meilleursScores.get(i).split(" - ", 0).length-1])) {
                meilleursScores.add(i,"#" + (i+1) + " - " + name + " - " +  score);
                index = i;
                break;
            }
        }
        if(index == meilleursScores.size()) {
            meilleursScores.add("#" + (index+1) + " - " + name + " - " +  score);
        }
        for (int i = index+1; i <meilleursScores.size() ; i++) {
            String saveName =  meilleursScores.get(i).split(" - ", 0)[1];
            String saveScore =  meilleursScores.get(i).split(" - ", 0)[2];
            meilleursScores.set(i, "#" + (i+1) + " - " + saveName + " - " +   saveScore);
        }
        if(meilleursScores.size()==11){
            meilleursScores.remove(10);
        }
        return index;
    }










    /**
     * Met a jour les donnes du jeu
     *
     * @param dt Temps écoulé depuis le dernier update()
     */
    public void update(double dt) {



        // Pour chaque groupe de bulle
        for (Bulle[] value : bulles) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                // mettre a jour la vitesse de la bulle
                Bulle bulle = value[j];
                bulle.update(dt);
            }
        }

        if (balles != null) {
            for (Iterator<Balle> iterator = balles.iterator(); iterator.hasNext(); ) {
                Balle balle = iterator.next();
                balle.update(dt);
                if (balle.getRayon() <= 0) {
                    iterator.remove();
                }
            }
        }


        if ( player.getNbVies() <= 0 ) {
            gameOver = true;

        }


        if(itemsEnabled) {
            Random random = new Random();
            if (!sniperGame) {
                int valeurRandom = random.nextInt(1001);
                if (valeurRandom == 0) {
                    newItem();
                }
            } else {
                int valeurRandom = random.nextInt(201);
                if (valeurRandom == 0) {
                    newItem();
                }
            }
        }


        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            item.update(dt);
            for (Balle balle : balles) {
                item.testCollision(balle);
                if (item.estAttrape() && !item.isUsed()) {
                    if(item instanceof Heart) {
                        if (item.getId().equals("vie bonus")) {
                            if (player.getNbVies() < 3) {
                                player.setNbVies(player.getNbVies() + 1);
                            }
                        } else {
                            if (!player.isInvicible()) {
                                player.setNbVies(player.getNbVies() - 1);
                                player.setSerie(0);
                            }
                        }
                    } else {
                        if(sniperGame){
                            if(!player.isInvicible()) {
                                player.setBalles(player.getBalles() + 2);
                            } else {
                                player.setBalles(player.getBalles() + 1);
                            }
                        }
                    }
                    item.setUsed(true);
                    balle.setAttrape(true);
                    iterator.remove();
                }
            }
            if(item.getLastTimeActivation()){
                iterator.remove();
            }
        }









        if (fishes != null) {
            ArrayList<Fish> poubelle = new ArrayList<>();
            for (Iterator<Fish> iterator = fishes.iterator(); iterator.hasNext(); ) {
                Fish fish = iterator.next();

                fish.update(dt);

                if((fish.getX()<-fish.largeur)||(fish.getX()>Jeu.WIDTH)){
                    iterator.remove();
                    if((fish.isFood())) {
                        if(!player.isInvicible()) {
                            player.setNbVies(player.getNbVies() - 1);
                            player.setSerie(0);



                            }
                        }
                    }


                if(!fish.isFood()){
                    if(fish.getY() < -fish.getHauteur()){
                        iterator.remove();
                    }
                }

                if(fish instanceof Predator) {
                    for (Iterator<Fish> iterator2 = fishes.iterator(); iterator2.hasNext(); ) {
                        Fish other = iterator2.next();
                        if(fish != other) {
                            if(!(other instanceof Appat)) {
                                if (((Predator)fish).intersects(other) && other.isLeftOfScreen() != ((Predator)fish).isLeftOfScreen() ) {
                                    poubelle.add(other);
                                    if(!player.isInvicible()){
                                        player.setNbVies(player.getNbVies()-1);
                                    }
                                    player.setSerie(0);
                                }
                            }                                             


                        }

                    }
                }






                for (Balle balle : balles) {
                    fish.testCollision(balle);

                    for (Fish f: fishes) {
                        if (f.getX() >= 0 && f.getX() <= Jeu.WIDTH - f.getLargeur()) {
                            if (f.getY() >= 0 && f.getY() <= Jeu.HEIGHT - f.getHauteur()) {
                                if (f instanceof Salmon) {
                                    if (!((Salmon) f).isMaxSpeed()) {
                                        f.setVX(2 * f.getVX());
                                        ((Salmon) f).setMaxSpeed(true);
                                    }
                                }
                            }
                        }
                    }



                    if (fish.estAttrape()) {
                        if((!fish.isFood())) {



                            if(!player.isInvicible()) {
                                player.setNbVies(player.getNbVies() - 2);
                            } else {
                                player.setSerie(0);
                            }
                        } else {
                            if(!gameOver) {

                                player.setPoints(player.getPoints() + 1);
                                player.setSerie(player.getSerie() + 1);
                            }
                        }

                          poubelle.add(fish);
                         //iterator.remove();
                         balle.setAttrape(true);

                    }

                }
            }
            fishes.removeAll(poubelle);
        }





        for (Balle balle : balles) {
            if(!balle.aAttrape()){
                player.setSerie(0);
            }
        }



        if(player.getSerie() != 0) {

            if ( !serieActivated && player.getSerie() % 10  == 0 ||player.getSerie() % 11  == 0 ||player.getSerie() % 12  == 0) {
                player.setInvicible(true);
                setSerieActivated(true);
            }

        }








        

             if (player.getPoints()  == palier + 5) {
                 firstChangeLevel = false;
           }

             if (player.getPoints() % 5 == 0 && !firstChangeLevel) {

                 setStopNewFish(true);
                 afficherLevel=true;

                 firstChangeLevel = true;
                 level +=  1;
                 palier = player.getPoints();
             }
                                                
         


        if(sniperGame){
            if(!player.isInvicible()) {
                if (player.getBalles() == 0) {
                    gameOver = true;
                }
            }
        }





    }


    /**
     * Dessine toutes les formes de l'animation dans le contexte
     * graphique du canvas
     *
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {

        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);


        // Pour chaque groupe de bulle
        for (Bulle[] value : bulles) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = value[j];
                // dessiner la bulle
                bulle.draw(context);
            }
        }


        if (fishes != null) {
            for (Fish fish : fishes) {
                fish.draw(context);
            }
        }




        if (balles != null) {
            for (Balle balle: balles) {
                balle.draw(context);
            }
        }

        for (Item item: items) {
            item.draw(context);
        }




        // dessine le score
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        context.setFont(Font.font(25));
        if(!modeInvicible) {
            context.setFill(Color.WHITE);
        } else {
            context.setFill(Color.rgb(126,211,33));
        }
        context.fillText(""+player.getPoints(), WIDTH/2.0, 60);


        // dessine les balles restantes

        context.setTextAlign(TextAlignment.LEFT);

        context.setFont(Font.font(17));
        context.setFill(Color.WHITE);
        context.fillText("Série: "+ player.getSerie(), 10, 20);

        if(sniperGame){

            // dessine les balles restantes
            context.fillText("Balles restantes: "+ player.getBalles(), 10, 45);
        }



        Image image;
        if(!modeInvicible) {
            image = new Image("fish/00.png");
        } else {
            image = new Image("fish/invincible.png");
        }

        // dessine les vies restantex
        if (player.getNbVies()==3) {
            context.drawImage(image, (WIDTH / 2.0) - 30/2, 85, 30, 30);
            context.drawImage(image, (WIDTH / 2.0) - 30/2 + 30 + 20, 85, 30, 30);
            context.drawImage(image, (WIDTH / 2.0) - 30/2 - 30 - 20, 85, 30, 30);
        }

        if (player.getNbVies()==2) {
            context.drawImage(image, (WIDTH / 2.0) - 30/2 - 30 - 20, 85, 30, 30);
            context.drawImage(image, (WIDTH / 2.0) - 30/2, 85, 30, 30);
        }

        if (player.getNbVies()==1) {
            context.drawImage(image, (WIDTH / 2.0) - 30/2 - 30 - 20, 85, 30, 30);
        }

       

        


    }

   
}
