import java.util.LinkedList;

/**
 * Die abstrakte Basisklasse Roboter verwaltet eine Warteschlange von Produkten
 * und arbeitet diese in einem eigenen Thread ab.
 * * Sie dient als Vorlage für spezifische Roboter (z.B. Holzbearbeitungs_Roboter).
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public abstract class Roboter extends Thread {

    //Die Warteschlange der Produkte, die dieser Roboter bearbeiten muss
    private LinkedList<Produkt> warteschlange;
    
    // Name des Roboters (z.B. "Holzbearbeitungsroboter")
    private String name;
    
    // Produktionszeit kann hier gespeichert oder im konkreten Roboter verwaltet werden
    protected int produktionsZeit;

    /**
     * Konstruktor für Objekte der Klasse Roboter
     * @param name Der Name des Roboters
     */
    public Roboter(String name) {
        this.name = name;
        // Initialisierung der Warteschlange als LinkedList
        this.warteschlange = new LinkedList<Produkt>();
    }

    /**
     * Die Run-Methode des Threads.
     * Enthält eine Endlosschleife, die prüft, ob Produkte in der Warteschlange sind.
     */
    public void run() {
        while (true) {
            Produkt aktuellesProdukt = null;

            // Zugriff auf die Warteschlange synchronisieren, um Thread-Probleme zu vermeiden
            synchronized (warteschlange) {
                // Solange die Schlange leer ist, wartet der Roboter
                while (warteschlange.isEmpty()) {
                    try {
                        warteschlange.wait(); // Thread legt sich schlafen und wartet auf Benachrichtigung
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Wenn wir hier sind, ist mindestens ein Produkt da.
                // Wir holen das erste Produkt aus der Schlange (FIFO).
                aktuellesProdukt = warteschlange.removeFirst();
            }

            // Die Produktion findet außerhalb des synchronized-Blocks statt,
            // damit neue Produkte hinzugefügt werden können, während der Roboter arbeitet.
            if (aktuellesProdukt != null) {
                // Diese Methode wird von der konkreten Unterklasse implementiert
                produziereProdukt(aktuellesProdukt);
            }
        }
    }

    /**
     * Fügt ein Produkt zur Warteschlange des Roboters hinzu.
     * Wird vom Produktions_Manager aufgerufen.
     * * @param produkt Das zu bearbeitende Produkt
     */
    public void fuegeProduktHinzu(Produkt produkt) {
        synchronized (warteschlange) {
            warteschlange.add(produkt);
            // Benachrichtigt den wartenden run()-Thread, dass Arbeit da ist
            warteschlange.notifyAll();
            System.out.println(this.name + ": Produkt in Warteschlange aufgenommen.");
        }
    }

    /**
     * Abstrakte Methode, die die spezifische Produktion simuliert.
     * Muss von den Unterklassen (z.B. Holzbearbeitungs_Roboter) implementiert werden.
     * * @param produkt Das zu produzierende Produkt
     */
    public abstract void produziereProdukt(Produkt produkt);

    /**
     * Gibt den Namen des Roboters zurück.
     * @return Der Name als String
     */
    public String gibNamen() {
        return name;
    }

    /**
     * Setzt die generelle Produktionszeit (falls benötigt).
     * @param zeit Zeit in Millisekunden
     */
    public void setzeProduktionsZeit(int zeit) {
        this.produktionsZeit = zeit;
    }
}