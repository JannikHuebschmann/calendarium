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
 *  01.03.2008			ejbUser			simplifying Datum by basing it on GregorianCalendar
 *
 */
package swtkal.domain;

import java.util.*;
import java.text.*;

/*****************************************************************************************************
 * This class represents date objects within the SWTKal application. It is realized as a direct
 * subclass of GregorianCalendar
 * 
 * @author ejbUser
 */
public class Datum extends GregorianCalendar
{
	private static final long serialVersionUID = 2558597652993498899L;
	
	public static final String DAYNAMESLONG[]   = { "Montag", "Dienstag", "Mittwoch", "Donnerstag",
		                                            "Freitag", "Samstag", "Sonntag" };
	public static final String DAYNAMESSHORT[]  = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
	public static final String MONTHNAMESLONG[] = { "Januar", "Februar", "März", "April", "Mai", "Juni",
		                                            "Juli", "August", "September", "Oktober", "November", "Dezember" };
	public static final String TIMETOKEN = ":";
	public static final String DATETOKEN = ".";
	
	protected static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	/** This function calcMonthDays returns the maximum days of this month in that year.
	 *
	 * @param mt 1 for January  till 12 for December
	 * @param yr for example 1982
	 * @return - the maximum days of this month in that year
	 */
	public static int calcMonthDays(int mt, int yr)
	{
		switch (mt)
		{
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				return new GregorianCalendar().isLeapYear(yr) ? 29 : 28;
			default:
				return 31;
		}
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
		GregorianCalendar cal = new GregorianCalendar(yr, mt, dy);
		return cal.get(GregorianCalendar.DAY_OF_WEEK);
	}

	/**
	 */
	public Datum()
	{
	}

	/** Set the initial time or date
	 * 
	 * @param s is a string which contains a date or time
	 * @throws ParseException 
	 */
	public Datum(String s)
	{
		super();
		
		try // DateFormat dd.MM.yyyy HH:mm
		{
			this.setTime(dateFormat.parse(s));
			return;
		} 
		catch (ParseException e)
		{
		}
		try // DateFormat dd.MM.yyyy
		{
			this.setTime(dateFormat.parse(s + " 00:00"));
			return;
		} 
		catch (ParseException e)
		{
			// XXX eigentlich sollte eine checked exception geworfen werden
			throw new RuntimeException(e);
		}
	}

	/** Set the initial time and date
	 * 
	 * No control if d = date or t = time
	 * 
	 * @param d	is a string which contains a date
	 * @param t is a string which contains a time
	 * @throws ParseException 
	 */
	public Datum(String d, String t)
	{
		this(d + " " + t);
	}

	/** Set the initial time and date out of a GregorianCalendar
	 * 
	 * @param cal is a GregorianCalendar date/time
	 * 
	 * @see java.util.GregorianCalendar
	 */
	public Datum(GregorianCalendar cal)
	{
		super();
		this.setTime(cal.getTime());
	}

	/** Set the initial date and time out of a <code>Date</code>
	 * 
	 * @param d is a Date
	 */
	public Datum(Date d)
	{
		super();
		this.setTime(d);
	}

	public void setDatum(String date)
	{
		try // DateFormat dd.MM.yyyy HH:mm
		{
			this.setTime(dateFormat.parse(date));
			return;
		} 
		catch (ParseException e)
		{
		}
		try // DateFormat dd.MM.yyyy
		{
			this.setTime(dateFormat.parse(date + " 00:00"));
		} 
		catch (ParseException e)
		{
			// XXX eigentlich sollte eine checked exception geworfen werden
			throw new RuntimeException(e);
		}
	}

	public void setDatum(String date, String time)
	{
		this.setDatum(date + " " + time);
	}

	public void setDatum(Datum d) throws ParseException
	{
		this.setDatum(d.toString());
	}

	public void setDatum(Date d)
	{
		this.setTime(d);
	}

