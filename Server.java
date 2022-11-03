import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {

    public static void main(String[] args) {
        String currPathName = "";
        try {
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                for (;;) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(), StandardCharsets.UTF_8));

                    PrintWriter writer = new PrintWriter(
                            socket.getOutputStream());

                    while (!reader.ready())
                        ;

                    String[] items = new String[200];
                    if (reader.ready()) {
                        String line = reader.readLine();
                        items = line.split(" ");
                    }

                    while (reader.ready()) {
                        System.out.println(reader.readLine());
                    }
                    currPathName = items[1];
                    File currDir = new File(currPathName);

                    if (currDir.exists()) {
                        File [] files = currDir.listFiles();
                        writer.println("HTTP/1.1 200 OK");
                        writer.println("Content-Type: text/html; charset=utf-8");
                        writer.println();
                        for (File file : files) {
                            writer.println(file.getName()+ "<br />");
                            //writer.println("");
                        }
                        
                        writer.flush();
                    } else {

                        writer.println("HTTP/1.1 404 OK");
                        writer.println("Content-Type: text/html; charset=utf-8");
                        writer.println();
                        writer.println("<h1>File is not exist</h1>");
                        writer.flush();
                    }

                 socket.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
