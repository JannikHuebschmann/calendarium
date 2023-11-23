
//Termin erstellen ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      
function createformpruefen(status)
/*
"status" gibt an ob die Funktion eine Alert-Ausgabe machen soll oder nicht,
status == 0 dann werden Alert-Ausgaben gemacht
*/
{

var Monat;
var Jahr;

//Anfang: Vervollständigung und Normierung

/* Hier wird bei der Eingabe geguckt,
 ob es sich um eine gültige Eingabe handelt (keine Kalenderfunktion),
 handelt es sich nicht um eine gültige Eingabe,
 so wird der Focus wieder an die Stelle gesetzt.
 Bei einer Eingabe einer einstelligen Zahl,
 wird eine Null oder z.B. fürs Jahr eine 200 davorgesetzt.
 
*/
	if(Number(document.createform.von_tag.value) < 10 && Number(document.createform.von_tag.value) > 0)
	{
	document.createform.von_tag.value = ("0" + Number(document.createform.von_tag.value));
	}
	else if(!(Number(document.createform.von_tag.value) < 32 && Number(document.createform.von_tag.value) > 0))
	{
	document.createform.von_tag.focus();
	return false;
	}
	
	if(Number(document.createform.von_mon.value) < 10 && Number(document.createform.von_mon.value) > 0)
	{
	document.createform.von_mon.value = ("0" + Number(document.createform.von_mon.value));
	}
	else if(!(Number(document.createform.von_mon.value) < 13 && Number(document.createform.von_mon.value) > 0))
	{
	document.createform.von_mon.focus();
	return false;
	}
	
	if(Number(document.createform.von_jahr.value) > 0)
	{
		if(Number(document.createform.von_jahr.value) < 10 )
		{
		document.createform.von_jahr.value = ("200" + Number(document.createform.von_jahr.value));
		}
		else if (Number(document.createform.von_jahr.value) < 100)
		{
		document.createform.von_jahr.value = ("20" + Number(document.createform.von_jahr.value));
		}
		else if (Number(document.createform.von_jahr.value) < 1000)
		{
		document.createform.von_jahr.value = ("2" + Number(document.createform.von_jahr.value));
		}
	}
	else if(!(Number(document.createform.von_jahr.value)<1000 && Number(document.createform.von_jahr.value)>0))
	{
	document.createform.von_jahr.focus();
	return false;
	}
	
	if(Number(document.createform.von_std.value) < 10 && Number(document.createform.von_std.value) > 0 && document.createform.von_std.value != "")
	{
	document.createform.von_std.value =  Number(document.createform.von_std.value);
	}
	else if(!(Number(document.createform.von_std.value) < 24 && document.createform.von_std.value != ""))
	{
	document.createform.von_std.focus();
	return false;
	}
	
	if(Number(document.createform.von_min.value) < 10 && Number(document.createform.von_min.value) >= 0 && document.createform.von_min.value != "" )
	{
	document.createform.von_min.value = ("0" + Number(document.createform.von_min.value));
	}
	else if(!(Number(document.createform.von_min.value) < 60 && document.createform.von_min.value != "" ))
	{
	document.createform.von_min.focus();
	return false;
	}
	



	if(Number(document.createform.bis_tag.value) < 10 && Number(document.createform.bis_tag.value) > 0)
	{
	document.createform.bis_tag.value = ("0" + Number(document.createform.bis_tag.value));
	}
	else if(!(Number(document.createform.bis_tag.value) < 32 && Number(document.createform.bis_tag.value) > 0))
	{
	document.createform.bis_tag.focus();
	return false;
	}
	
	if(Number(document.createform.bis_mon.value) < 10 && Number(document.createform.bis_mon.value) > 0)
	{
	document.createform.bis_mon.value = ("0" + Number(document.createform.bis_mon.value));
	}
	else if(!(Number(document.createform.bis_mon.value) < 13 && Number(document.createform.bis_mon.value) > 0))
	{
	document.createform.bis_mon.focus();
	return false;
	}
	
	if(Number(document.createform.bis_jahr.value) > 0)
	{
		if(Number(document.createform.bis_jahr.value) < 10 )
		{
		document.createform.bis_jahr.value = ("200" + Number(document.createform.bis_jahr.value));
		}
		else if (Number(document.createform.bis_jahr.value) < 100)
		{
		document.createform.bis_jahr.value = ("20" + Number(document.createform.bis_jahr.value));
		}
		else if (Number(document.createform.bis_jahr.value) < 1000)
		{
		document.createform.bis_jahr.value = ("2" + Number(document.createform.bis_jahr.value));
		}
	}
	else if(!(Number(document.createform.bis_jahr.value) < 3000 && Number(document.createform.bis_jahr.value) > 0))
	{
	document.createform.bis_jahr.focus();
	return false;
	}
	
	if(Number(document.createform.bis_std.value) < 10 && Number(document.createform.bis_std.value) >= 0 && document.createform.bis_std.value != "")
	{
	document.createform.bis_std.value =  Number(document.createform.bis_std.value);
	}
	else if(!(Number(document.createform.bis_std.value) < 24 && document.createform.bis_std.value != ""))
	{
	document.createform.bis_std.focus();
	return false;
	}
	
	if(Number(document.createform.bis_min.value) < 10 && Number(document.createform.bis_min.value) >= 0 && document.createform.bis_min.value != "")
	{
	document.createform.bis_min.value = ("0" + Number(document.createform.bis_min.value));
	}
	else if(!(Number(document.createform.bis_min.value) < 60 && document.createform.bis_min.value != ""))
	{
	document.createform.bis_min.focus();
	return false;
	}	
//Ende: Vervollständigung und Normierung
	
//Anfang: Ueberpruefung der Beschreibung und des Datums (mit Kalenderfunktion)
	
	if (document.createform.beschr.value == "" && (status==0))
	{
    	alert("Bitte eine Beschreibung eingeben!");
    	document.createform.beschr.focus();
   		return false;
    }
   

    var Stop = 31;
    Monat = Number(document.createform.von_mon.value);
    Jahr = Number(document.createform.von_jahr.value);
	if (Monat == 4 || Monat == 6 || Monat == 9 || Monat == 11)
    	--Stop;
	if (Monat == 2) 
	{
   		Stop = Stop - 3;
    	if (Jahr % 4 == 0)
      		Stop++;
    	if (Jahr % 100 == 0)
      		Stop--;
    	if (Jahr % 400 == 0)
      		Stop++;
    }

    if (!(Number(document.createform.von_tag.value) > 0 && Number(document.createform.von_tag.value) <= Stop))
   	{
    	if(status==0)
			alert("Bitte Tag richtig eingeben");
    	document.createform.von_tag.focus();
       	return false;
    } 

    if (!(Number(document.createform.von_mon.value) > 0 && Number(document.createform.von_mon.value) < 13))
   	{
   	    if(status==0)
    		alert("Bitte Monat richtig eingeben");
    	document.createform.von_mon.focus();
       	return false;
    } 

    if (!(Number(document.createform.von_jahr.value) > 2000 && Number(document.createform.von_jahr.value) < 2999))
   	{
   		if(status==0)
    		alert("Bitte Jahr richtig eingeben");
    	document.createform.von_jahr.focus();
    	return false;
    } 
    
    if (!(Number(document.createform.von_std.value) >= 0 && Number(document.createform.von_std.value) < 24))
   	{
   		if(status==0)
    		alert("Bitte Stunden richtig eingeben");
    	document.createform.von_std.focus();
    	return false;
    } 
       
    if (!(Number(document.createform.von_min.value) >= 0 && Number(document.createform.von_min.value) < 60))
   	{
    	if(status==0)
    		alert("Bitte Minuten richtig eingeben");
    	document.createform.von_min.focus();
    	return false;
    } 


    
    
    Stop = 31;
    Monat = Number(document.createform.bis_mon.value);
    Jahr = Number(document.createform.bis_jahr.value);
	if (Monat == 4 || Monat == 6 || Monat == 9 || Monat == 11)
    	--Stop;
	if (Monat == 2) 
	{
   		Stop = Stop - 3;
    	if (Jahr % 4 == 0)
      		Stop++;
    	if (Jahr % 100 == 0)
      		Stop--;
    	if (Jahr % 400 == 0)
      		Stop++;
    }
    
        if (!(Number(document.createform.bis_tag.value) > 0 && Number(document.createform.bis_tag.value) <= Stop))
   	{
    	if(status==0)
    		alert("Bitte Tag richtig eingeben");
    	document.createform.bis_tag.focus();
    	return false;
    } 

    if (!(Number(document.createform.bis_mon.value) > 0 && Number(document.createform.bis_mon.value) < 13))
   	{
    	if(status==0)
    		alert("Bitte Monat richtig eingeben");
    	document.createform.bis_mon.focus();
    	return false;
    } 

    if (!(Number(document.createform.bis_jahr.value) > 2000 && Number(document.createform.bis_jahr.value) < 2999))
   	{
    	if(status==0)
    		alert("Bitte Jahr richtig eingeben");
    	document.createform.bis_jahr.focus();
    	return false;
    }
     
    if (!(Number(document.createform.bis_std.value) >= 0 && Number(document.createform.bis_std.value) < 24))
   	{
    	if(status==0)
    		alert("Bitte Stunden richtig eingeben");
    	document.createform.bis_std.focus();
    	return false;
    } 
      
    if (!(Number(document.createform.bis_min.value) >= 0 && Number(document.createform.bis_min.value) < 60))
   	{
    	if(status==0)
    		alert("Bitte Minuten richtig eingeben");
    	document.createform.bis_min.focus();
    	return false;
    } 
//Ende: Ueberpruefung der Beschreibung und des Datums (mit Kalenderfunktion)

//Anfang: Ueberpruefung Startdatum/-uhrzeit vor Enddatum/-uhrzeit
    
    if (Number(document.createform.von_jahr.value) > Number(document.createform.bis_jahr.value ))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist das Jahr größer als beim Ende!");
    	}	
    		document.createform.von_jahr.focus();
    	
    	return false;
    }
    else if((Number(document.createform.von_jahr.value) ==  Number(document.createform.bis_jahr.value)) 
    && (Number(document.createform.von_mon.value) >  Number(document.createform.bis_mon.value )))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist der Monat größer als beim Ende!");
    	}	
    		document.createform.von_mon.focus();
    	
    	return false;
    }
    else if((Number(document.createform.von_jahr.value) ==  Number(document.createform.bis_jahr.value))
    &&(Number(document.createform.von_mon.value) ==  Number(document.createform.bis_mon.value) )
    && (Number(document.createform.von_tag.value) >  Number(document.createform.bis_tag.value)))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist der Tag größer als beim Ende!");
    	}
    		document.createform.von_tag.focus();
    	
    	return false;
    }
    else if((Number(document.createform.von_jahr.value) ==  Number(document.createform.bis_jahr.value))
    &&(Number(document.createform.von_mon.value) ==  Number(document.createform.bis_mon.value) )
    &&(Number(document.createform.von_tag.value) ==  Number(document.createform.bis_tag.value) )
    && (Number(document.createform.von_std.value) >  Number(document.createform.bis_std.value )))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn sind die Stunden größer als beim Ende!");
    	}	
    		document.createform.von_std.focus();
    	
    	return false;
    }
    else if((Number(document.createform.von_jahr.value) ==  Number(document.createform.bis_jahr.value))
    &&(Number(document.createform.von_mon.value) ==  Number(document.createform.bis_mon.value) )
    &&(Number(document.createform.von_tag.value) ==  Number(document.createform.bis_tag.value) )
    &&(Number(document.createform.von_std.value) ==  Number(document.createform.bis_std.value) )
    && (Number(document.createform.von_min.value) >  Number(document.createform.bis_min.value) ))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn sind die Minuten größer als beim Ende!");
    	}
    		document.createform.von_min.focus();
    	
    	return false;
    }
 //Ende: Ueberpruefung Startdatum/-uhrzeit vor Enddatum/-uhrzeit   
 
    return true; //Eingabe war richtig!!
}



