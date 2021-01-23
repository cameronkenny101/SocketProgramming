import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player extends JFrame{

    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int playerID;
    private int otherPlayerID;
    private int[] values;
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private ClientSideConnection csc;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
        values = new int[4];
        turnsMade = 0;
        myPoints = 0;
        enemyPoints = 0;
    }

    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Player: " + playerID + " Turn-Based Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);
        message.setText("A simple turn based game created in Java");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);

        if(playerID == 1) {
            message.setText("You are player #1. You go first");
            otherPlayerID = 2;
        } else {
            message.setText("You are player #2. Wait your turn");
            otherPlayerID = 1;
        }
        this.setVisible(true);
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    // Client Connection Inner Class
    private class ClientSideConnection {
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientSideConnection() {
            System.out.println("****  C L I E N T   ****");
            try {
                socket = new Socket("localhost", 30000);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerID = dataIn.readInt();
                System.out.println("Connected in server as Player " + playerID);
                maxTurns = dataIn.readInt() / 2;
                values[0] = dataIn.readInt();
                values[1] = dataIn.readInt();
                values[2] = dataIn.readInt();
                values[3] = dataIn.readInt();
                System.out.println("Max turns: " + maxTurns);
                System.out.println("Value 1: " + values[0]);
                System.out.println("Value 2: " + values[1]);
                System.out.println("Value 3: " + values[2]);
                System.out.println("Value 4: " + values[3]);
            } catch (IOException ex) {
                System.out.println("Error in CSC method");
            }
        }
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectToServer();
        p.setUpGUI();
    }


}
