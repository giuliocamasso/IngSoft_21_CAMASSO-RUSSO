package it.unicas.supermarket.controller;

import it.unicas.supermarket.model.Articoli;

/**
 * Il listener che consente di aggiornare i dettagli di un articolo sul pannello laterale sinistro,
 * cliccando sul grid item corrispondente
 */
public interface ArticleSelectionListener {
    void onClickListener(Articoli articolo);
}
