package app.game_client;

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
}
