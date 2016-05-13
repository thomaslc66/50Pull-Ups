package com.pullups.android.Realm;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.text.TextUtils.split;

/**
 * Created by Thomas on 16.05.2015.
 */
public class JourEntrainementDB {

    Context mContext;
    Realm   realm;
    String  int_day           = "indexDuJour";
    String  niveau            = "niveau";
    //String  nombreDeTractions = "nombreDeTractions";
    String  totalDeTractions  = "totalDeTractions";

    static final int ZERO  = 0;
    static final int UN    = 1;
    static final int CINQ  = 5;
    static final int SIX   = 6;
    static final int SEPT  = 7;

    public JourEntrainementDB(Context context) {
        realm    = Realm.getInstance(context);
    }

    public void miseAJourDuTotal(int id, int total) {
        try {
            realm.beginTransaction();
            JourEntainement t = realm.where(JourEntainement.class).equalTo(int_day, id).findFirst();
            t.setTotalDeTractions(total);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
    }

    public final JourEntainement trouveJourAvecIdentifiant(int id) {
        realm.beginTransaction();
        JourEntainement t = null;
        try {
            t = realm.where(JourEntainement.class).equalTo(int_day, id).findFirst();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
        return t;
    }//trouveJourAvecIdentifiant

    public final RealmResults<JourEntainement> getAllTrainingInLevel(float level) {
        realm.beginTransaction();
        RealmResults<JourEntainement> r = null;
        try {
            r = realm.where(JourEntainement.class).equalTo(niveau, level).findAll();
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
        return r;
    }//getAllTrainingInLevel

    /*********************************************************
     * @return
     *********************************************************/
    public final RealmResults<JourEntainement> getAllDB() {
        realm.beginTransaction();
        RealmResults<JourEntainement> r = null;
        try {
            r = realm.where(JourEntainement.class).findAll();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
        return r;
    }//getAllDB

    /************************************************************
     * @param jourEntrainement
     * @return tabSeriePourAffichage - tableau de String
     ***********************************************************/
    public final List<String> creationTableauDesSeriesPourAffichage(JourEntainement jourEntrainement) {
        List<String> tabSeriePourAffichage = new ArrayList<String>();
        tabSeriePourAffichage.add(jourEntrainement.getStr_s1());
        tabSeriePourAffichage.add(jourEntrainement.getStr_s2());
        tabSeriePourAffichage.add(jourEntrainement.getStr_s3());
        tabSeriePourAffichage.add(jourEntrainement.getStr_s4());
        tabSeriePourAffichage.add(jourEntrainement.getStr_s5());
        return tabSeriePourAffichage;
    }//creationTableauDesSeriesPourAffichage

    /*******************************************************
     * Cree un tableau des series du jour ainsi que le jour et l'index
     *
     * @param jourEntainement
     * @return tabOfSeries
     ********************************************************/
    public final int[] trouverLesSeriesPourLeChoixDuNiveau(JourEntainement jourEntainement) {
        final int[] tabOfSeries = new int[SEPT];
        String[] sub_tab;
        String str_serie;
        List<String> series = creationTableauDesSeriesPourAffichage(jourEntainement);

        tabOfSeries[CINQ] = jourEntainement.getIndexDuJour(); //index du jour position 6

        //si l'index du jour est plus petit que 17 -> dernier jour avec des tractions négatives
            for (int i = ZERO; i < CINQ; i++) {
                str_serie = series.get(i);
                if (str_serie.contains("n")) {
                    sub_tab         = split(str_serie, "\\s");
                    tabOfSeries[i]  = Integer.parseInt(sub_tab[ZERO]);
                } else {
                    tabOfSeries[i]  = Integer.parseInt(str_serie);
                }
            }//for

        tabOfSeries[SIX] = jourEntainement.getJourDansLeNiveau(); //Niveau position 0

        return tabOfSeries;
    }//trouverLesSeriesPourLeChoixDuNiveau


    /****************************************************************
     * @param day
     * @return
     **************************************************************/
    public int[] creationTableauDesSeries(JourEntainement day) {
        int[] tabOfSeries = new int[CINQ];
        List<String> series = creationTableauDesSeriesPourAffichage(day);
        String[] sub_tab;
        String str_serie;

        for (int i = ZERO; i < CINQ; i++) {
            str_serie = series.get(i);
            if (str_serie.contains("n")) {
                sub_tab = split(str_serie, "\\s");
                tabOfSeries[i] = Integer.parseInt(sub_tab[ZERO]);
            } else {
                tabOfSeries[i] = Integer.parseInt(str_serie);
            }
        }//for
        return tabOfSeries;
    }//creationTableauDesSeries


    /********************************************
     * trouverLeRecordDeTractions
     *
     * @param day
     * @return max
     **********************************************/
    public int trouverLeRecordDeTractions(JourEntainement day) {
        int max = ZERO;
        int[] tabofpullUps = creationTableauDesSeries(day);
        int tab_length = tabofpullUps.length;

        for (int i = UN; i < tab_length; i++) {
            if (max < tabofpullUps[i]) {
                max = tabofpullUps[i];
            }
        }
        return max;
    }//trouverLeRecordDeTractions


    /*************************************************
     * sommeDesTractionsDuJour
     *
     * @param day
     * @return sum
     ****************************************************/
    public int sommeDesTractionsDuJour(JourEntainement day) {
        int sum = ZERO;
        int[] series = creationTableauDesSeries(day);
        for (int element : series) {
            sum += element;
        }
        return sum;
    }//sommeDesTractionsDuJour


    /****************************************************************
     * remplirLaBaseDeDonnees
     *
     * @param arrayList rempli la base de donnees
     **************************************************************/
    public void remplirLaBaseDeDonnees(List<JourEntainement> arrayList) {
        try {
            realm.beginTransaction();
            int arrayList_size = arrayList.size();
            for (int i = ZERO; i < arrayList_size; i++) {
                JourEntainement t = realm.createObject(JourEntainement.class);
                t.setIndexDuJour(arrayList.get(i).getIndexDuJour());
                t.setNiveau(arrayList.get(i).getNiveau());
                t.setJourDansLeNiveau(arrayList.get(i).getJourDansLeNiveau());
                t.setStr_s1(arrayList.get(i).getStr_s1());
                t.setStr_s2(arrayList.get(i).getStr_s2());
                t.setStr_s3(arrayList.get(i).getStr_s3());
                t.setStr_s4(arrayList.get(i).getStr_s4());
                t.setStr_s5(arrayList.get(i).getStr_s5());
                t.setNombreDeTractions(arrayList.get(i).getNombreDeTractions());
                t.setTotalDeTractions(ZERO);

            }
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
    }//remplirLaBaseDeDonnees

    // Reinitialisation de la base de donnees
    public void resetDataBase() {
        try {
            realm.beginTransaction();
            RealmResults<JourEntainement> r = realm.where(JourEntainement.class).greaterThan(totalDeTractions, ZERO).findAll();
            //workaround from realm, iterate backward
            for (int i = r.size()-1; i >= 0; i--) {
                r.get(i).setTotalDeTractions(ZERO);
            }

            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
    }

    public void closeRealm() {
        realm.close();
    }
}//class JourEntrainementDB
