package com.example.laboratoire_5.observer;

/**
 * Cette classe est une interface servant à l'implémentation du patron observer. Ceci est l'interface étant
 * implémentée par les observateurs, donc par notre controller. Tous les observateurs doivent posséder une méthode
 * permettant de se mettre à jour lorsque le sujet observé change.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public interface Observer {
    public void update(Subject subject); // Méthode de mise à jour lorsque le sujet change
}
