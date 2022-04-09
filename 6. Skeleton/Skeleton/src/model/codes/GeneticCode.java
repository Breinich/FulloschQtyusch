package model.codes;


import model.Virologist;
import model.agents.Agent;

/**
 * Absztrakt ősként szolgál a különböző genetikai kódoknak.
 * Ezek felelősségei, hogy factory-ként szolgáljanak az ágenseknek,
 * hiszen ezek segítségével kell létrehozni őket,
 * ezen kívül tartalmazza az elkészítéshez tartozó aminosav, nukleotid tarifákat.
 */
public abstract class GeneticCode
{
	/**
	 * A kódhoz tartozó ágens előállításához szükséges aminosav mennyiség
	 */
	protected int aminoAcidPrice;

	/**
	 * A kódhoz tartozó ágens előállításához szükséges nukleotid mennyiség
	 */
	protected int nucleotidePrice;

	/**
	 * A kódhoz tartozó ágens hatásának ideje körökben mérve.
	 */
	protected int turnsLeft;

	/**
	 * Létrehoz egy, a kódhoz tartozó ágenst a megfelelő költség behajtásával a virológustól.
	 * @param v a virológus, aki szeretne ágenst készíteni
	 * @return az elkészített ágens
	 * @throws Exception ha a virológusnak nem volt elég anyaga az ágenskészítéshez
	 */
	public abstract Agent Create(Virologist v) throws Exception;
}
