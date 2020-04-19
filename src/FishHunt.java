import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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

        // Debut du jeu
        startGame();
        newTimer();



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
        controleur = new Controleur();
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

            // Initialiser la premiere et derniere fois que la meduse touche la tortue
            private long firstInter = 0;
            private long lastInter = 0;

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
                // faire apparaitre un groupe de bulles
                if ((now - firstTime) >= ((long)3e+9)) {
                    firstTime = now;
                    controleur.groupBulles();
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



}
