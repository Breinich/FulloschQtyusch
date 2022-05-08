package model;

import view.Observer;
import java.util.ArrayList;

/**
 * Egy megfigyelt oszt�ly, melynek v�ltoz�saira fel lehet iratkozniuk Observereknek
 */
public class Subject {
    /**
     * Observerek - megfigyel�k list�ja
     */
    ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Egy megfigyel� regisztr�l�sa
     * @param observer a hozz�adand� megfigyel�
     */
    public void attach(Observer observer){
        if (!observers.contains(observer)){
            observers.add(observer);
        }
    };

    /**
     * Egy megfigyel� lev�laszt�sa
     * @param observer az elt�vol�tand� megfigyel�
     */
    public void detach(Observer observer){
        observers.remove(observer);
    };

    /**
     * Az �sszes regisztr�lt megfigyel� �rtes�t�se
     */
    public void notifyAllObservers(){
        for (Observer o : observers) {
            o.update();
        }
    }
}
