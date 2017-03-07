package Sciezka_Server;

import java.util.Properties;

/**
 * Klasa przechowująca stałe operacyjne serwera gry.
 * Posiada metodę, która zapisuje wartości pół na podstawie
 * kolekcji typu Properties.
 *
 * @author Kamil Dzieżanowski
 * @author Kacper Bloch
 * @version 1.0
 */
public class ConfigServer {

    /**
     * Port wykorzystywany przez serwer
     */
    public static int port;

    /**
     * Limit czasu połączenia
     */
    public static int timeout;

    /**
     * Nazwy plików z definicjami poziomów
     */
    public static String[] filenameLevel;

    /**
     * Nazwa pliku z najlepszymi wynikami
     */
    public static String fileNameHighScores;

    /**
     * Liczba dostępnym poziomów
     */
    public static int numberOfLevels;

    /**
     * Metoda parsująca wczytane dane konfiguracyjne
     * @param properties dane do sparsowania
     */
    public static void writeValues(Properties properties) {

        port = Integer.parseInt(properties.getProperty("port"));
        timeout = Integer.parseInt(properties.getProperty("timeout"));
        filenameLevel = properties.getProperty("filenameLevel").split(" ");
        numberOfLevels = filenameLevel.length;
        fileNameHighScores = properties.getProperty("filenameHighScores");
    }
}
