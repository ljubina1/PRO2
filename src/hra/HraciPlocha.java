package hra;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import obrazek.Obrazek;
import obrazek.ZdrojObrazkuSoubor;

public class HraciPlocha extends JPanel {
	public static final boolean DEBUG =true;

	public static final int VYSKA = 800;
	public static final int SIRKA = 600;
	//sehnat pozadi ..v pravo a levo navazovat..nekonecne rolovaci pozadi vyska 800 a sirka delsi 1200/1800
	//rychlost bìhu pozadí
	public static final int RYCHLOST = -2; // zaporne cislo protoze se to bude posouvat na druhou stranu do leva
	//musi byt alespon tri zdi, jinak se prvni zed "nestihne" posunout za levy okraj /neestihne zajet za levy okraj hraci plochy drive, nez
	//je potreba ji posunout pred levy okraj hraci plochy a vykreslit
	
	public static final int POCET_ZDI = 4;
	private SeznamZdi seznamZdi;
	private Zed aktualniZed;
	private Zed predchoziZed;
	
	//TODO
	private Hrac hrac;
	
	private BufferedImage imgPozadi;
	private Timer casovacAnimace; //èasování, JPanel swingová konstanta
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;
	
	public HraciPlocha(){
		//TODO
		//správa obrázkù
		ZdrojObrazkuSoubor z = new ZdrojObrazkuSoubor();
		z.naplMapu();
		z.setZdroj(Obrazek.POZADI.getKlic());
		try {
			imgPozadi = z.getObrazek();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		z.setZdroj(Obrazek.HRAC.getKlic());
		BufferedImage imgHrac;
		//hrac = new Hrac(null);
		try {
			imgHrac = z.getObrazek();
			hrac = new Hrac(imgHrac);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		z.setZdroj(Obrazek.ZED.getKlic());
		BufferedImage imgZed;
		//hrac = new Hrac(null);
		try {
			imgZed = z.getObrazek();
			Zed.setObrazek(imgZed);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		seznamZdi = new SeznamZdi();
	}
	private void vyrobZdi(int pocet){
		Zed zed;
		int vzdalenost = HraciPlocha.SIRKA;
		
		for(int i = 0; i < pocet; i++){
			zed = new Zed(vzdalenost);
			seznamZdi.add(zed);
			vzdalenost = vzdalenost + (HraciPlocha.SIRKA / 2);
		}
		vzdalenost = vzdalenost - HraciPlocha.SIRKA - Zed.SIRKA;
		Zed.setVzdalenostPosledniZdi(vzdalenost);
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		//dve pozadi za sebe pro plynule prechody
		//prvni
		g.drawImage(imgPozadi, posunPozadiX, 0, null);
		//druhe je posunuto o sirku obrazku
		g.drawImage(imgPozadi, posunPozadiX + imgPozadi.getWidth(), 0, null);
		
		if(HraciPlocha.DEBUG){
			g.setColor(Color.WHITE);
			g.drawString("posunPozadiX ="+posunPozadiX, 0, 10);
		}
		for (Zed zed : seznamZdi) {
			zed.paint(g);
		}
		//vykresleni hrace
		hrac.paint(g);
		
	}
	private void posun(){
		if( ! pauza && hraBezi ){
			for (Zed zed : seznamZdi) {
				zed.posunX();
			}
			
			hrac.posun();
			
			//posun pozice pozadi hraci plochy (sklonovani)
			posunPozadiX = posunPozadiX + HraciPlocha.RYCHLOST;
			//kdyz se pozadi cele doposouva,zacni od zacatku
			if( posunPozadiX == -imgPozadi.getWidth()){
				posunPozadiX= 0;	
			}
		}
	}
	private void spustHru(){
		casovacAnimace = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint(); //pøekreslení
				posun();
			}
		});
		hraBezi = true;
		casovacAnimace.start();
	}
	public void pripravHraciPlochu(){
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){
					//skok hrace
					hrac.skok();
				}
				//pauza
				if(e.getButton() == MouseEvent.BUTTON3){
				if(hraBezi){
					if(pauza){
						pauza = false;
					}else{
						pauza = true;
					}
				}else{
					pripravNovouHru();
					spustHru();
				}
					
				}
			}
		});
		setSize(SIRKA, VYSKA);

	}

	protected void pripravNovouHru() {
		//TODO
		vyrobZdi(POCET_ZDI);
		
	}
	
	
}
