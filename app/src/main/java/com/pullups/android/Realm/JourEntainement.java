package com.pullups.android.Realm;

import io.realm.RealmObject;

/**
 * Created by Thomas on 16.05.2015.
 */
public class JourEntainement extends RealmObject{

    public JourEntainement(){    }

    public JourEntainement(int int_jour , float flt_level, int int_day_level,
                           String str_s1, String str_s2  , String str_s3    , String str_s4, String str_s5,
                           int int_max_pullUp            , int int_total_pullUp)
    {
        this.indexDuJour        = int_jour;
        this.niveau             = flt_level;
        this.jourDansLeNiveau   = int_day_level;
        this.str_s1             = str_s1;
        this.str_s2             = str_s2;
        this.str_s3             = str_s3;
        this.str_s4             = str_s4;
        this.str_s5             = str_s5;
        this.nombreDeTractions  = int_max_pullUp;
        this.totalDeTractions   = int_total_pullUp;
    }

    //id of the training day
    private int     indexDuJour;
    //num of the level
    private float   niveau;
    //jour dans le niveau
    private int     jourDansLeNiveau;
    //serie 1
    private String  str_s1;
    //serie 2
    private String  str_s2;
    //serie 3
    private String  str_s3;
    //serie 4
    private String  str_s4;
    //serie 5
    private String  str_s5;
    //traction max for the test
    private int     nombreDeTractions;
    //total of traction done
    private int     totalDeTractions;

    public int      getIndexDuJour      () { return indexDuJour;       }
    public float    getNiveau           () { return niveau;            }
    public int      getJourDansLeNiveau () { return jourDansLeNiveau;  }
    public String   getStr_s1           () { return str_s1;            }
    public String   getStr_s2           () { return str_s2;            }
    public String   getStr_s3           () { return str_s3;            }
    public String   getStr_s4           () { return str_s4;            }
    public String   getStr_s5           () { return str_s5;            }
    public int      getNombreDeTractions() { return nombreDeTractions; }
    public int      getTotalDeTractions () { return totalDeTractions;  }


    public void     setIndexDuJour       (int indexDuJour)      { this.indexDuJour = indexDuJour;          }
    public void     setNiveau            (float niveau)         { this.niveau = niveau;                    }
    public void     setJourDansLeNiveau  (int jourDansLeNiveau) { this.jourDansLeNiveau = jourDansLeNiveau;}
    public void     setStr_s1            (String str_s1)        { this.str_s1 = str_s1;                    }
    public void     setStr_s2            (String str_s2)        { this.str_s2 = str_s2;                    }
    public void     setStr_s3            (String str_s3)        { this.str_s3 = str_s3;                    }
    public void     setStr_s4            (String str_s4)        { this.str_s4 = str_s4;                    }
    public void     setStr_s5            (String str_s5)        { this.str_s5 = str_s5;                    }
    public void     setNombreDeTractions (int int_max_pullUp)   { this.nombreDeTractions = int_max_pullUp; }
    public void     setTotalDeTractions  (int totalDeTractions) { this.totalDeTractions = totalDeTractions;}
}


