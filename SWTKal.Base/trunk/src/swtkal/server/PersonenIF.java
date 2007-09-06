package swtkal.server;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;

import java.util.Vector;

public interface PersonenIF
{
	public void insert(Person p, String passwort) throws PersonException;
	public void delete(Person p) throws PersonException;
	public void update(Person p) throws PersonException;
	public void update(Person p, String passwort) throws PersonException;
	
	public Person authenticate(String kuerzel, String passwort) throws PersonException;
	public Person find(String kuerzel) throws PersonException;
	public boolean contains(String kuerzel);

	public Vector<Person> getOrderedVector();
}