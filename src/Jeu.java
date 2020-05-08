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

    public void setItemsEnabled(boolean itemsEnabled) {
        this.itemsEnabled = itemsEnabled;
    }

    // ----------------------------------------------------------------------------------------------------------------

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

    // ----------------------------------------------------------------------------------------------------------------

    /**
     *  Méthodes pour faire apparaître un objet du jeu
     */

    /**
     * Initialise un groupe de bulles
     */
    public void groupBulles() {
        bulles = new Bulle[3][5];
        Bulle.groupBulles(bulles, WIDTH, HEIGHT);
    }

    /**
     * Initialise une balle
     * @param x coordonnée x de la balle
     * @param y coordonnée y de la balle
     */
    public void newBall(double x, double y) {
        Balle balle = new Balle(x, y, true);
        balles.add(balle);
    }

    /**
     * Initialise une balle en tant qu'objets du jeu
     * @param x coordonnée x de la balle
     * @param y coordonnée y de la balle
     */
    public void newBallSniper(double x, double y) {
        Balle balle = new Balle(x, y, false);
        items.add(balle);
    }

    /**
     * Initialise un poisson commun
     * @param level niveau du jeu
     */
    public void newFish(int level) {
        Fish fish = new Fish(level);
        fishes.add(fish);
    }

    /**
     * Initialise un crabe ou une étoile avec 50% de probabilité
     * @param level niveau du jeu
     */
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

    /**
     * Initialise un appât, un saumon ou un prédateur avec 33% de probabilité
     * @param level
     */
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


    /**
     * Initialise un objet en mode normal
     * Dans le mode normal, un coeur est initialisé
     * Dans le mode spécial, soit un coeur (probabilité: 1/6) ou une balle(probabilité: 5/6) est initialisé.
     */
    public void newItem(){

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

    // ----------------------------------------------------------------------------------------------------------------


    /**
     * Active ou désactive la chanson du jeu
     * @param on valeur de vérité si on active la chanson
     */
    public void enableChanson(boolean on) {
        if(on) {
            String filepath = "src/music/Noisestorm - Crab Rave [Monstercat Release].mp3";
            chanson = new MusicGame();
            chanson.playMusic(filepath);
        }  else {
            chanson.stopMusic();
        }
    }


    /**
     * Vérifie si le score passé en paramètre est supérieur aux 10 meilleurs.
     * @param score score du joueur
     * @param meilleursScores Arraylist qui contient les 10 meilleurs scores du jeu
     * @return un boolean pour savoir si on doit ou non ajouter le score du joueur à l'Arraylist
     */
    public boolean checkNewScore (int score, ArrayList<String> meilleursScores) {
        if(meilleursScores.size()<10 || score > Integer.parseInt(meilleursScores.get(9).split(" - ", 0)[meilleursScores.get(9).split(" - ", 0).length-1]) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Trie le score en paramètre avec son nom dans l'Arraylist des meilleurs scores du jeu et retourne l'index du
     * score dans le nouveau tableau trié
     * @param score du joueur
     * @param meilleursScores Arraylist des meilleurs scores
     * @param name nom associé au score du joueur
     * @return l'index du score dans le nouveau tableau trié
     */
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

    // ----------------------------------------------------------------------------------------------------------------


    /**
     * Met a jour les donnes du jeu
     *
     * @param dt Temps écoulé depuis le dernier update()
     */
    public void update(double dt) {



        // met a jour la position des bulles
        for (Bulle[] value : bulles) {
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = value[j];
                bulle.update(dt);
            }
        }

        // met a jour la position des balles envoyées par le requin
        if (balles != null) {
            for (Iterator<Balle> iterator = balles.iterator(); iterator.hasNext(); ) {
                Balle balle = iterator.next();
                balle.update(dt);
                // si la balle a atteint un rayon nul ou negatif, supprimer la balle du jeu
                if (balle.getRayon() <= 0) {
                    iterator.remove();
                }
            }
        }

        // si le joueur n'a plus de vies disponibles, le jeu est terminé
        if ( player.getNbVies() <= 0 ) {
            gameOver = true;
        }

        // ajouter un item dans le jeu de manière aléatoire dans le temps en randomisant selon une loi de probabilité
        // à chaque update et verifiant si on a obtenu la valeur randomisé
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


        // Pour tous les objets du jeu, si une balle envoyé par le requin rentre en collision avec un item alors les
        // effets de l'objet s'appliquent.
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            item.update(dt);
            for (Balle balle : balles) {
                item.testCollision(balle);
                if (item.estAttrape() && !item.isUsed()) {
                    if(item instanceof Heart) {
                        //Si c'est un coeur vert, rajouter une vie au joueur si sa vie n'est pas au max.
                        if (item.getId().equals("vie bonus")) {
                            if (player.getNbVies() < 3) {
                                player.setNbVies(player.getNbVies() + 1);
                            }
                        } else {
                            // Si c'est un coeur rouge, faire perdre une vie au joueur et remettre a 0 la serie en
                            // cours de poissons attrapés d'affilés.
                            if (!player.isInvicible()) {
                                player.setNbVies(player.getNbVies() - 1);
                                player.setSerie(0);
                            }
                        }
                    } else {
                        // Si c'est une balle,
                        if(sniperGame){
                            if(!player.isInvicible()) {
                                // augmenter le nombre de balles du joueur de 2 pour compenser la balle perdu en
                                // attrapant l'objet
                                player.setBalles(player.getBalles() + 2);
                            } else {
                                // augmenter seulement le nombre de balle de 1 parce que on ne perd aucune balle si
                                // invincibile
                                player.setBalles(player.getBalles() + 1);
                            }
                        }
                    }
                    item.setUsed(true);
                    balle.setAttrape(true);
                    iterator.remove(); // supprimer l'objet après son utilisation
                }
            }
            if(item.getLastTimeActivation()){ // si le requin n'a pas attrapé l'objet, il disparait au bout d'un
                iterator.remove();            // certain temps.
            }
        }



         // Pour tous les poissons du jeu, mettre a jour la position du poisson.
        if (fishes != null) {
            ArrayList<Fish> poubelle = new ArrayList<>();
            for (Iterator<Fish> iterator = fishes.iterator(); iterator.hasNext(); ) {
                Fish fish = iterator.next();
                fish.update(dt);
                // Si le poisson est en dehors des bornes possibles, cela veut dire que le poisson a traversé l'ecran
                if((fish.getX()<-fish.largeur)||(fish.getX()>Jeu.WIDTH)){
                    // il faut donc le supprimer du jeu
                    iterator.remove();
                    // et faire perdre au joueur une vie ainsi que remettre à 0 la série en cours
                    if(!player.isInvicible()) {
                        player.setNbVies(player.getNbVies() - 1);
                        player.setSerie(0);
                    }
                }
                // Si le poisson est un appât et qu'il est en dehors des bornes possibles, cela veut dire que l'appât
                // n'a pas trompé le requin et il faut le supprimer
                if(!fish.isFood()){
                    if(fish.getY() < -fish.getHauteur()){
                        iterator.remove();
                    }
                }
                // Si le poisson est un requin noir aux yeux rouges,
                if(fish instanceof Predator) {
                    for (Iterator<Fish> iterator2 = fishes.iterator(); iterator2.hasNext(); ) {
                        Fish other = iterator2.next();
                        if(fish != other) {
                            if(!(other instanceof Appat)) {
                                // s'il rencontre un autre poisson sur son passage
                                if (((Predator)fish).intersects(other) && other.isLeftOfScreen() !=
                                        fish.isLeftOfScreen() ) {
                                    // mettre le poisson rencontré dans une liste "poubelle" afin qu'il soit supprimé
                                    // plus tard
                                    poubelle.add(other);
                                    // et faire perdre une vie au joueur et remettre la serie en cours à 0
                                    if(!player.isInvicible()){
                                        player.setNbVies(player.getNbVies()-1);
                                    }
                                    player.setSerie(0);
                                }
                            }
                        }
                    }
                }
                // Pour toutes les balles lancées 
                for (Balle balle : balles) {
                    // vérifier si elle collide un poisson
                    fish.testCollision(balle);
                    // si oui le poisson est attrapé
                    if (fish.estAttrape()) {
                        if((!fish.isFood())) {
                            // si le poisson attrapé est un appât
                            if(!player.isInvicible()) {
                                // faire perdre au joueur 2 vies
                                player.setNbVies(player.getNbVies() - 2);
                            } else {
                                // et remettre la serie à 0
                                player.setSerie(0);
                            }
                        } else {
                            // sinon augmenter le score de 1 et la série de 1
                            if(!gameOver) {
                                player.setPoints(player.getPoints() + 1);
                                player.setSerie(player.getSerie() + 1);
                            }
                        }
                        // mettre ensuite la poisson attrapé dans la liste poubelle pour le supprimer plus tard
                        poubelle.add(fish);
                        balle.setAttrape(true);
                    }
                    // et (après une balle lancée) pour chaque poisson du jeu
                    for (Fish f: fishes) {
                        // vérifier si le poisson est bien dans l'écran et pas juste en dehors
                        if (f.getX() >= 0 && f.getX() <= Jeu.WIDTH - f.getLargeur()) {
                            if (f.getY() >= 0 && f.getY() <= Jeu.HEIGHT - f.getHauteur()) {
                                // et si c'est un saumon
                                if (f instanceof Salmon) {
                                    // qui n'est pas en vitesse maximale
                                    if (!((Salmon) f).isMaxSpeed()) {
                                        // alors le faire accélérer
                                        f.setVX(2 * f.getVX());
                                        ((Salmon) f).setMaxSpeed(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Enfin supprimer tous les poissons du jeu de la liste poubelle
            fishes.removeAll(poubelle);
        }

        // Pour toutes les balles qui n'ont rien attrapé du tout, remettre la série à 0
        for (Balle balle : balles) {
            if(!balle.aAttrape()){
                player.setSerie(0);
            }
        }

        // Si le joueur a fait une série de 10 poissons d'affilés (ou 11/12 poissons d'affilés si jamais il attrapent
        // 2/3 poissons en une prise)
        if(player.getSerie() != 0) {
            if ( !serieActivated && player.getSerie() % 10  == 0 ||player.getSerie() % 11  == 0
                    ||player.getSerie() % 12  == 0) {
                // alors le joueur est invincible
                player.setInvicible(true);
                setSerieActivated(true);
            }
        }

        // Si on a atteint un palier de 5, on est prêt à changer de niveau
        if (player.getPoints()  == palier + 5) {
            firstChangeLevel = false;
        }
        // On affiche le nouveau level en faisant arrêter l'apparition de nouveaux poissons et en établissant le
        // prochain palier de 5
        if (player.getPoints() % 5 == 0 && !firstChangeLevel) {
            setStopNewFish(true);
            afficherLevel=true;
            firstChangeLevel = true;
            level +=  1;
            palier = player.getPoints();
        }

        // Si on n'a plus de balles disponibles, la partie est terminée.
        if(sniperGame){
            if(!player.isInvicible()) {
                if (player.getBalles() == 0) {
                    gameOver = true;
                }
            }
        }

    }

    // ----------------------------------------------------------------------------------------------------------------

    /**
     * Dessine toutes les formes de l'animation dans le contexte
     * graphique du canvas
     *
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {

        // Dessiner le background bleu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);


        // Dessiner les bulles
        for (Bulle[] value : bulles) {
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = value[j];
                bulle.draw(context);
            }
        }

        // Dessiner les poissons
        if (fishes != null) {
            for (Fish fish : fishes) {
                fish.draw(context);
            }
        }

        // Dessiner les balles
        if (balles != null) {
            for (Balle balle: balles) {
                balle.draw(context);
            }
        }

        // Dessiner les objets
        for (Item item: items) {
            item.draw(context);
        }

        // Dessiner le score
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        context.setFont(Font.font(25));
        if(!modeInvicible) {
            context.setFill(Color.WHITE);
        } else {
            context.setFill(Color.rgb(126,211,33)); // si on est invincible, l'afficher en vert
        }
        context.fillText(""+player.getPoints(), WIDTH/2.0, 60);


        // Dessiner le nombre de la série en cours
        context.setTextAlign(TextAlignment.LEFT);
        context.setFont(Font.font(17));
        context.setFill(Color.WHITE);
        context.fillText("Série: "+ player.getSerie(), 10, 20);

        // Dessiner le nombre de balles restantes
        if(sniperGame){
            context.fillText("Balles restantes: "+ player.getBalles(), 10, 45);
        }

        // Dessiner les vies sous forme d'images
        Image image;
        if(!modeInvicible) {
            image = new Image("fish/00.png");
        } else {
            image = new Image("fish/invincible.png");  // si on est invincible, afficher le poisson en vert
        }
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
