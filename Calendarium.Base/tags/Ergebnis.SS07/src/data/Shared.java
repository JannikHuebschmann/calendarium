package data; //
////////////////

import java.awt.*;

public interface Shared
{
    /////////////////////////////////////////////////////////////////////////////////////////////
    // Variablen // Variablen // Variablen // Variablen // Variablen // Variablen // Variablen //
    /////////////////////////////////////////////////////////////////////////////////////////////

    final String RECHTE[] = {"Lesen mit Details", "Eintragsrecht"};
    final int LESERECHT = 0;
    final int EINTRAGSRECHT = 1;

    final String DAYNAMESLONG[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    final String DAYNAMESSHORT[] = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
    final String MONTHNAMESLONG[] = {"Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
    final Color PERSFARBEN[] = {new Color(0,255,128), new Color(0,128,255), new Color(255,128,128), new Color(255,0,255), new Color(0,140,70), new Color(0,63,125), new Color(196,0,0), new Color(134,0,134)};

    final Color HEADING_BACKGRD = Color.gray;                 // new Color(128, 255, 128);   // Überschrift
    final Color TAGE_BACKGRD = new Color(230, 230, 230);      // Tage
    final Color TAG_BACKGRD = new Color(230, 205, 160);       // Datumslabel
    final Color FEIERTAG_FOREGRD = Color.red;                 // Feiertag

    final String NFKT_TYP[] = {"Beep", "EMail", "Fax"};
    final int NFKT_BEEP = 0;
    final int NFKT_EMAIL = 1;
    final int NFKT_FAX = 2;

    final String ZE_TYP[] = {"Minute(n)", "Stunde(n)", "Tag(e)"};
    final int ZE_MINUTEN = 0;
    final int ZE_STUNDEN = 1;
    final int ZE_TAGE = 2;

    final String TIMETOKEN = ":";
    final String DATETOKEN = ".";
}