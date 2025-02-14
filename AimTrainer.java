import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;       //The StackPane implemented in this app is not working as intented. It isn't centering data by default in showResults() 
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.animation.PauseTransition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.Cursor;

public class AimTrainer extends Application {
    @Override
    public void start(Stage primaryStage){
        Scene menuScene = menuScene();

        //Handling Events
        Button play = (Button)(menuScene.lookup("#play_button"));   //Finds the node with ID "play_button", returns it and is then casted into Button type
        play.setOnMouseClicked(e -> {
            //if the PLAY button is left-clicked a new scene for the actual game is set
            if(e.getButton() == MouseButton.PRIMARY){
                Scene gameScene = gameScene();
                configureStage(primaryStage, gameScene);
                gameLogic(gameScene, primaryStage);
            }
        });
        play.setOnMouseEntered(e -> {
            play.setStyle(
                play.getStyle().replace("-fx-text-fill: white;" , "-fx-text-fill: red;")
            );
        });
        play.setOnMouseExited(e -> {
            play.setStyle(
                play.getStyle().replace("-fx-text-fill: red;" , "-fx-text-fill: white;") 
            );
        });

        Button quit = (Button)(menuScene.lookup("#quit_button"));   //Finds the node with ID "quit_button", returns it and is then casted into Button
        quit.setOnMouseClicked(e -> {
            //if the QUIT button is left-clicked the application will close
            if(e.getButton() == MouseButton.PRIMARY){
                primaryStage.close();
            }
        });
        quit.setOnMouseEntered(e -> {
            quit.setStyle(
                quit.getStyle().replace("-fx-text-fill: white;" , "-fx-text-fill: red;")
            );
        });
        quit.setOnMouseExited(e -> {
            quit.setStyle(
                quit.getStyle().replace("-fx-text-fill: red;" , "-fx-text-fill: white;") 
            );
        });

        configureStage(primaryStage, menuScene);
    }

    private Scene menuScene(){
        //Creates a scene for the menu with a BorderPane node
        BorderPane rootMenu = new BorderPane();
        rootMenu.setStyle("-fx-background-color: black");
        Scene menuScene = new Scene(rootMenu);

        //Creates a HBox to center the menuTitle Text component
        Text menuTitle = new Text("AIM TRAINER");
        menuTitle.setStroke(Color.WHITE);
        menuTitle.setFont(new Font(130));
        HBox menuTitleBox = new HBox(menuTitle);
        menuTitleBox.setPadding(new Insets(150, 0, 0, 0));
        menuTitleBox.setAlignment(Pos.CENTER);

        //Creates a VBox to position the options in a vertical form,
        VBox options = new VBox();
        Button play = new Button("PLAY");
        play.setId("play_button");
        Button quit = new Button("QUIT");
        quit.setId("quit_button");
        play.setStyle(
            "-fx-background-color: black; " +    
            "-fx-text-fill: white; " +            
            "-fx-font-size: 40px; " +             
            "-fx-min-width: 200px; " +            
            "-fx-min-height: 35px;" +
            "-fx-cursor: hand;"      
        );
        quit.setStyle(
            "-fx-background-color: black; " +    
            "-fx-text-fill: white; " +            
            "-fx-font-size: 40px; " +             
            "-fx-min-width: 200px; " +            
            "-fx-min-height: 35px; " + 
            "-fx-cursor: hand" 
        );
        options.getChildren().addAll(play, quit);
        options.setAlignment(Pos.CENTER);
        options.setPadding(new Insets(0,0,100,0));

        //Adding children nodes to root node
        rootMenu.setTop(menuTitleBox);
        rootMenu.setBottom(options);

        return menuScene;
    }

    private Scene gameScene(){
        //Creates root node and assigns it to the Scene
        BorderPane rootGame = new BorderPane();
        rootGame.setStyle("-fx-background-color: black");
        Scene gameScene = new Scene(rootGame);

        String COUNTDOWN = "3";
        Text countdown = new Text(COUNTDOWN);     //countdown will control when the round starts
        countdown.setId("countdown");
        countdown.setFont(new Font(170));
        countdown.setFill(Color.WHITE);
        BorderPane.setMargin(countdown, new Insets(-140, 0, 0, 0));

        String ROUND_TIME = "45";
        Text roundTime = new Text(ROUND_TIME);         //'roundTime' will show the time left until end of round
        roundTime.setId("roundTime");
        roundTime.setFont(new Font(50));
        roundTime.setFill(Color.WHITE);
        roundTime.setStyle("-fx-opacity: 0;");
        HBox timeBox = new HBox(roundTime);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setPadding(new Insets(25, 0, 0, 0));

        //Adding children nodes to root node
        rootGame.setCenter(countdown);
        rootGame.setTop(timeBox);

        return gameScene;
    }

