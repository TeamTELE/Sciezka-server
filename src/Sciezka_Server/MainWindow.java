package Sciezka_Server;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa głównego okna aplikacji serwerowej. Tworzy okno Swinga
 * w którym jest pole tekstowe na log z działania serwera oraz przycisk
 * do sterowania jego działaniem, a także zapewnia obsługę interfejsu
 * i komunikacj z aplikacją serwerową.
 *
 * @author Kacper Bloch
 * @author Kamil Dzierżanowski
 * @version 1.0
 */
public class MainWindow extends JFrame {

    /**
     * Przecisk rozpoczynający działanie serwera
     */
    private JButton controlButton;

    /**
     * Pole zawierające log z działania serwera. Referencja przekazywana
     * jest do procesu serwerowego aby ten mógł dopisywać do niego linie.
     */
    private JTextArea textArea;

    /**
     * Wątek odpowiedzialny za działanie serwera
     */
    private Server server;

    /**
     * Konstruktor tworzy okno aplikacji serwerowej, w której zawarte jest
     * pole tekstowe, do którego logowane będą działania serwera, oraz
     * przycisk służący do uruchamiania albo zatrzymywania serwera.
     */
    public MainWindow() {

        this.setTitle("Sciezka_Server");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 400);

        JPanel panel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setHighlighter(null);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        controlButton = new JButton("Start");
        controlButton.addActionListener(new StartListener());

        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(controlButton);

        panel.add(scrollPane);
        panel.add(panel2, BorderLayout.SOUTH);

        this.getContentPane().add(panel);
    }

    /**
     * Słuchacz przycisku rozpoczęcia działania serwera.
     * Tworzy nowy wątek Server() i go uruchamia, a następnie
     * odczepia się od przycisku, i przyczepia mu słuchacza
     * zakończenia działania serwera.
     *
     * @author Kamil Dzierżanowski
     * @author Kacper Bloch
     * @version 1.0
     */
    private class StartListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            server = new Server(textArea);
            server.start();

            controlButton.setText("Stop");
            controlButton.removeActionListener(this);
            controlButton.addActionListener(new StopListener());
        }
    }

    /**
     * Słuchacz przycisku zakończenia pracy serwera. Wywołuje
     * metodę zatrzymania serwera, po czym odłącza się od przycisku
     * i przywraca mu słuchacza rozpoczęcia pracy serwera.
     *
     * @author Kamil Dzierżanowski
     * @author Kacper Bloch
     * @version 1.0
     */
    private class StopListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            server.halt();

            controlButton.setText("Start");
            controlButton.removeActionListener(this);
            controlButton.addActionListener(new StartListener());
        }
    }
}
