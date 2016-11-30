package hra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
	
	private int skore=0;//kolika zdmi hrac uspesne prosel
	private JLabel lbSkore;
	private JLabel lbZprava;
	private Font font;
	private Font fontZpravy;
	
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
		 vyrobFontyALabely();
	}
	private void vyrobFontyALabely(){
		font = new Font("Arial", Font.BOLD, 40);
		fontZpravy = new Font("Arial", Font.BOLD, 20);
		this.setLayout(new BorderLayout());
		
		lbZprava = new JLabel("");
		lbZprava.setFont(fontZpravy);
		lbZprava.setForeground(Color.GREEN);
		lbZprava.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbSkore = new JLabel("0");
		lbSkore.setFont(font);
		lbSkore.setForeground(Color.BLUE);
		lbSkore.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.add(lbSkore, BorderLayout.NORTH);
		this.add(lbZprava, BorderLayout.CENTER);
		
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
		
		lbSkore.paint(g);
		lbZprava.paint(g);
	}
	private void posun(){
		if( ! pauza && hraBezi ){
			//nastav zed, ktera je aktualni, nastav zed v poradi
			aktualniZed = seznamZdi.getAktualniZed();
			
			//nastav(vrat referenci) predchozi zed
			predchoziZed = seznamZdi.getPredchoziZed();
			 
			//detekce kolizi
			if( isKolizeSeZdi(predchoziZed,hrac) || isKolizeSeZdi(aktualniZed, hrac) || isKolizeSHraniciHraciPlochy(hrac)){
				ukonciAvyresetujHruPoNarazu();
			}else{
			
			for (Zed zed : seznamZdi) {
				zed.posunX();
			}
			
			hrac.posun();
			//hrac prosel zdi bez narazu
			//zjistit kde se nachazi, bud pred aktualni zdi-nedelej nic
			//anebo za aktualni zdi - posun dalsi zed v poradi a prepocitej skore
			
			if(hrac.getX() >= aktualniZed.getX()){
				seznamZdi.nastavDalsiZedNaAktualni();
				zvedniSkoreZed();
				lbSkore.setText(skore + "");
				
			}
			}
			
			//posun pozice pozadi hraci plochy (sklonovani)
			posunPozadiX = posunPozadiX + HraciPlocha.RYCHLOST;
			//kdyz se pozadi cele doposouva,zacni od zacatku
			if( posunPozadiX == -imgPozadi.getWidth()){
				posunPozadiX= 0;	
			}
		}
	}
	private void ukonciAvyresetujHruPoNarazu() {
		hraBezi = false;
		casovacAnimace.stop();
		casovacAnimace = null;
		vyresetujHru();
		nastavZpravuNarazDoZdi();
		
	}
	private boolean isKolizeSeZdi(Zed zed, Hrac hrac){
		
		return (zed.getMezSpodniZdi().intersects(hrac.getMez())) || (zed.getMezHorniCastiZdi().intersects(hrac.getMez()));
		
	}
	private boolean isKolizeSHraniciHraciPlochy(Hrac hrac){
		return (hrac.getY()<= 0) || (hrac.getY() >= HraciPlocha.VYSKA - hrac.getVyskaHrace() - 40);
	}
	
	private void spustHru(){
		casovacAnimace = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint(); //pøekreslení
				posun();
			}
		});
		nastavZpravuPrazdna();
		hraBezi = true;
		casovacAnimace.start();
	}
	public void pripravHraciPlochu(){
		nastavZpravuOvladani();
		
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
						nastavZpravuPrazdna();
						pauza = false;
					}else{
						nastavZpravuPauza();
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
		vyresetujHru();
		
	}
	private void vyresetujHru(){
		resetujVsechnyZdi();
		hrac.reset();
		//nejprve zobraz stare skore,aby hrac videl kolik bodu nasbiral
		lbSkore.setText(skore + "");
		//ale skore pak vynuluj
		
		vynulujSkore();
	}
	private void vynulujSkore() {
		skore = 0;
		
	}
	private void zvedniSkoreZed(){
		skore = skore + Zed.BODY_ZA_ZED;
	}
	
	private void resetujVsechnyZdi() {
		seznamZdi.clear();
		vyrobZdi(POCET_ZDI);
		
	}
	public void reset(){
		vygenerujNahodneHodnotyProZed();
	}
	private void vygenerujNahodneHodnotyProZed() {
		// TODO Auto-generated method stub
		
	}

	private void nastavZpravuNarazDoZdi(){
		lbZprava.setFont(font);
		lbZprava.setText("narazil jsi AU, zkus to znovu");
	}
	private void nastavZpravuPauza(){
		lbZprava.setFont(font);
		lbZprava.setText("pauza");
		
	}
	private void nastavZpravuOvladani(){
		lbZprava.setFont(fontZpravy);
		lbZprava.setText("pravy klik = start/stop, levy klik = skok");
		
	}
	private void nastavZpravuPrazdna(){
		lbZprava.setFont(font);
		lbZprava.setText("");
	}
	
}
