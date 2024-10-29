# Projekt: Vergleich einer relationalen Datenbank mit MongoDB

In diesem Projekt wird eine relationale Datenbank-Implementierung mit einer MongoDB-Variante verglichen. Der Fokus liegt auf Skalierbarkeit, CRUD-Operationen und der Benchmarking-Performance. Das Projekt besteht aus verpflichtenden und optionalen Teilen, wobei maximal 10 von 11,5 möglichen Punkten zur Endwertung beitragen.
Projektübersicht

Ziel ist die Auswahl eines skalierbaren relationalen Projekts und die Implementierung einer MongoDB-Äquivalenz, die für Frontend-Funktionalität optimiert ist. Die Leistung für verschiedene Operationen wird getestet und für beide Implementierungen verglichen. Es gibt zusätzliche, optionale Aufgaben, um Zusatzpunkte zu erwerben.

# Verpflichtender Teil (Maximal 6 Punkte)
## Schritt 1:
Auswahl eines bestehenden rel. Projektes mit skalierbarer seed-Integration Teamarbeit: jeder sucht ein vorhandenes Projekt raus, ich erhalte einen Screenshot vom Datenbankmodell und eine ungefähre Beschreibung des Seedings sowie ein Screenshot des Frontends. Wichtig ist, dass man dieses quasi beliebig skalieren kann. Also zwischen 10 und 100.000 Testfälle für writing-Operationen durchführen kann. Das relationale Projekt sollte zumindest in 2 verschiedene Tabellen persistieren.

## Schritt 2:
Nach Auswahl des relationalen Referenzprojektes Implementierung mit einer MongoDB-Schnittstelle der Wahl in der Variante "Optimiert auf Frontend" Musterprojekte für C# und Java sind zu finden unter https://github.com/schletz/Dbi3Sem/tree/master/13_NoSQL/Uebungen/SalesDb/SalesDbGenerator * 1 Punkt für Modell * 1 Punkt für lauffähige Implementierung

## Schritt 3:
Testen der CRUD-Operations sowohl auf json-DB als auch auf relationale DB mit Laufzeiten. Also zuerst * Writings in verschiedenen Skalierungen (100 - 1000 - 100000) , * 4 Finds (ohne Filter, mit Filter, mit Filter und Projektion, mit Filter, Projektion und Sortierung) * 1 Update * 1 Delete alles inkl. Vergleich auf die Relationale DB, dh das Programm kann sowohl relational als auch json-based speichern und Tests hintereinander ausführen. 2 Punkte für gesamten Schritt 3

## Abgabe:
- Pünktliche Abgabe (0.5)
- Fragen beim Prüfungsgespräch (1.5 Punkte)
- Achtung: pro Woche verspätete Abgabe 0.25 Abzug (das geht auch ins Minus)

# Erweiterte Kompetenzen (Zusatzpunkte)

## Weitere Punkte können erworben werden für folgende zusätzliche Aufgaben:
- Änderung der Abfrage, sodass eine Aggregation notwendig wird --> Vergleich der Read-Laufzeiten zum selben Query auf der Relationalen. 0.5 Punkte
- veränderte Version des Modells, bei dem mit referencing gearbeitet wird und Vergleich der Laufzeiten 1 Punkte
- Umsetzung auf Atlas-Cloud 0.5
- Verwendung eines json-Schemas und Tests, welche dieses Schema verletzen 0.75
- funktionales Frontend mit Auswahlmöglichkeit der Anzeige (=Filter auf Abfrage) 1.5 Punkte
- Vergleich der Laufzeiten beim Setzen eines Index auf die Mongo-Struktur 1.0 Punkte
- Irgendwas cooles, das ich hier nicht aufzähle 1 Punkt
