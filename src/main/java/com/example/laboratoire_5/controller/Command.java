package com.example.laboratoire_5.controller;

/**
 * Cette classe est une interface servant à l'implémentation du patron commande. Chacune des commandes concrètes doit
 * implémenter cette interface. Ceci veut dire que chaque classe étant responsable d'une action doit posséder une
 * méthode gérant l'exécution de cette action à travers le model.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public interface Command {
    public void execute(int index); // méthode d'exécution
}
