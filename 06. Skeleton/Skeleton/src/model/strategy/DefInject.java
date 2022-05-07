package model.strategy;


import model.Virologist;
import model.codes.GeneticCode;
import test.Tester;

/**
 * A default felkenés stratégia, ami engedélyezi a felkenést a kenőnek.
 */
public class DefInject implements IInjectStr
{
	/**
	 * Default ctor, csak a kiíratás miatt.
	 */
	public DefInject(){
		Tester.ctrMethodStart(new Object(){}.getClass().getEnclosingConstructor());
	}

	/**
	 * A felkenést végző függvény, ami elvégzi a felkenést a célpontra, valamint csökkenti v leléphető köreinek számát.
	 * @param v A virológus aki a kenést akarja végezni.
	 * @param target A célpont akit fel akar kenni.
	 * @param gc A genetikai kód, ami a felkenendő ágenst gyártja.
	 */
	@Override
	public void Inject(Virologist v, Virologist target, GeneticCode gc) {
		Tester.methodStart(new Object() {
		}.getClass().getEnclosingMethod());
		try {
			target.TargetedWith(gc.Create(v));
			v.DecreaseActions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Tester.methodEnd(new Object() {
		}.getClass().getEnclosingMethod());
	}
}
