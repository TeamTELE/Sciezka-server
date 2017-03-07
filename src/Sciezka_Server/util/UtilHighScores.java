package Sciezka_Server.util;

import Sciezka_Server.Score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zawierająca metody - narzędzia do obsługi najlepszych wyników, takie jak odczyt, zapis, aktualizacja
 * Created by Kacper on 2017-01-21.
 */
public class UtilHighScores {

    /**
     * Wczytuje liste najlepszych wyników z pliku, którego ścieżka zostaje podana jako argument
     *
     * @param textFileName ścieżka pliku zawierającego liste najlepszych wyników
     * @return lista najlepszych wyników
     */
    public static List<Score> readHighScores(String textFileName) {
        List<Score> highScores = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFileName));

            String lineStr;

            while ((lineStr = reader.readLine()) != null) {

                Score score = new Score(lineStr, Integer.parseInt(reader.readLine()));
                highScores.add(score);

            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return highScores;
    }

    /**
     * Zapisuje liste najlepszych wyników do pliku, którego ścieżka zostaje podana jako argument
     *
     * @param highScores   lista najlepszych wyników
     * @param textFileName ścieżka pliku, do którego zostanie zapisana lista najlepszych wyników
     */
    public static void writeHighScores(List<Score> highScores, String textFileName) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFileName));

            for (Score s : highScores) {

                writer.write(s.getNick());
                writer.newLine();
                writer.write(String.valueOf(s.getScore()));
                writer.newLine();
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

    }

    /**
     * Aktualizuje liste najlepszych wyników. Sprawdza czy uzyskany wynik jest większy od najgorszego wyniku na liście.
     * Jeśli tak lista zostaje zaktualizowana. Jeśli wynik rozgrywki jest mniejszy lub równy najgorszemu wynikowi to wynik rozgrywki nie zostaje zapisany i jest porzucany.
     *
     * @param highScores lista najlepszych wyników
     * @param score      wynik rozgrywki
     * @return false - wynik odrzucony, true - wynik przyjęty
     */
    public static boolean updateHighScores(List<Score> highScores, Score score) {

        if (score.getScore() > highScores.get(highScores.size() - 1).getScore()) {
            highScores.add(score);
            highScores.sort((o1, o2) -> o2.getScore() - o1.getScore());
            highScores.remove(highScores.size() - 1);

            writeHighScores(highScores, "highScores.txt");
            return true;
        } else {
            return false;
        }
    }
}
