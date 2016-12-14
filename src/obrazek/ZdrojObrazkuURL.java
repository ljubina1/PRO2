package obrazek;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ZdrojObrazkuURL extends ZdrojObrazku{

	private static final String SOUBOR ="imgurl.txt";
	private static final String ODDELOVAC =";";
	
	@Override
	public void naplMapu(){
		nactiCSV();
	}
	
	private void nactiCSV(){
	//vyrobime stream,TRYVITRESORCIES
		try (BufferedReader vstup = new BufferedReader(new FileReader(SOUBOR))){
			String radek;
			for(int i = 0; i<Obrazek.getSize(); i++){
				if((radek = vstup.readLine()) != null){
					zpracujRadek(radek);
				}
					
			}
			//pouzijeme stream
		} catch (IOException e) {
			System.out.println("Pri cteni CSV doslo k chybe: "+e.getMessage());
		}
		//uzavreme stream-vyresi java za nas sama
	}
	
	private void zpracujRadek(String radek) {
		//TVaR V csv
		//hrac;url
		//zed;url
		//...
		
		//soubor mze obsahovat vice radku, ty ALE cist nebudeme
		StringTokenizer st = new StringTokenizer(radek, ODDELOVAC);
		if(st.countTokens()== 2){//cteme jen dva prvni tokeny
			String prvek = st.nextToken();//hrac;
			String odkaz = st.nextToken();//url;
			
			if(jePrvekVSeznamu(prvek)){
				getMapa().put(prvek, odkaz);
				
			}else{
				System.out.println("Prvek "+prvek+" není v seznamu prvku");
			}
			
		}
	}

	private boolean jePrvekVSeznamu(String prvek) {
		for(Obrazek o: Obrazek.getObrazky()){
			if(o.getKlic().equals(prvek)){
				return true;
				
			}
			
		}
		return false;
	}

	@Override
	public BufferedImage getObrazek()throws IOException{
		URL url = new URL(getZdroj());
		URLConnection urlSpojeni = url.openConnection();
		
		urlSpojeni.setReadTimeout(3000);//3sekundy cti, pak prestan
		InputStream is = urlSpojeni.getInputStream();
		
		BufferedImage img = ImageIO.read(is);
		
		is.close();
		
		return img;
		}
}