function createerrechnedauer()
{
/* Hier wird die Dauer vom Startdatum/-uhrzeit bis zum Enddatum/-uhrzeit berechnet (mit Kalenderfunktion)
und als Tage, Stunden und Minuten ausgegeben.
*/

	
	if(createformpruefen(1)==true)
	{
		var jahr;
		var mon;
		var tag;

		var Stop;
		
		var tag_erg;
		var std_erg;
		var min_erg;
		
		jahr = Number(document.createform.von_jahr.value)
		mon = Number(document.createform.von_mon.value)
		tag = Number(document.createform.von_tag.value)
		
		jahr_end = Number(document.createform.bis_jahr.value)
		mon_end = Number(document.createform.bis_mon.value)
		tag_end = Number(document.createform.bis_tag.value)
		
		tag_erg = 0;
		std_erg = 0;
		min_erg = 0;
		
		while(1)
		{
			if(tag == tag_end && mon == mon_end && jahr == jahr_end)
    				break;
    				
				Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
    			
    			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag = 1;
    				mon++;
       			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
    			
    			tag_erg++;

		}
		
		
		if(Number(document.createform.von_std.value) <= Number(document.createform.bis_std.value))
			std_erg = Number(document.createform.bis_std.value) - Number(document.createform.von_std.value);
		else
		{
			std_erg = 24 - Number(document.createform.von_std.value) + Number(document.createform.bis_std.value);
			tag_erg--;
		}
			
		if(Number(document.createform.von_min.value) <= Number(document.createform.bis_min.value))
			min_erg = Number(document.createform.bis_min.value) - Number(document.createform.von_min.value);
		else
		{
			min_erg = 60 - Number(document.createform.von_min.value) + Number(document.createform.bis_min.value);
			if(std_erg>0)
				std_erg--;
			else
			{
				std_erg=23;
				tag_erg--;
			}	
		}
				
		document.createform.dauer_tage.value = tag_erg;
		document.createform.dauer_std.value = std_erg;
		document.createform.dauer_min.value = min_erg;
	}
	else
	{
		document.createform.dauer_tage.value = "";
		document.createform.dauer_std.value = "";
		document.createform.dauer_min.value = "";
	}	
}

