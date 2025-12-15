Dies ist die README-Datei des Projekts "Smart Door Manufacturing".

PROJEKTBEZEICHNUNG: Smart Door Manufacturing (Aeki Türenfabrik)

PROJEKTZWECK:
Simulation eines Produktionsmanagementsystems für den Türenhersteller "Aeki".
Das System realisiert ein Make-to-Order (MTO) Modell für Standard- und
Premiumtüren. Es demonstriert objektorientierte Konzepte wie Vererbung,
Kapselung und Fehlerbehandlung.

VERSION oder DATUM: Version 4.0 (07.12.2025)

WIE IST DAS PROJEKT ZU STARTEN:
Die Simulation kann am einfachsten über die Hauptklasse gestartet werden:

Rechtsklick auf die Klasse 'Fabrik'.

Die Methode 'void main(String[] args)' auswählen.

'Ok' klicken (keine Argumente notwendig).
-> Die Simulation führt einen kompletten Durchlauf (Bestellung, Produktion, Fehler-Demo) in der Konsole aus.

AUTOR(EN): Gruppe 17

BENUTZERHINWEISE & DESIGN-ENTSCHEIDUNGEN:

Architektur & Vererbung:

Die Basisklasse 'Produkt' verwaltet den allgemeinen Zustand (z.B. 'bestellt').

Die Subklassen 'Standardtuer' und 'Premiumtuer' erben davon.

Materialanforderungen sind als Konstanten (static final) in den Subklassen definiert, da diese für alle Instanzen gleich sind.

Fehlerbehandlung (Robustheit):

Das System fängt ungültige Eingaben (z.B. negative Bestellmengen oder Zeiten) aktiv ab.

Im Konstruktor der Klasse 'Bestellung' werden Exceptions (IllegalArgumentException) geworfen, um invalide Objekte zu verhindern.

Die Klasse 'Fabrik' fängt diese Fehler in der Simulation ab, ohne abzustürzen.

Tests (Qualitätssicherung):

Das Projekt enthält Unit-Tests für alle Klassen.

Es werden nicht nur Standardfälle ("Happy Path"), sondern auch Grenzwerte und Fehlerfälle getestet.

Testklassen: FabrikTest, BestellungTest, ProduktTest, PremiumtuerTest, StandardtuerTest.