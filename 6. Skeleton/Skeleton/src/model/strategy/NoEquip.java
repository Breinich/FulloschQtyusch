package model.strategy;//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : strategy.NoEquip.java
//  @ Date : 2022. 03. 23.
//  @ Author : 
//
//



import model.Virologist;
import model.map.Field;
import test.Tester;

/**
 * Olyan védőfelszerelés felvételének mechanikájáért felelős stratégia, ami nem engedélyezi a felvételt.
 */
public class NoEquip implements IEquipStr
{
	/**
	 * A metódus üres, hiszen nem engedélyezi a felvételt.
	 * @param v A felvételt végző virológus.
	 * @param f Erről a mezőről próbálkozik védőfelszerelés felvételével v.
	 */
	public void Equip(Virologist v, Field f)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}
}
