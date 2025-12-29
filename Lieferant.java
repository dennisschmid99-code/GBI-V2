import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Die Klasse Lieferant simuliert die Anlieferung von Rohstoffen.
 * UPDATE: Flotten-Management! Kann nun mehrere Lieferungen parallel abwickeln.
 *
 * @author GBI Gruppe 17
 * @version 29.12.25
 */
public class Lieferant extends Thread {

    private Lager lager;
    private boolean laeuft;
    
    private final BlockingQueue<BestellAuftrag> auftragsQueue;

    // Thread-Safe Liste aller aktuellen Ankunftszeiten (Timestamps)
    // CopyOnWrite ist ideal für GUI-Iterationen (verhindert ConcurrentModificationException)
    private final CopyOnWriteArrayList<Long> laufendeLieferungen;

    // Standard-Lieferzeit: 48 Sekunden
    private int lieferZeitMs = 48000;
    
    /**
     * Interne Hilfsklasse für einen Auftrag.
     */
    private static class BestellAuftrag {
        final int holz, schrauben, farbe, karton, glas;

        BestellAuftrag(int h, int s, int f, int k, int g) {
            this.holz = h; this.schrauben = s; this.farbe = f; 
            this.karton = k; this.glas = g;
        }
    }

    public Lieferant(Lager lager) {
        this.lager = lager;
        this.laeuft = true;
        this.auftragsQueue = new LinkedBlockingQueue<>();
        this.laufendeLieferungen = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("[Lieferant] Disponent bereit. Warte auf Aufträge...");
        while (laeuft) {
            try {
                // Disponent wartet auf Auftrag
                BestellAuftrag auftrag = auftragsQueue.take();

                // ELEGANT: Wir starten einen eigenen Thread pro Lieferung (LKW)
                // Dadurch blockiert der Lieferant nicht und kann sofort den nächsten Auftrag annehmen.
                startLKW(auftrag);
                                      
            } catch (InterruptedException e) {
                laeuft = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void startLKW(BestellAuftrag auftrag) {
        Thread lkw = new Thread(() -> {
            long ankunftsZeit = System.currentTimeMillis() + lieferZeitMs;
            
            // 1. Lieferung registrieren (für GUI)
            laufendeLieferungen.add(ankunftsZeit);
            System.out.println("[Lieferant] LKW gestartet. ETA: " + lieferZeitMs + "ms");

            try {
                // 2. Fahrt simulieren
                Thread.sleep(lieferZeitMs);
                
                // 3. Abliefern
                lager.wareLiefern(auftrag.holz, auftrag.schrauben, auftrag.farbe, 
                                  auftrag.karton, auftrag.glas);
                                  
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 4. Lieferung aus Liste entfernen (LKW ist zurück/fertig)
                laufendeLieferungen.remove(ankunftsZeit);
            }
        });
        
        // Wichtig: Daemon-Flag, damit LKWs stoppen, wenn das Hauptprogramm beendet wird
        lkw.setDaemon(true); 
        lkw.start();
    }

    public boolean wareBestellen(int holz, int schrauben, int farbe, int karton, int glas) {
        auftragsQueue.offer(new BestellAuftrag(holz, schrauben, farbe, karton, glas));
        System.out.println("[Lieferant] Bestellung disponiert.");
        return true; 
    }
    
    /**
     * Gibt eine Liste der verbleibenden Zeiten (in Sekunden) für alle aktiven LKWs zurück.
     * Sortiert: Die nächste Lieferung zuerst.
     */
    public List<Long> gibVerbleibendeSekundenListe() {
        List<Long> sekundenListe = new java.util.ArrayList<>();
        long jetzt = System.currentTimeMillis();
        
        for (Long ankunft : laufendeLieferungen) {
            long rest = Math.max(0, ankunft - jetzt);
            sekundenListe.add(rest / 1000 + 1);
        }
        
        Collections.sort(sekundenListe);
        return sekundenListe;
    }
    
    public void setzeLieferzeitFuerTests(int ms) {
        this.lieferZeitMs = ms;
    }
    
    public void stoppen() {
        laeuft = false;
        this.interrupt();
    }
}