function createerrechneende()
/*
Diese Funktion fuellt das Enddatum/-Uhrzeit aus, wenn man bei der Dauer eine Zeit angibt.
(mit Kalenderfunktion)
*/
{
	
	var jahr;
	var mon;
	var tag;
	
	var std;
	var min;

	var Stop;
		
	var tag_erg;
	var std_erg;
	var min_erg;
		
	jahr = Number(document.createform.von_jahr.value);
	mon = Number(document.createform.von_mon.value);
	tag = Number(document.createform.von_tag.value);
	
	std = Number(document.createform.von_std.value);
	min = Number(document.createform.von_min.value);
	
	tag_erg	= Number(document.createform.dauer_tage.value);
	
	std_erg	= Number(document.createform.dauer_std.value);
	if(std_erg > 23)
	{
	document.createform.dauer_std.value = 23;
	std_erg =23;
	}
	
	min_erg	= Number(document.createform.dauer_min.value);
	if(min_erg > 59)
	{
	document.createform.dauer_min.value = 59;
	min_erg =59;
	}
	
	Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
			
	while(tag_erg)
		{

    				
				Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
    			
    			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
    			
    			tag_erg--;

		}
		
		
		if((24 - Number(document.createform.von_std.value)) > std_erg)
			std = Number(document.createform.von_std.value) + std_erg;
		else
		{
			std = std_erg - ( 24 - Number(document.createform.von_std.value));
			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
		}
			
		if((60 - Number(document.createform.von_min.value)) > min_erg)
			min = Number(document.createform.von_min.value) + min_erg;
		else
		{
			min = min_erg - ( 60 - Number(document.createform.von_min.value));
			if(std<23)
				std++;
			else
			{
				std=0;
				if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
				
			}
		}
		
	document.createform.bis_jahr.value = jahr;
	
	if(mon<10)
		document.createform.bis_mon.value = ("0" + mon);
	else
		document.createform.bis_mon.value = mon;
		
	if(tag<10)	
		document.createform.bis_tag.value = ("0" + tag);
	else
		document.createform.bis_tag.value = tag;
		
	document.createform.bis_std.value = std;
	
	if(min<10)
		document.createform.bis_min.value = ("0" + min);
	else
		document.createform.bis_min.value =  min;
}


