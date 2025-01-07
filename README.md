Allgemeine Beschreibung
Dieses Projekt ist eine Media-Streaming-Plattform, die die Verwaltung von Inhalten wie Filme und Serien ermöglicht. Benutzer können Filme und Serien hinzufügen, löschen und abrufen, Episoden ansehen sowie Top-Listen basierend auf Bewertungen generieren. Das Projekt unterstützt zwei Arten der Datenspeicherung: temporäre Speicherung im Speicher und persistente Speicherung in einer Datenbank.

Hauptfunktionen
Verwaltung von Filmen:

Hinzufügen von Filmen.
Löschen eines Films anhand des Titels.
Abrufen eines Films anhand des Titels.
Anzeigen aller verfügbaren Filme.
Generierung einer Top-10-Liste von Filmen basierend auf Bewertungen.
Verwaltung von Serien:

Hinzufügen von Serien.
Löschen einer Serie anhand des Titels.
Abrufen einer Serie anhand des Titels.
Anzeigen aller verfügbaren Serien.
Generierung einer Top-10-Liste von Serien basierend auf Bewertungen.
Abrufen der nächsten Episode einer Serie.
Datenspeicherung:

Option "In-Memory" (für schnelle Tests und Entwicklung).
Option "Datenbank" (für persistente Speicherung).
Projektstruktur
Modelle (Package: org.stream.model):

Hauptklassen:

Movie (Film mit Attributen wie Titel, Genre, Bewertung usw.).
Serial (Serie mit Episoden und spezifischen Details).
Episode (Episode mit Titel, Beschreibung und Dauer).
Ausnahmen:

EntityNotFoundException (wird ausgelöst, wenn eine Entität nicht gefunden wird).
ValidationException (wird bei fehlgeschlagenen Validierungen ausgelöst).
Repository (Package: org.stream.repository):

InMemoryRepo: Implementierung für die temporäre Speicherung im Speicher.
DatabaseRepo: Implementierung für die Speicherung in einer Datenbank.
Service (Package: org.stream.service):

ContentService: Hauptklasse, die die Geschäftslogik für Filme und Serien verwaltet.

![image](https://github.com/user-attachments/assets/8496047c-82b8-4b87-8598-e1a7b0e8d1e8)
