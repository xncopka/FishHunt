import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;




/**
 * Classe qui sert à définir ce qui doit être affiché (Vue)
 */
public class FishHunt extends Application {

    // Largeur et hauteur de la fenêtre
    public static final int WIDTH = 640, HEIGHT = 480;

    // Contrôleur de l'application
    private Controleur controleur;

    //  Contexte graphique du canvas
    private GraphicsContext context;

    // Classe anonyme servant à creer des animations
    private AnimationTimer timer;

    // Conteneur générique
    private Pane root;

    // Temps qui s’est écoulé depuis le dernier appel de la fonction handle
    private double deltaTime;

    // nombre de joueurs dans le jeu
    private int nbPlayers = 1; // à changer une fois que le mode multi est implenté

    // Texte du Game Over
    private Text over;

    private Text level;






    /** Méthode main de HighSeaTower
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Méthode start qui est redéfinie et sert à construire l’interface
     * @param primaryStage représente la fenetre de l'application
     * @throws Exception // FXMLLoader.load(...) throws an IOException
     */
    @Override
    public void start(Stage primaryStage) throws Exception {


        // icone de la barre de tache
        Image icone = new Image("/crabe.png");


        // racine
        root = new Pane();

        //  contenu graphique à afficher dans la fenêtre
        Scene scene = new Scene(root, WIDTH, HEIGHT);



        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        // Contexte graphique du canvas
        context = canvas.getGraphicsContext2D();


        Image img = new Image("/cible.png");
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        root.getChildren().add(imageView);

        // Debut du jeu
        startGame();
        newTimer();



        scene.setOnMouseMoved((event) -> {
            double x = event.getX() - 25;
            double y = event.getY() - 25;
            imageView.setX(x);
            imageView.setY(y);
            
        });

        scene.setOnMouseClicked((event) -> {
            controleur.newBall(event.getX(), event.getY());
        });




        // Actions sur le clavier en appuyant sur la touche
        scene.setOnKeyPressed((event) -> {

            // Quitter l'application si on appuie sur Echap
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }



            // Restart la partie en appuyant sur R
            if (event.getCode() == KeyCode.R) {
                restart();
            }


        });




        // titre de la fenetre
        primaryStage.setTitle("Fish Hunt");

        // ajouter la scene
        primaryStage.setScene(scene);

        // fenetre non resizable
        primaryStage.setResizable(false);

        // ajouter l'icone dans la barre des taches
        primaryStage.getIcons().add(icone);

        // afficher
        primaryStage.show();
    }



    /**
     *  Reinitialise les valeurs du jeu au debut
     */
    public void startGame() {
        controleur = new Controleur(nbPlayers);
        controleur.draw(context);
    }


    /**
     *  Cree un nouveau timer permettant de creer des animations
     */
    public void newTimer() {
        // Création de l'animation
        timer = new AnimationTimer() {


            // Initialiser dernier temps et premier temps
            private long lastTime = 0;
            private long firstTime = 0;
            private long firstTime5Sec = 0;
            private long firstTime10Sec = 0;
            private long firstTime15Sec = 0;
            private long firstTimeLevel = 0;
            private long firstTimeInvicible = 0;


            // fonction appelée à chaque frame
            @Override
            public void handle(long now) {

                // Si dernier temps = 0, faire apparaitre le groupe de bulles
                if (lastTime == 0) {
                    lastTime = now;
                    firstTime = now;
                    controleur.groupBulles();
                    return;
                }



                // Si 3 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un groupe de bulles et un poisson
                if ((now - firstTime) >= ((long)3e+9)) {
                    firstTime = now;
                    controleur.groupBulles();
                    controleur.newFish(controleur.getLevel());
                }

                // Si 5 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if(controleur.getLevel()>=2) {
                    if ((now - firstTime5Sec) >= ((long) 5e+9)) {
                        firstTime5Sec = now;
                        controleur.newSpecialFish(controleur.getLevel());
                    }
                }

                // Si 10 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if(controleur.getLevel()>=3) {
                    if ((now - firstTime10Sec) >= ((long) 10e+9)) {
                        firstTime10Sec = now;
                        controleur.newBadFish(controleur.getLevel());
                    }
                }

                // Si 5 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if(controleur.getLevel()>=4) {
                    if ((now - firstTime15Sec) >= ((long) 10e+9)) {
                        firstTime15Sec = now;
                        controleur.newFastFish(controleur.getLevel());
                    }
                }


                if(!controleur.getItem().isEmpty()){
                    for (Item item: controleur.getItem()) {
                        if(!item.getFirstTimeActivation()) {
                            item.setFirstTime(now);
                            item.setFirstTimeActivation(true);
                        }
                        if ((now - item.getFirstTime()) >= ((long) 1.5e+9)) {
                            item.setLastTimeActivation(true);
                        }
                    }
                }


                if(controleur.isInvicible() & !controleur.getModeInvicible()){
                    firstTimeInvicible = now;
                    controleur.setModeInvicible(true);
                }
                if(now-firstTimeInvicible >= ((long) 10e+9) && controleur.getModeInvicible()) {
                    controleur.setInvicible(false);
                    controleur.setModeInvicible(false);
                    controleur.setSerie(0);
                }




                // redemarre une partie si la partie est terminée
                if (getGameOver()) {

                    textOver();


                }
                        //players[0].getPoints() % 5 == 0 && firstChangeLevel==false
                if (controleur.getAfficherLevel() ){
                    controleur.setAfficherLevel(false);
                    firstTimeLevel = now;
                    textLevel();
                }

                if ((now - firstTimeLevel) >= ((long)0.5e+9)) {
                    root.getChildren().remove(level);
                    firstTimeLevel = 0;
                }




                // temps = (temps now - dernier temps) converti en seconde
                deltaTime = (now - lastTime) * 1e-9;


                // mettre a jour les nouvelles positions
                controleur.update(deltaTime);

                // dessiner le nouveau dessin
                controleur.draw(context);


                // mettre a jour le dernier temps
                lastTime = now;
            }
        };
        timer.start();
    }


    /**
     *  Permet de restart la partie
     */
    public void restart(){

        timer.stop();
        deltaTime =0;
        startGame();
        newTimer();

    }

    /**
     * Methode qui renvoit si la partie est terminée
     * @return un boolean
     */
    public boolean getGameOver() {
        return controleur.getGameOver();
    }



    /**
     * Methode qui renvoit si la partie est terminée
     * @return un boolean
     */
    public int getLevel() {
        return controleur.getLevel();
    }






    /**
     * Cree le texte du Game Over et l'ajoute à la racine
     */
    public void textOver(){
        over = new Text("GAME OVER");
        over.setFill(Color.RED);
        over.setFont(Font.font(50));
        over.setTextAlignment(TextAlignment.CENTER);
        over.setX(200);
        over.setY(210);
        root.getChildren().add(over);
    }


    /**
     * Cree le texte du level
     */
    public void textLevel(){
        level = new Text("Level " + getLevel());
        level.setFill(Color.WHITE);
        level.setFont(Font.font(100));
        level.setTextAlignment(TextAlignment.CENTER);
        level.setX(200);
        level.setY(210);
        root.getChildren().add(level);
    }







}