    private void configureStage(Stage Stage, Scene scene){
        Stage.setScene(scene);
        Stage.setTitle("Aim Trainer");
        Stage.setResizable(false);
        Stage.setFullScreen(true);
        Stage.setFullScreenExitHint("");
        Stage.show();
    }

    private void gameLogic(Scene scene, Stage primaryStage){
        PauseTransition everySecondCountdown = new PauseTransition(Duration.seconds(1));        //Transition for countdown
        PauseTransition everySecondRoundTime = new PauseTransition(Duration.seconds(1));        //Transition for round time
        BorderPane gameSceneRoot = (BorderPane)(scene.getRoot());    //Get root node
        Text ct = (Text)(scene.lookup("#countdown"));       //Finds 'countdown' node in gameScene and assigns a reference to 'rt'
        Text rt = (Text)(scene.lookup("#roundTime"));       //Finds 'roundTime' node in gameScene and assigns a reference to 'rt'

        everySecondCountdown.play();         //Starts transition 
        //Handling end of transition for countdown node
        everySecondCountdown.setOnFinished(e -> {
            int currentValue = Integer.parseInt(ct.getText());   
            if (currentValue > 1) {
                ct.setText(String.valueOf(currentValue - 1));     // Decreases the countdown
                everySecondCountdown.play();                     // Restarts the transition
            } 
            else if (currentValue == 1) {                        //Last transition
                gameSceneRoot.getChildren().remove(ct);           //Removes 'countdown'/'ct' Node from the scene's root
                System.out.println("Countdown finished!");
                rt.setStyle("-fx-opacity: 1.0;");                      //Changes original opacity to make 'roundTime' visible after the countdown finishes
                everySecondRoundTime.play();                                 //Starts transition to then control round time through event handler
            }    
        });

        //Handling end of transition for roundTime node
        everySecondRoundTime.setOnFinished(e -> {
            int currentValue = Integer.parseInt(rt.getText());
            if (currentValue > 1) {
                rt.setText(String.valueOf(currentValue - 1));     // Decreases the countdown
                prepareScene(gameSceneRoot);
                handleTargets(gameSceneRoot);
                everySecondRoundTime.play();                     // Restarts the transition
            } 
            else if (currentValue == 1) {                        //Last transition
                rt.setText(String.valueOf(currentValue - 1)); 
                gameSceneRoot.setBottom(null);  //removes everything that's in the Bottom region of the BorderPane layout.
                gameSceneRoot.setLeft(null);    //removes everything that's in the Left region of the BorderPane layout.
                gameSceneRoot.setRight(null);   //removes everything that's in the Right region of the BorderPane layout.
                System.out.println("Round finished!");
                System.out.println("You hit " + Target.getTargetsHit() + " targets");
                System.out.println("You missed " + Target.getTargetsMissed() + " targets");
                showResults(scene, primaryStage);     //This arguments allow showResults() to either return to go to the Game Menu or start a new round
            }    
        });
    }

    private void prepareScene(BorderPane root){
        //Setting up root regions **top is reserved for round time**
        root.setCenter(getGrid());
        root.setBottom(getGrid());
        root.setLeft(getGrid());
        root.setRight(getGrid());
    }

    private GridPane getGrid(){
        GridPane gp = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        RowConstraints row = new RowConstraints();
        column.setPercentWidth(100.0 / 5); // Set column width to percentage value
        row.setPercentHeight(100.0 / 5);  // Set row height to percentage value
        //Adds 5 rows and 5 columns to 'gp'
        for(int i=0; i < 5; i++){
            gp.getColumnConstraints().add(column);
            gp.getRowConstraints().add(row);
        }
        return gp;
    }

