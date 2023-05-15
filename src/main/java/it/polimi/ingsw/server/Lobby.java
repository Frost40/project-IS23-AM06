package it.polimi.ingsw.server;

import it.polimi.ingsw.server.Exceptions.LobbyNotExists;
import it.polimi.ingsw.server.Exceptions.UsernameAlreadyTaken;
import it.polimi.ingsw.server.Model.Model;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.VirtualView.VirtualView;

import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Lobby {
    private Controller controller;

    //Diventa una mappa tra username e  indirizzo ip e facciamo i check li
    private final ArrayList<String> usernames = new ArrayList<>();
    private final HashMap<Integer,ArrayList<String>> lobbys = new HashMap<>();
    private final HashMap<Integer,Integer> gamePlayerNumber = new HashMap<>();
    private final Queue<Integer> waitingLobbys = new LinkedList<>();
    private final HashMap<Integer, Model> games = new HashMap<>();
    private final HashMap<String , Player> players = new HashMap<>();
    private final HashMap<String , VirtualView> views = new HashMap<>();
    private int gameNumber = 0;


    public synchronized boolean waitingLobbies(){
        return !waitingLobbys.isEmpty();
    }

    public synchronized int handleClient(String client,VirtualView view) throws UsernameAlreadyTaken {

        if(!usernames.contains(client.toLowerCase())) {

            usernames.add(client.toLowerCase());
            controller.addView(view);

            System.out.println("handleClient lobby" + client);

            //if there are waiting lobbies, add the client to the longest waiting lobby
            if (waitingLobbies()) {
                try {
                    //return the game number
                    return addClient(client);
                } catch (LobbyNotExists e) {
                    return -1;
                }

                //if there aren't any, return -1
            } else return -1;
        }else throw new UsernameAlreadyTaken();
    }




    public synchronized int newLobby(String client,int numplayers){

        //Update the game number
        gameNumber+=1;

        //create a new lobby and add the player
        ArrayList<String> newLobby = new ArrayList<>();
        newLobby.add(client);

        //add the new lobby to the lobby list
        lobbys.put(gameNumber,newLobby);

        System.out.println("new lobby :" + gameNumber);
        //record the selected number of player
        gamePlayerNumber.put(gameNumber, numplayers);

        //add it to the waiting lobbies list
        waitingLobbys.add(gameNumber);
        System.out.println("waiting new lobby"+waitingLobbys.peek());
        //create the new game
        newGame(gameNumber);

        //return the game number
        return gameNumber;
    }



    /**
     * Adds a client to a waiting lobby and starts the game when it is full
     * @param client    The client you want to add
     */
    public synchronized int addClient(String client) throws LobbyNotExists {

        //Get the ID of the lobby that is waiting for the longest time
        Integer index = waitingLobbys.peek();

        if(index!=null) {
            System.out.println("add client: " + index);
            //Add the client to the lobby and set his lobbyID
            lobbys.get(index).add(client);

            checkStart(index);

            //return the game number
            return index;

        }else throw new LobbyNotExists();
    }

    public void checkStart(int index){
        //If the lobby reached the max number of player, start the game.
        if (lobbys.get(index).size() == gamePlayerNumber.get(index)) {
            waitingLobbys.remove();
            startGame(index);
        }
    }

    public void startGame(int index) {
        //create the model and the array that will contain alla players
        ArrayList<Player> playerList = new ArrayList<>();
        ArrayList<VirtualView> virtualViews = new ArrayList<>();
        ArrayList<String> myLobby = lobbys.get(index);

        System.out.println("Start game:"+index);
        //for every client in the lobby, create his player and add it to the player map
        for (String s : myLobby) {
            System.out.println("start game lobby"+s);
            Player p = new Player(s);

            players.put(s,p);
            playerList.add(p);
            virtualViews.add(views.get(s));
        }

        games.get(index).setVirtualViews(virtualViews);
        games.get(index).setPlayers(playerList);

        //start the game
        controller.startGame(index);
    }


    public void newGame(int num){
        Model m = new Model();
        games.put(num, m);
        m.setGameID(num);

    }


    public void closeGame(int gameID){

        for(String s : games.get(gameID).getPlayers().stream().map(Player::getUsername).toList()){
            players.remove(s);
        }

        //Remove the model
        games.remove(gameID);

    }
    public void playerDisconnection(String username){
        //Forever player disconnection
        views.remove(username);
        players.remove(username);
        usernames.remove(username);

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public HashMap<Integer,Model> getGames() {
        return games;
    }
    public HashMap<String, Player> getPlayers() {
        return players;
    }
    public HashMap<String, VirtualView> getViews() {
        return views;
    }

}
