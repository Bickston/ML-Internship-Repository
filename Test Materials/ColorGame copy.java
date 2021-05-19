import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

/**
 * Creates a javafx game with several features including a name entry box
 * as well as keyboard options as well.
 * @author blaenger3
 * @version 11.0.9.1
 */
public class ColorGame extends Application {
    private static final Random RAND = new Random();
    private static Integer gameScore = 0;
    private static Integer maximum = 0;
    private static Integer seconds = 0;

    private static final String[] colors = {
        "Red",
        "Orange",
        "Yellow",
        "Green",
        "Blue",
        "Purple"
    };
    private static final String[] colorList = {
        "-fx-background-color: red",
        "-fx-background-color: orange",
        "-fx-background-color: yellow",
        "-fx-background-color: green",
        "-fx-background-color: blue",
        "-fx-background-color: purple"
    };
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Color Game :)");

        //name labels
        Label nameLabel = new Label("Name: ");
        Label nameChange = new Label("None");
        HBox nameHolder = new HBox();
        nameHolder.setAlignment(Pos.CENTER);
        nameHolder.setPadding(new Insets(20, 0, 10, 0));
        nameHolder.getChildren().addAll(nameLabel, nameChange);

        //name entry box
        TextField nameText = new TextField();
        nameText.setPromptText("Enter your name here");
        Button textButton = new Button("Enter");
        HBox textEntry = new HBox();
        textEntry.setAlignment(Pos.CENTER);
        textEntry.setSpacing(10);
        textEntry.setPadding(new Insets(0, 0, 20, 0));
        textEntry.getChildren().addAll(nameText, textButton);

        //score displays and timer
        Label timer = new Label("Time: " + seconds.toString());
        Label scoreLabel = new Label("Score: " + gameScore.toString());
        Label bestScore = new Label("Best score: " + maximum.toString());
        HBox scoreHolder = new HBox();
        scoreHolder.setSpacing(20);
        scoreHolder.setAlignment(Pos.CENTER);
        scoreHolder.setPadding(new Insets(0, 0, 10, 0));
        scoreHolder.getChildren().addAll(timer, scoreLabel, bestScore);

        //example rectangle and reset button
        Button correctBtn = new Button(randomColorLabel());
        correctBtn.setStyle(randomColor());
        Button resetButton = new Button("Reset");
        HBox rectAndReset = new HBox();
        rectAndReset.setSpacing(50);
        rectAndReset.setAlignment(Pos.CENTER);
        rectAndReset.setPadding(new Insets(0, 0, 15, 100));
        rectAndReset.getChildren().addAll(correctBtn, resetButton);

        //Answer buttons
        Button leftColor = new Button(randomColorLabel());
        leftColor.setStyle(randomColor());
        Button centerColor = new Button(randomColorLabel());
        centerColor.setStyle(randomColor());
        Button rightColor = new Button(randomColorLabel());
        rightColor.setStyle(randomColor());
        Button noneButton = new Button("None");
        HBox answerLabels = new HBox();
        answerLabels.setSpacing(12);
        answerLabels.setAlignment(Pos.CENTER);
        answerLabels.setPadding(new Insets(0, 0, 15, 0));
        answerLabels.getChildren().addAll(leftColor, centerColor, rightColor, noneButton);

        //Status label
        Label statusLabel = new Label("Match text in top box with color of bottom");
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().add(statusLabel);

        //Local class methods
        class NoDupe {
            public void duplicate(Button btn) {
                String correctColor = randomColorLabel();
                String correctAnswer = randomColor();
                btn.setText(correctColor);
                while (correctAnswer.equals("-fx-background-color: " + correctColor.toLowerCase())) {
                    correctAnswer = randomColor();
                }
                btn.setStyle(correctAnswer);
            }
        }

        class BtnReset {
            public void resetLabels() {
                new NoDupe().duplicate(correctBtn);
                new NoDupe().duplicate(leftColor);
                new NoDupe().duplicate(centerColor);
                new NoDupe().duplicate(rightColor);
            }
        }

        class CheckCorrect {
            public void check(Button btn) {
                if (btn.getStyle().equals("-fx-background-color: " + correctBtn.getText().toLowerCase())) {
                    gameScore += 1;
                    scoreLabel.setText("Score: " + gameScore);
                    statusLabel.setText("Correct! It was the " + correctBtn.getText() + " box");
                } else {
                    if (gameScore > maximum) {
                        maximum = gameScore;
                        bestScore.setText("Best score: " + maximum);
                    }
                    gameScore = 0;
                    scoreLabel.setText("Score: " + gameScore);
                    if (leftColor.getStyle().equals("-fx-background-color: " + correctBtn.getText().toLowerCase())
                            || centerColor.getStyle().equals("-fx-background-color: "
                            + correctBtn.getText().toLowerCase())
                            || rightColor.getStyle().equals("-fx-background-color: "
                            + correctBtn.getText().toLowerCase())) {
                        statusLabel.setText("Incorrect, the answer was the " + correctBtn.getText() + " box");
                    } else {
                        statusLabel.setText("Incorrect, the answer was the None box");
                    }
                }
                new BtnReset().resetLabels();
            }
        }

        class NoneButton {
            public void none() {
                if (leftColor.getStyle().equals("-fx-background-color: " + correctBtn.getText().toLowerCase())
                        || centerColor.getStyle().equals("-fx-background-color: " + correctBtn.getText().toLowerCase())
                        || rightColor.getStyle().equals("-fx-background-color: "
                        + correctBtn.getText().toLowerCase())) {
                    if (gameScore > maximum) {
                        maximum = gameScore;
                        bestScore.setText("Best score: " + maximum);
                    }
                    gameScore = 0;
                    scoreLabel.setText("Score: " + gameScore);
                    statusLabel.setText("Incorrect, the answer was the " + correctBtn.getText() + " box");
                } else {
                    gameScore += 1;
                    scoreLabel.setText("Score: " + gameScore);
                    statusLabel.setText("Correct! It was the None box");
                }
                new BtnReset().resetLabels();
            }
        }

        class ResetButton {
            public void reset() {
                nameChange.setText("None");
                statusLabel.setText("Match text in top box with color of bottom");
                gameScore = 0;
                maximum = 0;
                seconds = 0;
                scoreLabel.setText("Score: " + gameScore);
                bestScore.setText("Best Score: " + maximum);
                timer.setText("Time: " + seconds);
                nameText.setText("");
                new BtnReset().resetLabels();
            }
        }

        //Timer setup
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), actionEvent -> {
            seconds++;
            timer.setText("Time: " + seconds);
        }));
        timeline.playFromStart();

        //Button events
        textButton.setOnAction(event -> nameChange.setText(nameText.getText()));
        resetButton.setOnAction(event -> new ResetButton().reset());
        leftColor.setOnAction(event -> new CheckCorrect().check(leftColor));
        centerColor.setOnAction(event -> new CheckCorrect().check(centerColor));
        rightColor.setOnAction(event -> new CheckCorrect().check(rightColor));
        noneButton.setOnAction(event -> new NoneButton().none());

        //Set up Parent VBox and keyboard events
        VBox root = new VBox();
        root.getChildren().addAll(nameHolder, textEntry, scoreHolder, rectAndReset, answerLabels, labelBox);
        root.setOnKeyPressed(event -> {
            switch (event.getText()) {
            case "z":
                new CheckCorrect().check(leftColor);
                break;
            case "x":
                new CheckCorrect().check(centerColor);
                break;
            case "c":
                new CheckCorrect().check(rightColor);
                break;
            case "v":
                new NoneButton().none();
                break;
            case "r":
                new ResetButton().reset();
                break;
            default:
                break;
            }
            if ("ENTER".equals(event.getCode().toString())) {
                nameChange.setText(nameText.getText());
            }
        });
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        root.requestFocus();
    }

    private static String randomColor() {
        return colorList[RAND.nextInt(6)];
    }

    private static String randomColorLabel() {
        return colors[RAND.nextInt(6)];
    }

    /**
     * main method that launches the game.
     * @param args empty array of type string
     */
    public static void main(String[] args) {
        launch(args);
    }
}