    private void handleTargets(BorderPane root){
        Target target = new Target();               //Every second a new instance of Target will be created
        int position= (int)(Math.random() * 4);     //'position' will determine in what region of root will the target.circle be placed
        Target.targetMissed();     //A target will be considered missed until it's not been hit

        GridPane centerGrid = (GridPane)(root.getCenter());
        GridPane bottomGrid = (GridPane)(root.getBottom()); 
        GridPane leftGrid = (GridPane)(root.getLeft()); 
        GridPane rightGrid = (GridPane)(root.getRight());

        switch(position){
            case 0: centerGrid.add(target.circle, (int)(Math.random() * 5), (int)(Math.random() *  5)); break;
            case 1: bottomGrid.add(target.circle, (int)(Math.random() * 5), (int)(Math.random() *  5)); break;
            case 2: leftGrid.add(target.circle, (int)(Math.random() * 5), (int)(Math.random() * 5)); break;
            case 3: rightGrid.add(target.circle, (int)(Math.random() * 5), (int)(Math.random() * 5)); break;
            default: break;
        }

        target.circle.setOnMousePressed(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                Target.targetHit();
                GridPane parent = (GridPane)(target.circle.getParent());
                parent.getChildren().remove(target.circle);
            }
        });
    }

    private void showResults(Scene gameScene, Stage primaryStage){
        gameScene.setRoot(new StackPane());         //a StackPane node will be the new root of gameScene so the content is centered by default
        StackPane gameSceneRoot = (StackPane)(gameScene.getRoot());     //Gets a reference to the the StackPane root
        Scene newGameScene = gameScene();           //Creates a new gameScene() with no modifications, for later use reseting the round
        
        VBox results = new VBox();          //results is the direct child of gameSceneRoot
        //Declaring results' children nodes
        HBox titleBox, buttonsBox, container;
        VBox labelsBox, valuesBox;

        //Initializing results' children nodes
        titleBox = new HBox();      
        buttonsBox = new HBox();
        container = new HBox();
        labelsBox = new VBox();
        valuesBox = new VBox();

        //Initializing nodes inside boxes
        Text title = new Text("-- Round Results --");
        Text targetsHitLabel = new Text("Targets Hit:");
        Text targetsMissedLabel = new Text("Targets Missed:");
        Text scoreLabel = new Text("Score:");
        Text hitsCount = new Text(String.valueOf(Target.getTargetsHit()));
        Text missedCount = new Text(String.valueOf(Target.getTargetsMissed()));
        Text scoreValue = new Text(String.valueOf(Target.getScore()) + "%");
        Button goToMenu = new Button("Go to Menu");
        Button tryAgain = new Button("Try Again");

        //Adding nodes to boxes
        titleBox.getChildren().add(title);
        buttonsBox.getChildren().addAll(goToMenu, tryAgain);
        labelsBox.getChildren().addAll(targetsHitLabel, targetsMissedLabel, scoreLabel);
        valuesBox.getChildren().addAll(hitsCount, missedCount, scoreValue);
        container.getChildren().addAll(labelsBox, valuesBox);

        //Configures layout and styles
        results.setStyle(
            "-fx-background-color: rgb(101, 214, 152);"
        );
        results.setSpacing(30);

        //I did this because for some reason, 'gameSceneRoot' is not placing 'results' in the center by default 
        results.setPadding(new Insets(gameScene.getHeight()/4,0,0,0));

        titleBox.setAlignment(Pos.CENTER);

        container.setAlignment(Pos.CENTER);
        container.setSpacing(50);

        labelsBox.setSpacing(20);
        labelsBox.setAlignment(Pos.CENTER);
        valuesBox.setSpacing(20);
        valuesBox.setAlignment(Pos.CENTER);

        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);
        buttonsBox.setPadding(new Insets(30, 0,0,0 ));
        goToMenu.setStyle(
            "-fx-background-color: rgb(0, 120, 230); " +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 15;"
        );
        tryAgain.setStyle(
            "-fx-background-color: rgb(180, 0, 0); " +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 15;"
        );
        configureButtons(goToMenu);
        configureButtons(tryAgain);
    
        title.setFont(new Font(70));
        targetsHitLabel.setFont(new Font(40));
        targetsMissedLabel.setFont(new Font(40));
        scoreLabel.setFont(new Font(40));
        hitsCount.setFont(new Font(40));
        missedCount.setFont(new Font(40));
        scoreValue.setFont(new Font(40));

        //Handles MouseEvent events on buttons 
        goToMenu.setOnMousePressed(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                Target.resetStats();
                start(primaryStage);    //Going back to Game Menu
            }
        });

        goToMenu.setOnMouseEntered(e -> {
            goToMenu.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: rgb(0, 120, 230); " +
                "-fx-background-radius: 15;"
            );
        });

        goToMenu.setOnMouseExited(e -> {
            goToMenu.setStyle(
                "-fx-background-color: rgb(0, 120, 230); " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 15;"
            );
        });

        tryAgain.setOnMousePressed(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                Target.resetStats();
                //Reconfigure Stage with a new gameScene because the gameScene passed to this function suffered changes not wanted for a new round
                configureStage(primaryStage, newGameScene);
                //New round starts as gameLogic is called
                gameLogic(newGameScene, primaryStage);
            }
        });

        tryAgain.setOnMouseEntered(e -> {
            tryAgain.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: rgb(180, 0, 0); " +
                "-fx-background-radius: 15;"
            );
        });

        tryAgain.setOnMouseExited(e -> {
            tryAgain.setStyle(
                "-fx-background-color: rgb(180, 0, 0); " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 15;"
            );
        });

        results.getChildren().addAll(titleBox, container, buttonsBox);
        gameSceneRoot.getChildren().add(results);                 
    }

    private void configureButtons(Button btn){
        btn.setCursor(Cursor.HAND);
        btn.setPrefHeight(60);
        btn.setPrefWidth(280);
        btn.setFont(new Font(40));
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
