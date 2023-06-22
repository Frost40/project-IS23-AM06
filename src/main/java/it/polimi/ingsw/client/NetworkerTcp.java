package it.polimi.ingsw.client;


import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.utils.Messages.*;
import it.polimi.ingsw.utils.JsonReader;
import org.json.simple.parser.ParseException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;

/**
 * <p>Class used to manage the tcp connection</p>
 * <ul>
 *     <il>Creates socket, reader and the object output stream</il>
 *     <il>Sends message to server</il>
 *     <il>Starts disconnection</il>
 * </ul>
 */
public class NetworkerTcp implements Networker, PropertyChangeListener {
    private static int port;
    private String serverIP;
    private Socket socket ;
    private ObjectOutputStream oos;
    private View view;
    private final ClientState clientState;
    private Reader reader;
    /**
     * Initialize the networker with tho the given client state and host
     * @param clientState client state
     * @param host server's host
     */
    public NetworkerTcp(ClientState clientState,String host) {
        JsonReader config;
        try {
            InputStream is=this.getClass().getClassLoader().getResourceAsStream("ConnectionPorts.json");
            config=new JsonReader(is);
            this.serverIP =host;
            port=config.getInt("tcpPort");
        } catch (IOException e) {
            System.out.println("Input/Output problems. Try to close and reopen the program");
            e.printStackTrace();
            close();
        } catch (ParseException e) {
            System.out.println("Problem with tha parsing of file json. Try to close and reopen the program");
            e.printStackTrace();
            close();
        }
        this.clientState = clientState;
    }

    /**
     * Initialize the networker with tho the given client state
     * @param clientState client state
     */
    public NetworkerTcp(ClientState clientState) {
        JsonReader config;
        try {
            InputStream is=this.getClass().getClassLoader().getResourceAsStream("ConnectionPorts.json");
            config=new JsonReader(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.clientState = clientState;
        port=config.getInt("tcpPort");
    }

    /**
     * Method to create socket, object output stream and reader
     * @return true if everything goes fine, false in other cases
     */
    public boolean initializeConnection() {
        try {
            socket = new Socket(serverIP, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            reader=new Reader(socket,oos,this, clientState);
            reader.start();
        } catch (IOException e) {
//            e.printStackTrace();
            if (socket!=null){
                System.out.println( "Server is not responding...");
                close();
            }
            return false;
        }
        return true;

    }

    /**
     * Method to close socket and Object Output Stream
     */
    private void close(){
        try {
            socket.close();
            oos.close();
        } catch (IOException e) {
            System.out.println( "Error: unable to close the socket...");
            e.printStackTrace();
        }
    }

    /**
     * Method to send a disconnection message to server and then to start closing the programme
     * @param closing message to send to server
     */
    @Override
    public void closeProgram(Message closing) {
        try {
            oos.writeObject(closing);
            oos.flush();
            reader.disconnect();
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }
    }


    /**
     * Method to send to server the username selected by player
     * @param username a message which contains the username selected by the palyer
     */
    public void firstConnection (Message username){
        try {
            oos.writeObject(username);
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }

    }

    /**
     * Method to send to server the number of player selected
     * @param numberOfPlayers a message which contains the number of player
     */
    public void numberOfPlayersSelection(Message numberOfPlayers){
        try {
            oos.writeObject(numberOfPlayers);
            oos.flush();
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }

    }

    /**
     * Method to send to the server the positions of the tiles to remove from board
     * @param tiles a message which contains the positions of the tiles to remove from board
     */
    public void removeTilesFromBoard(Message tiles){
        try {
            oos.writeObject(tiles);
            oos.flush();
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }
    }

    /**
     * Method to send to the server the order of the tiles
     * @param ints a message which contains the order of the tiles
     */
    public void switchTilesOrder(Message ints){
        try {
            oos.writeObject(ints);
            oos.flush();
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }
    }

    /**
     * Method to send to the server the column chosen from the player
     * @param column a message which contains the column chosen by the player
     */
    public void addTilesToBookshelf (Message column){
        try {
            oos.writeObject(column);
            oos.flush();
        } catch (IOException e) {
            System.out.println( "Server is not responding...");
            e.printStackTrace();
        }
    }

    /**
     * Method to send to the view the message received
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        view.receivedMessage((Message) evt.getNewValue());
    }

    /**
     * <strong>Setter</strong> -> Sets the view
     * @param view The view created by the clientbase
     */
    @Override
    public void setView(View view) {
        this.view=view;
    }
    /**
     * <strong>Setter</strong> -> Sets the view
     * @param serverIP serverIp
     */
    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }



    /**
     * Method to send to the server the chat message written by the player
     * @param message a message which contains the chat message by the player
     */
    @Override
    public void chat(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
