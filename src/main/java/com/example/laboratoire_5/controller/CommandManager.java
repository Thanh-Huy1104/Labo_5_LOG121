package com.example.laboratoire_5.controller;

/**
 * Cette classe permet d'implémenter le patron Commande. Cette classe est le "invoker" dans l'architechture du patron
 * commande. Ceci veut dire que cette classe est responsable d'appeler l'exécution d'une certaine commande lorsque
 * l'utilisateur fait une certaine action. Nous ne voulons qu'une seule instance de CommandManager, donc cette classe
 * est implémentée avec le patron Singleton.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class CommandManager {
    private static CommandManager instance = new CommandManager(); // instance statique unique
    private Command command; // commande que l'on veut exécuter

    public void setCommand(Command command) {
        this.command = command;
    }

    // CONSTRUCTOR privé, car patron Singleton
    private CommandManager() {}

    // Ceci est la seule méthode permettant d'obtenir l'instance unique de CommandManager
    public static CommandManager getInstance() {
        return instance;
    }

    /**
     * Cette méthode gère les exécutions de différentes commandes. Lorsque le controlleur veut effectuer une certaine
     * commande, c'est part cette méthode que la séquence va passer. Cette méthode appelle l'exécution de la commande
     * actuelle.
     *
     * @param index l'index permet de différencier laquelle des perspectives est affectée par une commande
     */
    public void executeCommand(int index) {
        command.execute(index);
    }
}
