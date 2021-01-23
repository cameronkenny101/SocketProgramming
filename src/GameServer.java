import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;

    public GameServer() {
        System.out.println("*******  G A M E  S E R V E R  *******");
        numPlayers = 0;
        turnsMade = 0;
        maxTurns = 4;
        values = new int[4];

        for(int i = 0; i < values.length; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Value " + (i + 1) + ": " + values[i]);
        }

        try {
            ss = new ServerSocket(30000);
        } catch (IOException ex) {
            System.out.println("Error in Game Server constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections");
            while (numPlayers < 2) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player has Joined. Num players = " + numPlayers);
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                if (numPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("Game lobby full. There are two players in game");
        } catch (IOException ex) {
            System.out.println("Error in acceptConnections method");
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            socket = s;
            playerID = id;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            } catch(IOException ex) {
                System.out.println("Error in SSC method");
            }
        }

        public void run() {
            try {
                dataOut.writeInt(playerID);
                dataOut.writeInt(maxTurns);
                dataOut.writeInt(values[0]);
                dataOut.writeInt(values[1]);
                dataOut.writeInt(values[2]);
                dataOut.writeInt(values[3]);
                dataOut.flush();

                while (true) {

                }

            } catch (IOException ex) {
                System.out.println("Error in run method in SSC");
            }
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
