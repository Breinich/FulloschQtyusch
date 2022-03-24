package model.strategy;//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : strategy.NoInjected.java
//  @ Date : 2022. 03. 23.
//  @ Author : 
//
//



import model.Virologist;
import model.agents.Agent;
import test.Tester;

/**
 * Az a felkenődés stratégia, mikor a virológusra nem engedi felkenődni az adott ágenst.
 */
public class NoInjected implements IInjectedStr
{
	/**
	 * A felkenődést nem engedélyezi v-re a stratégia, így nem történik semmi.
	 * @param v A virológus, akire felkenték az ágenst.
	 * @param a A felkent ágens.
	 */
	public void Injected(Virologist v, Agent a)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}
}
