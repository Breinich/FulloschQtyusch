package model.strategy;



import model.Virologist;
import model.agents.Agent;
import test.Tester;

/**
 * Az a felkenődés stratégia, mikor a virológusra nem engedi felkenődni az adott ágenst.
 */
public class NoInjected implements IInjectedStr
{
	/**
	 * Default ctor, csak a kiíratás miatt.
	 */
	public NoInjected(){
		Tester.ctrMethodStart(new Object(){}.getClass().getEnclosingConstructor());
	}

	/**
	 * A felkenődést nem engedélyezi v-re a stratégia, így nem történik semmi.
	 * @param v A virológus, akire felkenték az ágenst.
	 * @param a A felkent ágens.
	 */
	@Override
	public void Injected(Virologist v, Agent a)
	{
		Tester.methodStart(new Object(){}.getClass().getEnclosingMethod());
		Tester.methodEnd(new Object(){}.getClass().getEnclosingMethod());
	}
}
