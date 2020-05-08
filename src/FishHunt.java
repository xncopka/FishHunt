import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/** 8 mai 2020
 * Jeu en interface graphique,ou le joueur incarne un requin qui chasse des poissons
 * pour son souper.Au bout de 3 poissons ratés, la partie est perdue.
 * Le jeu se contrôle à la souris et continue à l’infini, ou jusqu’à ce que 3 poissons
 * aient été ratés
 * @author Thomas Bui
 * @author Vanda Gaonac'h-Lovejoy, 1018781
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

    // Conteneurs d’éléments de la fenêtre de jeu
    private StackPane root;

    // Temps qui s’est écoulé depuis le dernier appel de la fonction handle
    private double deltaTime;

    // Texte du Game Over
    private Text over;

    // Texte du niveau
    private Text level;

    // si on affiche pour la premiere fois le level
    private boolean firstTimeLevelActivation = false;

    // si on a cliqué sur une touche faisant parti des raccourcis clavier au nombre de 14
    private boolean [] firstClics = new boolean[14];

    // si on passe de la fenêtre de jeu à celle du score
    private boolean gameToScore = false;

    // si le jeu précédent était un jeu de type spécial
    private boolean prevGameSpecial;

    // Stage principal
    private Stage primaryStage;

    // Arraylists contenant les 10 meilleurs scores pour le mode normal et spécial
    private ArrayList<String> meilleursScores ;
    private ArrayList<String> meilleursScoresSpecial;

    // Musique de l'ecran d'accueil
    private MusicGame backgroundMusic;

    // HBox contenant les textes du début d'invincibilité et de la fin d'invincibilité
    private HBox debutInvinc;
    private HBox finInvinc;

    // Si on doit afficher le message d'erreur du séparateur " - "
    private boolean printErreur;

    // Si il y a du son
    private boolean speakerOn;



    /** Méthode main de FishHunt
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
        Image icone = new Image("gui/shark-jaws.png");


        this.primaryStage = primaryStage;


        // titre de la fenetre
        primaryStage.setTitle("Fish Hunt");

        this.speakerOn = true;
        // On est à l'accueil en lancant le jeu
        primaryStage.setScene(creerAccueil());

        // musique de background au lancement du jeu

        String filepath = "src/music/Aqua Road - Shining Sea.mp3";
        backgroundMusic = new MusicGame();
        backgroundMusic = new MusicGame();
        backgroundMusic.playMusic(filepath);


        // fenetre non resizable
        primaryStage.setResizable(false);
        // ajouter l'icone dans la barre des taches
        primaryStage.getIcons().add(icone);

        primaryStage.show();


    }

    /**
     * Cree la scène d'accueil
     * @return cette scène
     */
    private Scene creerAccueil() {

        // conteneur générique
        Pane root = new Pane();

        // Background bleu du jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        context = canvas.getGraphicsContext2D();
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        context.drawImage(new Image("gui/logo.png"), 100, 40, 440, 300);

        // Bouton pour lancer une nouvelle partie
        Button btn1 = new Button("Nouvelle partie!");
        btn1.setLayoutX(280);
        btn1.setLayoutY(350);
        btn1.setPrefWidth(105);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(false));
        });

        // Bouton pour afficher les meilleurs scores
        Button btn2 = new Button("Meilleurs Scores");
        btn2.setLayoutX(280);
        btn2.setLayoutY(380);
        btn2.setPrefWidth(105);
        btn2.setOnAction((e) -> {
            primaryStage.setScene(creerSceneScores());
        });

        // Bouton pour lancer une partie du mode spécial
        Button btn3 = new Button("Mode Spécial");
        btn3.setLayoutX(280);
        btn3.setLayoutY(410);
        btn3.setPrefWidth(105);
        btn3.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(true));
        });

        // Bouton pour afficher la rubrique d'aide
        Image question = new Image(getClass().getResourceAsStream("gui/newQuestion.png"));
        Button btn4 = new Button();
        ImageView imageView = new ImageView(question);
        btn4.setGraphic(imageView);
        btn4.setLayoutX(600);
        btn4.setLayoutY(10);
        btn4.setPrefHeight(20);
        btn4.setPrefWidth(20);
        btn4.setOnAction((e) -> {
            primaryStage.setScene( instructionsJeu());
        });

        // Bouton pour afficer les crédits
        Button btn5 = new Button("Crédits");
        btn5.setLayoutX(280);
        btn5.setLayoutY(440);
        btn5.setPrefWidth(105);
        btn5.setOnAction((e) -> {
            primaryStage.setScene(creerCredits());
        });

        // ajouter tous les éléments aux conteneur générique
        root.getChildren().addAll(canvas, btn1, btn2, btn3, btn4, btn5);

        // crée le bouton pour couper ou allumer le son et l'ajoute au conteneur
        speaker(root);

        gameToScore = false;

        return new Scene(root);
    }

    /**
     * Crée la fenêtre des meilleurs scores
     * @return cette scène
     */
    private Scene creerSceneScores() {

        // Conteneur avec un top, center et bottom
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(25, 50, 50, 50));
        Scene scene = new Scene(mainPane, WIDTH, HEIGHT);


        // Hbox contenant le titre de la fenêtre
        HBox node2 = new HBox();
        Text titre = new Text("Meilleurs Scores");
        titre.setFont(Font.font(32));
        node2.getChildren().add(titre);

        // et en faire le top du borderpane
        mainPane.setTop(node2);
        node2.setAlignment(Pos.TOP_CENTER);
        Insets insets = new Insets(15);
        BorderPane.setMargin(node2, insets);



        // Lire les fichiers des meilleurs scores
        FileReader fileReader = null;
        FileReader fileReader2 = null;
        try {
            fileReader = new FileReader("src/scores/highScore.txt");
            fileReader2 = new FileReader("src/scores/highScore2.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        // Titre de la listview pour le mode normal
        Text titreListView = new Text("Mode Normal");

        // mettre dans l'arraylist les données du fichier pour ensuite les mettre dans la listview correspondante
        meilleursScores=getMeilleursScores(fileReader) ;
        ListView<String> listView = new ListView<>();
        listView.getItems().setAll(meilleursScores);

        // mettre le titre et la list view dans un Vbox
        VBox listAndTitre = new VBox();
        listAndTitre.setAlignment(Pos.CENTER);
        listAndTitre.setSpacing(2);
        listAndTitre.getChildren().setAll(titreListView, listView);

        // Et repeter la meme chose pour le mode spécial
        Text titreListView2 = new Text("Mode Spécial");

        meilleursScoresSpecial=getMeilleursScores(fileReader2) ;
        ListView<String> listView2 = new ListView<>();
        listView2.getItems().setAll(meilleursScoresSpecial);

        VBox listAndTitre2 = new VBox();
        listAndTitre2.setAlignment(Pos.CENTER);
        listAndTitre2.setSpacing(2);
        listAndTitre2.getChildren().setAll(titreListView2, listView2);


        // mettre les deux Vbox dans un Hbox
        HBox conteneurListView = new HBox();
        conteneurListView.setSpacing(25);
        conteneurListView.setAlignment(Pos.CENTER);
        conteneurListView.setPadding(new Insets(10, 0, 10, 0));
        conteneurListView.getChildren().addAll(listAndTitre, listAndTitre2);

        // et en faire le centre du borderpane
        mainPane.setCenter(conteneurListView);


        // Vbox contenant le bottom du borderpane
        VBox node3 = new VBox();


        if(gameToScore) {
            // choisir la bonne listview
            ArrayList<String> checkArrayList;
            if (!prevGameSpecial) {
                checkArrayList = meilleursScores;
            } else {
                checkArrayList = meilleursScoresSpecial;
            }
            // vérifier si on doit ajouter un nouveau score à la listview correspondante
            if (controleur.checkNewScore(controleur.getScore(), checkArrayList)) {
                // Si oui, ajouter un Hbox permettant d'enregistrant le score avec son nom
                HBox hBoxTemp = new HBox();
                Label label1 = new Label("Votre nom:");
                Label label2 = new Label("a fait " + controleur.getScore() + " points!");
                Button btn2 = new Button("Ajouter!");
                TextField textField = new TextField();

                // Action du bouton ajouter
                btn2.setOnAction((e) -> {
                    if (!(textField.getText().equals(""))) {

                        // Si le nom ajouté par le joueur contient le séparateur " - "
                        if (textField.getText().contains(" -") || textField.getText().contains("- ")) {
                            // alors le message d'erreur doit être affiché
                            printErreur = true;
                            primaryStage.setScene(creerSceneScores());

                        } else {
                            // trier l'arraylist avec l'ajout du nouveau score et écrire l'arraylist trié dans
                            // le fichier
                            if (!prevGameSpecial) {
                                int indexScore = controleur.trierScore(controleur.getScore(), meilleursScores, textField.getText());
                                writeScore(indexScore, meilleursScores, "src/scores/highScore.txt");
                            } else {
                                int indexScore = controleur.trierScore(controleur.getScore(), meilleursScoresSpecial, textField.getText());
                                writeScore(indexScore, meilleursScoresSpecial, "src/scores/highScore2.txt");
                            }
                            // retour à l'accueil
                            primaryStage.setScene(creerAccueil());
                        }
                    }
                });
                // ajouter tous les éléments à l'Hbox
                hBoxTemp.getChildren().addAll(label1, textField, label2, btn2);
                hBoxTemp.setSpacing(10);
                hBoxTemp.setAlignment(Pos.CENTER);
                // ajouter le Hbox au Vbox
                node3.getChildren().addAll(hBoxTemp);

                // Si le message d'erreur doit être affiché, un nouveau label doit être ajouté au Vbox
                if(printErreur == true) {
                    Label erreur = new Label("Votre nom ne doit pas contenir le séparateur \" - \". Veillez réessayer s’il vous plaît.");
                    erreur.setTextFill(Color.RED);
                    node3.getChildren().add(erreur);
                }
            }
        }

        // Bouton pour revenir au menu
        Button btn1 = new Button("Menu");
        // ajouter ce menu au Vbox
        node3.getChildren().add(btn1);
        node3.setSpacing(10);
        node3.setAlignment(Pos.CENTER);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        // faire du Vbox le bottom du borderpane
        mainPane.setBottom(node3);

        return scene;
    }

    /**
     * Créer la scene pour le jeu
     * @param modeSpecial booléen qui vérifie si c'est un mode spécial du jeu ou non
     * @return la scène
     */
    private Scene creerFenetreJeu(boolean modeSpecial) {

        backgroundMusic.stopMusic();

        root = new StackPane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        // Contexte graphique du canvas
        context = canvas.getGraphicsContext2D();

        // Cible
        Pane pane = new Pane();
        Image img = new Image("gui/cible.png");
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        pane.getChildren().add(imageView);
        root.getChildren().add(pane);

        // Debut du jeu
        startGame(modeSpecial, speakerOn);
        newTimer();

        // deplace la cible quand on deplace la souris
        scene.setOnMouseMoved((event) -> {
            double x = event.getX() - 25;
            double y = event.getY() - 25;
            imageView.setX(x);
            imageView.setY(y);
            
        });

        // lance une balle quand on clique sur la souris et fait perdre une balle
        scene.setOnMouseClicked((event) -> {
            controleur.newBall(event.getX(), event.getY());
            if(controleur.getSniperGame()) {
                if(!controleur.getModeInvicible()){
                controleur.setBalles(controleur.getBalles() - 1);
                }
            }
        });



        // Actions sur le clavier en appuyant sur la touche
        scene.setOnKeyPressed((event) -> {

            // Quitter l'application si on appuie sur Echap
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }


            // Faire monter le niveau de +1 si on appuie sur H
            if (event.getCode() == KeyCode.H && !firstClics[0]) {
                controleur.setLevel(controleur.getLevel()+1);
                controleur.setAfficherLevel(true);
                firstClics[0]=true;

            }

            // Faire monter le score de +1 si on appuie sur J
            if (event.getCode() == KeyCode.J && !firstClics[1]) {
                controleur.setScore(controleur.getScore()+1);
                firstClics[1]=true;
            }

            // Faire monter le nombre de poissons restants de +1 si on appuie sur K
            if (event.getCode() == KeyCode.K && !firstClics[2]) {
                controleur.setLife(controleur.getLife()+1);
                firstClics[2]=true;
            }

            // Faire perdre la partie si on appuie sur L
            if (event.getCode() == KeyCode.L && !firstClics[3]) {
                controleur.setGameOver(true);
                firstClics[3]=true;
            }

            // Faire monter la série en cours de +1 si on appuie sur S
            if (event.getCode() == KeyCode.S && !firstClics[4]) {
                controleur.setSerie(controleur.getSerie()+1);
                firstClics[4]=true;
            }

            // Couper ou allumer le son si on appuie sur O
            if (event.getCode() == KeyCode.O && !firstClics[5]) {
                speakerOn = !speakerOn;
                controleur.enableChanson(speakerOn);
                firstClics[5]=true;
            }

            // Redémarrer la partie si on appuie sur R
            if (event.getCode() == KeyCode.R  && !firstClics[6]) {
                restart();
                firstClics[6]=true;
            }

            // Revenir à l'accueil si on appuie sur A
            if (event.getCode() == KeyCode.A && !firstClics[7]) {
                timer.stop();
                deltaTime = 0;
                primaryStage.setScene(creerAccueil());
                if(speakerOn) {
                    controleur.getChanson().stopMusic();
                    String filepath = "src/music/Aqua Road - Shining Sea.mp3";
                    backgroundMusic = new MusicGame();
                    backgroundMusic.playMusic(filepath);
                }
                firstClics[7]=true;
            }

            // Faire monter le nombre de balles restantes de +1 si on appuie sur B
            if (event.getCode() == KeyCode.B  && !firstClics[8]) {
                if(controleur.getSniperGame()) {
                    controleur.setBalles(controleur.getBalles()+1);
                }
                firstClics[8]=true;
            }

            // Faire apparaître un appat si on appuie sur T
            if (event.getCode() == KeyCode.T  && !firstClics[9]) {
                controleur.newAppat(controleur.getLevel());
                firstClics[9]=true;
            }

            // Faire apparaître un crabe si on appuie sur C
            if (event.getCode() == KeyCode.C  && !firstClics[10]) {
                controleur.newCrab(controleur.getLevel());
                firstClics[10]=true;
            }

            // Faire apparaître une étoile de mer si on appuie sur E
            if (event.getCode() == KeyCode.E  && !firstClics[11]) {
                controleur.newStar(controleur.getLevel());
                firstClics[11]=true;
            }

            // Faire apparaître un saumon si on appuie sur S
            if (event.getCode() == KeyCode.V  && !firstClics[12]) {
                controleur.newSalmon(controleur.getLevel());
                firstClics[12]=true;
            }

            // Faire apparaître un prédateur si on appuie sur P
            if (event.getCode() == KeyCode.P  && !firstClics[13]) {
                controleur.newPredator(controleur.getLevel());
                firstClics[13]=true;
            }


        });


        // Actions sur le clavier en relachant la touche
        // Interdit le déclenchement à la suite des raccourcis du clavier en maintenant enfoncé la touche
        scene.setOnKeyReleased((event) -> {



            if (event.getCode() == KeyCode.H) {
                firstClics[0]=false;  

            }


            if (event.getCode() == KeyCode.J) {
                firstClics[1]=false;
            }


            if(event.getCode() == KeyCode.K) {
                firstClics[2]=false;
            }
            if (event.getCode() == KeyCode.L){
                firstClics[3]=false;
            }

            if (event.getCode() == KeyCode.S){
                firstClics[4]=false;
            }

            if (event.getCode() == KeyCode.O){
                firstClics[5]=false;
            }

            if (event.getCode() == KeyCode.R){
                firstClics[6]=false;
            }

            if (event.getCode() == KeyCode.A){
                firstClics[7]=false;
            }

            if (event.getCode() == KeyCode.B){
                firstClics[8]=false;
            }

            if (event.getCode() == KeyCode.T){
                firstClics[9]=false;
            }

            if (event.getCode() == KeyCode.C){
                firstClics[10]=false;
            }

            if (event.getCode() == KeyCode.E){
                firstClics[11]=false;
            }

            if (event.getCode() == KeyCode.V){
                firstClics[12]=false;
            }

            if (event.getCode() == KeyCode.P){
                firstClics[13]=false;
            }


        });
        return scene;
    }


    /**
     * Crée la 1ere page de la rubrique d'aide
     * Cette page décrit de manière brève le jeu
     * @return la scène
     */
    private Scene instructionsJeu() {

        // Conteneur qui contient un top, un right, un center et un bottom
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Background bleu
        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(25));

        // Le top est le titre
        Label titre = new Label("Description :");
        titre.setTextFill(Color.WHITE);
        titre.setTextAlignment(TextAlignment.CENTER);
        titre.setFont(Font.font ("Verdana", 32));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);


        // Le center est le texte
        Label label = new Label("Vous incarnez un requin qui chasse des poissons pour son souper.\n\n" +
                "Étant un requin gourmand, vous ne pouvez pas vous permettre de laisser trop de poissons passer... Au bout de 3 poissons ratés, la\n" +
                "partie est perdue.\n\n" +
                "Le jeu se contrôle à la souris et continue à l’infini, ou jusqu’à ce que 3 poissons aient été ratés (selon lequel arrive en premier)."+
                "\n\nSi vous vous sentez courageux, vous pouvez faire le mode spécial."+
                        " Mais attention! Vous aurez un nombre limité de balles!" );
        label.setFont(Font.font ("Verdana", 14));
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setWrapText(true);
        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);

        // Le bouton du menu est le bottom
        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);
        btn1.setPrefHeight(20);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });
        root.setBottom(btn1);
        BorderPane.setAlignment(btn1, Pos.CENTER);

        // Le bouton pour passer à la page suivante est le right
        Image next = new Image(getClass().getResourceAsStream("gui/next.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(next));
        btn2.setPrefHeight(20);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(instructions2Jeu());
        });


        root.setRight(btn2);
        BorderPane.setAlignment(btn2, Pos.BOTTOM_RIGHT);


        return scene;
    }

    /**
     * Crée la 2nde page de la rubrique d'aide
     * Cette page explique les différents poissons spéciaux du jeu
     * @return la scène
     */
    private Scene instructions2Jeu() {

        // Conteneur qui contient un top, un left, un right, un center et un bottom
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Background bleu
        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(25));

        // Le top est le titre
        Label titre = new Label("Poissons Spéciaux :");
        titre.setTextFill(Color.WHITE);
        titre.setTextAlignment(TextAlignment.CENTER);
        titre.setFont(Font.font ("Verdana", 32));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);

        // Le center est le texte
        Label label = new Label(
                "Le crabe et l'étoile de mer ont une façon différente des autres de se déplacer." +
                        " Le crabe avance en oscillant horizontalement." +
                        " L'étoile de mer avance en oscillant verticalement.\n\n" +
                        "Le saumon prend peur et accélere quand le requin essaye d'attraper quelque chose dans les parages.\n\n"+
                        "Un requin noir aux yeux rouges sévit dans l'océan et attrape les poissons qu'ils rencontrent. " +
                        "Ne laissez pas ce prédateur manger ces poissons. Vous perdrez une vie pour chaque poisson qu'il mange!\n\n"+
                        "Certains poissons sont trompeurs. Ce sont des appâts qu'utilisent les pêcheurs pour vous attraper. " +
                        "Ne les attrapez pas sinon ils vous feront perdre 2 vies! "
        );
        label.setFont(Font.font ("Verdana", 14));
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setWrapText(true);
        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);

        // Le bouton du menu est le bottom
        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);
        btn1.setPrefHeight(20);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });
        root.setBottom(btn1);
        BorderPane.setAlignment(btn1, Pos.CENTER);


        // Le bouton pour passer à la page suivante est le right
        Image next = new Image(getClass().getResourceAsStream("gui/next.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(next));
        btn2.setPrefHeight(20);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(instructions3Jeu());
        });

        root.setRight(btn2);
        BorderPane.setAlignment(btn2, Pos.BOTTOM_RIGHT);


        //  Le bouton pour passer à la page précédente est le left
        Image previous = new Image(getClass().getResourceAsStream("gui/back.png"));
        Button btn3 = new Button();
        btn3.setGraphic(new ImageView(previous));
        btn3.setPrefHeight(20);
        btn3.setOnAction((e) -> {
            primaryStage.setScene( instructionsJeu());
        });

        root.setLeft(btn3);
        BorderPane.setAlignment(btn3, Pos.BOTTOM_LEFT);

        return scene;
    }


    /**
     * Crée la 3ème page de la rubrique d'aide
     * Cette page explique les objets et l'effet d'invincibilité après une série de poissons attrapés d'affilés
     * @return la scène
     */
    private Scene instructions3Jeu() {

        // Conteneur qui contient un top, un left, un right, un center et un bottom
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Background bleu
        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(25));

        // Le titre est le top
        Label titre = new Label("Objets et Série :");
        titre.setTextFill(Color.WHITE);
        titre.setFont(Font.font ("Verdana", 32));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);

        // Le texte est le center
        Label label = new Label(
                "Divers objets aparraissent de manière aléatoire durant la partie. Il vous est possible de les attraper." +
                        " Les coeurs vert avec un + " +
                        "fait gagner une vie tandis que les coeurs rouge avec un - en fait perdre une.\n" +
                        "Des balles noires apparaissent de manière régulière durant le mode spécial. Attrapez-lez sinon vous" +
                        " allez être à cours de munitions très rapidement!\n\n" +
                        "Visez juste et vous allez être récompensé! A la suite d'une série de 10 poissons attrapés d'affilés, le requin devient invincible à d'eventuelles" +
                        " pertes ou blessures pendant 10 secondes.");


        label.setFont(Font.font ("Verdana", 14));
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setWrapText(true);
        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);

        // Le bouton du menu est le bottom
        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);
        btn1.setPrefHeight(20);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });
        root.setBottom(btn1);
        BorderPane.setAlignment(btn1, Pos.CENTER);


        // Le bouton pour passer à la page suivante est le right
        Image next = new Image(getClass().getResourceAsStream("gui/next.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(next));
        btn2.setPrefHeight(20);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(instructions4Jeu());
        });
        root.setRight(btn2);
        BorderPane.setAlignment(btn2, Pos.BOTTOM_RIGHT);


        // Le bouton pour passer à la page précédente est le left
        Image previous = new Image(getClass().getResourceAsStream("gui/back.png"));
        Button btn3 = new Button();
        btn3.setGraphic(new ImageView(previous));
        btn3.setPrefHeight(20);
        btn3.setOnAction((e) -> {
            primaryStage.setScene( instructions2Jeu());
        });

        root.setLeft(btn3);
        BorderPane.setAlignment(btn3, Pos.BOTTOM_LEFT);

        return scene;
    }


    /**
     * Crée la derniere page de la rubrique d'aide
     * Cette page explique les raccourcis claviers possible du jeu
     * @return la scène
     */
    private Scene instructions4Jeu() {

        // Conteneur qui contient un top, un left, un center et un bottom
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Background bleu
        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(25));

        // Le titre est le top
        Label titre = new Label("Raccourcis Clavier :");
        titre.setTextFill(Color.WHITE);
        titre.setFont(Font.font ("Verdana", 32));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);


        // Le texte est le center
        Label label = new Label(
                "Les raccourcis suivants sont disponibles durant la partie :\n\n" +
                        "- H pour faire monter le niveau de +1 \n" +
                        "- J pour faire monter le score de +1  \n" +
                        "- K pour faire monter le nombre de poissons restants de +1\n" +
                        "- S pour faire monter la série en cours de +1\n" +
                        "- B pour faire monter le nombre de balles restantes de +1 \n" +
                        "- C pour faire apparaître un crabe\n"+
                        "- E pour faire apparaître une étoile de mer\n"+
                        "- V pour faire apparaître un saumon\n"+
                        "- T pour faire apparaître un appat\n"+
                        "- P pour faire apparaître un prédateur\n"+
                        "- O pour couper ou allumer le son\n" +
                        "- R pour faire redémarrer la partie\n" +
                        "- L pour faire perdre la partie\n" +
                        "- A pour revenir à l'accueil \n" +
                        "- ECHAP pour quitter le jeu"


        );
        label.setFont(Font.font ("Verdana", 14));
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.LEFT);
        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);


        // Le bouton du menu est le bottom
        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });
        root.setBottom(btn1);
        BorderPane.setAlignment(btn1, Pos.CENTER);

        // Le bouton pour passer à la page précédente est le left
        Image previous = new Image(getClass().getResourceAsStream("gui/back.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(previous));
        btn2.setPrefHeight(20);
        btn2.setOnAction((e) -> {
            primaryStage.setScene( instructions3Jeu());
        });

        root.setLeft(btn2);
        BorderPane.setAlignment(btn2, Pos.BOTTOM_LEFT);


        return scene;
    }


    /**
     * Crée la scène des crédits spécifiant les auteurs, les images utilisés et la musique utilisée
     * @return la scène
     */
    private Scene creerCredits() {

        // Borderpane qui contient un top, un center et un bottom
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Background bleu
        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(50));


        // Le titre est le top
        Label titre = new Label("Crédits");
        titre.setTextFill(Color.WHITE);
        titre.setTextAlignment(TextAlignment.CENTER);
        titre.setFont(Font.font ("Verdana", 40));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);


        // Vbox qui contient le center
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(50));
        vbox.setSpacing(2);

        Label realisation =  new Label("Réalisation :");
        realisation.setFont(Font.font ("Verdana", 18));
        realisation.setTextFill(Color.WHITE);
        realisation.setTextAlignment(TextAlignment.CENTER);
        realisation.setAlignment(Pos.CENTER);

        Label credits = new Label("Thomas Bui\nVanda Lovejoy");
        credits.setFont(Font.font ("Verdana", 14));
        credits.setTextFill(Color.WHITE);
        credits.setTextAlignment(TextAlignment.LEFT);
        credits.setAlignment(Pos.CENTER);

        HBox separateur = new HBox();
        separateur.setPrefHeight(14);

        Label musique = new Label("Musique :");
        musique.setFont(Font.font ("Verdana", 18));
        musique.setTextFill(Color.WHITE);
        musique.setTextAlignment(TextAlignment.CENTER);
        musique.setAlignment(Pos.CENTER);

        Label chansons = new Label("Background : Aqua Road - Shining Sea\nJeu : Noisestorm - Crab Rave\n");
        chansons.setFont(Font.font ("Verdana", 14));
        chansons.setTextFill(Color.WHITE);
        chansons.setTextAlignment(TextAlignment.LEFT);
        chansons.setAlignment(Pos.CENTER);

        HBox separateur2 = new HBox();
        separateur2.setPrefHeight(14);

        Label image = new Label("Image :");
        image.setFont(Font.font ("Verdana", 18));
        image.setTextFill(Color.WHITE);
        image.setTextAlignment(TextAlignment.CENTER);
        image.setAlignment(Pos.CENTER);

        Label site = new Label("Game-icon.net");
        site.setFont(Font.font ("Verdana", 14));
        site.setTextFill(Color.WHITE);
        site.setTextAlignment(TextAlignment.LEFT);
        site.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(realisation, credits, separateur, musique, chansons, separateur2, image, site);
        root.setCenter(vbox);

        // Le bouton du menu est le bottom
        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);
        btn1.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(btn1, Pos.CENTER);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        root.setBottom(btn1);

        return scene;
    }



    /**
     *  Reinitialise les valeurs du jeu au debut
     */
    public void startGame(boolean modeSpecial, boolean speakerOn) {
        controleur = new Controleur(modeSpecial, speakerOn);
        controleur.draw(context);
        printErreur = false;
        Arrays.fill(firstClics, false);
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
            private long firstTimeBonusFish = 0;
            private long firstTimeInvicible = 0;
            private long lastTimeInvicible = 0;
            private long firstTimeNewFish = 0;
            private long firstTimeGameOver = 0;

            private ArrayList<Long> firstTimeLevels = new ArrayList<>();




            // fonction appelée à chaque frame
            @Override
            public void handle(long now) {

                // Si dernier temps = 0, faire apparaitre le groupe de bulles
                if (lastTime == 0) {
                    lastTime = now;
                    firstTime = now;
                    firstTimeNewFish= now;
                    controleur.groupBulles();
                    return;
                }



                // Si 3 secondes se sont écoulés depuis le debut de l'animation, faire apparaitre un groupe de bulles
                if ((now - firstTime) >= ((long)3e+9)) {
                    firstTime = now;
                    controleur.groupBulles();

                }

                // faire apparaitre un poisson normal à chaque 3 secondes apres le debut du niveau
                if(((now - firstTimeNewFish) >= ((long)3e+9)) && (!controleur.getStopNewFish())) {
                    firstTimeNewFish = now;
                    controleur.newFish(controleur.getLevel());
                    controleur.setEnableItems(true);
                }


                // faire apparaitre un poisson spécial à chaque 5 seconde apres le debut d'un niveau
                if(controleur.getLevel()>=2 && !controleur.getStopNewFish()) {
                    if ((now - firstTime5Sec) >= ((long) 5e+9)) {
                        firstTime5Sec = now;
                        controleur.newSpecialFish(controleur.getLevel());
                    }
                }


                if(controleur.getLevel()>=3 && controleur.getStopNewFish()) {
                    firstTimeBonusFish = now;
                }

                // faire apparaitre un nouveau poisson special
                if(controleur.getLevel()>=3 && !controleur.getStopNewFish()) {
                    if ((now - firstTimeBonusFish) >= ((long) 2.5e+9)) {
                        firstTimeBonusFish = now;
                        controleur.newBonusFish(controleur.getLevel());
                    }
                }

                // faire apparaitre un objet
                if(!controleur.getItem().isEmpty()){
                    for (Item item: controleur.getItem()) {
                        if(!item.getFirstTimeActivation()) {
                            item.setFirstTime(now);
                            item.setFirstTimeActivation(true);
                        }
                        // faire disparaitre un objet
                        if ((now - item.getFirstTime()) >= ((long) 1.5e+9)) {
                            item.setLastTimeActivation(true);
                        }                                                      
                    }
                }

                // faire apparaitre le texte de début d'invincibilité
                if(controleur.isInvicible() & !controleur.getModeInvicible()){
                    firstTimeInvicible = now;
                    controleur.setModeInvicible(true);
                    textDebutInvincible();
                }

                // faire disparaitre le texte de début d'invincibilité
                if(firstTimeInvicible>0) {
                    if ((now - firstTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(debutInvinc);
                    }
                }

                // faire apparaitre le texte de fin d'invincibilité
                if (lastTimeInvicible>0) {
                    if ((now - lastTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(finInvinc);
                        lastTimeInvicible = 0;
                        controleur.setSerieActivated(false);
                    }
                }

                // faire disparaitre le texte de début d'invincibilité
                if(now-firstTimeInvicible >= ((long) 10e+9) && controleur.getModeInvicible()) {
                    controleur.setInvicible(false);
                    controleur.setModeInvicible(false);
                    textFinInvincible();
                    firstTimeInvicible = 0;
                    lastTimeInvicible = now;
                }


                // afficher le texte du level
                if (controleur.getAfficherLevel() & !firstTimeLevelActivation){
                    firstTimeLevelActivation = true;
                    controleur.setAfficherLevel(false);
                    long firstTimeLevel = now;
                    firstTimeLevels.add(firstTimeLevel);
                    textLevel();
                }

                // fait disparaitre les textes du level (si on appuie en repete sur H par exemple)
                if(!firstTimeLevels.isEmpty()){
                    for (Iterator<Long> iterator = firstTimeLevels.iterator(); iterator.hasNext(); ) {
                        long firstTimeLevel = iterator.next();

                        if ((now - firstTimeLevel) >= ((long)3e+9)) {
                            root.getChildren().remove(level);
                            iterator.remove();
                            firstTimeLevelActivation = false;
                            controleur.setStopNewFish(false);
                            firstTimeNewFish=0;
                        }
                    }
                }


                // faire apparaitre le texte du Game Over
                if (controleur.getGameOver() && !gameToScore) {

                    if(controleur.getSniperGame()) {
                        prevGameSpecial = true;
                    } else {
                        prevGameSpecial = false;
                    }
                    textOver();
                    firstTimeGameOver = now;
                    gameToScore = true;

                }


                //Si cela fait plus de 3 secondes que la partie est finie, allez aux meilleurs scores
                if(firstTimeGameOver > 0) {
                    if (now - firstTimeGameOver >= (long) 3e+9) {
                        timer.stop();
                        deltaTime = 0;
                        firstTimeGameOver = 0;
                        if(speakerOn) {
                            controleur.getChanson().stopMusic();
                            String filepath = "src/music/Aqua Road - Shining Sea.mp3";
                            backgroundMusic = new MusicGame();
                            backgroundMusic.playMusic(filepath);
                        }
                        primaryStage.setScene(creerSceneScores());

                    }
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
        if(speakerOn) {
            controleur.getChanson().stopMusic();
        }
        timer.stop();
        deltaTime =0;
        startGame(controleur.getSniperGame(), speakerOn);
        newTimer();

    }


    /**
     * Cree le texte du Game Over et l'ajoute à la racine
     */
    public void textOver(){
        root.getChildren().remove(level);
        over = new Text("GAME OVER");
        over.setFill(Color.RED);
        over.setFont(Font.font(50));
        over.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(over);
    }


    /**
     * Cree le texte du level et l'ajoute à la racine
     */
    public void textLevel(){
        level = new Text("Level " + controleur.getLevel());
        level.setFill(Color.WHITE);
        level.setFont(Font.font(50));
        level.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(level);
    }

    /**
     * Cree le texte de début d'invincibilité et l'ajoute à la racine
     */
   public void textDebutInvincible(){
       Text invincible = new Text("Serie de "+ controleur.getSerie() +" atteinte :\ndebut invincibilité");
        invincible.setFill(Color.rgb(126,211,33));
        invincible.setFont(Font.font(20));
        invincible.setTextAlignment(TextAlignment.CENTER);
         debutInvinc = new HBox(invincible);
       debutInvinc.setAlignment(Pos.TOP_RIGHT);
       debutInvinc.setPadding(new Insets(10));
        root.getChildren().add(debutInvinc);
    }


    /**
     * Cree le texte de fin d'invincibilité et l'ajoute à la racine
     */
    public void textFinInvincible(){
        Text invincible = new Text("10 sec écoulées :\nfin invincibilité");
        invincible.setFill(Color.RED);
        invincible.setFont(Font.font(20));
        invincible.setTextAlignment(TextAlignment.CENTER);;
         finInvinc = new HBox(invincible);
        finInvinc.setAlignment(Pos.TOP_RIGHT);
        finInvinc.setPadding(new Insets(10));
        root.getChildren().add(finInvinc);
    }


    /**
     * Met les données du fichier dans un Arraylist
     * @param fileReader le fichier
     * @return l'arraylist
     */
    public ArrayList<String> getMeilleursScores(FileReader fileReader)  {
        BufferedReader reader;
        ArrayList<String> arrayList = new ArrayList<>() ;
        try {
            reader = new BufferedReader(fileReader);
            String ligne;
            while ((ligne = reader.readLine()) != null) {

                arrayList.add(ligne);
            }
            reader.close();

        } catch (IOException ex) {
            System.out.println("Erreur à l’ouverture du fichier");
        }

        return arrayList;
    }


    /**
     * Ecrit le nouveau fichier avec l'insertion du nouveau score
     * @param indexNewScore index du nouveau score
     * @param meilleursScores arraylist des meilleurs scores
     * @param adresse  adresse du fichier
     */
    public void writeScore(int indexNewScore, ArrayList<String> meilleursScores, String adresse) {
        try {
            FileWriter filewriter = new FileWriter(adresse, false);
            BufferedWriter writer = new BufferedWriter(filewriter);
     

            for (int i = 0; i < indexNewScore; i++) {
                writer.append(meilleursScores.get(i) + "\n");
            }
            writer.append(meilleursScores.get(indexNewScore) + "\n");
            if(indexNewScore<meilleursScores.size()-1) {
                for (int i = indexNewScore + 1; i < meilleursScores.size(); i++) {
                    writer.append(meilleursScores.get(i) + "\n");
                }
            }

            writer.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l’écriture du fichier");
        }
    }

    /**
     * Ajoute au pane le bouton du volume
     * @param pane conteneur
     */
    public void speaker(Pane pane) {
        Image img;
        if(speakerOn) {
            img = new Image(getClass().getResourceAsStream("gui/speaker-on.png"));
        } else {
            img = new Image(getClass().getResourceAsStream("gui/speaker-off.png"));
        }

        Button speaker = new Button();
        ImageView imageView = new ImageView(img);

        speaker.setGraphic(imageView);
        speaker.setLayoutX(10);
        speaker.setLayoutY(10);
        pane.getChildren().add(speaker);

        speaker.setOnAction((e) -> {
            if(speakerOn) {
                backgroundMusic.stopMusic();
                speakerOn = false;
                primaryStage.setScene(creerAccueil());
            } else {
                String filepath = "src/music/Aqua Road - Shining Sea.mp3";
                backgroundMusic = new MusicGame();
                backgroundMusic.playMusic(filepath);
                speakerOn = true;
                primaryStage.setScene(creerAccueil());
            }
        });

    }
}
