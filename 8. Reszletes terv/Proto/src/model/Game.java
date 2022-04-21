package model;

import model.codes.GeneticCode;
import model.map.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy singleton osztály, ami a játék kezeléséért felelős.
 * A játékot indítja, és lezárja.
 * Számontartja a játékosokat, és köreiket.(átadja egyiktől a másiknak az irányítást)
 * Mivel a játkosok köreit kezeli, tehát gyakorlatilag az időegységeket,
 * így az ő feladata az ágensek hátralévő idejét csökkenti is.
 */
public class Game
{
	/**
	 * A pálya mezői
	 */
	private List<Field> fields;
	public static int playerCount = 0;
	/**
	 * A játékban szereplő genetikai kódok
	 */
	private List<GeneticCode> codes;

	public ArrayList<Virologist> getVirologists() {
		return virologists;
	}

	public boolean randOn = true; //by default, parancs kell a kikapcsoláshoz (+jelszó)

	/**
	 * A játékban szereplő virpológusok
	 */
	private ArrayList<Virologist> virologists;

	private int currentPlayer = 0;

	private Game(){}; //singleton miatt
	private static Game instance = null;
	public static Game Create(){
		if (instance == null){
			instance = new Game();
		}
		return instance;
	}

	/**
	 * Elindít egy új játékot, inicializálja a tagváltozók listáit.
	 */
	public void NewGame() {
		fields = new ArrayList<>();
		codes = new ArrayList<>();
		virologists = new ArrayList<>();
		currentPlayer = 0;
	}

	/**
	 * Átadja az irányítást a sorrendben a következő játékosnak, az aktuálisnak lezárja a körét.
	 * @param codes a megismert genetikai kódok száma
	 */
	public void NextPlayer(int codes){
		// A VIROLÓGUSNÁL KELL MEGKÉRDEZNI, HOGY MEGVAN-E AZ ÖSSZES KÓDJA
		// EZ ALAPJÁN KELL MEGFELELŐEN PARAMÉTEREZNI MAJD A FÜGGVÉNYHÍVÁST
		if(codes == this.codes.size())
			EndGame();
		else {
			for (Virologist v: virologists) {
				v.Update();
			}
			currentPlayer++;
			if (currentPlayer == virologists.size()) currentPlayer = 0;
		}
	}

	public Virologist GetCurrentPlayer(){
		return virologists.get(currentPlayer);
	}

	/**
	 * Befejezi a játékot és kihírdeti a nyertest
	 */
	public void EndGame()
	{
		System.out.println(virologists.get(currentPlayer).getName() + " won the game!");
		System.exit(0);
	}

	/**
	 * Hozzáad egy virológust a játékhoz
	 * @param v a hozzáadandó virológus
	 */
	public void AddVirologist(Virologist v)
	{
		virologists.add(v);
		v.SetGame(this);
		playerCount++;
	}

	/**
	 * Hozzáad egy új genetikai kódot a játékhoz
	 * @param gc hozzáadandó genetikai kód
	 */
	public GeneticCode AddGeneticCode(GeneticCode gc){
		if (!codes.contains(gc)){
			codes.add(gc);
			return gc;
		} else{
			return codes.get(codes.indexOf(gc));
		}
	}

	/**
	 * Hozzáad egy mezőt a játékhoz
	 * @param f hozzáadandó mező
	 */
	public void AddField(Field f){
		fields.add(f);
	}

	public List<Field> GetFields(){return fields;}

	public void RemoveVirologist(Virologist virologist) {
		virologists.remove(virologist);
		playerCount--;
	}
}
