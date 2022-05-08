package control;

import model.Game;

public class Main {

    /**
     * Alkalmazás belépési pontja.
     * @param args parancsori argumentumok.
     */
    public static void main(String[] args){
        try {
            Controller controller = new Controller(Game.Create());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
