README Supermercato


- Creazione del modello su Workbench:
    Nella cartella SQL è presente il file .mwb del modello ER utilizzato.
    È necessario fare un forward e creare una connessione nominata 'market_connection' con utente 'market_user' con password 'ROOT'


- Inizializzazione database:
    In Util.java è presente un flag 'dbInitializationEnabled' inizializzato a false.
    Settarlo a true per caricare il database al primo lancio del programma, e rimetterlo a false successivamente per evitare di svuotate e riempire il database a ogni lancio.

    I dati del db sono contenuti nei file .txt in /src/it/unicas/supermarket/dataloader


- Note di configurazione:
    Potrebbe essere necessario modificare le VM options nella configurazione di lancio come segue

    --module-path
    ${PATH_TO_FX}
    --add-modules
    javafx.controls,javafx.fxml

    dove PATH_TO_FX e' il path di JavaFX sul proprio sistema.

    NB. dal menu' "File -> Settings... -> Path Variables" e' possibile inserire le proprie variabili path,
        cosi' da mantenere la notazione ${PATH_TO_FX} nelle VM Options



Studenti:       Giulio Camasso, Giulio Russo
Corso:          Software Engineering 2021-2022
Docente:        M.Molinara