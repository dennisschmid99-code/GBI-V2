import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Die Klasse Lieferant simuliert die Anlieferung von Rohstoffen.
 * Er arbeitet als eigener Thread (asynchron) und nutzt eine Warteschlange,
 * damit Bestellungen auch während einer Auslieferung sicher angenommen werden.
 *
 * Zeit-Skalierung: 1 Stunde = 1 Sekunde.
 * Lieferzeit: 2 Tage = 48 Stunden = 48 Sekunden (48.000 ms).
 *
 * @author GBI Gruppe 17
 * @version 16.12.2025
 */
public class Lieferant extends Thread {

    private Lager lager;
    private boolean laeuft;
    
    // Thread-Safe Queue: Speichert Aufträge sicher, bis der Lieferant Zeit hat.
    private final BlockingQueue<BestellAuftrag> auftragsQueue;

    // Standard-Lieferzeit: 48.000 ms (48 Sekunden)
    // Kann für Tests angepasst werden.
    private int lieferZeitMs = 48000;

    /**
     * Interne Hilfsklasse (Immutable Record) für einen Auftrag.
     */
    private static class BestellAuftrag {
        final int holz, schrauben, farbe, karton, glas;

        BestellAuftrag(int h, int s, int f, int k, int g) {
            this.holz = h; this.schrauben = s; this.farbe = f; 
            this.karton = k; this.glas = g;
        }
    }

    /**
     * Konstruktor der Klasse Lieferant.
     * @param lager Das Lager, an das die Waren geliefert werden.
     */
    public Lieferant(Lager lager) {
        this.lager = lager;
        this.laeuft = true;
        // LinkedBlockingQueue hat theoretisch unbegrenzte Kapazität -> Blockiert den Aufrufer nie
        this.auftragsQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Die Thread-Logik: Wartet auf Aufträge und arbeitet sie nacheinander ab.
     */
    @Override
    public void run() {
        System.out.println("[Lieferant] Thread gestartet. Warte auf Aufträge...");
        while (laeuft) {
            try {
                // take() blockiert ressourcenschonend, bis ein Auftrag in der Queue liegt.
                BestellAuftrag auftrag = auftragsQueue.take();

                System.out.println("[Lieferant] Auftrag entnommen. Fahre los (Dauer: " + lieferZeitMs + "ms)...");
                
                // Simulation der Lieferzeit (Schlafen)
                Thread.sleep(lieferZeitMs);
                
                // Nach dem Aufwachen: Ware im Lager abliefern
                lager.wareLiefern(auftrag.holz, auftrag.schrauben, auftrag.farbe, 
                                  auftrag.karton, auftrag.glas);
                                      
            } catch (InterruptedException e) {
                System.out.println("[Lieferant] Unterbrochen. Beende Schicht.");
                laeuft = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Nimmt eine Bestellung entgegen und reiht sie in die Warteschlange ein.
     * Diese Methode ist thread-safe und kehrt sofort zurück (non-blocking).
     *
     * @return true (Bestellung immer angenommen)
     */
    public boolean wareBestellen(int holz, int schrauben, int farbe, int karton, int glas) {
        auftragsQueue.offer(new BestellAuftrag(holz, schrauben, farbe, karton, glas));
        System.out.println("[Lieferant] Bestellung in Warteschlange eingereiht.");
        return true; 
    }
    
    /**
     * Ermöglicht das Anpassen der Lieferzeit (z.B. für Unit-Tests).
     * @param ms Zeit in Millisekunden
     */
    public void setzeLieferzeitFuerTests(int ms) {
        this.lieferZeitMs = ms;
    }
    
    /**
     * Stoppt den Thread sauber.
     */
    public void stoppen() {
        laeuft = false;
        this.interrupt();
    }
}