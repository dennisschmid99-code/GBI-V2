import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Der Produktions_Manager steuert den Fertigungsprozess. 
 * Gemäß Aufgabe 3 steuert er exemplarisch den Holzbearbeitungs_Roboter.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Produktions_Manager extends Thread {

    private Lager meinLager;
    private Holzbearbeitungs_Roboter holzRoboter; // Nur dieser ist gefordert

    private LinkedList<Bestellung> zuVerarbeitendeBestellungen;
    private LinkedList<Bestellung> bestellungenInProduktion;

    private boolean laeuft;

    /**
     * Konstruktor: Initialisiert Listen und startet den Holzroboter.
     */
    public Produktions_Manager(Lager lager) {
        this.meinLager = lager;
        this.zuVerarbeitendeBestellungen = new LinkedList<Bestellung>();
        this.bestellungenInProduktion = new LinkedList<Bestellung>();
        this.laeuft = true;

        // Nur den Holzroboter instanziieren und starten
        this.holzRoboter = new Holzbearbeitungs_Roboter();
        this.holzRoboter.start();
        
        System.out.println("Produktions-Manager: Gestartet (Holzroboter bereit).");
    }

    public void run() {
        while (laeuft) {
            try {
                verarbeiteNeueBestellungen();
                pruefeFertigeBestellungen();
                // Kurze Pause, um CPU zu sparen (100ms reicht für schnelle Reaktion)
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

    private void verarbeiteNeueBestellungen() {
        synchronized (this) {
            while (!zuVerarbeitendeBestellungen.isEmpty()) {
                Bestellung aktuelleBestellung = zuVerarbeitendeBestellungen.removeFirst();
                
                // Material holen
                meinLager.materialEntnehmen(aktuelleBestellung);

                // Produktion starten
                starteProduktion(aktuelleBestellung);
                
                // In Überwachungsliste verschieben
                bestellungenInProduktion.add(aktuelleBestellung);
            }
        }
    }

    private void starteProduktion(Bestellung bestellung) {
        System.out.println("Manager: Übergebe Produkte an Holzroboter...");
        ArrayList<Produkt> produkte = bestellung.liefereBestellteProdukte();

        for (Produkt produkt : produkte) {
            // Zuweisung an den Holzroboter
            produkt.fuegeRoboterHinzu(holzRoboter);
            
            // Starten (da nur 1 Roboter da ist, ist das Produkt danach fertig)
            produkt.naechsteProduktionsStation();
        }
    }

    private void pruefeFertigeBestellungen() {
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
                System.out.println(">>> Manager: Bestellung " + bestellung.gibBestellungsNr() + " FERTIG. <<<");
                iterator.remove();
            }
        }
    }
}