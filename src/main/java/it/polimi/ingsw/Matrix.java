package it.polimi.ingsw;

import java.awt.*;
import java.util.ArrayList;

public class Matrix {
    /**
     *  Tile Matrix Class
     */
    private final ArrayList<ArrayList<Tiles>> board = new ArrayList<ArrayList<Tiles>>();
    private final int numRows;
    private final int numCols;

    //costruttore
    public Matrix(int rows, int columns ){
        numCols=columns;
        numRows=rows;

        for( int i=0; i<columns;i++){
            board.add(new ArrayList<Tiles>());
            for( int j=0; j<rows;j++){
                board.get(i).add(Tiles.EMPTY);
            }

        }
    }

    /**
     * Method which set the selected tile to value 'tile'
     * @param tile  wanted value of the tile
     * @param row   the number of the row where you want to set the tile
     * @param col   the number of the column where you want to set the tile
     */
    //aggunge tile
    public void setTile(Tiles tile, int row, int col){

        board.get(col).set(row,tile);

    }

    /**
     * Set the selected tile to EMPTY
     * @param row   The number of the row where you want to set the tile
     * @param col   the number of the column where you want to set the tile
     */
    //rimuove tile
    public void remove(int row, int col){

        board.get(col).set(row,Tiles.EMPTY);

    }

    //imposta cella a NotAllowed
    public void setNotAllowed(int row, int col){

        board.get(col).set(row,Tiles.NOTALLOWED);

    }
    public void setEmpty(int row, int col){

        board.get(col).set(row,Tiles.EMPTY);

    }

    public Tiles getTile(int row, int col){
        return board.get(col).get(row);
    }
    public Tiles getTile(Point p){
        int row=p.x;
        int col=p.y;
        return board.get(col).get(row);
    }

    //stampa la matrice
    public void print(){
        for( int i=0; i<numRows;i++){
            for( int j=0; j<numCols;j++){
                System.out.print(board.get(j).get(i));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public ArrayList<ArrayList<Tiles>> getBoard() {
        return board;
    }

    public ArrayList<Tiles> getColumn(int col){
        return board.get(col);
    }
    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    // method to check if column x is full
    public boolean columnIsFull(int x){
        for (int i=0; i<numRows; i++){
            if (board.get(x).get(i).equals(Tiles.EMPTY)) return false;
        }
        return true;
    }

    // method to check if row x is full
    public boolean rowIsFull(int x){
        for (int i=0; i<numRows; i++){
            if (board.get(i).get(x).equals(Tiles.EMPTY)) return false;
        }
        return true;
    }
}
