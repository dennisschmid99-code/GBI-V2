import java.util.LinkedList;

/**
 * Die abstrakte Basisklasse Roboter.
 * UPDATE: Implementiert "Backpressure" (Gegendruck). 
 * Die Warteschlange hat nun eine maximale Kapazität. Wenn das Band voll ist,
 * muss der Anlieferer (Manager) warten. Das synchronisiert den Lagerabbau mit dem Produktionstempo.
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public abstract class Roboter extends Thread {

    private LinkedList<Produkt> warteschlange;
    private String name;
    protected int produktionsZeit;
    
    // NEU: Maximale Kapazität des Fließbands (Puffer). Wert auf 1 gesetzt damit man die logik im GUI besser sieht. logisch wären 5 oder so da kein Fliessband nur etwas aufs mal produziert. 
    private static final int BAND_KAPAZITAET = 1;

    public Roboter(String name) {
        this.name = name;
        this.warteschlange = new LinkedList<Produkt>();
    }

    public void run() {
        while (true) {
            Produkt aktuellesProdukt = null;

            synchronized (warteschlange) {
                // 1. Warten, bis Arbeit da ist (Consumer)
                while (warteschlange.isEmpty()) {
                    try {
                        warteschlange.wait(); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                // 2. Produkt vom Band nehmen
                aktuellesProdukt = warteschlange.removeFirst();
                
                // WICHTIG: Wir haben Platz gemacht! Den Manager aufwecken, falls er wartet.
                warteschlange.notifyAll(); 
            }

            // 3. Arbeiten (außerhalb des Locks, damit währenddessen das Band aufgefüllt werden kann)
            if (aktuellesProdukt != null) {
                produziereProdukt(aktuellesProdukt);
            }
        }
    }

    /**
     * Fügt ein Produkt zur Warteschlange hinzu.
     * BLOCKIERT nun, wenn das Band voll ist (Flow Control).
     */
    public void fuegeProduktHinzu(Produkt produkt) {
        synchronized (warteschlange) {
            // Backpressure-Logik: Warten, solange Band voll ist
            while (warteschlange.size() >= BAND_KAPAZITAET) {
                try {
                    // Optional: Log, damit man sieht, dass das System bremst
                    // System.out.println(this.name + ": Band voll! Produktion wird gedrosselt...");
                    warteschlange.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            
            warteschlange.add(produkt);
            
            // Den Worker-Thread aufwecken (Arbeit ist da)
            warteschlange.notifyAll();
            
            System.out.println(this.name + ": Produkt aufgenommen. Puffer: " + warteschlange.size() + "/" + BAND_KAPAZITAET);
        }
    }

    public abstract void produziereProdukt(Produkt produkt);

    public String gibNamen() {
        return name;
    }

    public void setzeProduktionsZeit(int zeit) {
        this.produktionsZeit = zeit;
    }
}