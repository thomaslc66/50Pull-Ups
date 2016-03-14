package com.pullups.android.model;

import android.net.Uri;

/**
 * Created by Thomas on 05.04.2015.
 */
public class ExerciceObject {
    ///Lien de la vidéo
    private String lien;
    //Muscles utilisés lors de l'exercice
    private String muscles;
    //Intensité de l'exercice
    private String intensity;
    //nom de l'exercice
    private String exoNamme;


    /**
     * Constructeur pour les Objets Exercices qui contiennent un titre, les muscules utilisés, l'intensité et un lien
     * vers une vidéo
     * @param name
     * @param muscles
     * @param intensity
     * @param lien
     */
    public ExerciceObject(String name, String muscles, String intensity, String lien){
        this.exoNamme = name;
        this.muscles = muscles;
        this.intensity = intensity;
        this.lien = lien;
    }


    //getters

    public String getTitle(){
        return this.exoNamme;
    }

    public String getMuscles(){
        return this.muscles;
    }

    public String getIntensity(){
        return this.intensity;
    }

    public String getLien(){
        return this.lien;
    }

    public void setExoNamme(String name){
        this.exoNamme = name;
    }

    public void setMuscles(String muscles){
        this.muscles = muscles;
    }

    public void setIntensity(String intensity){
        this.intensity = intensity;
    }

    public void setLien(String lien){
        this.lien = lien;
    }

}
