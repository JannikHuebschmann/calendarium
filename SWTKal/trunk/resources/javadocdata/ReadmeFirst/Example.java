/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		12.09.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	12.09.2007			Jan-Bernd		generate initially
 *
 */
package swtkal.server;
import java.lang.Object;

import swtkal.domain.Datum;
import swtkal.domain.Person;

/*****************************************************************************************************
 * This Example Demonstrate some Javadoc uses
 * 
 * For Example: This is a <b>grafik</b> comment.
 * 
 * <img src="{@docRoot}/../../grafik/swtkal.verteilungsdiagramm.gif"> -> bilder greifen auf /grafik/ zu
 * vorsicht da die bilder relativ vom javadoc dir verlinkt werden müssen.
 * evt ein übersichts diagramm einfügen womit die klasse zusammenhängt
 * 
 * 
 * @author Jan-Bernd
 * 
 */

public class Example 
{
	
	/** This one_null is a boolean which is use for ... 
	 * 
	 */
	boolean one_null;
	
	
	/** This status is a int which is use for ... 
	 * 
	 */
	int status;
	
	/** This var is a char which is use for.....
	 */
	char var;
	
	/** This fun is a long which is use for.....
	 */
	
	/** This fun is a long which is use for ... 
	 * 
	 */
	long fun;
	
	/** This shor is a byte which is use for...aktueller entry autognerate..
	 */
	byte shor;
	
	/** This text is a String which is use for.....
	 */
	String text = new String();
	
	/** This array is....
	 *
	 * array
	 * 
	 */
	int array[];
	/** This tabelle is....
	 *
	 * tabelle
	 * 
	 */
	char tabelle[][];
	
	/**
	 * @param one_null	*Hier könnte eine Beschreibung stehen*
	 * @param status
	 * @param var
	 * @param fun
	 * @param shor		*Hier könnte eine Beschreibung stehen*
	 * @param text
	 * @param array
	 * @param tabelle
	 * 	
	 * @deprecated since today, replaced by {@link #Example(boolean, int, char, long, String, int[], char[][])} -> als Beipsiel verfasst
	 *
	 */
	public Example(boolean one_null, int status, char var, long fun, byte shor,
			String text, int[] array, char[][] tabelle) {
		super();
		this.one_null = one_null;
		this.status = status;
		this.var = var;
		this.fun = fun;
		this.shor = shor;
		this.text = text;
		this.array = array;
		this.tabelle = tabelle;
	}
	
	
	/** 
	 * @param one_null
	 * @param status
	 * @param var		*Hier könnte eine Beschreibung stehen*
	 * @param fun		*Hier könnte eine Beschreibung stehen*
	 * @param text
	 * @param array		*Hier könnte eine Beschreibung stehen*
	 * @param tabelle
	 * 
	 * @see #Example(boolean, int, char, long, byte, String, int[], char[][])
	 */
	public Example(boolean one_null, int status, char var, long fun, String text, int[] array, char[][] tabelle) 
	{
		super();
		this.one_null = one_null;
		this.status = status;
		this.var = var;
		this.fun = fun;
		this.text = text;
		this.array = array;
		this.tabelle = tabelle;
	}
	
	/**
	 * @return the one_null
	 */
	public boolean isOne_null() {
		return one_null;
	}
	/**
	 * @param one_null the one_null to set
	 */
	public void setOne_null(boolean one_null) {
		this.one_null = one_null;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the var
	 */
	public char getVar() {
		return var;
	}
	/**
	 * @param var the var to set
	 */
	public void setVar(char var) {
		this.var = var;
	}
	/**
	 * @return the fun
	 */
	public long getFun() {
		return fun;
	}
	/**
	 * @param fun the fun to set
	 */
	public void setFun(long fun) {
		this.fun = fun;
	}
	/**
	 * @return the shor
	 */
	public byte getShor() {
		return shor;
	}
	/**
	 * @param shor the shor to set
	 */
	public void setShor(byte shor) {
		this.shor = shor;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the array	*Hier könnte eine Beschreibung stehen*
	 */
	public int[] getArray() {
		return array;
	}
	/**
	 * @param array the array to set
	 */
	public void setArray(int[] array) {
		this.array = array;
	}
	/**
	 * @return the tabelle
	 */
	public char[][] getTabelle() {
		return tabelle;
	}
	/**
	 * @param tabelle the tabelle to set
	 */
	public void setTabelle(char[][] tabelle) {
		this.tabelle = tabelle;
	}
	
	/** This function create_a_table ....
	 *
	 * @param table		*Hier könnte eine Beschreibung stehen*
	 * @param length
	 * @param range
	 * @return boolean 	*Hier könnte eine Beschreibung stehen*
	 */
	
	/** This function create_a_table ....
	 *
	 * @param table
	 * @param length
	 * @param range
	 * @return false for evertytime
	 */
	public boolean create_a_table(int table[][], int length, int range)
	{
		for(int i = 0, k = 0; (i + k) < length ; i++,k++ )
			table[i][k]= i*k;		
		return false;
	}
}
