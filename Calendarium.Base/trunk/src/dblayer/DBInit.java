package dblayer;

public class DBInit
{

    public void createTables()
    {

            ConnectionManager.processSql("Admin","create table Person ("+
                               "id  number(38),"+
                               "kuerzel   varchar2(5),"+
                               "nachname varchar2(20),"+
                               "vorname  varchar2(10),"+
                               "email    varchar2(50),"+
                               "fax      varchar2(20),"+
                               "passwd   varchar2(8),"+
                               "vorzugsNfkt  number(1))");

            ConnectionManager.processSql("Admin","create table Eintragstyp ("+
                               "id   number(38) primary key,"+
                               "besitzer  number(38),"+
                               "farbe  number(38),"+
                               "bezeichnung varchar2(20))");

            ConnectionManager.processSql("Admin","create type gruppe_typ as object (id  number(38))");

            ConnectionManager.processSql("Admin","create type gruppe_tab as table of gruppe_typ");


            ConnectionManager.processSql("Admin","create type nfkt_typ as object (id  number(38))");

            ConnectionManager.processSql("Admin","create type nfkt_tab as table of nfkt_typ");


            ConnectionManager.processSql("Admin","create type teilnehmer_typ as object ("+
                               "id  number(38),"+
                               "nfkt  varchar2(3))");

            ConnectionManager.processSql("Admin","create type person_typ as object (id  number(38))");
            ConnectionManager.processSql("Admin","create type person_tab as table of person_typ");

            ConnectionManager.processSql("Admin","create table Gruppe ("+
                               "id   number(38) primary key,"+
                               "owner  number(38),"+
                               "admin  number(1),"+
                               "kuerzel  varchar2(8),"+
                               "name  varchar2(40),"+
                               "gruppen  gruppe_tab default gruppe_tab(),"+
                               "personen person_tab default person_tab())"+
                               "nested table gruppen store as gruppentab "+
                               "nested table personen store as personentab");


            ConnectionManager.processSql("Admin","create type teilnehmer_tab as table of teilnehmer_typ");

            ConnectionManager.processSql("Admin","create table ToDo ("+
                               "id  number(38)   primary key,"+
                               "besitzer  number(38),"+
                               "beginn  number(19),"+
                               "erinnernAb  number(19),"+
                               "kurztext  varchar2(20),"+
                               "langtext varchar2(200),"+
                               "ort  varchar2(30),"+
                               "hyperlink  varchar2(60),"+
                               "eintragstyp  number(38),"+
                               "nfkt  nfkt_tab  default nfkt_tab(),"+
                               "teilnehmer teilnehmer_tab  default teilnehmer_tab(),"+
                               "serie  number(38) )"+
                               "nested table nfkt  store as nfkttab1 "+
                               "nested table teilnehmer store as teilnehmertab1");

            ConnectionManager.processSql("Admin","create table Termin ("+
                               "id  number(38)   primary key,"+
                               "besitzer  number(38),"+
                               "beginn  number(19),"+
                               "ende number(19),"+
                               "kurztext  varchar2(20),"+
                               "langtext varchar2(200),"+
                               "ort  varchar2(30),"+
                               "hyperlink  varchar2(60),"+
                               "eintragstyp  number(38),"+
                               "verschiebbar  number(1),"+
                               "nfkt  nfkt_tab  default nfkt_tab(),"+
                               "teilnehmer teilnehmer_tab  default teilnehmer_tab(),"+
                               "serie  number(38) )"+
                               "nested table nfkt  store as nfkttab2 "+
                               "nested table teilnehmer store as teilnehmertab2");
            
            ConnectionManager.processSql("Admin","create table Antwort ("+
            					"id number (38) primary key,"+
								"besitzer number (38),"+
								"terminid number(38),"+
								"nachricht varchar2(60),"+
								"nfkt nfkt_tab default nfkt_tab(),"+
								"teilnehmer teilnehmer_tab default teilnehmer_tab() )"+
								"nested table nfkt store as nfkttab3 "+
								"nested table teilnehmer store as teilnehmertab3");

             ConnectionManager.processSql("Admin","create table Serie ("+
                                "id   number(38)  primary key,"+
                                "beginn  number(19),"+
                                "ende   number(19),"+
                                "typ  number(3),"+
                                "frequenz number(3),"+
                                "werktags number(1))");

             ConnectionManager.processSql("Admin","create table Notifikation ("+
                                "id  number(38)  primary key,"+
                                "zeit  number(3),"+
                                "typ  number(3),"+
                                "zeitEH  number(3),"+
                                "erledigt number(1))");

             ConnectionManager.processSql("Admin","create table Rechte ("+
                                          "sender  number(38),"+
                                          "rec_person  number(38),"+
                                          "rec_gruppe  number(38),"+
                                          "recht  number(1),"+
                                          "eintragstyp  number(38))");

             ConnectionManager.processSql("Admin","create table CalendariumEvent ("+
                                          "eventID   number(1),"+
                                          "sender  number(38),"+
                                          "empfaenger number(38),"+
                                          "timestamp  varchar2(20),"+
                                          "title varchar2(50),"+
                                          "message  varchar2(400),"+
                                          "termin  number(38),"+
                                          "todo number(38),"+
                                          "delivery number(2),"+
                                          "showing  number(1))");

             //by johnny war auskommentiert
             ConnectionManager.processSql("Admin","create table Feiertag ("+
                                "id   number(38)  primary key,"+
                                "datum  number(19),"+
                                "bezeichnung varchar2(20))");

             ConnectionManager.processSql("Admin","create table Calendarium ("+
                                           "classname  varchar2(20),"+
                                           "tablename  varchar2(20),"+
                                           "attribute  varchar2(20),"+
                                           "col     varchar2(20),"+
                                           "key   number(1),"+
                                           "nested  number(1),"+
                                           "nestedclass  varchar2(20))");

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

             ConnectionManager.processSql("Admin","create table Keygenerator (help char(1))");
             ConnectionManager.processSql("Admin","insert into Keygenerator values('"+"X"+"')");

             ConnectionManager.processSql("Admin","create sequence sequenz start with 1 nomaxvalue");
             ConnectionManager.commit("Admin");

    }

    public void deleteTables()
    {
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

            ConnectionManager.processSql("Admin","drop sequence sequenz");
            ConnectionManager.processSql("Admin","drop table Keygenerator");

            ConnectionManager.commit("Admin");


    }


}