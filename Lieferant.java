/**
 * Diese Klasse wird verwendet, um die Lieferzeiten des Lieferanten und die
 * Bestellung über diesen zu simulieren.
 * * Die Klasse arbeitet nun als Thread, um die Lieferzeit asynchron zu simulieren.
 * 1 Stunde in der Simulation entspricht 1 Sekunde in Echtzeit.
 * Eine Lieferung dauert 2 Tage (48 Stunden = 48 Sekunden).
 *
 * @author GBI Gruppe 17 (bearbeitet für Aufgabe 3)
 * @version 21.12.2025
 */
public class Lieferant extends Thread {

    // Referenz auf das Lager, damit der Lieferant die Ware dort abliefern kann
    private Lager lager;

    // Variablen zum Speichern der aktuell laufenden Bestellung
    private int holzEinheiten;
    private int schrauben;
    private int farbEinheiten;
    private int kartonEinheiten;
    private int glasEinheiten;

    // Status, ob der Lieferant gerade eine Bestellung bearbeitet
    private boolean istBeschaeftigt;

    // Konstante für die Lieferzeit: 2 Tage = 48 Stunden. 1 Stunde = 1 Sekunde.
    // 48 Sekunden * 1000 = 48000 Millisekunden.
    private static final int LIEFERZEIT = 48000; 

    /**
     * Konstruktor der Klasse Lieferant.
     * * @param lager Das Lager, an das die Waren geliefert werden sollen.
     */
    public Lieferant(Lager lager) {
        this.lager = lager;
        this.istBeschaeftigt = false;
        // Initialisierung der Bestellmengen auf 0
        this.holzEinheiten = 0;
        this.schrauben = 0;
        this.farbEinheiten = 0;
        this.kartonEinheiten = 0;
        this.glasEinheiten = 0;
    }

    /**
     * Die Run-Methode des Threads. 
     * Sie prüft in einer Endlosschleife, ob eine Bestellung vorliegt.
     * Wenn ja, wird gewartet (simulierte Lieferzeit) und dann geliefert.
     */
    public void run() {
        while (true) {
            // Wir prüfen, ob eine Bestellung bearbeitet werden muss
            if (istBeschaeftigt) {
                try {
                    // Ausgabe zur Information im Terminal
                    System.out.println("Lieferant: Bestellung erhalten. Lieferung in 48 Stunden (48 Sek)...");
                    
                    // Der Thread schläft für die definierte Lieferzeit
                    Thread.sleep(LIEFERZEIT);
                    
                    // Nach dem Aufwachen: Ware an das Lager übergeben
                    // Hinweis: Die Methode 'wareLiefern' muss in der Klasse Lager existieren
                    // und die Bestände dort erhöhen.
                    lager.wareLiefern(holzEinheiten, schrauben, farbEinheiten, kartonEinheiten, glasEinheiten);
                    
                    System.out.println("Lieferant: Ware wurde im Lager abgeliefert.");

                    // Bestellung ist abgeschlossen, Lieferant ist wieder frei
                    istBeschaeftigt = false;
                    resetBestellung();

                } catch (InterruptedException e) {
                    // Falls der Thread während des Schlafens unterbrochen wird
                    System.out.println("Lieferant: Lieferung wurde unterbrochen!");
                    e.printStackTrace();
                }
            } else {
                // Wenn nichts zu tun ist, kurz warten, um CPU-Last zu sparen
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Mit dieser Methode wird eine Bestellung beim Lieferanten aufgegeben.
     * Die Werte werden gespeichert und der Status auf 'beschäftigt' gesetzt,
     * damit die run()-Methode die Bearbeitung startet.
     * * @param holzEinheiten   Anzahl bestellter Holzeinheiten
     * @param schrauben       Anzahl bestellter Schrauben
     * @param farbEinheiten   Anzahl bestellter Farbeinheiten
     * @param kartonEinheiten Anzahl bestellter Kartoneinheiten
     * @param glasEinheiten   Anzahl bestellter Glaseinheiten
     * @return true, wenn die Bestellung angenommen wurde, false wenn der Lieferant noch beschäftigt ist.
     */
    public synchronized boolean wareBestellen(int holzEinheiten, int schrauben, int farbEinheiten, 
                                              int kartonEinheiten, int glasEinheiten) {
        
        // Prüfen, ob der Lieferant gerade schon eine Lieferung ausfährt
        if (istBeschaeftigt) {
            System.out.println("Lieferant: Ich bearbeite bereits eine Bestellung. Bitte warten.");
            return false; 
        }

        // Übernahme der Bestellmengen in die Instanzvariablen
        this.holzEinheiten = holzEinheiten;
        this.schrauben = schrauben;
        this.farbEinheiten = farbEinheiten;
        this.kartonEinheiten = kartonEinheiten;
        this.glasEinheiten = glasEinheiten;

        // Signalisiert der run()-Methode, dass Arbeit da ist
        this.istBeschaeftigt = true;
        
        return true; 
    }

    /**
     * Hilfsmethode, um die internen Bestellvariablen zurückzusetzen.
     */
    private void resetBestellung() {
        this.holzEinheiten = 0;
        this.schrauben = 0;
        this.farbEinheiten = 0;
        this.kartonEinheiten = 0;
        this.glasEinheiten = 0;
    }
}