package dblayer;

public class DBInit
{

    public void createTables()
    {
    	//create new table for the keygenerator
    	ConnectionManager.processSql("Admin", "create table keygenerator ("+
				"ID  BIGINT(38) not null," +
				"CONSTRAINT PRIMKEY_KEYGEN PRIMARY KEY (ID))");
    	ConnectionManager.processSql("Admin","INSERT INTO keygenerator (id) VALUES (1)");
    	
    	//create new "nested" tables, Stefan:02.06.07
    	ConnectionManager.processSql("Admin", "create table NFKTNESTED ("+
    							"TERMIN_ID  BIGINT(38) not null," +
    							"NOTIF_ID   BIGINT(38) not null," +
    							"CONSTRAINT PRIMKEY_NFKT PRIMARY KEY (TERMIN_ID, NOTIF_ID))");
    	
    	
    	ConnectionManager.processSql("Admin", "create table GROUPNESTED ("+
				"GROUP_ID         BIGINT(38) not null," +
				"GROUP_ID_CHILD   BIGINT(18) not null," +
				"CONSTRAINT PRIMKEY_GROUP PRIMARY KEY (GROUP_ID, GROUP_ID_CHILD))");
    	
    	ConnectionManager.processSql("Admin", "create table PERSONNESTED ("+
				"GROUP_ID         BIGINT(38) not null," +
				"PERSON_ID		  BIGINT(38) not null," +
				"CONSTRAINT PRIMKEY_PERSON PRIMARY KEY (GROUP_ID, PERSON_ID))");
    	
    	ConnectionManager.processSql("Admin", "create table TEILNEHMERNESTED ("+
    			"TERMIN_ID  BIGINT(38) not null," +
				"TEILNEHMER_ID BIGINT(38) not null," +
				"NFKT VARCHAR(3) ," + //this attribute can be "null", so "not null" cannot be used
				"CONSTRAINT PRIMKEY_TEILNEHMER PRIMARY KEY (TERMIN_ID, TEILNEHMER_ID))");
    	
            
    	
    	//create "standard" tables.
    	ConnectionManager.processSql("Admin","create table Person ("+
                               "id  BIGINT(38),"+
                               "kuerzel   VARCHAR(5),"+
                               "nachname VARCHAR(20),"+
                               "vorname  VARCHAR(20),"+
                               "email    VARCHAR(50),"+
                               "fax      VARCHAR(20),"+
                               "passwd   VARCHAR(8),"+
                               "vorzugsNfkt  BIGINT(1))");
            
            ConnectionManager.processSql("Admin","create table Eintragstyp ("+
                               "id   BIGINT(38) primary key,"+
                               "besitzer  BIGINT(38),"+
                               "farbe  BIGINT(38),"+
                               "bezeichnung VARCHAR(200))");
            //VARCHAR(200) because of errors with string length

            ConnectionManager.processSql("Admin","create table Gruppe ("+
                               "id   BIGINT(38) primary key,"+
                               "owner  BIGINT(38),"+
                               "admin  BIGINT(1),"+
                               "kuerzel  VARCHAR(8),"+
                               "name  VARCHAR(40))");

           
            ConnectionManager.processSql("Admin","create table ToDo ("+
                               "id  BIGINT(38)   primary key,"+
                               "besitzer  BIGINT(38),"+
                               "beginn  BIGINT(38),"+
                               "erinnernAb  BIGINT(38),"+
                               "kurztext  VARCHAR(20),"+
                               "langtext VARCHAR(200),"+
                               "ort  VARCHAR(30),"+
                               "hyperlink  VARCHAR(60),"+
                               "eintragstyp  BIGINT(38),"+
                               "serie  BIGINT(38))");

            ConnectionManager.processSql("Admin","create table Termin ("+
                               "id  BIGINT(38)   primary key,"+
                               "besitzer  BIGINT(38),"+
                               "beginn  BIGINT(38),"+
                               "ende BIGINT(38),"+
                               "kurztext  VARCHAR(20),"+
                               "langtext VARCHAR(200),"+
                               "ort  VARCHAR(30),"+
                               "hyperlink  VARCHAR(60),"+
                               "eintragstyp  BIGINT(38),"+
                               "verschiebbar  BIGINT(1),"+
                               "serie  BIGINT(38))");
            
            ConnectionManager.processSql("Admin","create table Antwort ("+
            					"id BIGINT(38) primary key,"+
								"besitzer BIGINT(38),"+
								"terminid BIGINT(38),"+
								"nachricht VARCHAR(60))");

             ConnectionManager.processSql("Admin","create table Serie ("+
                                "id   BIGINT(38)  primary key,"+
                                "beginn  BIGINT(38),"+
                                "ende   BIGINT(38),"+
                                "typ  BIGINT(3),"+
                                "frequenz BIGINT(3),"+
                                "werktags BIGINT(1))");

             ConnectionManager.processSql("Admin","create table Notifikation ("+
                                "id  BIGINT(38)  primary key,"+
                                "zeit  BIGINT(3),"+
                                "typ  BIGINT(3),"+
                                "zeitEH  BIGINT(3),"+
                                "erledigt BIGINT(1))");

             ConnectionManager.processSql("Admin","create table Rechte ("+
                                          "sender  BIGINT(38),"+
                                          "rec_person  BIGINT(38),"+
                                          "rec_gruppe  BIGINT(38),"+
                                          "recht  BIGINT(1),"+
                                          "eintragstyp  BIGINT(38))");

             ConnectionManager.processSql("Admin","create table CalendariumEvent ("+
                                          "eventID   BIGINT(1),"+
                                          "sender  BIGINT(38),"+
                                          "empfaenger BIGINT(38),"+
                                          "timestamp  VARCHAR(20),"+
                                          "title VARCHAR(50),"+
                                          "message  VARCHAR(400),"+
                                          "termin  BIGINT(38),"+
                                          "todo BIGINT(38),"+
                                          "delivery BIGINT(2),"+
                                          "showing  BIGINT(1))");

             //by johnny war auskommentiert
             ConnectionManager.processSql("Admin","create table Feiertag ("+
                                "id   BIGINT(38)  primary key,"+
                                "datum  BIGINT(38),"+
                                "bezeichnung VARCHAR(200))");
             //VARCHAR(200) because of errors with string length

             ConnectionManager.processSql("Admin","create table Calendarium ("+
                                           "classname  VARCHAR(20),"+
                                           "tablename  VARCHAR(20),"+
                                           "attribute  VARCHAR(20),"+
                                           "col     VARCHAR(20),"+
                                           "`key`   BIGINT(1),"+
                                           "nested  BIGINT(1),"+
                                           "nestedclass  VARCHAR(20))");
    
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"kuerzel"+"','"+"kuerzel"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"nachname"+"','"+"nachname"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"vorname"+"','"+"vorname"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"email_addr"+"','"+"email"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"faxnr"+"','"+"fax"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"vorzugsNfkt"+"','"+"vorzugsNfkt"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Person"+"','"+"Person"+"','"+"passwd"+"','"+"passwd"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"kuerzel"+"','"+"kuerzel"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"name"+"','"+"name"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"owner"+"','"+"owner"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"gruppen"+"','"+"gruppen"+"',0,1,'"+"GroupNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"personen"+"','"+"personen"+"',0,1,'"+"PersonNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Gruppe"+"','"+"Gruppe"+"','"+"admin"+"','"+"admin"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"owner"+"','"+"besitzer"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"nfktRelevant"+"','"+"beginn"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"ende"+"','"+"ende"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"kurzText"+"','"+"kurztext"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"langText"+"','"+"langtext"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"ort"+"','"+"ort"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"hyperlink"+"','"+"hyperlink"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"typ"+"','"+"eintragstyp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"nfkt"+"','"+"nfkt"+"',0,1,'"+"NfktNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"teilnehmer"+"','"+"teilnehmer"+"',0,1,'"+"TeilnehmerNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"serie"+"','"+"serie"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Termin"+"','"+"Termin"+"','"+"verschiebbar"+"','"+"verschiebbar"+"',0,0,null)");


             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"owner"+"','"+"besitzer"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"nfktRelevant"+"','"+"beginn"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"erinnernAb"+"','"+"erinnernAb"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"kurzText"+"','"+"kurztext"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"langText"+"','"+"langtext"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"ort"+"','"+"ort"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"hyperlink"+"','"+"hyperlink"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"typ"+"','"+"eintragstyp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"nfkt"+"','"+"nfkt"+"',0,1,'"+"NfktNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"teilnehmer"+"','"+"teilnehmer"+"',0,1,'"+"TeilnehmerNested"+"')");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDo"+"','"+"ToDo"+"','"+"serie"+"','"+"serie"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"GroupNested"+"',null,'"+"objectIdentifier"+"','"+"id"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"PersonNested"+"',null,'"+"objectIdentifier"+"','"+"id"+"',0,0,null)");


             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktNested"+"',null,'"+"objectIdentifier"+"','"+"id"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Teilnehmer"+"','"+"Teilnehmer"+"','"+"nfkt"+"','"+"nfkt"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TeilnehmerNested"+"',null,'"+"objectIdentifier"+"','"+"id"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TeilnehmerNested"+"',null,'"+"nfkt"+"','"+"nfkt"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"EintragsTyp"+"','"+"EintragsTyp"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"EintragsTyp"+"','"+"EintragsTyp"+"','"+"farbe"+"','"+"farbe"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"EintragsTyp"+"','"+"EintragsTyp"+"','"+"bezeichnung"+"','"+"bezeichnung"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"EintragsTyp"+"','"+"EintragsTyp"+"','"+"owner"+"','"+"besitzer"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"beginn"+"','"+"beginn"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"ende"+"','"+"ende"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"typ"+"','"+"typ"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"frequenz"+"','"+"frequenz"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Serie"+"','"+"Serie"+"','"+"werktags"+"','"+"werktags"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Notifikation"+"','"+"Notifikation"+"','"+"objectIdentifier"+"','"+"id"+"',1,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Notifikation"+"','"+"Notifikation"+"','"+"typ"+"','"+"typ"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Notifikation"+"','"+"Notifikation"+"','"+"zeit"+"','"+"zeit"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Notifikation"+"','"+"Notifikation"+"','"+"zeitEH"+"','"+"zeitEH"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Notifikation"+"','"+"Notifikation"+"','"+"erledigt"+"','"+"erledigt"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Feiertag"+"','"+"Feiertag"+"','"+"objectIdentifier"+"','"+"id"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Feiertag"+"','"+"Feiertag"+"','"+"date"+"','"+"datum"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Feiertag"+"','"+"Feiertag"+"','"+"bezeichnung"+"','"+"bezeichnung"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Rechte"+"','"+"Rechte"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Rechte"+"','"+"Rechte"+"','"+"receiver_person"+"','"+"rec_person"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Rechte"+"','"+"Rechte"+"','"+"receiver_gruppe"+"','"+"rec_gruppe"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Rechte"+"','"+"Rechte"+"','"+"rechtsIndex"+"','"+"recht"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"Rechte"+"','"+"Rechte"+"','"+"eintragsTyp"+"','"+"eintragsTyp"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"title"+"','"+"title"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"termin"+"','"+"termin"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"toDo"+"','"+"toDo"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"delivery"+"','"+"delivery"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"showing"+"','"+"showing"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"message"+"','"+"message"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"CalendariumEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"AdminEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"AdminEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"AdminEvent"+"','"+"CalendariumEvent"+"','"+"message"+"','"+"message"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"AdminEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"AdminEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");


             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"message"+"','"+"message"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"MessageEvent"+"','"+"CalendariumEvent"+"','"+"title"+"','"+"title"+"',0,0,null)");


             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"title"+"','"+"title"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"termin"+"','"+"termin"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"toDo"+"','"+"toDo"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"delivery"+"','"+"delivery"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"showing"+"','"+"showing"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"message"+"','"+"message"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"NfktEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");

             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TerminEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TerminEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TerminEvent"+"','"+"CalendariumEvent"+"','"+"termin"+"','"+"termin"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TerminEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"TerminEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");

            ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDoEvent"+"','"+"CalendariumEvent"+"','"+"sender"+"','"+"sender"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDoEvent"+"','"+"CalendariumEvent"+"','"+"empfänger"+"','"+"empfaenger"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDoEvent"+"','"+"CalendariumEvent"+"','"+"toDo"+"','"+"todo"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDoEvent"+"','"+"CalendariumEvent"+"','"+"timeStamp"+"','"+"timestamp"+"',0,0,null)");
             ConnectionManager.processSql("Admin","insert into Calendarium values ("+
                                          "'"+"ToDoEvent"+"','"+"CalendariumEvent"+"','"+"eventID"+"','"+"eventID"+"',0,0,null)");

             ConnectionManager.commit("Admin");

    }

    public void deleteTables()
    {		
    		//delete new table for the keygenerator
    		ConnectionManager.processSql("Admin","drop table keygenerator");
    		//delete new "nested" tables, Stefan:02.06.07
    		ConnectionManager.processSql("Admin","drop table groupnested");
    		ConnectionManager.processSql("Admin","drop table nfktnested");
    		ConnectionManager.processSql("Admin","drop table personnested");
    		ConnectionManager.processSql("Admin","drop table teilnehmernested");
    		//delete "standard" tables
            ConnectionManager.processSql("Admin","drop table Person");
            ConnectionManager.processSql("Admin","drop table Gruppe");
            ConnectionManager.processSql("Admin","drop table Eintragstyp");
            ConnectionManager.processSql("Admin","drop table Serie");
            ConnectionManager.processSql("Admin","drop table Notifikation");
            ConnectionManager.processSql("Admin","drop table Termin");
            ConnectionManager.processSql("Admin","drop table ToDo");
            ConnectionManager.processSql("Admin","drop table Antwort");

            ConnectionManager.processSql("Admin","drop type person_tab");
            ConnectionManager.processSql("Admin","drop type person_typ");

            ConnectionManager.processSql("Admin","drop type gruppe_tab");
            ConnectionManager.processSql("Admin","drop type gruppe_typ");

            ConnectionManager.processSql("Admin","drop type nfkt_tab");
            ConnectionManager.processSql("Admin","drop type nfkt_typ");

            ConnectionManager.processSql("Admin","drop type teilnehmer_tab");
            ConnectionManager.processSql("Admin","drop type teilnehmer_typ");

            ConnectionManager.processSql("Admin","drop table rechte");

            ConnectionManager.processSql("Admin","drop table feiertag");//by johnny, war auskommentiert
            ConnectionManager.processSql("Admin","drop table CalendariumEvent");
            ConnectionManager.processSql("Admin","drop table Calendarium");

            ConnectionManager.commit("Admin");


    }


}