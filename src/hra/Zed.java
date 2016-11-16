package hra;

import java.awt.image.BufferedImage;

public class Zed {

	public static final int SIRKA = 45;
	//rychlost pohybu zdi
	public static final int RYCHLOST = -6;
	public static final int MEZERA_MEZI_HORNI_A_DOLNI_CASTI_ZDI = 200;
	
	//TODO
	//ruzne zdi ruzne obrazky nelze pouzit static
	private static BufferedImage img = null;
	private int x; //x-ova souradnice zdi ( meni se zprava doleva)
	//y)ova souradnice zdi(horni souradnice spodni casti zdi)
	private int y;
	private int vyska;
	
	public Zed(int vzdalenostZdiOdZacatkuHraciPlochy){
		this.x = vzdalenostZdiOdZacatkuHraciPlochy;
		//TODO
		
	}
	
//UKOL VYROBIT SI OBRAZEK ZDI, JAKO MAME PREDEPSANOU SIRKA 45 VYSKA 800
}
