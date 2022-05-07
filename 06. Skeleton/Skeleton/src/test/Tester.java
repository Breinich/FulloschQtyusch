package test;

import model.Game;
import model.Virologist;
import model.codes.BlockCode;
import model.codes.ChoreaCode;
import model.codes.ForgetCode;
import model.codes.StunCode;
import model.equipments.Bag;
import model.equipments.Cloak;
import model.map.Field;
import model.map.Shelter;
import model.strategy.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Tesztelést vezérlő osztály, ebben van megvalósítva a teszteseteket kezelő menürendszer
 * és a teszteseteket végrehajtó függvények, továbbá a tesztekhez szükséges
 * függvnényneveket kiíró segédfüggvények.
 */
public class Tester {

    /**
     * indentálási szint
     */
    private static int indention = 0;

    /*
     * Technically this will work...
     *
     * String name = new Object(){}.getClass().getEnclosingMethod().getName();
     * However, a new anonymous inner class will be created during compile time
     * (e.g. YourClass$1.class). So this will create a .class file for each method that deploys this trick.
     * Additionally, an otherwise unused object instance is created on each invocation during runtime.
     * So this may be an acceptable debug trick, but it does come with significant overhead.
     *
     * An advantage of this trick is that getEnclosingMethod() returns java.lang.reflect.Method which can
     * be used to retrieve all other information of the method including annotations and parameter names.
     * This makes it possible to distinguish between specific methods with the same name (method overload).
     *
     * Note that according to the JavaDoc of getEnclosingMethod() this trick should not throw a SecurityException
     * as inner classes should be loaded using the same class loader.
     * So there is no need to check the access conditions even if a security manager is present.
     *
     * Please be aware: It is required to use getEnclosingConstructor() for constructors.
     * During blocks outside of (named) methods, getEnclosingMethod() returns null
     */

    /**
     * Tesztelés során az aktuálisan meghívott függvény nevét és osztályát írja ki
     * @param m a hívott függvény reflekciója
     */
    public static void methodStart(Method m){
        for(int i = 0; i < indention; i++)
            System.out.print("\t");
        System.out.println(m.getDeclaringClass()+"::"+m.getName()+"()");
        indention++;
    }

    /**
     * Tesztelés során az aktuálisan visszatérő függvény nevét és osztályát írja ki
     * @param m a visszatérő függvény reflekciója
     */
    public static void methodEnd(Method m){
        indention--;
        for(int i = 0; i < indention; i++)
            System.out.print("\t");
        System.out.println(m.getDeclaringClass()+"::"+m.getName()+"()");
    }

    /**
     * Tesztelés során az aktuálisan meghívott konstruktor függvény nevét írja ki indentálás nélkül
     * @param m a hívott függvény reflekciója
     */
    public static void ctrMethodStart(Constructor<?> m){
        System.out.println(m.getName()+"()");
    }

    /**
     * UserInput egységes kezelésére használandó függvény.
     * @param message A kiírandó üzenet. Ajánlott benne megfogalmazni az elvárt bemenet formátumát.
     * @param trueMatch A bemenet, ami esetén igazzal tér majd vissza a függvény.
     * @return A felhasználói bemenet megegyezett-e trueMatch-el.
     */
    public static boolean getUserInput(String message, String trueMatch)
    {
        boolean ret = false;
        System.out.println(message);
        Scanner sc = new Scanner(System.in);
        ret = sc.nextLine().equals(trueMatch);
        sc.close();
        return ret;
    }

    /**
     * 5.1.2.1.-es teszteset
     * Lebénult virológus megpróbál Aminosavat lopni egy másiktól,
     * de mivel béna, ezért nem tud.
     */
    @SkeletonTestCase
    public static void test1(){
        Virologist v1 = new Virologist();
        NoLoot nl = new NoLoot();
        Virologist v2 = new Virologist();
        v1.SetLootStr(nl);

        v1.LootAminoAcidFrom(v2);
    }

    /**
     * 5.1.2.2.-es teszteset
     * Egy virológus megpróbál egy másikat kirabolni (felszerelést lopni tőle), de mivel az nem bénult,
     * ezért nem lesz sikeres.
     * Ha a virológusnál 3 felszerelés van, akkor se lesz sikeres, de akkor az 5.1.2.3.-as teszteset
     * fog végrehajtódni.
     */
    @SkeletonTestCase
    public static void test2(){
        Virologist v1 = new Virologist();
        Virologist v2 = new Virologist();

        v1.LootEquipmentFrom(v2);
    }

