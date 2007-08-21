package dblayer;       //
/////////////////////////

import java.util.*;
import java.sql.*;


public class ClassMappings
{
    private static Hashtable classMap = new Hashtable();
    /* Stefan: 
     * classMap ist eine Liste, welche die unterschiedlichen ClassMap-Objekte
     * enthält. !! Kleinschrift = konkrete Liste, Großschrift= Klasse
     * Anmerkung: Besser wäre ein Name wie myclassMap oder dbclassMap 
     * 
     * Die Classmaps sorgen für eine Übersetzung zwischen Java-Objekt
     * - Bezeichnungen und DB-Namen:
     * DB-Tabelle : Tablename >>String	
     * Java-Object: Classname >>String
     * 
     * DB-Spalte    : Col
     * Java-Attribut: attribute >>Map
     * 
     * !! Da ja inner-Table, so gibt es mehrere Daten in einer DB-Spalte.
     *    deshalb werden diese Daten in einer Map gespeichert.
     * ToDo:Please take a dictionary and translate this into english.
     */
    /** return ClassMap instance from given classname 'name'
      */
    public static ClassMap getClass(String name)
    {
        return (ClassMap)classMap.get(name);
    }

    /** fetch mapping from db and store it in corresponding instances of ClassMap
      */
    @SuppressWarnings("unchecked")
	public static void fillMappings()
    {
        LogTool log1 = new LogTool("INFO", "ClassMappings");
        //log1.engageLogging();
        
    	ResultSet res,res1;
        ClassMap cm = null;
        String nestedClass, help = null;
        boolean key = false, isNested = false;

        if (!classMap.isEmpty()) return;   // classMap already filled
        try
        {
            ConnectionManager.databaseConnection("Admin");
            res = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE tablename IS NOT NULL ORDER BY classname");

            if (res == null)    // tables don't exist in db because no ResultSet came back.
            {
                DBInit dbInit = new DBInit();
                dbInit.createTables();
                res = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE tablename IS NOT NULL ORDER BY classname");
            }
            /* Stefan:
             * geht jetzt die einzelnen Elemente durch und erzeugt Classmaps für
             * die Klassen (Klassenname,Tabellenname und Attribute)
             */
            while (res.next())
            {   
            	log1.logStatement(res.getString("classname"));
            	/*Stefan:
            	 *tricky: das if hat 2 aufgaben:
            	 *1. erster Durchlauf: 1.if true. 2.if(cm !=null) false >>anlegen Klassen-Classmap
            	 *2. letzer Durchlauf einer Klasse: die Klassenklassmap der letzen (help) Klasse wird
            	 *   der globalen Map hinzugefügt. Dann wird für die neue Klasse
            	 *   eine Klassenmap angelegt. 
            	 */  
             if (!res.getString("classname").equals(help)) //prüfe ob aktuelle klasse != letzer Klasse
                {
                    if (cm != null) //Prüfe ob schon Classmap erzeugt, falls ja>weiter
                    {		
                    	log1.logStatement("Klasse "+ new String(help)+" erzeugt");
                    	//hinzufügen der Klassen-Klassmap in globale Klassmap.
                        classMap.put(new String(help),(ClassMap)cm);
                    }
                    //Klassen-Classmap noch nicht erzeugt. Initial anlegen.
                    cm = new ClassMap(res.getString("classname"),res.getString("tablename"));
                }
                //holen des Primärschlüssels(Spalte "key" in Tabelle Calendarium)
                key = (res.getInt("key") == 1);
                //holen des Wertes ob nested oder nicht
                isNested = (res.getInt("nested") == 1);
                //Erzeuge eine Map, die die Attribute enthält. Diese wird dann
                //zu den Attributliste der Klasse hinzugefügt.
                AttributeMap am = new AttributeMap(res.getString("attribute"),res.getString("col"),key,isNested);
                cm.setAttributes(am);
                /* Stefan:
                 * handelt es sich um eine nestedClass > mehrere Attribute, deshalb
                 * Schleife welche alle Attribute in noch eine Map speichert.
                 * Diese Map wird dann am Ende an die bereits existierende 
                 * als "nestedAttributes" (AttributeMap.java) angehängt.
                 */
                if (res.getString("nestedClass") != null)
                {   
                	log1.logStatement("nested");
                	//Hole den Klassennamen
                    nestedClass = res.getString("nestedClass");
                    //Hole alle Datensätze des Klassennamens (z.B. 'PersonNested').
                    res1 = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE classname = '"+nestedClass+"'");
                    while (res1.next())
                    {
                        AttributeMap aam = new AttributeMap(res1.getString("attribute"),res1.getString("col"));
                        am.setNestedAttribute(aam);
                    }
                    res1.close();
                }

                help = res.getString("classname");
            }
            // Die letzte Klasse abspeichern(da nicht in Schleife)
            classMap.put(help,(ClassMap)cm);

        }catch (Exception s){System.out.println(s);}
    }

}