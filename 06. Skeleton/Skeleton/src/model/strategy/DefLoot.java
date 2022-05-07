package model.strategy;


import model.Virologist;
import test.Tester;

/**
 * A default zsákmányolási stratégia, engedélyezi a zsákmányolás kezdeményezését.
 */
public class DefLoot implements ILootStr
{
	/**
	 * Default ctor, csak a kiíratás miatt.
	 */
	public DefLoot(){
		Tester.ctrMethodStart(new Object(){}.getClass().getEnclosingConstructor());
	}

	/**
	 * Amniosav zsákmányolást kezdeményez v a target virológus felé.
	 * @param v A zsákmányoló virológus.
	 * @param target A kizsákmányolandó virológus.
	 */
	@Override
	public void LootAmino(Virologist v, Virologist target)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		v.DecreaseActions();
		target.StealAminoAcid(v);
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}

	/**
	 * Nukleotid zsákmányolást kezdeményez v a target virológus felé.
	 * @param v A zsákmányoló virológus.
	 * @param target A kizsákmányolandó virológus.
	 */
	@Override
	public void LootNucleotide(Virologist v, Virologist target)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		v.DecreaseActions();
		target.StealNukleotid(v);
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}

	/**
	 * Védőfelszerelés zsákmányolást kezdeményez v a target virológus felé.
	 * @param v A zsákmányoló virológus.
	 * @param target A kizsákmányolandó virológus.
	 */
	@Override
	public void LootEquipment(Virologist v, Virologist target)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		v.DecreaseActions();
		target.StealEquipment(v);
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}
}