    /**
     * 5.1.2.4.-es teszteset
     * Egy virológus nukleotidot lop egy másik, lebénult virológustól. Aminosav lopás is ugyanígy nézne ki.
     * Akkor fog a várt eredménnyel lefutni, ha a virológusnak van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test4(){
        Virologist v1 = new Virologist();
        Virologist v2 = new Virologist();
        Looted ltd = new Looted();
        v2.SetLootedStr(ltd);
        v2.AddNucleotide(20);

        v1.LootNucleotideFrom(v2);
    }

    /**
     * 5.1.2.5.-ös teszteset
     * Egy virológus egy másik, bénult virológustól szeretne felszerelést lopni, de annak nincs.
     * Akkor fog a várt eredménnyel lefutni, ha a virológusnak van még hátra akciója és van helye
     * plusz felszerelésre.
     */
    @SkeletonTestCase
    public static void test5(){
        Virologist v1 = new Virologist();
        Virologist v2 = new Virologist();
        Looted lt = new Looted();
        v2.SetLootedStr(lt);

        v1.LootEquipmentFrom(v2);
    }

    /**
     * 5.1.2.6.-os teszteset
     * A virológus sikeresen lop egy köpenyt egy másik, bénult virológustól.
     * Akkor fog a várt eredménnyel lefutni, ha a virológusnak van még hátra akciója és van helye
     * plusz felszerelésre.
     */
    @SkeletonTestCase
    public static void test6(){
        Virologist v1 = new Virologist();
        Virologist v2 = new Virologist();
        Looted lt = new Looted();
        v2.SetLootedStr(lt);
        Cloak c = new Cloak();
        v2.AddEquipment(c);

        v1.LootEquipmentFrom(v2);
    }

    /**
     * 5.1.2.7-es teszteset
     * A virológus mozog egy megadott mezőre.
     * Akkor fog a várt eredménnyel lefutni a teszt, hogyha van még rendelkezésre álló akciója
     * a virológusnak, különben visszatér a move(Field: f) függvényből.
     */
    @SkeletonTestCase
    public static void test7(){
        Field field = new Field();
        Virologist v1 = new Virologist();
        field.AddVirologist(v1);
        Field f = new Field();

        v1.Move(f);
    }

    /**
     * 5.1.2.8-as teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus megakadályozódik abban,
     * hogy átlépjen egy mezőről egy másik, szomszédos mezőre. Persze csak akkor, ha még van rendelkezésre álló akciója.
     */
    @SkeletonTestCase
    public static void test8(){
        Field field = new Field();
        Virologist v = new Virologist();
        field.AddVirologist(v);
        IMoveStr s = new NoMove();
        v.SetMoveStr(s);

        v.Move(new Field());
    }

    /**
     * 5.1.2.9-es teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus sikeresen felvesz egy felszerelést az adott mezőről,
     * feltéve ha van még háta akciója.
     */
    @SkeletonTestCase
    public static void test9(){
        Virologist v = new Virologist();
        Field field = new Field();
        field.AddVirologist(v);
        Bag e = new Bag();
        field.Drop(e);

        v.Equip();
    }

    /**
     * 5.1.2.10-es teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus megpróbál egy felszerelést felvenni az adott mezőről,
     * de a mezőn nincsenek felszerelések.
     * Csak akkor próbálkozik, ha van mág hátra akciója.
     */
    @SkeletonTestCase
    public static void test10(){
        Virologist v = new Virologist();
        Field field = new Field();
        field.AddVirologist(v);

        v.Equip();
    }

    /**
     * 5.1.2.11-es teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus sikeresen felvesz egy felszerelést egy óvóhely,
     * feltéve ha van még akciója.
     */
    @SkeletonTestCase
    public static void test11(){
        Virologist v = new Virologist();
        Bag e = new Bag();
        Shelter field = new Shelter(e);
        field.AddVirologist(v);
        field.Drop(e);
        v.Equip();
    }

    /**
     * 5.1.2.12-es teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus megpróbál egy felszerelést felvenni egy óvóhelyről,
     * de az óvóhelyen nincsenek felszerelések.
     * Egyáltalán csak akkor próbálja meg felvenni, ha van akciója.
     */
    @SkeletonTestCase
    public static void test12(){
        Virologist v = new Virologist();
        Shelter field = new Shelter(null);
        field.AddVirologist(v);

        v.Equip();
    }

