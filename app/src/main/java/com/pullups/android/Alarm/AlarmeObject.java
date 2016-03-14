package com.pullups.android.Alarm;

/**
 * Created by Thomas on 31.03.2015.
 * Class qui gère la cération des objets alarme
 * Elle permet de créer un objet AlarmeObject qui contient
 * 1) Un Jour de la semaine
 * 2) Une heure d'alarme
 * 3) si L'objet et activé ou non
 */
public class AlarmeObject {

    /* Attributs de l'objet*/
    private int int_id;
    private int int_id_day;
    private String str_day;
    private String str_Heure;
    private int int_hour;
    private int int_min;
    private boolean bln_alarmeSet;

    //Constructeur de l'objet AlarmeObject
    public AlarmeObject (int id, int int_id_day, String day, int hour, int min, String heure, boolean alarmeOn){

        this.int_id = id;
        this.int_id_day = int_id_day;
        this.str_day = day;
        this.int_hour = hour;
        this.int_min = min;
        this.str_Heure = heure;
        this.bln_alarmeSet = alarmeOn;
    }

    /* Méthodes pour la gestion de l'objet */

    ///* Méthode qui permet d'alumer ou d'éteindre l'alarme */
    public void setAlarme (boolean isAlarmeOn){
        //on Donne a l'alarme la valeur contenu de la paramètre de la méthode
        this.bln_alarmeSet = isAlarmeOn;
    }

    /* Méthode qui permet de modifier l'heure de l'alarme */
    public void setAlarmeTime(String time){
        this.str_Heure = time;
    }

    /* Méthode qui permet d'enregistrer les int pour les récuperer après dans
     la partie qui valide les alarmes et les heures */
    public void setHourAndMinutes(int hour, int min){
        this.int_hour = hour;
        this.int_min = min;
    }

    /* Méthode qui retourne l'id de l'alarme*/
    public int getInt_id(){return this.int_id;}

    /* Méthode qui permet de récupérer le jour */
    public String getDayName (){
        return this.str_day;
    }

    /* Méthode qui permet de retrouver si l'alarme du jour est activée ou non */
    public boolean getAlarmeStatus (){
        return this.bln_alarmeSet;
    }

    /* Méthode qui permet de retourner l'heure de l'alarme */
    public String getAlarmeTime(){
        return this.str_Heure;
    }

    /* Méthode qui récupère les heures sous la forme d'un int*/
    public int getAlarmeHour(){return this.int_hour;}

    /* Méthode qui récupère les minutes sous la forme d'un int*/
    public int getAlarmeMin(){return this.int_min;}

    /* Méthode qui retourne le numéro du jour selon le calendrier (Dimanche = 1) */
    public int getInt_id_day(){return this.int_id_day;}

}//AlarmeObject
