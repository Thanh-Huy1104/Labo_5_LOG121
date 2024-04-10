package com.example.laboratoire_5.observer;

/**
 * Cette classe est une interface servant à l'implémentation du patron observer. Ceci est l'interface étant
 * implémentée par le Sujet, donc par notre model. Tous les sujets doivent posséder des méthodes permettant d'ajouter
 * et de retirer des observateurs. De plus, tous les sujets doivent posséder la méthode notifyObservers, car c'est
 * avec cette méthode que le patron observeur fonctionne. Le sujet peut alerter tous ses observateurs d'un changement.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public interface Subject {
    public void attach(Observer observer); // Méthode d'ajout d'observateur
    public void detach(Observer observer); // Méthode de retrait d'observateur
    public void notifyObservers(); // Méthode alertant les observateurs
}