    /**
     * 5.1.2.13-as teszteset
     * Azt a folyamatot szimulálja, mikor egy virológus megakadályozódik abban,
     * hogy felvegyen egy felszerelést az adott mezőről.
     * Egyáltalán csak akkor próbálja meg, ha van akciója.
     */
    @SkeletonTestCase
    public static void test13(){
        Virologist v = new Virologist();
        NoEquip s = new NoEquip();
        v.SetEquipStr(s);

        v.Equip();
    }

    @SkeletonTestCase
    public static void test14(){} // TODO?

    /**
     * 5.1.2.15-ös teszteset
     * A virológus megpróbál megkenni egy másik virológust, de a virológus nem képes rá.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test15(){
        Virologist v = new Virologist();
        NoInject injectStr = new NoInject();
        BlockCode bCode = new BlockCode();
        Virologist target = new Virologist();
        v.AddGeneticCode(bCode);
        v.SetInjectStr(injectStr);

        v.Inject(target, bCode);
    }

    /**
     * 5.1.2.16-os teszteset
     * A virolúgus megpróbál megkenni egy másik virológust, de megakadályozódik abban.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test16(){
        Virologist v = new Virologist();
        NoInject injectStr = new NoInject();
        BlockCode bCode = new BlockCode();
        Virologist target = new Virologist();

        v.AddGeneticCode(bCode);
        v.SetInjectStr(injectStr);
        v.Inject(target, bCode);
    }

    /**
     * 5.1.2.17-es teszteset
     * A virolúgus sikeresen megken egy másik virológust bénító ágenssel.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test17(){
        Virologist injecter = new Virologist();
        StunCode sCode = new StunCode();
        Virologist target = new Virologist();
        DefInject injectStr = new DefInject();
        DefInjected injectedStr = new DefInjected();

        injecter.AddGeneticCode(sCode);

        // TODO: nem így van a diagrammon
        injecter.SetInjectStr(injectStr);

        target.SetInjectedStr(injectedStr);

        injecter.Inject(target, sCode);
    }

    /**
     * 5.1.2.18-as teszteset
     * A virolúgus sikertelenül megken egy másik virológust bénító ágenssel.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test18(){
        Virologist injecter = new Virologist();
        StunCode sCode = new StunCode();
        Virologist target = new Virologist();
        DefInject injectStr = new DefInject();
        NoInjected injectedStr = new NoInjected();

        injecter.AddGeneticCode(sCode);
        target.SetInjectedStr(injectedStr);

        // TODO: nem így van a diagrammon
        injecter.SetInjectStr(injectStr);
        injecter.Inject(target, sCode);
    }

    /**
     * 5.1.2.19-es teszteset
     * A virolúgus sikeresen megken egy másik virológust felejtő ágenssel.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test19(){
        Virologist injecter = new Virologist();
        DefInject injectStr = new DefInject();
        ForgetCode fCode = new ForgetCode();
        Virologist target = new Virologist();
        DefInjected injectedStr = new DefInjected();

        injecter.AddGeneticCode(fCode);

        // TODO: nem így van a diagrammon
        injecter.SetInjectStr(injectStr);

        target.SetInjectedStr(injectedStr);

        injecter.Inject(target, fCode);
    }

    /**
     * 5.1.2.20-as teszteset
     * A virolúgus sikertelenül megken egy másik virológust felejtő ágenssel.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test20(){
        Virologist injecter = new Virologist();
        DefInject injectStr = new DefInject();
        ForgetCode fCode = new ForgetCode();
        Virologist target = new Virologist();
        NoInjected injectedStr = new NoInjected();

        injecter.AddGeneticCode(fCode);
        target.SetInjectedStr(injectedStr);

        // TODO: nem így van a diagrammon
        injecter.SetInjectStr(injectStr);
        injecter.Inject(target, fCode);
    }

    /**
     * 5.1.2.21-es teszteset
     * A virolúgus sikeresen megken egy másik virológust blokkoló ágenssel.
     * Csak akkor próbálkozik, ha van még hátra akciója.
     */
    @SkeletonTestCase
    public static void test21(){
        Virologist injecter = new Virologist();
        DefInject injectStr = new DefInject();
        BlockCode bCode = new BlockCode();
        Virologist target = new Virologist();
        DefInjected injectedStr = new DefInjected();

        injecter.AddGeneticCode(bCode);

        // TODO: nem így van a diagrammon
        injecter.SetInjectStr(injectStr);

        target.SetInjectedStr(injectedStr);

        injecter.Inject(target, bCode);
    }

