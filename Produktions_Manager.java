import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Der Produktions_Manager steuert den Fertigungsprozess. 
 * UPDATE: Implementiert "Item-by-Item"-Verarbeitung für grosse Bestellungen.
 *
 * @author GBI Gruppe 17
 * @version 29.12.25
 */
public class Produktions_Manager extends Thread {

    private Lager meinLager;
    private Holzbearbeitungs_Roboter holzRoboter;

    private LinkedList<Bestellung> zuVerarbeitendeBestellungen;
    private LinkedList<Bestellung> bestellungenInProduktion;

    private boolean laeuft;

    public Produktions_Manager(Lager lager) {
        this.meinLager = lager;
        this.zuVerarbeitendeBestellungen = new LinkedList<Bestellung>();
        this.bestellungenInProduktion = new LinkedList<Bestellung>();
        this.laeuft = true;

        this.holzRoboter = new Holzbearbeitungs_Roboter();
        this.holzRoboter.start();
        
        System.out.println("Produktions-Manager: Gestartet.");
    }

    public void run() {
        while (laeuft) {
            try {
                verarbeiteNeueBestellungen();
                pruefeFertigeBestellungen();
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void fuegeZuVerarbeitendeBestellungenHinzu(Bestellung bestellung) {
        zuVerarbeitendeBestellungen.add(bestellung);
        System.out.println("Manager: Bestellung " + bestellung.gibBestellungsNr() + " eingereiht.");
    }

    /**
     * Die optimierte Verarbeitungsschleife.
     * Nimmt Bestellungen entgegen und schleust Produkte einzeln durch (Flow-Prinzip).
     */
    private void verarbeiteNeueBestellungen() {
        while (true) {
            Bestellung aktuelleBestellung = null;
            
            // 1. Bestellung holen (kurzer Lock)
            synchronized (this) {
                if (zuVerarbeitendeBestellungen.isEmpty()) {
                    break; 
                }
                aktuelleBestellung = zuVerarbeitendeBestellungen.removeFirst();
            }
            
            // 2. Verarbeitung starten
            if (aktuelleBestellung != null) {
                // Wir fügen die Bestellung SOFORT zur Überwachungsliste hinzu.
                // Dadurch sieht man in der GUI sofort "IN ARBEIT" (sobald das erste Teil läuft).
                bestellungenInProduktion.add(aktuelleBestellung);
                
                System.out.println("Manager: Starte schrittweise Produktion für Best. " + aktuelleBestellung.gibBestellungsNr());

                // STREAMING ANSATZ: Wir iterieren über jedes Produkt EINZELN.
                // Wir holen Material -> Übergeben an Roboter -> Nächstes Produkt.
                ArrayList<Produkt> produkte = aktuelleBestellung.liefereBestellteProdukte();

                for (Produkt produkt : produkte) {
                    // A: Material holen (Blockiert hier kurz, falls Lager leer, aber nur für EINE Tür)
                    meinLager.materialEntnehmen(produkt);
                    
                    // B: Roboter zuweisen
                    produkt.fuegeRoboterHinzu(holzRoboter);
                    
                    // C: Loslegen
                    produkt.naechsteProduktionsStation();
                    
                    // Kleines Delay, damit wir sehen, wie die Balken in der GUI runtergehen (Eye-Candy)
                    try { Thread.sleep(50); } catch (Exception e) {}
                }
            }
        }
    }

    private void pruefeFertigeBestellungen() {
        // Nutzung eines Iterators, um sicher während der Iteration löschen zu können
        Iterator<Bestellung> iterator = bestellungenInProduktion.iterator();
        
        while (iterator.hasNext()) {
            Bestellung bestellung = iterator.next();
            
            boolean allesFertig = true;
            for (Produkt p : bestellung.liefereBestellteProdukte()) {
                if (!p.istFertig()) {
                    allesFertig = false;
                    break;
                }
            }

            if (allesFertig) {
                bestellung.setzeAlleProdukteProduziert();
                System.out.println(">>> Manager: Bestellung " + bestellung.gibBestellungsNr() + " KOMPLETT FERTIG. <<<");
                iterator.remove();
            }
        }
    }
}