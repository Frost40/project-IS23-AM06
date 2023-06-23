package it.polimi.ingsw.server;
import it.polimi.ingsw.server.Exceptions.LobbyNotExists;
import it.polimi.ingsw.server.Exceptions.UsernameAlreadyTaken;
import it.polimi.ingsw.server.Model.Model;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.VirtualView.VirtualView;
import java.util.*;

public class Lobby {
    private Controller controller;
    private final HashMap<Integer,ArrayList<String>> lobbys = new HashMap<>();
    private final HashMap<Integer,Integer> gamePlayerNumber = new HashMap<>();
    private final Queue<Integer> waitingLobbys = new LinkedList<>();
    private final HashMap<String,Integer> playerToLobby = new HashMap<>();
    private final HashMap<Integer, Model> games;
    private int gameNumber = 0;
    private final HashMap<String,Integer> playerToGame = new HashMap<>();

    private final ArrayList<String> usernames = new ArrayList<>();
    private final HashMap<String , Player> players;
    private final HashMap<String , Player> disconnectedPlayers = new HashMap<>();
    private final HashMap<String , VirtualView> views = new HashMap<>();


    public Lobby() {
        this.games = new HashMap<>();
        this.players = new HashMap<>();
    }

    public Lobby(HashMap<Integer, Model> games, HashMap<String, Player> players) {
        this.games = games;
        this.players = players;
    }

    public synchronized boolean waitingLobbies(){
        return !waitingLobbys.isEmpty();
    }

    public synchronized Optional<Integer> handleClient(String client,VirtualView view) throws UsernameAlreadyTaken {

        if(!usernames.contains(client.toLowerCase())) {

            usernames.add(client.toLowerCase());
            controller.addView(view);

            System.out.println(client+ " has logged in successfully");

            if (waitingLobbies()) {//if there are waiting lobbies, add the client to the longest waiting lobby
                    try {
                        //return the game number
                        return Optional.of(addClient(client));
                    } catch (LobbyNotExists e) {
                        return Optional.empty();
                    }
                    //if there aren't any, return empty Optional
                } else return Optional.empty();
            }else throw new UsernameAlreadyTaken();
    }




    public synchronized int newLobby(String client,int numplayers){
        //Update the game number
        gameNumber+=1;

        System.out.println(client+" has created a new lobby. Number of players: "+ numplayers + " ID: " +gameNumber);
        //create a new lobby and add the player
        ArrayList<String> newLobby = new ArrayList<>();
        newLobby.add(client);

        //add the new lobby to the lobby list
        lobbys.put(gameNumber,newLobby);
        playerToLobby.put(client,gameNumber);

        //record the selected number of player
        gamePlayerNumber.put(gameNumber, numplayers);

        //add it to the waiting lobbies list
        waitingLobbys.add(gameNumber);

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
            System.out.println( client+ " added to lobby number: " + index);
            //Add the client to the lobby and set his lobbyID
            lobbys.get(index).add(client);
            playerToLobby.put(client,index);

            checkStart(index);

            //return the game number
            return index;

        }else throw new LobbyNotExists();
    }

    public void checkStart(int index){
        //If the lobby reached the max number of player, start the game.
        if (lobbys.get(index).size() == gamePlayerNumber.get(index)) {
            waitingLobbys.remove(index);
            startGame(index);
        }
    }

    private void startGame(int index) {
        //create the model and the array that will contain alla players
        ArrayList<Player> playerList = new ArrayList<>();
        ArrayList<VirtualView> virtualViews = new ArrayList<>();
        ArrayList<String> myLobby = lobbys.get(index);

        //for every client in the lobby, create his player, add it to the playerToGame map
        // and remove it from the playerToLobby map
        for (String s : myLobby) {
            Player p = new Player(s);

            players.put(s,p);
            playerList.add(p);
            virtualViews.add(views.get(s));
            playerToGame.put(s,index);
            playerToLobby.remove(s);
        }

        //Close the lobby
        lobbys.remove(index);

        //Add players and virtual views to the model
        games.get(index).setVirtualViews(virtualViews);
        games.get(index).setPlayers(playerList);

        //start the game
        controller.startGame(index);
    }


    private void newGame(int num){
        Model m = new Model();
        games.put(num, m);
        m.setGameID(num);

    }


    public synchronized void closeGame(int gameID){

        for(String s : games.get(gameID).getPlayers().stream().map(Player::getUsername).toList()){
            disconnectedPlayers.remove(s);
            players.remove(s);
            playerToGame.remove(s);
            views.get(s).setDisconnected(true);
            views.remove(s);

        }

        //Remove the model
        games.remove(gameID);

    }
    public synchronized void playerDisconnection(String username){

        VirtualView view =views.get(username);
        if(view != null) {
            view.setDisconnected(true);
        }


        //If in a lobby remove him
        Integer lobbyID = playerToLobby.get(username);
        if(lobbyID!=null){

            lobbys.get(lobbyID).remove(username);

            if(lobbys.get(lobbyID).size()==0){
                waitingLobbys.remove(lobbyID);
            }
        }

        //If in a game disconnect him
        Integer gameID=playerToGame.get(username);
        if(gameID!=null){
            games.get(gameID).disconnectPlayer(players.get(username), views.get(username));

        }

        //Add him to the disconnected players
        disconnectedPlayers.put(username,players.get(username));

        //Remove him from the necessary maps
        views.remove(username);
        players.remove(username);
        usernames.remove(username);

    }


    public synchronized int playerReconnection(String username,VirtualView view){
        Player player = disconnectedPlayers.get(username);
        disconnectedPlayers.remove(username);

        usernames.add(username);
        players.put(username,player);
        views.put(username,view);

        int index = playerToGame.get(username);
        games.get(index).playerReconnection(player,view);
        return index;

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

    public HashMap<String, Integer> getPlayerToGame() {
        return playerToGame;
    }

    public HashMap<String, Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }
}
