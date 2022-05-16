package lobanovich.michael.kursach;

import java.util.*;

public class Solver {
    protected ArrayList<int[][]> allSolves = new ArrayList<>();

    protected void solve(int[][] matrix, int index, boolean isSnowfall) {
        if(index == 81){
            allSolves.add(matrix);
        }
        else{
            int row = index / 9;
            int col = index % 9;
            if(matrix[row][col] != 0) solve(matrix, index + 1, isSnowfall);
            else{
                for(int i = 1; i <= 9; i++){
                    if(isPossibleMeaning(matrix, row, col, i, isSnowfall)){
                        matrix[row][col] = i;
                        solve(matrix,index+1, isSnowfall);
                        matrix[row][col] = 0;
                    }
                }
            }
        }
    }

    private boolean isPossibleMeaning(int[][] board, int row, int col, int c, boolean isSnowfall) {
        for(int i = 0; i < 9; i++){
            if(board[row][i] == c) return false;
            if(board[i][col] == c) return false;
        }

        int rowStart = row - row % 3;
        int colStart = col - col % 3;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[rowStart + j][colStart + i] == c) {
                    return false;
                }
            }
        }
        if (isSnowfall) {
            if (isOnMainDiagonal(row, col)) {
                for (int i = 0; i < 9; ++i) {
                    if (board[i][i] == c) {
                        return false;
                    }
                }
            }
            if (isOnSideDiagonal(row, col)) {
                for (int i = 0; i < 9; ++i) {
                    if (board[i][8 - i] == c) {
                        return false;
                    }
                }
            }
            if (isOnDownMainDiagonal(row, col)) {
                for (int i = 3; i < 9; ++i) {
                    if (board[i][i - 3] == c) {
                        return false;
                    }
                }
            }
            if (isOnDownSideDiagonal(row, col)) {
                for (int i = 3; i < 9; ++i) {
                    if (board[i][11 - i] == c) {
                        return false;
                    }
                }
            }
            if (isOnUpperMainDiagonal(row, col)) {
                for (int i = 0; i < 6; ++i) {
                    if (board[i][i + 3] == c) {
                        return false;
                    }
                }
            }
            if (isOnUpperSideDiagonal(row, col)) {
                for (int i = 0; i < 6; ++i) {
                    if (board[i][5 - i] == c) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean isOnMainDiagonal (int rowIndex, int columnIndex) {
        return rowIndex == columnIndex;
    }

    protected boolean isOnSideDiagonal (int rowIndex, int columnIndex) {
        return rowIndex + columnIndex == 8;
    }

    protected boolean isOnUpperSideDiagonal (int rowIndex, int columnIndex) {
        return rowIndex + columnIndex == 5;
    }

    protected boolean isOnDownSideDiagonal (int rowIndex, int columnIndex) {
        return rowIndex + columnIndex == 11;
    }

    protected boolean isOnUpperMainDiagonal (int rowIndex, int columnIndex) {
        return columnIndex - rowIndex == 3;
    }

    protected boolean isOnDownMainDiagonal (int rowIndex, int columnIndex) {
        return rowIndex - columnIndex == 3;
    }
}
