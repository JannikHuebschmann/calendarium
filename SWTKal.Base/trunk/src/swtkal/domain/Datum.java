/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			calproj			transfer out of the calendarium project
 *
 */
package swtkal.domain;

// diese Klasse wurde im Wesentlichen unverändert aus dem Calendarium-Projekt übernommen
// FIXME Klasse Datum enthält einige Fehler und muss überarbeitet werden

import java.util.*;
import java.io.Serializable;
import java.text.*;

/////////////////////////////////////////////////////////////////////////////////////////////////////
// Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum //
/////////////////////////////////////////////////////////////////////////////////////////////////////


/*****************************************************************************************************
 * This Datum ......
 * 
 * @author calendarium project
 */
public class Datum implements Serializable
{
   private static final long serialVersionUID = -8731434115896598180L;
   
   public static final String DAYNAMESLONG[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
   public static final String DAYNAMESSHORT[] = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
   public static final String MONTHNAMESLONG[] = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
   public static final String TIMETOKEN = ":";
   public static final String DATETOKEN = ".";

	private String date = "99.99.9999"; // Ensure date format: dd.mm.yyyy !!

	private String time = "99:99"; // Ensure time format: mm:hh      !!

	// Konstanten
	private static int URSPRUNG = 1990; // Montag, 01.01.1990

	private int TAG = 0, MONAT = 1, JAHR = 2;

	
	/**
	 */
	public Datum()
	{}

	
	/** Set the initial time or date
	 * 
	 * @param s is a string which contains a date or time
	 */
	public Datum(String s)
	{
		if (s.indexOf(TIMETOKEN) >= 0)
		{
			time = ensureTimeFormat(s);
		}
		else
		{
			date = ensureDateFormat(s);
		}
	}

	
	/** Set the initial time and date
	 * 
	 * No control if d = date or t = time
	 * 
	 * @param d	is a string which contains a date
	 * @param t is a string which contains a time
	 */
	public Datum(String d, String t)
	{
		date = ensureDateFormat(d);
		time = ensureTimeFormat(t);
	}

	
	/** Set the initial time and date out of a GregorianCalendar
	 * 
	 * @param cal is a GregorianCalendar date/time
	 * 
	 * @see java.util.GregorianCalendar
	 */
	public Datum(GregorianCalendar cal)
	{
		date = ensureDateFormat(cal.get(Calendar.DATE) + DATETOKEN
				+ (cal.get(Calendar.MONTH) + 1) + DATETOKEN
				+ cal.get(Calendar.YEAR));

		time = ensureTimeFormat(cal.get(Calendar.HOUR) + TIMETOKEN
				+ cal.get(Calendar.MINUTE));
	}

	
	/** Set the initial date and time out of a <code>Date</code>
	 * 
	 * @param d is a Date
	 */
	public Datum(Date d)
	{
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
		date = simpleDate.format(d);

		SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
		time = simpleTime.format(d);
	}

	// Datumsformat ueberpruefen
	private String ensureDateFormat(String date)
	{
		String[] dy = new String[3];
		StringTokenizer st = new StringTokenizer(date, DATETOKEN);

		for (int i = 0; i < 3; i++)
		{
			dy[i] = st.nextToken();
			if (dy[i].length() == 1)
				dy[i] = "0" + dy[i];
		}

		return dy[0] + DATETOKEN + dy[1] + DATETOKEN + dy[2];
	}

	// Zeitformat ueberpruefen
	private String ensureTimeFormat(String time)
	{
		String[] tm = new String[2];
		StringTokenizer st = new StringTokenizer(time, TIMETOKEN);

		for (int i = 0; i < 2; i++)
		{
			tm[i] = st.nextToken();
			if (tm[i].length() == 1)
				tm[i] = "0" + tm[i];
		}

		return tm[0] + TIMETOKEN + tm[1];
	}

	// Tag
	private int get(String date, int pos)
	{
		StringTokenizer st = new StringTokenizer(date, DATETOKEN);
		for (int i = 0; i < pos; i++)
		{
			st.nextToken();
		}

		return Integer.valueOf(st.nextToken()).intValue();
	}

	// Uhrzeit in Minuten
	private int getMinuten(String time)
	{
		int pos = time.indexOf(TIMETOKEN);

		return Integer.valueOf(time.substring(0, pos)).intValue()
				* 60
				+ Integer.valueOf(time.substring(pos + 1, time.length()))
						.intValue();
	}

	// Tage seit 1.1.1990
	private int getDaysSinceUrsprung(String date)
	{
		int days = 0;
		int i;

		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		for (i = URSPRUNG; i < yr; i++)
		{
			days += isSchaltjahr(i) ? 366 : 365;
		}

		for (i = 1; i < mt; i++)
		{
			days += calcMonthDays(i, yr);
		}

		return days + get(date, TAG);
	}

	// Tage seit 1.1.1990
	private static int getDaysSinceUrsprung(int dy, int mt, int yr)
	{
		int days = 0;
		int i;

		for (i = URSPRUNG; i < yr; i++)
		{
			days += isSchaltjahr(i) ? 366 : 365;
		}

		for (i = 1; i < mt; i++)
		{
			days += calcMonthDays(i, yr);
		}

		return days + dy;
	}

	// Ist Schaltjahr?
	private static boolean isSchaltjahr(int yr)
	{
		return yr % 4 == 0 && (yr % 100 != 0 || yr % 1000 == 0);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
	//////////////////////////////////////////////////////////////////////////////////////

	// Tage eines Monats
	
	/** This function calcMonthDays returns the maximum days of this month in that year.
	 *
	 * @param mt 1 for January  till 12 for December
	 * @param yr for example 1982
	 * @return - the maximum days of this month in that year
	 */
	public static int calcMonthDays(int mt, int yr)
	{
		if ((mt == 4) || (mt == 6) || (mt == 9) || (mt == 11))
			return (30);
		else if (mt == 2)
		{
			if (isSchaltjahr(yr))
				return (29);
			else
				return (28);
		}
		else
			return (31);
	}

	/** The function getWeekDay returns the specific Day Mo-So
	 *
	 * @param dy The Day of the date
	 * @param mt The Month of the date
	 * @param yr The year of the date
	 * @return - one of <code>DAYNAMESSHORT</code>
	 */
	public static int getWeekDay(int dy, int mt, int yr)
	{
		return (getDaysSinceUrsprung(dy, mt, yr) - 1) % 7;
	}

	public void setDatum(String date)
	{
		this.date = ensureDateFormat(date);
	}

	public void setDatum(String date, String time)
	{
		this.date = ensureDateFormat(date);
		this.time = ensureTimeFormat(time);
	}

	public void setDatum(Datum d)
	{
		this.date = d.getDate();
		this.time = d.getTime();
	}

	public void setDatum(Date d)
	{
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
		date = simpleDate.format(d);

		SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
		time = simpleTime.format(d);
	}

	public void setDatum(GregorianCalendar cal, boolean time)
	{
		this.date = ensureDateFormat(cal.get(Calendar.DATE) + DATETOKEN
				+ (cal.get(Calendar.MONTH) + 1) + DATETOKEN
				+ cal.get(Calendar.YEAR));
		if (time)
		{
			this.time = ensureTimeFormat(cal.get(Calendar.HOUR) + TIMETOKEN
					+ cal.get(Calendar.MINUTE));
		}
	}

	public void setToMonthBeginn()
	{
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		date = "01" + DATETOKEN + (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public void setToMonthEnd()
	{
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);
		int dy = calcMonthDays(mt, yr);

		date = dy + DATETOKEN + (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public long getDatum()
	{
		GregorianCalendar cal;
		if (time.equals("99:99"))
			cal = new GregorianCalendar(getYear(), getMonth() - 1, getDay());
		else
			cal = new GregorianCalendar(getYear(), getMonth() - 1, getDay(),
					getHour(), getMin());
		return cal.getTime().getTime();
	}

	public int getHour()
	{
		int pos = time.indexOf(TIMETOKEN);
		return Integer.valueOf(time.substring(0, pos)).intValue();
	}

	public int getMin()
	{
		int pos = time.indexOf(TIMETOKEN);
		return Integer.valueOf(time.substring(pos + 1, time.length())).intValue();
	}

	public int getDay()
	{
		int pos = date.indexOf(DATETOKEN);
		return Integer.valueOf(date.substring(0, pos)).intValue();
	}

	public int getWeekDay()
	{
		return (getDaysSinceUrsprung(date) - 1) % 7;
	}

	public int getWeek()
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(yr, mt - 1, dy);

		int week = cal.get(Calendar.WEEK_OF_YEAR);
		if (week == 0)
			week = 53;

		return week;
	}

	public int getMonth()
	{
		int pos1 = date.indexOf(DATETOKEN) + 1;
		int pos2 = date.indexOf(DATETOKEN, pos1);

		return Integer.valueOf(date.substring(pos1, pos2)).intValue();
	}

	public int getYear()
	{
		return get(date, JAHR);
	}

	public String getDate()
	{
		return date;
	}

	public String getTime()
	{
		return time;
	}

	public double getHoursBetween(Datum d)
	{
		String bgnDate = d.getDate();
		String bgnTime = d.getTime();
		double h = 0;

		if (!date.equals(bgnDate))
		{ // Tage dazwischen
			h = (getDaysSinceUrsprung(this.date) - getDaysSinceUrsprung(bgnDate)) * 1440;
		}

		return (h + getMinuten(this.time) - getMinuten(bgnTime)) / 60;
	}

	public int getDaysBetween(Datum d)
	{
		return getDaysSinceUrsprung(d.getDate())
				- getDaysSinceUrsprung(this.date);
	}

	// Korrekt?
	public boolean isCorrectDate()
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		if (dy != 99 && mt != 99 & yr != 9999 && dy > 0 && mt > 0 && yr > 0
				&& mt < 13 && dy <= calcMonthDays(mt, yr))
			return true;
		else
			return false;
	}

	// Korrekt?
	public boolean isCorrectTime()
	{
		if (getHour() < 24 && getMin() < 60)
			return true;
		else
			return false;
	}

	// Korrekt?
	public boolean isCorrect()
	{
		return isCorrectDate() && isCorrectTime();
	}

	public Datum addDauer(double dauer) // dauer in Stunden
	{
		int yr = URSPRUNG, mt = 1;
		int dy, hr, min, btr;

		double summe = getDaysSinceUrsprung(date) * 1440 + getMinuten(time)
				+ dauer * 60;

		// Jahr
		while ((btr = isSchaltjahr(yr) ? 366 * 1440 : 365 * 1440) <= summe - 1440)
		{
			summe -= btr;
			yr++;
		}

		// Monat
		while ((btr = calcMonthDays(mt, yr) * 1440) <= summe - 1440)
		{
			summe -= btr;
			mt++;
		}

		// Tag
		dy = (int) summe / 1440;
		summe -= dy * 1440;

		// Stunde
		hr = (int) summe / 60;
		summe -= hr * 60;

		// Minute
		min = (int) Math.rint(summe);

		return new Datum(String.valueOf(dy) + DATETOKEN + String.valueOf(mt)
				+ DATETOKEN + String.valueOf(yr), String.valueOf(hr) + TIMETOKEN
				+ String.valueOf(min));
	}

	public void add(int nr)
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);
		int monthDays = calcMonthDays(mt, yr);

		for (int i = 0; i < nr; i++)
		{
			if (dy == monthDays)
			{
				dy = 1;

				if (mt < 12)
				{
					mt++;

				}
				else
				{
					mt = 1;
					yr++;
				}

				monthDays = calcMonthDays(mt, yr);

			}
			else
				dy++;
		}

		date = (dy < 10 ? "0" + dy : "" + dy) + "."
				+ (mt < 10 ? "0" + mt : "" + mt) + "." + yr;
	}

	public void addMonth(int nr)
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		do
		{
			for (int i = 0; i < nr; i++)
			{
				if (mt > 12)
				{
					mt = 1;
					yr++;

				}
				else
					mt++;
			}
		}
		while (dy > calcMonthDays(mt, yr));

		date = (dy < 10 ? "0" + dy : "" + dy) + DATETOKEN
				+ (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public void addMonth(Datum vglDat, int nr)
	{
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);
		int dy = 1;

		int d = getDaysSinceUrsprung(vglDat.getDate()) - 1;

		int vglWoche = (int) (d / 7) - (int) ((d - vglDat.getDay()) / 7);
		int vglWoTag = d % 7;

		do
		{
			for (int i = 0; i < nr; i++)
			{
				if (mt > 12)
				{
					mt = 1;
					yr++;

				}
				else
					mt++;

				int woTag = (getDaysSinceUrsprung(dy, mt, yr) - 1) % 7;

				while (woTag % 7 != vglWoTag)
				{
					woTag++;
					dy++;
				}

				dy += (vglWoche - 1) * 7;
			}
		}
		while (dy > calcMonthDays(mt, yr));

		date = (dy < 10 ? "0" + dy : "" + dy) + DATETOKEN
				+ (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public void addYear(int nr)
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		do
		{
			for (int i = 0; i < nr; i++)
			{
				yr++;
			}
		}
		while (dy > calcMonthDays(mt, yr));

		date = (dy < 10 ? "0" + dy : "" + dy) + DATETOKEN
				+ (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public void substract(int nr)
	{
		int dy = get(date, TAG);
		int mt = get(date, MONAT);
		int yr = get(date, JAHR);

		for (int i = 0; i < nr; i++)
		{
			if (dy == 1)
			{
				if (mt == 1)
				{
					mt = 12;
					yr--;

				}
				else
				{
					mt--;
				}

				dy = calcMonthDays(mt, yr);

			}
			else
				dy--;
		}

		date = (dy < 10 ? "0" + dy : "" + dy) + DATETOKEN
				+ (mt < 10 ? "0" + mt : "" + mt) + DATETOKEN + yr;
	}

	public String getDayRelativ()
	{
		int dy = get(date, 0);
		int d = getDaysSinceUrsprung(date) - 1;

		int wo = (int) (d / 7) - (int) ((d - dy) / 7);
		int wTag = d % 7;

		return wo + ". " + DAYNAMESLONG[wTag];
	}

	public String toString()
	{
		return date + ", " + time;
	}

	public boolean equals(Datum d)
	{
		return date.equals(d.getDate()) && time.equals(d.getTime());
	}

	public int isGreater(Datum dtB)
	{
		int dyA = get(date, TAG);
		int mtA = get(date, MONAT);
		int yrA = get(date, JAHR);
		int hhA = getHour();
		int mmA = getMin();

		int dyB = get(dtB.getDate(), TAG);
		int mtB = get(dtB.getDate(), MONAT);
		int yrB = get(dtB.getDate(), JAHR);
		int hhB = dtB.getHour();
		int mmB = dtB.getMin();

		if (yrA > yrB)
		{
			return 1;
		}
		else
		{
			if (yrA == yrB)
			{
				if (mtA > mtB)
				{
					return 1;
				}
				else
				{
					if (mtA == mtB)
					{
						if (dyA > dyB)
						{
							return 1;
						}
						else
						{
							if (dyA == dyB)
							{
								if (hhA != 99 && hhB != 99)
								{
									if (hhA > hhB)
									{
										return 1;
									}
									else
									{
										if (hhA == hhB)
										{
											if (mmA > mmB)
											{
												return 1;
											}
											else
											{
												if (mmA == mmB)
												{
													return 0;
												}
											}
										}
									}
								}
								else
									return 0;
							}
						}
					}
				}
			}
		}

		return -1;
	}
}
