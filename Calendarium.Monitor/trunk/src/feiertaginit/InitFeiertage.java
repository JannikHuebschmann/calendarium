package feiertaginit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


import basisklassen.Feiertag;
import data.Data;

/**
 * @author MartiniDestroyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InitFeiertage {

	public InitFeiertage(){
		initdb();
	}
	
	private void initdb() {
		File feiertagfile=new File("feiertaginit/feiertage.txt");
		
		String line;
		int tag;
		int monat;
		int jahr;

		String bezeichnung;
		StringTokenizer token;
		StringTokenizer datetoken;
		String datum;
		BufferedReader buffer;
		
		GregorianCalendar date;
		Feiertag feiertag;
		
		try {
			buffer = new BufferedReader(new FileReader(feiertagfile));
			while((line=buffer.readLine())!=null){
				bezeichnung="";
				token=new StringTokenizer(line);
				datum=token.nextToken();
				datetoken=new StringTokenizer(datum, ".");
				tag=Integer.valueOf(datetoken.nextToken()).intValue();
				monat=Integer.valueOf(datetoken.nextToken()).intValue();
				jahr=Integer.valueOf(datetoken.nextToken()).intValue();
				//while weil es bezeichnungen mit mehreren wörtern gibt "Heilige drei Könige"
				while(token.hasMoreElements()){
					bezeichnung+=token.nextToken()+" ";
				}
				
				
				date=new GregorianCalendar(jahr,monat-1,tag);
				feiertag=new Feiertag(date,bezeichnung);
				
				Data.feiertage.create(feiertag);

				System.out.println("eingefuegt: "+line);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
