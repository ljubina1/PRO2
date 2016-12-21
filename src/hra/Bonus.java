package hra;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bonus {

	public static final int SIRKA=40;
	public static final int VYSKA=40;
	public static final int BODY_ZA_BONUS = 5;
	private Random random;
	private BufferedImage img;
	private SeznamZdi seznamZdi;
	private int x;
	private int y;
	
	public Bonus(BufferedImage img){
		this.img = img;
		random = new Random();
		x= -200;
		x=-200;
	}
	public void paint(Graphics g){
		g.drawImage(img, x, y, null);
		
	}
	public void posun(){
		x = x + Zed.RYCHLOST;
		//TODO
		if(x<=0){
			nastavNovyBonus();
		}
	}
	public void nastavNovyBonus(){
		vygenerujNahodneHondnotyProBonus();
	}
	public Rectangle getMez(){
		return new Rectangle(x,y,SIRKA,VYSKA);
	}
	private void vygenerujNahodneHondnotyProBonus(){
		x=HraciPlocha.SIRKA + (HraciPlocha.SIRKA / 2);
		y = random.nextInt(HraciPlocha.VYSKA - 100);
		
	}
	public boolean isBonusVygenerovaniDoZdi(Zed zed){
		return( zed.getMezSpodniZdi().intersects(this.getMez())) ||
				(zed.getMezHorniCastiZdi().intersects(this.getMez()));
		
		
	}
	
}
