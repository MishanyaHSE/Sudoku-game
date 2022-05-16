package lobanovich.michael.kursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SceneController {
    @FXML
    private Button quit;
    @FXML
    private Button backToMain;
    @FXML
    private TextArea rulesText;
    @FXML
    private Button snowfallMode;
    @FXML
    private Button classicMode;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private GridPane field;
    @FXML
    private HBox numberButtons;
    @FXML
    private Button mainMenu;
    @FXML
    private VBox infoNameBox;
    @FXML
    private VBox infoBox;
    @FXML
    private Label numberOfMoves;
    @FXML
    private Label numberOfMistakes;
    @FXML
    private Label numberOfHints;
    @FXML
    private Button continueGame;
    @FXML
    private VBox mainMenuBox;
    @FXML
    private Button hintButton;
    @FXML
    private VBox complexityBox;
    @FXML
    private Button backToComplexity;
    @FXML
    private Button startNewGame;
    @FXML
    private Text congratulations;
    @FXML
    private Button closeNewWindowButton;

    private final String redStyle = "-fx-background-color: crimson";
    private final String greenStyle = "-fx-background-color: palegreen";
    private final String blueStyle = "-fx-background-color: lightblue";
    private final String orangeStyle = "-fx-background-color: navajowhite";
    private Button currentCell;
    private Button lastGreenCell;
    private Button lastOrangeCell;
    int currentX;
    int currentY;
    SudokuGenerator generator = new SudokuGenerator();
    int[][] toSolve = new int[9][9];
    int[][] fullField = new int[9][9];
    boolean[][] isClickable = new boolean[9][9];
    private boolean isSnowflake = false;
    int movesCounter = 0, mistakesCounter = 0, hintsCounter = 0;
    Repainter repainter;
    private final Random randomizer = new Random();
    private int complexity;

    @FXML
    protected void snowfallModeClick() throws IOException {
        isSnowflake = true;
        loadGameStage();
        saveGame();
    }
    @FXML
    protected void classicModeClick() throws IOException {
        isSnowflake = false;
        loadGameStage();
        saveGame();
    }

    @FXML
    protected void continueGameClick() throws IOException {
        if (gameCanBeLoaded()) {
            makeEverythingNotVisible();
            field.getChildren().clear();
            mainMenu.setVisible(true);
            numberButtons.setVisible(true);
            loadGame();
            field.setVisible(true);
            addActionOnNumberButtons();
            infoBox.setVisible(true);
            infoNameBox.setVisible(true);
            hintButton.setVisible(true);
        } else {
            Stage newWindow = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("no-save.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 100);
            newWindow.setTitle("Save not found!");
            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.show();
        }
    }

    @FXML
    protected void closeNewWindow() {
        Stage stage = (Stage)closeNewWindowButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void startGameClick () {
        makeEverythingNotVisible();
        complexityBox.setVisible(true);
        backToMain.setVisible(true);
    }

    @FXML
    protected void rulesClick () {
        makeEverythingNotVisible();
        backToMain.setVisible(true);
        rulesText.setVisible(true);
    }

    @FXML
    protected void quitClick() {
        Stage stage = (Stage)quit.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void backToMainClick () {
        makeEverythingNotVisible();
        mainMenuBox.setVisible(true);
        numberButtons.setDisable(true);
    }

    @FXML
    protected void hintButtonClick() throws IOException {
        getHint();
        numberButtons.setDisable(true);
        saveGame();
        endOfGame();
    }

    @FXML
    protected void easyClick() {
        complexity = 25;
        makeEverythingNotVisible();
        classicMode.setVisible(true);
        snowfallMode.setVisible(true);
        backToComplexity.setVisible(true);

    }

    @FXML
    protected void mediumClick() {
        complexity = 36;
        makeEverythingNotVisible();
        classicMode.setVisible(true);
        snowfallMode.setVisible(true);
        backToComplexity.setVisible(true);
    }

    @FXML
    protected void hardClick() {
        complexity = 50;
        makeEverythingNotVisible();
        classicMode.setVisible(true);
        snowfallMode.setVisible(true);
        backToComplexity.setVisible(true);
    }

    @FXML
    protected void backToComplexityClick() {
        makeEverythingNotVisible();
        complexityBox.setVisible(true);
        backToMain.setVisible(true);
    }

    protected void makeEverythingNotVisible () {
        for(var element : mainAnchor.getChildren()) {
            element.setVisible(false);
        }
    }

    protected void initializeField() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                Button btn = new Button();
                btn.setId(i + "_" + j);
                // Заполняем поле существующими значениями.
                if (toSolve[i][j] != 0) {
                    btn.setText(Integer.toString(toSolve[i][j]));
                }
                // Делаем кнопки размером с ячейку GridPane.
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(btn, Priority.ALWAYS);
                GridPane.setVgrow(btn, Priority.ALWAYS);
                // Задаем каждой пустой кнопке действия при нажатии.
                if (isClickable[i][j]) {
                    btn.setOnAction(e -> {
                        repainter.repaintRowAndColumn("", 1, currentY, currentX);
                        repainter.repaintBlock("", 1, currentY, currentX);
                        if (isSnowflake) {
                            repainter.repaintForSnowfall("", 1, currentY, currentX);
                        }
                        currentCell = btn;
                        numberButtons.setDisable(false);
                        currentX = GridPane.getColumnIndex(btn);
                        currentY = GridPane.getRowIndex(btn);
                        if(lastGreenCell != null) {
                            lastGreenCell.setStyle("");
                            lastGreenCell = null;
                        }
                        if (lastOrangeCell != null) {
                            lastOrangeCell.setStyle("");
                            lastOrangeCell = null;
                        }
                        repainter.repaintRowAndColumn(blueStyle, 0.7, currentY, currentX);
                        repainter.repaintBlock(blueStyle, 0.7, currentY, currentX);
                        if (isSnowflake) {
                            repainter.repaintForSnowfall(blueStyle, 0.7, currentY, currentX);
                        }
                        if (!btn.getStyle().equals(redStyle)) {
                            btn.setStyle("");
                        }
                    });
                }
                field.add(btn, j, i);
            }
        }
        repainter = new Repainter(field);
    }

    protected void addActionOnNumberButtons() {
        for (Node btn : numberButtons.getChildren()) {
            ((Button)btn).setOnAction(e -> {
                movesCounter++;
                numberOfMoves.setText(Integer.toString(movesCounter));
                currentCell.setText(((Button)btn).getText());
                toSolve[currentY][currentX] = Integer.parseInt(((Button)btn).getText());
                repainter.repaintRowAndColumn("", 1, currentY, currentX);
                repainter.repaintBlock("", 1, currentY, currentX);
                if (isSnowflake) {
                    repainter.repaintForSnowfall("", 1, currentY, currentX);
                }
                numberButtons.setDisable(true);
                if (fullField[currentY][currentX] == Integer.parseInt(((Button)btn).getText())) {
                    currentCell.setStyle(greenStyle);
                    lastGreenCell = currentCell;
                } else {
                    currentCell.setStyle(redStyle);
                    mistakesCounter++;
                    numberOfMistakes.setText(Integer.toString(mistakesCounter));
                }
                try {
                    saveGame();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                endOfGame();
            });
        }
    }


    protected void loadGameStage() {
        makeEverythingNotVisible();
        generator.generateSudoku(complexity, isSnowflake);
        toSolve = generator.toSolve;
        fullField = generator.fullField;
        updateComplexity();
        updateIsClickable();
        field.getChildren().clear();
        mainMenu.setVisible(true);
        numberButtons.setVisible(true);
        initializeField();
        field.setVisible(true);
        addActionOnNumberButtons();
        infoBox.setVisible(true);
        infoNameBox.setVisible(true);
        hintButton.setVisible(true);
        movesCounter = 0;
        numberOfMoves.setText(Integer.toString(movesCounter));
        hintsCounter = 0;
        numberOfHints.setText(Integer.toString(hintsCounter));
        mistakesCounter = 0;
        numberOfMistakes.setText(Integer.toString(mistakesCounter));
    }

    protected void updateIsClickable() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                isClickable[i][j] = toSolve[i][j] == 0;
            }
        }
    }

    protected void saveGame() throws IOException {
        StringBuilder full = new StringBuilder();
        StringBuilder solve = new StringBuilder();
        StringBuilder clickable = new StringBuilder();
        StringBuilder colors = new StringBuilder();
        String gameInfo;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                full.append(fullField[i][j]);
                solve.append(toSolve[i][j]);
                if (isClickable[i][j]) {
                    clickable.append(1);
                } else {
                    clickable.append(0);
                }
            }
        }
        for (Node button : field.getChildren()) {
            if (button.getStyle().equals(redStyle)) {
                colors.append("R");
            } else {
                colors.append("C");
            }
        }
        try(FileWriter writer = new FileWriter("save.txt")) {
            writer.append(full);
            writer.append("\n");
            writer.append(solve);
            writer.append("\n");
            writer.append(clickable);
            writer.append("\n");
            writer.append(colors);
            writer.append("\n");
            gameInfo = numberOfMoves.getText() + " " + numberOfMistakes.getText() + " " + numberOfHints.getText();
            if (isSnowflake) {
                gameInfo += " 1";
            } else {
                gameInfo += " 0";
            }
            gameInfo += " " + complexity;
            writer.append(gameInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadGame() {
        ArrayList<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("save.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                fullField[i][j] = Integer.parseInt(String.valueOf(allLines.get(0).charAt(i * 9 + j)));
                toSolve[i][j] = Integer.parseInt(String.valueOf(allLines.get(1).charAt(i * 9 + j)));
                if (allLines.get(2).charAt(9 * i + j) == '1') {
                    isClickable[i][j] = true;
                } else {
                    isClickable[i][j] = false;
                }
            }
        }
        isSnowflake = allLines.get(4).split(" ")[3].equals("1");
        complexity = Integer.parseInt(allLines.get(4).split(" ")[4]);
        initializeField();
        int index = 0;
        for (Node button : field.getChildren()) {
            if (allLines.get(3).charAt(index) == 'R') {
                button.setStyle(redStyle);
            }
            index++;
        }
        numberOfMoves.setText(allLines.get(4).split(" ")[0]);
        numberOfMistakes.setText(allLines.get(4).split(" ")[1]);
        numberOfHints.setText(allLines.get(4).split(" ")[2]);
        movesCounter = Integer.parseInt(allLines.get(4).split(" ")[0]);
        mistakesCounter = Integer.parseInt(allLines.get(4).split(" ")[1]);
        hintsCounter = Integer.parseInt(allLines.get(4).split(" ")[2]);
    }

    protected void getHint() {
        ArrayList<Pair<Integer, Integer>> allCords = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (toSolve[i][j] != fullField[i][j]) {
                    allCords.add(new Pair<>(i, j));
                }
            }
        }
        repainter.repaintRowAndColumn("",1, currentY, currentX);
        repainter.repaintBlock("", 1, currentY, currentX);
        repainter.repaintForSnowfall("", 1, currentY, currentX);
        if(allCords.size() > 0) {
            int cordToHint = randomizer.nextInt(allCords.size());
            for (Node button : field.getChildren()) {
                if (GridPane.getRowIndex(button).equals(allCords.get(cordToHint).getKey()) &&
                        GridPane.getColumnIndex(button).equals(allCords.get(cordToHint).getValue())) {
                    ((Button) button).setText(Integer.toString(fullField[allCords.get(cordToHint).getKey()][allCords.get(cordToHint).getValue()]));
                    button.setStyle(orangeStyle);
                    if (lastOrangeCell != null) {
                        lastOrangeCell.setStyle("");
                    }
                    lastOrangeCell = (Button) button;
                    hintsCounter++;
                    numberOfHints.setText(Integer.toString(hintsCounter));
                }
            }
            toSolve[allCords.get(cordToHint).getKey()][allCords.get(cordToHint).getValue()] =
                    fullField[allCords.get(cordToHint).getKey()][allCords.get(cordToHint).getValue()];
        }
        endOfGame();
    }

    private void endOfGame() {
        if (complexity == movesCounter - mistakesCounter + hintsCounter) {
            hintButton.setVisible(false);
            startNewGame.setVisible(true);
            congratulations.setVisible(true);
            for (Node node : field.getChildren()) {
                ((Button)node).setOnAction(e -> {

                });
            }
            try(FileWriter writer = new FileWriter("save.txt")) {
                String deleteSave = "none";
                writer.append(deleteSave);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean gameCanBeLoaded() {
        ArrayList<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("save.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allLines.size() == 5;
    }

    private void updateComplexity() {
        complexity = 0;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9;++j) {
                if (toSolve[i][j] == 0) {
                    complexity++;
                }
            }
        }
    }
}