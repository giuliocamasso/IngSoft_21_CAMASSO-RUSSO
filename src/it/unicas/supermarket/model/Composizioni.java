package it.unicas.supermarket.model;

/**
 * La classe modello per la tabella Composizioni
 */
public class Composizioni {

    private int idArticolo;
    private int idOrdine;
    private float prezzo;
    private int quantita;

    /**
     * Costruttore
     * @param idArticolo autodescrittivo
     * @param idOrdine autodescrittivo
     * @param prezzo autodescrittivo
     * @param quantita autodescrittivo
     */
    public Composizioni(int idArticolo, int idOrdine, float prezzo, int quantita) {
        this.idArticolo = idArticolo;
        this.idOrdine = idOrdine;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    // getter e setter
    public int getIdArticolo()                              { return idArticolo;            }
    public void setIdArticolo(int idArticolo)               { this.idArticolo = idArticolo; }

    public int getIdOrdine()                                { return idOrdine;              }
    public void setIdOrdine(int idOrdine)                   { this.idOrdine = idOrdine;     }

    public float getPrezzo()                                { return prezzo;                }
    public void setPrezzo(float prezzo)                     { this.prezzo = prezzo;         }

    public int getQuantita()                                { return quantita;              }
    public void setQuantita(int quantita)                   { this.quantita = quantita;     }
}