	public void setDatum(GregorianCalendar cal, boolean time)
	{
		this.setTime(cal.getTime());
		if (!time) {
			this.set(GregorianCalendar.HOUR_OF_DAY, 0);
			this.set(GregorianCalendar.MINUTE, 0);
			this.set(GregorianCalendar.SECOND, 0);
			this.set(GregorianCalendar.MILLISECOND, 0);
		}
	}

	public void setToMonthBeginn()
	{
		this.set(this.get(GregorianCalendar.YEAR), this.get(GregorianCalendar.MONTH), 1, 0, 0, 0);
	}

	public void setToMonthEnd()
	{
		int year = this.get(GregorianCalendar.YEAR);
		int month = this.get(GregorianCalendar.MONTH);
		int day = calcMonthDays(month, year);
		this.set(year, month, day, 0, 0, 0);
	}

	public int getHour()
	{
		return this.get(GregorianCalendar.HOUR_OF_DAY);
	}

	public int getMin()
	{
		return this.get(GregorianCalendar.MINUTE);
	}

	public int getDay()
	{
		return this.get(GregorianCalendar.DAY_OF_MONTH);
	}

	public int getWeekDay()
	{
		return this.get(GregorianCalendar.DAY_OF_WEEK);
	}

	public int getWeek()
	{
		return this.get(GregorianCalendar.WEEK_OF_YEAR);
	}

	public int getMonth()
	{
		return this.get(GregorianCalendar.MONTH)+1;
	}

	public int getYear()
	{
		return this.get(GregorianCalendar.YEAR);
	}

	public String getDateStr()
	{
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(this.getTime());
	}

	public String getTimeStr()
	{
		DateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(this.getTime());
	}

	public double getHoursBetween(Datum d)
	{
		Date d1 = this.getTime();
		Date d2 = d.getTime();
		return Math.abs((d1.getTime() - d2.getTime()) / 1000 / 60);
	}

	public int getDaysBetween(Datum d)
	{
		Date d1 = super.getTime();
		Date d2 = super.getTime();
		return Math.abs((int) Math.floor(d1.getTime() - d2.getTime()) / 1000 / 60 / 24);
	}

	public Datum addDauer(double dauer) // dauer in Stunden
	{
		Datum t = new Datum(this);
		t.add(GregorianCalendar.MINUTE, (int) (dauer*60));
		return t;
	}

	public void add(int nr)
	{
		this.add(GregorianCalendar.DAY_OF_MONTH, nr);
	}

	public void addMonth(int nr)
	{
		this.add(GregorianCalendar.MONTH, nr);
	}

	public void addMonth(Datum vglDat, int nr)
	{
		this.setDatum(vglDat, false);
		this.add(GregorianCalendar.MONTH, nr);
	}

	public void addYear(int nr)
	{
		this.add(GregorianCalendar.YEAR, nr);
	}

	public void substract(int nr)
	{
		this.add(GregorianCalendar.DAY_OF_MONTH, -nr);
	}

	public String toString()
	{
		return getDateStr() + " " + getTimeStr();
	}

	public boolean equals(Datum d)
	{
		return this.getTime().equals(d.getTime());
	}

	public boolean isCorrect()
	{
		return isCorrectDate() && isCorrectTime();
	}

	// nichts mehr zu tun!
	public boolean isCorrectDate()
	{
		return true;
	}

	// nichts mehr zu tun!
	public boolean isCorrectTime()
	{
			return true;
	}

	public int isGreater(Datum dtB)
	{
		return this.getTime().compareTo(dtB.getTime());
	}
	
	public boolean isDateBefore(GregorianCalendar date) {
		Datum d1 = new Datum();
		d1.setDatum(this, false);
		Datum d2 = new Datum();
		d2.setDatum(date, false);
		return d1.before(d2);
	}
	
	public boolean isDateAfter(GregorianCalendar date) {
		Datum d1 = new Datum();
		d1.setDatum(this, false);
		Datum d2 = new Datum();
		d2.setDatum(date, false);
		return d1.after(d2);
	}
}