//Termin aendern ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      
function changeformpruefen(status)
{
/*
"status" gibt an ob die Funktion eine Alert-Ausgabe machen soll oder nicht,
status == 0 dann werden Alert-Ausgaben gemacht
*/
var Monat;
var Jahr;

//Anfang Vervollständigung und Normierung

/* Hier wird bei der Eingabe geguckt,
 ob es sich um eine gültige Eingabe handelt (keine Kalenderfunktion),
 handelt es sich nicht um eine gültige Eingabe,
 so wird der Focus wieder an die Stelle gesetzt.
 Bei einer Eingabe einer einstelligen Zahl,
 wird eine Null oder z.B. fürs Jahr eine 200 davorgesetzt.
 
*/
	if(Number(document.changeform.von_tag.value) < 10 && Number(document.changeform.von_tag.value) > 0)
	{
	document.changeform.von_tag.value = ("0" + Number(document.changeform.von_tag.value));
	}
	else if(!(Number(document.changeform.von_tag.value) < 32 && Number(document.changeform.von_tag.value) > 0))
	{
	document.changeform.von_tag.focus();
	return false;
	}
	
	if(Number(document.changeform.von_mon.value) < 10 && Number(document.changeform.von_mon.value) > 0)
	{
	document.changeform.von_mon.value = ("0" + Number(document.changeform.von_mon.value));
	}
	else if(!(Number(document.changeform.von_mon.value) < 13 && Number(document.changeform.von_mon.value) > 0))
	{
	document.changeform.von_mon.focus();
	return false;
	}
	
	if(Number(document.changeform.von_jahr.value) > 0)
	{
		if(Number(document.changeform.von_jahr.value) < 10 )
		{
		document.changeform.von_jahr.value = ("200" + Number(document.changeform.von_jahr.value));
		}
		else if (Number(document.changeform.von_jahr.value) < 100)
		{
		document.changeform.von_jahr.value = ("20" + Number(document.changeform.von_jahr.value));
		}
		else if (Number(document.changeform.von_jahr.value) < 1000)
		{
		document.changeform.von_jahr.value = ("2" + Number(document.changeform.von_jahr.value));
		}
	}
	else if(!(Number(document.changeform.von_jahr.value)<1000 && Number(document.changeform.von_jahr.value)>0))
	{
	document.changeform.von_jahr.focus();
	return false;
	}
	
	if(Number(document.changeform.von_std.value) < 10 && Number(document.changeform.von_std.value) > 0 && document.changeform.von_std.value != "")
	{
	document.changeform.von_std.value =  Number(document.changeform.von_std.value);
	}
	else if(!(Number(document.changeform.von_std.value) < 24 && document.changeform.von_std.value != ""))
	{
	document.changeform.von_std.focus();
	return false;
	}
	
	if(Number(document.changeform.von_min.value) < 10 && Number(document.changeform.von_min.value) >= 0 && document.changeform.von_min.value != "" )
	{
	document.changeform.von_min.value = ("0" + Number(document.changeform.von_min.value));
	}
	else if(!(Number(document.changeform.von_min.value) < 60 && document.changeform.von_min.value != "" ))
	{
	document.changeform.von_min.focus();
	return false;
	}
	



	if(Number(document.changeform.bis_tag.value) < 10 && Number(document.changeform.bis_tag.value) > 0)
	{
	document.changeform.bis_tag.value = ("0" + Number(document.changeform.bis_tag.value));
	}
	else if(!(Number(document.changeform.bis_tag.value) < 32 && Number(document.changeform.bis_tag.value) > 0))
	{
	document.changeform.bis_tag.focus();
	return false;
	}
	
	if(Number(document.changeform.bis_mon.value) < 10 && Number(document.changeform.bis_mon.value) > 0)
	{
	document.changeform.bis_mon.value = ("0" + Number(document.changeform.bis_mon.value));
	}
	else if(!(Number(document.changeform.bis_mon.value) < 13 && Number(document.changeform.bis_mon.value) > 0))
	{
	document.changeform.bis_mon.focus();
	return false;
	}
	
	if(Number(document.changeform.bis_jahr.value) > 0)
	{
		if(Number(document.changeform.bis_jahr.value) < 10 )
		{
		document.changeform.bis_jahr.value = ("200" + Number(document.changeform.bis_jahr.value));
		}
		else if (Number(document.changeform.bis_jahr.value) < 100)
		{
		document.changeform.bis_jahr.value = ("20" + Number(document.changeform.bis_jahr.value));
		}
		else if (Number(document.changeform.bis_jahr.value) < 1000)
		{
		document.changeform.bis_jahr.value = ("2" + Number(document.changeform.bis_jahr.value));
		}
	}
	else if(!(Number(document.changeform.bis_jahr.value) < 3000 && Number(document.changeform.bis_jahr.value) > 0))
	{
	document.changeform.bis_jahr.focus();
	return false;
	}
	
	if(Number(document.changeform.bis_std.value) < 10 && Number(document.changeform.bis_std.value) >= 0 && document.changeform.bis_std.value != "")
	{
	document.changeform.bis_std.value =  Number(document.changeform.bis_std.value);
	}
	else if(!(Number(document.changeform.bis_std.value) < 24 && document.changeform.bis_std.value != ""))
	{
	document.changeform.bis_std.focus();
	return false;
	}
	
	if(Number(document.changeform.bis_min.value) < 10 && Number(document.changeform.bis_min.value) >= 0 && document.changeform.bis_min.value != "")
	{
	document.changeform.bis_min.value = ("0" + Number(document.changeform.bis_min.value));
	}
	else if(!(Number(document.changeform.bis_min.value) < 60 && document.changeform.bis_min.value != ""))
	{
	document.changeform.bis_min.focus();
	return false;
	}	
//Ende: Vervollständigung und Normierung	
	
	
//Anfang: Ueberpruefung der Beschreibung und Datums (mit Kalenderfunktion)
	
	if (document.changeform.beschr.value == "" && (status==0))
	{
    	alert("Bitte eine Beschreibung eingeben!");
    	document.changeform.beschr.focus();
   		return false;
    }
   

    var Stop = 31;
    Monat = Number(document.changeform.von_mon.value);
    Jahr = Number(document.changeform.von_jahr.value);
	if (Monat == 4 || Monat == 6 || Monat == 9 || Monat == 11)
    	--Stop;
	if (Monat == 2) 
	{
   		Stop = Stop - 3;
    	if (Jahr % 4 == 0)
      		Stop++;
    	if (Jahr % 100 == 0)
      		Stop--;
    	if (Jahr % 400 == 0)
      		Stop++;
    }

    if (!(Number(document.changeform.von_tag.value) > 0 && Number(document.changeform.von_tag.value) <= Stop))
   	{
    	if(status==0)
			alert("Bitte Tag richtig eingeben");
    	document.changeform.von_tag.focus();
       	return false;
    } 

    if (!(Number(document.changeform.von_mon.value) > 0 && Number(document.changeform.von_mon.value) < 13))
   	{
   	    if(status==0)
    		alert("Bitte Monat richtig eingeben");
    	document.changeform.von_mon.focus();
       	return false;
    } 

    if (!(Number(document.changeform.von_jahr.value) > 2000 && Number(document.changeform.von_jahr.value) < 2999))
   	{
   		if(status==0)
    		alert("Bitte Jahr richtig eingeben");
    	document.changeform.von_jahr.focus();
    	return false;
    } 
    
    if (!(Number(document.changeform.von_std.value) >= 0 && Number(document.changeform.von_std.value) < 24))
   	{
   		if(status==0)
    		alert("Bitte Stunden richtig eingeben");
    	document.changeform.von_std.focus();
    	return false;
    } 
       
    if (!(Number(document.changeform.von_min.value) >= 0 && Number(document.changeform.von_min.value) < 60))
   	{
    	if(status==0)
    		alert("Bitte Minuten richtig eingeben");
    	document.changeform.von_min.focus();
    	return false;
    } 


    
    
    Stop = 31;
    Monat = Number(document.changeform.bis_mon.value);
    Jahr = Number(document.changeform.bis_jahr.value);
	if (Monat == 4 || Monat == 6 || Monat == 9 || Monat == 11)
    	--Stop;
	if (Monat == 2) 
	{
   		Stop = Stop - 3;
    	if (Jahr % 4 == 0)
      		Stop++;
    	if (Jahr % 100 == 0)
      		Stop--;
    	if (Jahr % 400 == 0)
      		Stop++;
    }
    
        if (!(Number(document.changeform.bis_tag.value) > 0 && Number(document.changeform.bis_tag.value) <= Stop))
   	{
    	if(status==0)
    		alert("Bitte Tag richtig eingeben");
    	document.changeform.bis_tag.focus();
    	return false;
    } 

    if (!(Number(document.changeform.bis_mon.value) > 0 && Number(document.changeform.bis_mon.value) < 13))
   	{
    	if(status==0)
    		alert("Bitte Monat richtig eingeben");
    	document.changeform.bis_mon.focus();
    	return false;
    } 

    if (!(Number(document.changeform.bis_jahr.value) > 2000 && Number(document.changeform.bis_jahr.value) < 2999))
   	{
    	if(status==0)
    		alert("Bitte Jahr richtig eingeben");
    	document.changeform.bis_jahr.focus();
    	return false;
    }
     
    if (!(Number(document.changeform.bis_std.value) >= 0 && Number(document.changeform.bis_std.value) < 24))
   	{
    	if(status==0)
    		alert("Bitte Stunden richtig eingeben");
    	document.changeform.bis_std.focus();
    	return false;
    } 
      
    if (!(Number(document.changeform.bis_min.value) >= 0 && Number(document.changeform.bis_min.value) < 60))
   	{
    	if(status==0)
    		alert("Bitte Minuten richtig eingeben");
    	document.changeform.bis_min.focus();
    	return false;
    } 
//Ende: Ueberpruefung der Beschreibung und Datums (mit Kalenderfunktion)

//Anfang: Ueberpruefung Startdatum/-uhrzeit vor Enddatum/-uhrzeit   

    if (Number(document.changeform.von_jahr.value) > Number(document.changeform.bis_jahr.value ))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist das Jahr größer als beim Ende!");
    	}	
    		document.changeform.von_jahr.focus();
    	
    	return false;
    }
    else if((Number(document.changeform.von_jahr.value) ==  Number(document.changeform.bis_jahr.value)) 
    && (Number(document.changeform.von_mon.value) >  Number(document.changeform.bis_mon.value )))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist der Monat größer als beim Ende!");
    	}	
    		document.changeform.von_mon.focus();
    	
    	return false;
    }
    else if((Number(document.changeform.von_jahr.value) ==  Number(document.changeform.bis_jahr.value))
    &&(Number(document.changeform.von_mon.value) ==  Number(document.changeform.bis_mon.value) )
    && (Number(document.changeform.von_tag.value) >  Number(document.changeform.bis_tag.value)))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn ist der Tag größer als beim Ende!");
    	}
    		document.changeform.von_tag.focus();
    	
    	return false;
    }
    else if((Number(document.changeform.von_jahr.value) ==  Number(document.changeform.bis_jahr.value))
    &&(Number(document.changeform.von_mon.value) ==  Number(document.changeform.bis_mon.value) )
    &&(Number(document.changeform.von_tag.value) ==  Number(document.changeform.bis_tag.value) )
    && (Number(document.changeform.von_std.value) >  Number(document.changeform.bis_std.value )))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn sind die Stunden größer als beim Ende!");
    	}	
    		document.changeform.von_std.focus();
    	
    	return false;
    }
    else if((Number(document.changeform.von_jahr.value) ==  Number(document.changeform.bis_jahr.value))
    &&(Number(document.changeform.von_mon.value) ==  Number(document.changeform.bis_mon.value) )
    &&(Number(document.changeform.von_tag.value) ==  Number(document.changeform.bis_tag.value) )
    &&(Number(document.changeform.von_std.value) ==  Number(document.changeform.bis_std.value) )
    && (Number(document.changeform.von_min.value) >  Number(document.changeform.bis_min.value) ))
    {
    	if(status==0)
    	{
    		alert("Beim Beginn sind die Minuten größer als beim Ende!");
    	}
    		document.changeform.von_min.focus();
    	
    	return false;
    }
