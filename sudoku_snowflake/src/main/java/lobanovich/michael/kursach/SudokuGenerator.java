package lobanovich.michael.kursach;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    private final Random random = new Random();
    public int[][] toSolve = new int[9][9];
    public int[][] fullField = new int[9][9];
    public int[][] copyField= new int[9][9];
    Solver solver = new Solver();

    protected void transposition() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                    copyField[i][j] = fullField[j][i];
            }
        }
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                fullField[i][j] = copyField[i][j];
            }
        }
    }

    protected void swapRowsInOneBlock() {
        int block = random.nextInt(3);
        int firstRow = random.nextInt(3);
        int secondRow = random.nextInt(3);
        while (firstRow == secondRow) {
            secondRow = random.nextInt(3);
        }
        int buffer;
        for (int i = 0; i < 9; ++i) {
            buffer = fullField[block * 3 + firstRow][i];
            fullField[block * 3 + firstRow][i] = fullField[block * 3 + secondRow][i];
            fullField[block * 3 + secondRow][i] = buffer;
        }
    }

    protected void swapColumnsInOneBlock() {
        transposition();
        swapRowsInOneBlock();
        transposition();
    }

    protected void swapRows() {
        int firstRow = random.nextInt(3);
        int secondRow = random.nextInt(3);
        while (firstRow == secondRow) {
            secondRow = random.nextInt(3);
        }
        int buffer;
        for(int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                buffer = fullField[firstRow * 3 + i][j];
                fullField[firstRow * 3 + i][j] =  fullField[secondRow * 3 + i][j];
                fullField[secondRow * 3 + i][j] = buffer;
            }
        }
    }

    protected void swapColumns() {
        transposition();
        swapRows();
        transposition();
    }

    protected void mixInRandomOrder() {
        int numberOfOperations = random.nextInt(8, 16);
        int currentOperation;
        for(int i = 0; i < numberOfOperations; ++i) {
            currentOperation = random.nextInt(5);
            switch (currentOperation) {
                case 0:
                    transposition();
                case 1:
                    swapRowsInOneBlock();
                case 2:
                    swapColumnsInOneBlock();
                case 3:
                    swapRows();
                case 4:
                    swapColumns();
            }
        }
    }

    protected void hideSomeNumbers(int numberOfHidden) {
        for(int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                copyField[i][j] = fullField[i][j];
            }
        }
        int currentNum = 0;
        int i;
        int j;
        while (numberOfHidden > currentNum) {
            i = random.nextInt(9);
            j = random.nextInt(9);
            if (copyField[i][j] != 0) {
                copyField[i][j] = 0;
                currentNum++;
            }
        }
    }

    protected void print(int[][] matrix) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                System.out.print(matrix[i][j] + " ");
                if (j == 8){
                    System.out.println();
                }
            }
        }
    }

    private boolean isAllChecked(boolean[][] matrix) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (!matrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void generateSudoku(int difficulty, boolean isSnowflake) {
        String path;
        if (isSnowflake) {
            path = "snowflake.txt";
        } else {
            path = "classic.txt";
        }
        ArrayList<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int sudokuInd = random.nextInt(allLines.size());
        for (int j = 0; j < 81; ++j) {
            fullField[j / 9][j % 9] = Integer.parseInt(String.valueOf(allLines.get(sudokuInd).charAt(j)));
        }
        if(!isSnowflake) {
            mixInRandomOrder();
        }
        int hiddenElements = 0;
        for(int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                toSolve[i][j] = fullField[i][j];
            }
        }
        boolean[][] hasChecked = new boolean[9][9];
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                hasChecked[i][j] = false;
            }
        }
        int[] lastElement = new int[2];
        while (hiddenElements < difficulty && !isAllChecked(hasChecked)) {
            lastElement[0] = random.nextInt(9);
            lastElement[1] = random.nextInt(9);
            if (!hasChecked[lastElement[0]][lastElement[1]]) {
                hasChecked[lastElement[0]][lastElement[1]] = true;
                toSolve[lastElement[0]][lastElement[1]] = 0;
                for (int i = 0; i < 9; ++i) {
                    for (int j = 0; j < 9; ++j) {
                        copyField[i][j] = toSolve[i][j];
                    }
                }
                hiddenElements++;
                solver.allSolves = new ArrayList<>();
                solver.solve(copyField, 0, isSnowflake);
                if (solver.allSolves.size() != 1) {
                    hiddenElements--;
                    toSolve[lastElement[0]][lastElement[1]] = fullField[lastElement[0]][lastElement[1]];
                }
            }
        }
    }
}
