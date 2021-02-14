package server;


import java.io.*;
import java.net.Socket;

    /**
     * Klasa Watek Serwera
     */
    public class ServerThread implements Runnable {
        /**
         * Socket
         */
        private Socket socket;

        /**
         * Konstruktor
         * @param socket socket
         */
        public ServerThread(Socket socket){
            this.socket=socket;
        }

        /**
         * Metoda odpowiedzialna za prace serwera z klientem
         */
        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    String fromClient = br.readLine();
                    if(fromClient!=null) {
                        //System.out.println("Client: " + fromClient);
                        String serverMessage = ServerCommands.serverAction(fromClient);
                        if(serverMessage=="CLOSE_CONNECTION_NOW"){
                            socket.close();
                        }
                        if(serverMessage=="CONNECTION_REJECTED")
                            socket.close();
                        else {
                            pw.println(serverMessage);
                            pw.flush();
                            //System.out.println("Server: " + serverMessage);
                            if (serverMessage == "LOGGEDOUT") {
                                socket.close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