//Ende: Ueberpruefung Startdatum/-uhrzeit vor Enddatum/-uhrzeit  
     
    return true;//Eingabe war richtig!!
}



function changeerrechnedauer()
{
/* Hier wird die Dauer vom Startdatum/-uhrzeit bis zum Enddatum/-uhrzeit berechnet (mit Kalenderfunktion)
und als Tage, Stunden und Minuten ausgegeben.
*/

	
	if(changeformpruefen(1)==true)
	{
		var jahr;
		var mon;
		var tag;

		var Stop;
		
		var tag_erg;
		var std_erg;
		var min_erg;
		
		jahr = Number(document.changeform.von_jahr.value)
		mon = Number(document.changeform.von_mon.value)
		tag = Number(document.changeform.von_tag.value)
		
		jahr_end = Number(document.changeform.bis_jahr.value)
		mon_end = Number(document.changeform.bis_mon.value)
		tag_end = Number(document.changeform.bis_tag.value)
		
		tag_erg = 0;
		std_erg = 0;
		min_erg = 0;
		
		while(1)
		{
			if(tag == tag_end && mon == mon_end && jahr == jahr_end)
    				break;
    				
				Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
    			
    			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag = 1;
    				mon++;
       			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
    			
    			tag_erg++;

		}
		
		
		if(Number(document.changeform.von_std.value) <= Number(document.changeform.bis_std.value))
			std_erg = Number(document.changeform.bis_std.value) - Number(document.changeform.von_std.value);
		else
		{
			std_erg = 24 - Number(document.changeform.von_std.value) + Number(document.changeform.bis_std.value);
			tag_erg--;
		}
			
		if(Number(document.changeform.von_min.value) <= Number(document.changeform.bis_min.value))
			min_erg = Number(document.changeform.bis_min.value) - Number(document.changeform.von_min.value);
		else
		{
			min_erg = 60 - Number(document.changeform.von_min.value) + Number(document.changeform.bis_min.value);
			if(std_erg>0)
				std_erg--;
			else
			{
				std_erg=23;
				tag_erg--;
			}	
		}
				
		document.changeform.dauer_tage.value = tag_erg;
		document.changeform.dauer_std.value = std_erg;
		document.changeform.dauer_min.value = min_erg;
	}
	else
	{
		document.changeform.dauer_tage.value = "";
		document.changeform.dauer_std.value = "";
		document.changeform.dauer_min.value = "";
	}	
}

