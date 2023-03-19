package it.polimi.ingsw.CommonObjective;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Matrix;
import it.polimi.ingsw.Tiles;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class CommonObjective1 extends CommonObjective{


    static{
        subclasses.add(CommonObjective1.class);
    }

    public boolean checkCondition(Player player){

        // creation of set to contain all tiles approved by the algorithm
        Set<Point> nodes = new HashSet<>();

        // creation of 2 thread that will apply the algorithm
        // (the first one on the rows, the second on the columns) of player's bookshelf
        MyRunnable firstRunnable = new MyRunnable("row-Thread 1", player.getBookshelf().getTiles(), nodes);
        MyRunnable secondRunnable = new MyRunnable("column-Thread 2", player.getBookshelf().getTiles(), nodes);

        Thread rowThread = new Thread(firstRunnable);
        Thread columnThread = new Thread(secondRunnable);

        // starting the threads
        rowThread.start();
        columnThread.start();

        try {
            rowThread.join();
            columnThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // the division by 2 of the set's size will give us the number of pair found in the bookshelf
        if (nodes.size()/2 > 5) return true;

        return false;
    }
}

class MyRunnable implements Runnable {
    private String name;
    private final Matrix matrix;
    Set<Point> buffer;
    Point point1;
    Point point2;

    public MyRunnable(String name, Matrix matrix, Set<Point> buffer) {
        this.name = name;
        this.matrix = matrix;
        this.buffer = buffer;
    }

    public void run() {

        // code for the thread 1 that will analyze the rows
        if (name.equals("row-Thread 1")){
            for (int i=0; i<6; i++){
                for (int j=0; j<4; j++){

                    // Skipping if cell is set to EMPTY
                    if (matrix.getTile(i, j).equals(Tiles.EMPTY)) continue;

                    if (matrix.getTile(i, j).equals(matrix.getTile(i, j+1))){

                        // synchronizing on buffer so threads will check and add the coordinates one at the time
                        synchronized (buffer) {
                            point1 = new Point(i, j);
                            point2 = new Point(i, j+1);
                            if (!buffer.contains(point1) && !buffer.contains(point2)) {
                                buffer.add(point1);
                                buffer.add(point2);
                            }
                        }

                    }
                }

            }
        }

        // code for the thread 2 that will analyze the columns
        else if (name.equals("column-Thread 2")) {
            for (int j=0; j<5; j++){
                for (int i=0; i<5; i++){

                    // Skipping if cell is set to EMPTY
                    if (matrix.getTile(i, j).equals(Tiles.EMPTY)) continue;

                    if (matrix.getTile(i, j).equals(matrix.getTile(i+1, j))){

                        // synchronizing on buffer so threads will check and add the coordinates one at the time
                        synchronized (buffer) {
                            point1 = new Point(i, j);
                            point2 = new Point(i+1, j);
                            if (!buffer.contains(point1) && !buffer.contains(point2)) {
                                buffer.add(point1);
                                buffer.add(point2);
                            }
                        }

                    }

                }
            }
        }
    }
}
