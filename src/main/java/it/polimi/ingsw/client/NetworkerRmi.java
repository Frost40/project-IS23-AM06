package it.polimi.ingsw.client;


import it.polimi.ingsw.server.ControllerInterface;
import it.polimi.ingsw.server.Messages.IntArrayMessage;
import it.polimi.ingsw.server.Messages.IntMessage;
import it.polimi.ingsw.server.Messages.Message;
import it.polimi.ingsw.server.Messages.PointsMessage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkerRmi implements Networker {
    private static int port = 1234;
    private String username;
    private int lobbyID;
    private int gameID;
    private Message message;
    private static ControllerInterface controller;

    /*
    public NetworkerRmi()  {
        JsonReader config;
        try {
            config = new JsonReader("src/main/java/it/polimi/ingsw/server/config/Server.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        port=config.getInt("port");
    }

     */

    /**
     * Method to initialize an RMI connection
     *
     */
    public void initializeConnection () {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            // Looking up the registry for the remote object
            controller = (ControllerInterface) registry.lookup("Controller");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("Created RMI connection with Server");
    }

    /**
     * Asks the client to enter a valid username. Once he has
     * done the client gets added to the lobby
     */
    public Message firstConnection (Message username) {
        try {
            message = controller.handleNewClient(username.getUsername());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    /**
     *
     * @param numberOfPlayers
     */
    public Message numberOfPlayersSelection(Message numberOfPlayers) {
        IntMessage tempMessage = (IntMessage) numberOfPlayers;

        try {
            message = controller.newLobby(this.username, tempMessage.getNum());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    /**
     * Removes a maximum of 3 tiles from the board send by coordinates
     *
     * @param tiles     ArrayList containing the coordinates of the tiles to remove
     */
    public Message removeTilesFromBoard(Message tiles) {
        PointsMessage tempMessage = (PointsMessage) tiles;
        try {
            message = controller.removeTiles(gameID, username, tempMessage.getTiles());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    /**
     *
     * @param ints
     */
    public Message switchTilesOrder(Message ints) {
        IntArrayMessage tempMessage = (IntArrayMessage) ints;
        try {
            message = controller.swapOrder(tempMessage.getIntegers(), gameID, username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    /**
     *
     * @param column
     */
    public Message addTilesToBookshelf (Message column) {
        IntMessage tempMessage = (IntMessage) column;
        try {
            message = controller.addToBookshelf(gameID, username, tempMessage.getNum());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }
}