function changeerrechneende()
/*
Diese Funktion fuellt das Enddatum/-Uhrzeit aus, wenn man bei der Dauer eine Zeit angibt.
(mit Kalenderfunktion)
*/
{
	
	var jahr;
	var mon;
	var tag;
	
	var std;
	var min;

	var Stop;
		
	var tag_erg;
	var std_erg;
	var min_erg;
		
	jahr = Number(document.changeform.von_jahr.value);
	mon = Number(document.changeform.von_mon.value);
	tag = Number(document.changeform.von_tag.value);
	
	std = Number(document.changeform.von_std.value);
	min = Number(document.changeform.von_min.value);
	
	tag_erg	= Number(document.changeform.dauer_tage.value);
	
	std_erg	= Number(document.changeform.dauer_std.value);
	if(std_erg > 23)
	{
	document.changeform.dauer_std.value = 23;
	std_erg =23;
	}
	
	min_erg	= Number(document.changeform.dauer_min.value);
	if(min_erg > 59)
	{
	document.changeform.dauer_min.value = 59;
	min_erg =59;
	}
	
	Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
			
	while(tag_erg)
		{

    				
				Stop = 31;
			
				if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
    				--Stop;
				if (mon == 2) 
				{
   					Stop = Stop - 3;
    				if (jahr % 4 == 0)
      					Stop++;
    				if (jahr % 100 == 0)
      					Stop--;
    				if (jahr % 400 == 0)
      					Stop++;
    			}
    			
    			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
    			
    			tag_erg--;

		}
		
		
		if((24 - Number(document.changeform.von_std.value)) > std_erg)
			std = Number(document.changeform.von_std.value) + std_erg;
		else
		{
			std = std_erg - ( 24 - Number(document.changeform.von_std.value));
			if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
		}
			
		if((60 - Number(document.changeform.von_min.value)) > min_erg)
			min = Number(document.changeform.von_min.value) + min_erg;
		else
		{
			min = min_erg - ( 60 - Number(document.changeform.von_min.value));
			if(std<23)
				std++;
			else
			{
				std=0;
				if(Stop>tag)
    				tag++;
       			else if(Stop==tag && mon < 12)
    			{
    				tag=1;
					mon++;
      			}
       			else if(Stop==tag && mon == 12)
       			{
       			    tag = 1;
    				mon = 1;
    				jahr++;
    			}
				
			}
		}
		
	document.changeform.bis_jahr.value = jahr;
	
	if(mon<10)
		document.changeform.bis_mon.value = ("0" + mon);
	else
		document.changeform.bis_mon.value = mon;
		
	if(tag<10)	
		document.changeform.bis_tag.value = ("0" + tag);
	else
		document.changeform.bis_tag.value = tag;
		
	document.changeform.bis_std.value = std;
	
	if(min<10)
		document.changeform.bis_min.value = ("0" + min);
	else
		document.changeform.bis_min.value =  min;
}


function senden(ziel)
{
	if (ziel == 0) document.changeform.action = "DoChangeEvent";
	if (ziel == 1) document.changeform.action = "DoDeleteEvent";
	if (ziel == 2) document.changeform.action = "overview.jsp";
}
