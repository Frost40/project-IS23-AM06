package it.polimi.ingsw.server;

public enum Define {
    NUMBEROFROWS_BOOKSHELF (6),
    NUMBEROFCOLUMNS_BOOKSHELF (5),
    MAXNUMBEROFTILES_BOOKSHELF (NUMBEROFROWS_BOOKSHELF.i*NUMBEROFCOLUMNS_BOOKSHELF.i),
    NUMBEROFROWS_BOARD (9),
    NUMBEROFCOLUMNS_BOARD (9),
    NUMBEROFPERSONALOBJECTIVE (12),
    NUMBEROFCOMMONOBJECTIVE (12),
    NUMBEROFTILEPERCOLOR_SACHET (22),
    NUMBEROFCOLOR_SACHET (6),
    MAXNUMBEROFTILES_SACHET (NUMBEROFTILEPERCOLOR_SACHET.i* NUMBEROFCOLOR_SACHET.i)
    ;

    private final int i;
    Define(int i) {
        this.i=i;
    }

    public int getI() {
        return i;
    }
}
