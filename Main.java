
/**
 * Die Klasse Main ist die Hauptklasse des Programms.
 * Hier befindet sich die main Methode zum Starten des Programms.
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class Main {

    public static void main(String[] args) {

        Fabrik testFabrik = new Fabrik();

        System.out.println("Willkommen bei der AEKI Fabrik.");

        // Best 1: 2, 2
        testFabrik.bestellungAufgeben(2, 2);
        testFabrik.bestellungenAusgeben();

        // Best 2: 5, 0
        testFabrik.bestellungAufgeben(5, 0);
        testFabrik.bestellungenAusgeben();

        // Best 3: 0, 6
        testFabrik.bestellungAufgeben(0, 6);
        testFabrik.bestellungenAusgeben();

        // Best 4: -5, 6
        testFabrik.bestellungAufgeben(-5, 6);
        testFabrik.bestellungenAusgeben();

        // Best 5: 1000000, 6
        testFabrik.bestellungAufgeben(1000000, 6);
        testFabrik.bestellungenAusgeben();

        // Best 6: 0, 0
        testFabrik.bestellungAufgeben(0, 0);
        testFabrik.bestellungenAusgeben();

        // Best 7: 0, 21
        testFabrik.bestellungAufgeben(0, 21);
        testFabrik.bestellungenAusgeben();

    }

}
