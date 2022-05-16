package lobanovich.michael.kursach;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Repainter {
    public String redStyle = "-fx-background-color: crimson";
    protected GridPane field;
    public Repainter(GridPane field) {
        this.field = field;
    }


    protected void repaintRowAndColumn(String style, double opacity, int row, int column) {
        for (Node node : field.getChildren()) {
            if (!node.getStyle().equals(this.redStyle) &&
                    (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row ||
                            GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column)) {
                node.setStyle(style);
                node.setOpacity(opacity);
            }
        }
    }

    protected void repaintBlock(String style, double opacity, int row, int column) {
        for (Node node: field.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (!node.getStyle().equals(redStyle) &&
                        nodeRow >= row / 3 * 3 && nodeRow < row / 3 * 3 + 3 &&
                        nodeColumn >= column / 3 * 3 && nodeColumn < column / 3 * 3 + 3) {
                    node.setStyle(style);
                    node.setOpacity(opacity);
                }
            }
        }
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

    protected void repaintForSnowfall (String style, double opacity, int row, int column) {
        for (Node node : this.field.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (isOnMainDiagonal(row, column)) {
                    if (nodeRow == nodeColumn && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
                if (isOnSideDiagonal(row, column)) {
                    if (nodeRow + nodeColumn == 8 && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
                if (isOnUpperMainDiagonal(row, column)) {
                    if (nodeColumn - nodeRow == 3 && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
                if (isOnDownMainDiagonal(row, column)) {
                    if (nodeRow - nodeColumn == 3 && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
                if (isOnUpperSideDiagonal(row, column)) {
                    if (nodeColumn + nodeRow == 5 && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
                if (isOnDownSideDiagonal(row, column)) {
                    if (nodeColumn + nodeRow == 11 && !node.getStyle().equals(redStyle)) {
                        node.setStyle(style);
                        node.setOpacity(opacity);
                    }
                }
            }
        }
    }
}
