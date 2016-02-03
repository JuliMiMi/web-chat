import jdk.internal.util.xml.impl.Input;

import java.util.*;
import java.io.*;
import java.net.*;

public class chatServer {

    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run() {
            String message;
                // поток входит в цикл (пока ответ сервера будет равен null), считывает за раз одну строчку
                // и добавляет её в прокручиваемую текстовую область (используя перенос строки (/n)
                try {
                    while ((message = reader.readLine()) != null) {
                        System.out.println("read" + message);
                        tellEveryone(message); // вызываем метод, который отправит сообщение клиенту
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}
