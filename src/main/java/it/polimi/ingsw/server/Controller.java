package it.polimi.ingsw.server;

import it.polimi.ingsw.server.Exceptions.MoveNotPossible;
import it.polimi.ingsw.server.Exceptions.NotCurrentPlayer;
import it.polimi.ingsw.server.Messages.Message;
import it.polimi.ingsw.server.Messages.MessageTypes;
import it.polimi.ingsw.server.View.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    Lobby lobby;
    private HashMap<Integer,Model> games;

    private HashMap<String ,Player > playerIDs;

    private ArrayList<ArrayList<View>> views;

    /**
     * Contructor
     * @param loby The lobby of the server
     * @param models  The hashmap of all current games
     */
    public Controller(Lobby loby, HashMap<Integer,Model> models) {
        lobby = loby;
       games = models;
       views = new ArrayList<>();
       playerIDs = new HashMap<>();
    }


    /**
     * Adds the player of the game to the list af all players,
     * initializes the selected model.
     * @param ID The ID of the game you want to start
     */
    public void startGame(int ID)  {
        //Adds
        for(Player p : games.get(ID).getPlayers()) {
            playerIDs.put(p.getUsername(),p);
        }
        games.get(ID).initialization();
    }




    public void addToBookshelf(int gameID, String playerID, ArrayList<Tiles> array, int col ){

        try {
            games.get(gameID).addToBookShelf(playerIDs.get(playerID),array,col);
        } catch (MoveNotPossible e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Swap the order of the array of selected tiles to the order describes in the array ints.
     * ex. oldSelectedTiles[G,B,Y], ints[2,1,3] --> newSelectedTiles[B,G,Y]
     *
     * @param ints The new order in which you want the array
     * @param gameID ID of the game
     * @param playerID username of the player
     */
    public void swapOrder(ArrayList<Integer> ints, int gameID, String playerID){

        try {
            games.get(gameID).swapOrder(ints,playerIDs.get(playerID));
        } catch (NotCurrentPlayer e) {
            throw new RuntimeException(e);
        }

    }


    public void removeTiles(int gameID,String playerID, ArrayList<Point> points){
        try {
            games.get(gameID).removeTileArray(playerIDs.get(playerID),points);
        } catch (MoveNotPossible e) {
            throw new RuntimeException(e);
        }
    }




    //public void saveState(int gameID){games.get(gameID).saveState();}

    //Lobby methods

    public void newLobby(ServerClientHandler client,int players){
        lobby.newLobby(client,players);
    }

    public Message handleNewClient(ServerClientHandler client){
        Message reply = new Message();
        if(lobby.handleClient(client)){
            reply.setType(MessageTypes.WAITING_FOR_PLAYERS);
            reply.setContent("Added to a game. Waiting for other player...");
            return  reply;
        }else {
            reply.setType(MessageTypes.NEW_LOBBY);
            reply.setContent("Select the number of players");
            return reply;
        }
    }




    public void addPlayer(String username, Player player){
        playerIDs.put(username,player);
    }
}