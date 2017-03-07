package Sciezka_Server;

import Sciezka_Server.util.ReadConfig;

import javax.swing.*;
import java.io.IOException;

/**
 * Aplikacja serwerowa do gry Scieżka.
 * Umożliwia uruchomienie serwera, który będzie zapewniał
 * dane konfiguracyjne do gry oraz plansze do gry, a także
 * obsługiwał listę najlepszych wyników aktualizowaną na
 * bieżąco.
 *
 * @author Kacper Bloch
 * @author Kamil Dzierżanowski
 * @version 1.0
 */
public class Main {

    /**
     * Scieżka dostępu do pliku konfiguracyjnego gry
     */
    public static String gameConfigPath;

    /**
     * Statyczna metoda wykonywana jako uruchomienie aplikacji.
     * Wczytuje dane konfiguracyjne do przekazania klientowi oraz
     * własne dane konfiguracyjne potrzebne do funkcjonowania.
     * Tworzy instancję okna Swing, które będzie udostępniało
     * dalszą część aplikacji poprzez interfejs.
     *
     * @param args argumenty konsolowe
     */
    public static void main(String[] args) {

        String serverConfigPath;

        if (args.length < 2) {
            serverConfigPath = "serverconfig.properties";
            gameConfigPath = "gameconfig.properties";
        } else {
            serverConfigPath = args[0];
            gameConfigPath = args[1];
        }

        SwingUtilities.invokeLater(() -> {

            try {
                ConfigServer.writeValues(ReadConfig.getProperties(serverConfigPath));

                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);

            } catch (IOException e) {
                System.out.println("Exception when loading properties: " + e);
            }
        });

    }
}
