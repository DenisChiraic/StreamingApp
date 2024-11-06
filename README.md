Descrierea Proiectului
Această aplicație de streaming este proiectată să ofere utilizatorilor funcționalități de vizionare a filmelor și serialelor, cu opțiuni de organizare a conținutului în liste personalizate (playlisturi și liste de top). Utilizatorii pot avea conturi gratuite sau premium, fiecare cu caracteristici specifice.

Clase și Funcții
1.	User (Utilizator):
o	authentification(): Autentifică utilizatorul în aplicație.
o	logOut(): Deconectează utilizatorul.
2.	Account (Cont):
o	setType(): Setează tipul contului (Free sau Premium).
3.	PremiumAccount și FreeAccount:
o	Gestionarea limitelor de dispozitive pentru fiecare tip de cont.
4.	WatchList (Lista de Vizionare):
o	addMovieSerial(): Adaugă un film/serial în listă.
o	removeMovieSerial(): Elimină un film/serial din listă.
5.	Movie (Film) și Serial (Serial):
o	play(): Redă filmul sau serialul selectat.
6.	Episode (Episod):
o	nextEpisode(): Redă episodul următor al serialului.
7.	TopList (Lista de Topuri):
o	topMovies() și topSerials(): Afișează cele mai vizionate filme și seriale.
8.	PlayList (Playlist):
o	addMovieSerial() și removeMovieSerial(): Adaugă sau elimină filme și seriale din playlistul personalizat al utilizatorului.

Funcționalități
1.	Autentificare și gestionare utilizatori: Utilizatorii pot să se autentifice și să se deconecteze.
2.	Gestionarea conturilor: Utilizatorii pot avea conturi gratuite sau premium, fiecare cu propriile limitări.
3.	Gestionarea listei de vizionare: Utilizatorii pot adăuga sau elimina filme și seriale din lista lor de vizionare.
4.	Redare conținut: Filmele și serialele pot fi redate utilizând metodele play().
5.	Navigare între episoade: Utilizatorii pot accesa episodul următor.
6.	Topuri de conținut: Utilizatorii pot vedea listele cu cele mai populare filme și seriale.
7.	Playlisturi personalizate: Utilizatorii își pot crea și organiza propriile playlisturi.
 
