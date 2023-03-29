package it.polimi.ingsw;

import it.polimi.ingsw.server.Board;
import it.polimi.ingsw.server.Sachet;
import it.polimi.ingsw.server.Tiles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SachetTest {


    //TEST draw()
    @DisplayName("Return a randomic Tiles")
    @Test
    void draw() {
        Sachet sachet=new Sachet();
        assertNotEquals(sachet.draw(), Tiles.EMPTY);
        assertNotEquals(sachet.draw(), Tiles.NOTALLOWED);
    }

    //TEST remainigTiles()
    @DisplayName("Tiles that remaining in the sachet after the inizialization of 2 players' game")
    @Test
    void remainingTiles1() {
        Sachet sachet=new Sachet();
        assertEquals(132, sachet.remainingTiles());
        Board board=new Board(2, sachet);
        board.BoardInitialization();
        assertEquals(132-29, sachet.remainingTiles());
    }
    @DisplayName("Tiles that remaining in the sachet after the inizialization of 3 players' game")
    @Test
    void remainingTiles2() {
        Sachet sachet=new Sachet();
        assertEquals(132, sachet.remainingTiles());
        Board board=new Board(3, sachet);
        board.BoardInitialization();
        assertEquals(132-29-8, sachet.remainingTiles());
    }
    @DisplayName("Tiles that remaining in the sachet after the inizialization of 4 players' game")
    @Test
    void remainingTiles3() {
        Sachet sachet=new Sachet();
        assertEquals(132, sachet.remainingTiles());
        Board board=new Board(4, sachet);
        board.BoardInitialization();
        assertEquals(132-29-8-8, sachet.remainingTiles());
    }

    //TEST remainigTilesPerColor(Tiles)
    @DisplayName("Tiles that remaining per color in the sachet after the inizialization of 2 players' game")
    @Test
    void remainingTilesPerColor1() {
        Sachet sachet=new Sachet();
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.WHITE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.PINK));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.LIGHT_BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.YELLOW));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
        Board board=new Board(2, sachet);
        board.placeTiles(Tiles.WHITE, 3,3);
        board.placeTiles(Tiles.WHITE, 4,3);
        assertEquals(22-2, sachet.remainingTilesPerColor(Tiles.WHITE));
        board.placeTiles(Tiles.YELLOW, 5,3);
        assertEquals(22-1, sachet.remainingTilesPerColor(Tiles.YELLOW));
    }
    @DisplayName("Tiles that remaining per color in the sachet after the inizialization of 3 players' game")
    @Test
    void remainingTilesPerColor2() {
        Sachet sachet=new Sachet();
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.WHITE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.PINK));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.LIGHT_BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.YELLOW));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
        Board board=new Board(3, sachet);
        board.placeTiles(Tiles.WHITE, 3,3);
        board.placeTiles(Tiles.WHITE, 4,3);
        assertEquals(22-2, sachet.remainingTilesPerColor(Tiles.WHITE));
        board.placeTiles(Tiles.YELLOW, 5,3);
        assertEquals(22-1, sachet.remainingTilesPerColor(Tiles.YELLOW));
    }
    @DisplayName("Tiles that remaining per color in the sachet after the inizialization of 4 players' game")
    @Test
    void remainingTilesPerColor3() {
        Sachet sachet=new Sachet();
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.WHITE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.PINK));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.LIGHT_BLUE));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.YELLOW));
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
        Board board=new Board(4, sachet);
        board.placeTiles(Tiles.WHITE, 3,3);
        board.placeTiles(Tiles.WHITE, 4,3);
        assertEquals(22-2, sachet.remainingTilesPerColor(Tiles.WHITE));
        board.placeTiles(Tiles.YELLOW, 5,3);
        assertEquals(22-1, sachet.remainingTilesPerColor(Tiles.YELLOW));
    }

    //TEST addTiles(Tiles)
    @DisplayName("Added tiles in sachet")
    @Test
    void addTiles() {
        Sachet sachet=new Sachet();
        assertEquals(132, sachet.remainingTiles());
        //aggiunta tessera non ammessa
        sachet.addTiles(Tiles.GREEN);
        assertEquals(132, sachet.remainingTiles());
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
        //rimossa tessera
        sachet.removeTiles(Tiles.GREEN);
        assertEquals(132-1, sachet.remainingTiles());
        assertEquals(22-1, sachet.remainingTilesPerColor(Tiles.GREEN));
        //aggiunta tessera
        sachet.addTiles(Tiles.GREEN);
        assertEquals(132, sachet.remainingTiles());
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
    }

    //TEST removeTiles(Tiles)
    @DisplayName("Remove tiles from sachet")
    @Test
    void removeTiles() {
        Sachet sachet=new Sachet();
        assertEquals(132, sachet.remainingTiles());
        sachet.addTiles(Tiles.GREEN);
        assertEquals(132, sachet.remainingTiles());
        assertEquals(22, sachet.remainingTilesPerColor(Tiles.GREEN));
        sachet.removeTiles(Tiles.GREEN);
        assertEquals(132-1, sachet.remainingTiles());
        assertEquals(22-1, sachet.remainingTilesPerColor(Tiles.GREEN));
    }
}