    /**
     * 5.4.22-es teszteset
     * A virológus sikeresen megken egy virológust
     */
    @SkeletonTestCase
    public static void test22(){
        Virologist v = new Virologist();
        ChoreaCode code = new ChoreaCode();
        Virologist target = new Virologist();
        v.AddGeneticCode(code);

        v.Inject(target, code);
    }

    /**
     * 5.4.23-es teszteset
     * A virológus próbál megkenni egy virológust, de a célpont stratégiája nem engedi
     */
    @SkeletonTestCase
    public static void test23(){
        Virologist v = new Virologist();
        ChoreaCode code = new ChoreaCode();
        v.AddGeneticCode(code);
        Virologist target = new Virologist();
        NoInjected iStr = new NoInjected();
        target.SetInjectedStr(iStr);

        v.Inject(target, code);
    }

    /**
     * 5.4.24-es teszteset
     * Kör vége és játék vége
     */
    @SkeletonTestCase
    public static void test24(){
        Game g = new Game();
        Virologist v = new Virologist();
        g.AddVirologist(v);

        ChoreaCode c = new ChoreaCode();
        BlockCode b = new BlockCode();
        ForgetCode f = new ForgetCode();
        StunCode s = new StunCode();
        v.AddGeneticCode(c);
        v.AddGeneticCode(b);
        v.AddGeneticCode(f);
        v.AddGeneticCode(s);

        v.EndTurn();
    }

    /**
     * 5.4.25-ös teszteset
     * Kör vége és játék vége nélkül
     */
    public static void test25(){
        Game g = new Game();
        Virologist v = new Virologist();
        g.AddVirologist(v);
        BlockCode b = new BlockCode();
        v.AddGeneticCode(b);

        v.EndTurn();
    }

    /**
     * 5.4.26-os teszteset
     * Sikeres eldobás
     */
    @SkeletonTestCase
    public static void test26(){
        Virologist v = new Virologist();
        Field f = new Field();
        f.AddVirologist(v);
        Cloak c = new Cloak();
        v.AddEquipment(c);

        v.Drop();
    }

    /**
     * 5.4.29-es teszteset
     * Sikertelen eldobás stratégia miatt
     */
    @SkeletonTestCase
    public static void test29(){
        Virologist v = new Virologist();
        Field f = new Field();
        f.AddVirologist(v);
        Cloak c = new Cloak();
        v.AddEquipment(c);

        v.Drop();
    }

    @SkeletonTestCase
    public static void test30(){}

    @SkeletonTestCase
    public static void test31(){}

    @SkeletonTestCase
    public static void test32(){}

    @SkeletonTestCase
    public static void test33(){}



    public static void menu(){
        System.out.println(
                "1) 5.1.2.1 Stunned Virologist tries to loot AminoAcid\n" +
                "2) 5.1.2.2 Virologist tries to loot other Virologist who is not stunned\n" +
                "   5.1.2.3 Virologist tries to loot with 3 equipment stored already\n" +
                "4) 5.1.2.4 Virologist loots Nucleotide from other Virologist\n" +
                "5) 5.1.2.5 Virologist tries to loot equipment from other Virologist who has none\n" +
                "6) 5.1.2.6 Virologist tries to loot equipment from other Virologist who has a cloak\n" +
                "7) 5.1.2.7 Virologist moves to field\n"
        );
    }

    public static void main(String[] args){
        HashMap<String, Method> tests = new HashMap<>();

        for (Method method : Tester.class.getMethods()){
            if (method.isAnnotationPresent(SkeletonTestCase.class)){
                tests.put(method.getName(), method);
            }
        }

        menu();
        Scanner sc = new Scanner(System.in);
        String input;
        while (!(input = sc.nextLine()).equals("exit")){
            Method m = tests.get(input);
            try {
                m.invoke(null); //kezelődik a null dereferálás, invoke exception-ök is.
            } catch (Exception e){
                System.out.println("Nem megfelelő input!");
            }
        }
    }
}
