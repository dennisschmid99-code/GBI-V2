/**
 * Die Klasse Main demonstriert das Verhalten der Fabrik
 * für Aufgabe 2 (Materialprüfung, Beschaffungszeit, Lieferzeit).
 *
 * @author Gruppe 17
 * @version 04.12.2025
 */
public class Main {

    public static void main(String[] args) {

        Fabrik testFabrik = new Fabrik();

        System.out.println("Willkommen bei der AEKI Fabrik – Aufgabe 2 Simulation.\n");

        // Best 1: Kleine Bestellung -> Lager reicht aus -> Beschaffung = 0
        System.out.println("=== Bestellung 1: 10 Standardtüren, 5 Premiumtüren ===");
        testFabrik.bestellungAufgeben(10, 5);
        testFabrik.bestellungenAusgeben();
        System.out.println();

        // Best 2: Mittlere Bestellung -> Lager könnte noch reichen
        System.out.println("=== Bestellung 2: 30 Standardtüren, 20 Premiumtüren ===");
        testFabrik.bestellungAufgeben(30, 20);
        testFabrik.bestellungenAusgeben();
        System.out.println();

        // Best 3: Grosse Bestellung -> Lager reicht sicher NICHT -> Beschaffung = 2
        System.out.println("=== Bestellung 3: 200 Standardtüren, 100 Premiumtüren ===");
        testFabrik.bestellungAufgeben(200, 100);
        testFabrik.bestellungenAusgeben();
        System.out.println();

        // Best 4: Noch eine grosse Bestellung -> Lager wurde aufgefüllt -> kann wieder reichen oder nicht
        System.out.println("=== Bestellung 4: 50 Standardtüren, 10 Premiumtüren ===");
        testFabrik.bestellungAufgeben(50, 10);
        testFabrik.bestellungenAusgeben();

        System.out.println("\nSimulation abgeschlossen.");
    }
}
