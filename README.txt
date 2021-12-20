README Supermercato

- Creazione del modello su Workbench: nella cartella SQL è presente il file .mwb del modello ER utilizzato. È necessario fare un forward su una connessione nominata 'market_connection' con utente 'market_user' con password 'ROOT'

- Inizializzazione database: In Utili.java è presente un flag 'dbInitializationEnabled' inizializzato a false. Settarlo a true per caricare il database al primo lancio del programma, e risettarlo a false successivamente per evitare di riempire il database da capo ogni volta che si lancia il codice

I dati sono contenuti in file .txt in /src/it/unicas/supermarket/dataloader

- Esecuzione: La configurazione di lancio parte dal metodo start() della classe Main