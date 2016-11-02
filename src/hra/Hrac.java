package hra;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Hrac {

	public static final int SIRKA = 40;
	public static final int VYSKA = 33;
	//final --konstanty,veøejna aby v jine tride se mohlo precist
	private static final int KOEF_ZRYCHLENI = 1;
	//velikost skoku hrace
	//rychlost padu hrace
	private static final int KOEF_RYCHLOST = 2;
	private BufferedImage img = null;
	//pamatuje si co se nakreslilo, obrazek zustane v okne, i pres zmenu okna
	//pocatecni x-ova pozice hrace, nemeni se(hrac neskace dopredu dozadu)
	private int x;
	//pocatecni y-ova pozice hrace,meni se(skace nahoru a dolu)
	private int y;
	private int rychlost;
	//pomoci toho koeficienty vypocitame
	
	public Hrac(BufferedImage img) {
		this.img = img;
		//zaciname uprostred, musime definovat stred
		x = (HraciPlocha.SIRKA /2) - (img.getWidth() / 2); //obrazek posunout o pùlku, aby byl uprostøed
		y = HraciPlocha.VYSKA / 2;
		
		rychlost = KOEF_RYCHLOST;
	}
	//vola se po narazu zdi, do kraje okna
	
	public void reset(){
		y = HraciPlocha.VYSKA / 2; // x se nemeni
		
		rychlost = KOEF_RYCHLOST;
		
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	//metoda pro skok hrace a posun=pohybu pri padu
	public void skok(){
		rychlost = -17;
		
	}
	//zajistuje pohyb hrace
	public void posun(){
		rychlost = rychlost + KOEF_ZRYCHLENI;
		y = y + rychlost;
		
	}
	public void paint(Graphics g){ //platno hraci plochy a hrace
		g.drawImage(img, x, y, null);
	}
	public int getVyskaHrace(){
		return img.getHeight(); // vracet vysku obrazku, stejna vysoka jako vyska hrace
	}
	//vraci pomyslny ctveres/obdelnik, ktery opisuje hrace
	public Rectangle getMez(){
		return new Rectangle(x, y, img.getWidth(), img.getHeight());
		
	}
	
}
