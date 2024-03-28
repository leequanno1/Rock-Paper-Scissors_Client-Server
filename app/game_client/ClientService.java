package app.game_client;

import app.messages.PlayTurnMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientService {
    public static String getUserNameFormInput(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter your username:");
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Short getRoomFormInput(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter room:");
            return Short.parseShort(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printDecisions(){
        System.out.println("============ Please enter your choice =============");
        System.out.println("||  1. ROCK \t\t\t\t\t ||");
        System.out.println("||  2. PAPPER \t\t\t\t\t ||");
        System.out.println("||  3. SCISSORS \t\t\t\t ||");
        System.out.println("||  \t\t\t\t\t\t ||");
        System.out.println("||  Please enter 1,2 or 3 only \t\t\t ||");
        System.out.println("===================================================");
    }

    public static Short getDecisionsFromInput(){
        Short desision = null;
        System.out.print("Enter here: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            desision = Short.parseShort(reader.readLine());
            if(desision < 1 || desision > 3){
                desision = null;
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println(" > Your desisions must be 1, 2 or 3.\n > Please type again.");
        }
        return desision;
    }

    public static void handleSetUsername(GameClient client) throws IOException {
        String username;
        do {
            username = ClientService.getUserNameFormInput();
            client.sendToServerAndWait(username);
        } while (!client.isEnterOk());
        client.setUserName(username);
    }

    public static void handleSetRoomNumber(GameClient client) throws IOException {
        Short roomNumber;
        do {
            roomNumber = ClientService.getRoomFormInput();
            client.sendToServerAndWait(roomNumber);
        } while (!client.isEnterOk());
        client.setRoomNumber(roomNumber);
    }

    public static void handleGamePlay(GameClient client) throws Exception {
        for(int i = 1; i <= 5; i++){
            System.out.println(">> Turn " + i);
            ClientService.printDecisions();
            Short desision = null;
            do {
                desision = ClientService.getDecisionsFromInput();
            } while (desision == null);
            PlayTurnMessage playTurnMessage = new PlayTurnMessage(desision);
            client.sendToServerAndWait(playTurnMessage);
            Thread.sleep(500);
        }
    }
//    public static void main(String[] args) {
//        printDecisions();
//        Short desi = null;
//        do {
//            desi = getDecisionsFromInput();
//        } while (desi==null);
//    }
}
