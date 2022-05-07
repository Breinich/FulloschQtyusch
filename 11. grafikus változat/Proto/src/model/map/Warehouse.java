package model.map;


import control.SingleTonScanner;
import model.Virologist;
import model.equipments.Equipment;

import java.util.Random;
import java.util.Scanner;

/**
 * Olyan mező, amelyen anyag gyüjthető
 */
public class Warehouse extends Field
{
	/**
	 * Ennyivel növeli a virológusok anyagát
	 */
	private int delta = 5;

	/**
	 * Anyag gyüjtése
	 * Nem determinisztikus esetben a paraméterül kapott virológus anyagkészletét deltával növeli meg, random választva a 2 fajta anyag közül
	 * Determinisztikus esetben a paraméterül kapott virológus anyagkészletét deltával növeli meg, a kiválasztott anyag közül
	 * @param v gyüjtő virológus
	 */
	public void CollectMaterial(Virologist v) {
		if (v.GetContext().randOn){
			Random random = new Random();
			int r = random.nextInt(2) ;

			if (r == 0) {
				v.AddAminoAcid(delta);
			}
			else {
				v.AddNucleotide(delta);
			}
		}else{
			//game-be kellene egy ask for input, mert ő kommunikál a külvilággal
			//de most jó ez így csak kérdés hogyan kell dokumentálni?, meg pszeudót írni, de szerintem most az eredeti algot írjuk le
			//Scanner sc = new Scanner(System.in); //Nem szabad bezárni
			Scanner sc = SingleTonScanner.Create();
			System.out.println("0 - Amino acid");
			System.out.println("1 - Nucleotide");
			if (sc.nextLine().equals("0")){
				v.AddAminoAcid(delta);
			} else{
				v.AddNucleotide(delta);
			}
		}
	}

	/**
	 * A mezőn az anyagok tönkretételét szimbolizálja, nem vehető fel anyag ezután a mezőről
	 */
	@Override
	public void DestroyMaterial(){
		delta = 0;
	}

	/**
	 * Listázza a mezőt, felszereléseket, a mező anyag kibocsátásának mennyiségét, virológusokat és a szomszédos mezőket
	 */
	@Override
	public void bark(){
		System.out.println(this.getClass().getSimpleName() + ": " + name);
		System.out.println("\tEquipments:");
		for (Equipment e: equipments) {
			System.out.println("\t\t" + e.getName());
		}
		System.out.println("\tMaterial output:");
		System.out.println("\t\tAmount: " + delta);
		System.out.println("\tVirologists:");
		for (Virologist v: virologists) {
			System.out.println("\t\t" + v.getName());
		}
		System.out.println("\tNeighbours:");
		for (Field f: neighbours) {
			System.out.println("\t\t" + f.getName());
		}
	}
}
