package it.polimi.ingsw.app;

import it.polimi.ingsw.client.ClientBase;
import it.polimi.ingsw.server.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

/**
 * Class that lets you chose if you want to open Server or Client.
 */
public class MyShelfieApplication extends Application{

    /**
     * Default constructor.
     */
    public MyShelfieApplication() {
    }



    /**
     * Static method to run Server or ClientBase.
     *
     * @param args the arguments that you insert on the opening jar (args[0] contains the choice to start Server or Client).
     * @throws IOException in case of problem with input/output.
     * @throws InterruptedException in case of problem with thread interruption.
     * @throws NotBoundException in cas of attempt to search or disassociate in the registry a name that does not have an associated link.
     */
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {

        //if there is not args in input
        if (args.length==0){
            String st;

            do {
                System.out.print("""
                                        Insert --server to start the Server
                                        or
                                        Insert --client to start the Client
                                        """);

                Scanner scanner = new Scanner(System.in);
                String word;

                //wait for data input and reads them
                word=String.valueOf(scanner.nextLine());

                st=correctInput(word);
                //if args[0] contains the commands --server or --client run the correct one
                if (!st.equals("Error")) {
                    runCorrectService(st, args);
                }
                //else request the command
            }while (st.equals("Error"));
        }
        //if there is args in input
        else {
            String st;
            st=correctInput(args[0]);
            //if args[0] contains the commands --server or --client run the correct one
            if (!st.equals("Error")){
                runCorrectService(st, args);
            }
            //else request the command
            do{

                System.out.print("""
                                    Insert --server to start the Server
                                    or
                                    Insert --client to start the Client
                                    """);
                Scanner scanner = new Scanner(System.in);

                //wait for data input and reads them
                String word=String.valueOf(scanner.nextLine());
                st=correctInput(word);
                if (!st.equals("Error")){
                    runCorrectService(st, args);
                }

            }
            while (st.equals("Error"));
        }
    }



    /**
     * The main entry point for all JavaFX applications.
     *
     * @param stage the stage for this application, onto which the application scene can be set.
     * @throws IOException in case of problems with input/output.
     */
    @Override
    public void start(Stage stage) throws IOException {
    }



    /**
     * Method to verify if the user insert the correct choice (--server or --client).
     *
     * @param p0 the string that contains the choice.
     * @return a <i>string</i> containing Error if there is an error, otherwise contains the choice (Server or Client).
     */
    private static String correctInput(String p0){
        if (p0.equals("--server")){
            return "Server";
        }
        else if (p0.equals("--client")) {
            return "Client";
        }
        else {
            return "Error";
        }
    }

    /**
     * Method to run Server or Client.
     *
     * @param st contains the choice of whether to run the Server or the Client.
     * @param args the arguments that you insert on the opening .jar (args[0] contains the choice to start Server or Client).
     * @throws IOException in case of problem with input/output.
     */
    private static void runCorrectService(String st, String[] args) throws IOException {
        switch (st){
            case "Server" ->
                runAsServer(args);
            case "Client" ->
                runAsClient(args);
        }
    }

    /**
     * Method to run the Server
     *
     * @param args the arguments that you insert on the opening .jar (args[0] contains the choice to start Server or Client).
     * @throws IOException in case of problem with input/output.
     */
    private static void runAsServer(String[] args) throws IOException {
        Server.main(args);
    }

    /**
     * Method to run the Client
     *
     * @param args the arguments that you insert on the opening .jar (args[0] contains the choice to start Server or Client).
     */
    private static void runAsClient(String[] args) {
        ClientBase.main(args);
    }
}
