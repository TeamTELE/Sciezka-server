package Sciezka_Server;

import Sciezka_Server.util.ReadConfig;
import Sciezka_Server.util.UtilHighScores;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Wątek odpowiedzialny za komunikację serwera z klientami.
 * Umożliwia wysłanie listy najlepszych wyników, definicji poziomów,
 * danych konfiguracyjnych oraz pobranie wyniku rozgrywki
 *
 * @author Kacper Bloch
 * @author Kamil Dzierżanowski
 * @version 1.0
 */
public class Server extends Thread {

    /**
     * Gniazdo servera
     */
    private ServerSocket serverSocket;

    /**
     * Określa czy serwer ma działać
     */
    private boolean execute;

    /**
     * Port wykorzystywany przez serwer
     */
    private int port;

    /**
     * Pole tekstowe do którego wypisywany jest log z dzaiałania serwera
     */
    private JTextArea log;

    /**
     * Konstruktor klasy. Przypisuje numer portu
     */
    public Server(JTextArea textArea) {

        this.log = textArea;
        this.port = ConfigServer.port;
    }

    /**
     * Metoda wykonywana przy starcie wątku, czyli uruchamiająca serwer.
     * Otwiera socket na porcie wskazanym w pliku konfiguracyjnym, a następnie
     * czeka na połączenie. W momencie nawiązania go, otwiera strumienie IO i czyta
     * treść połączenia. Pierwszy wyraz jest przyrównywany do definicji interfejsu
     * i podejmowana jest decyzja o sformułowaniu odpowiedzi, która następnie jest
     * wysyłana przez strumień. Liczba połączeń jest monitorowana i całe działanie
     * serwera jest logowane w oknie aplikacji.
     */
    public void run() {
        log.append("[START] Uruchamianie serwera...\n");
        int count = 0;

        try {
            log.append("Otwieranie gniazdka na porcie " + port + "... ");

            serverSocket = new ServerSocket(port);
            execute = true;

            log.append("Sukces!\n");

            while (execute) {
                try {
                    log.append("\n[" + (count  + 1) + "] Oczekiwanie na połączenie... ");

                    Socket clientSocket = serverSocket.accept();

                    log.append("\nOtrzymano połączenie z " + clientSocket.getRemoteSocketAddress() + "\n");
                    count++;

                    log.append("Otwieranie strumieni IO... ");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                    log.append("Sukces!\n");

                    String[] str = reader.readLine().split("/");
                    String command = str[0];

                    log.append("Otrzymane polecenie: " + command + "\n");

                    System.out.println(command);
                    String answer;

                    switch (command) {

                        case Protocol.PING:
                            answer = "CONNECTION_SUCCESSFUL";
                            break;

                        case Protocol.GETCONFIG:
                            answer = Protocol.CONFIG;
                            Properties property = ReadConfig.getProperties(Main.gameConfigPath);
                            Set<String> keys = property.stringPropertyNames();

                            for (String key: keys) {
                                String value = property.getProperty(key);
                                answer = answer + "#" + key + "=" + value;
                            }

                            break;

                        case Protocol.GETHIGHSCORES:
                            List<Score> highScores = UtilHighScores.readHighScores("highscores.txt");

                            answer = Protocol.HIGHSCORES;

                            for (Score score : highScores) {
                                answer = answer + "/" + score.getNick() + "=" + score.getScore();
                            }
                            break;

                        case Protocol.GETLEVEL:
                            int numberOfLevel = Integer.parseInt(str[1]);

                            BufferedReader levelReader = new BufferedReader(new FileReader(ConfigServer.filenameLevel[numberOfLevel - 1]));

                            answer = Protocol.LEVEL;

                            String lineStr;

                            while ((lineStr = levelReader.readLine()) != null) {
                                answer = answer + "/" + lineStr;
                            }

                            levelReader.close();
                            break;

                        case Protocol.GETLEVELS:
                            answer = (Protocol.LEVELS + "/" + ConfigServer.filenameLevel.length);

                            break;

                        case Protocol.GAMESCORE:

                            String[] score = str[1].split(new String("="));

                            if (UtilHighScores.updateHighScores(UtilHighScores.readHighScores("highscores.txt"), new Score(score[0], Integer.parseInt(score[1])))) {

                                answer = Protocol.GAMESCOREACCEPTED;

                            } else {
                                answer = Protocol.GAMESCOREREJECTED;
                            }

                            break;

                        default:
                            answer = Protocol.ERROR;
                            break;
                    }

                    writer.println(answer);
                    clientSocket.close();
                    log.append("Wysłano odpowiedź: " + answer + "\n");

                } catch (SocketException se) {
                    log.append("\n\nZamknięto połączenie.\n");
                } catch (Exception e) {
                    log.append("Błąd! Wyjątek:\n");
                    log.append(e.toString());
                }
            }
            serverSocket.close();
        } catch (Exception ex) {
            log.append("Błąd! Wyjątek:\n");
            log.append(ex.toString());
        }

        log.append("[STOP] Zatrzymano serwer.\n\n");
    }

    /**
     * Ustawia flagę powodującą zatrzymanie pętli w wątku i przez to jego zakończenie
     */
    public void halt() {

        try {
            serverSocket.close();
        } catch (IOException e) {
            log.append("\nNie udało się zamknąć portu! Wyjątek:\n");
            log.append(e.toString());
        }
        execute = false;
    }
}
