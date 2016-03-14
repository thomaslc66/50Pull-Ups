package com.pullups.android.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.Alarm.AlarmeObject;
import com.pullups.android.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * FR -
 * Created by Thomas on 31.03.2015.
 * But: Permet de créer la liste des alarmes pour
 * pouvoir envoyer un rappel d'entrainement
 */


/** EN -
 * Created by Thomas on 31.03.2015.
 * Goal: The Adapter is needed to create the Activity with the listView
 * the constructor takes in parameters the context and an ArrayList that contains Alarm Object
 *
 */

public class AlarmeAdapter extends BaseAdapter implements TimePickerDialog.OnTimeSetListener{

    /* FR - Attributs de l'objet AlarmeAdapter */
    /* EN - Attributs */
    /* EN - Default time of alarms */
    private String time = "11:00";
    private int id;
    private final ArrayList<AlarmeObject> alarmeObjectArrayList;
    private final LayoutInflater mInflater;
    private Calendar calendar;
    private final String TIME_PATTERN = "HH:mm";
    private SimpleDateFormat timeFormat;
    private CheckBox chkBox_day;
    private TextView txtView_Heure;
    private Context context;
    private View view;

    /* FR - tableau pour savoir si les jours sont choisi ou non
       EN - tab to check if and alarm is set or not */
    private Boolean[] itemChecked = new Boolean[6];


    /* FR - Constructeur de l'alarme Adapter
     * EN - Constructor */
    public AlarmeAdapter (Context context, ArrayList<AlarmeObject> alarmeObjectArrayList){

        this.mInflater = LayoutInflater.from(context);
        /* FR - pour les objets alarmesObject
           EN - set ArrayList from adapter = ArrayList from activity */
        this.alarmeObjectArrayList = alarmeObjectArrayList;
    }//AlarameAdapter

    @Override
    public int getCount() {
        return this.alarmeObjectArrayList.size();
    }//getCount

    @Override
    public Object getItem(int position) {
        /* FR -Retourne l'objet AlarmeObject à la postion
        *  EN - return the AlarmeObject at position */
        return alarmeObjectArrayList.get(position);
    }//getItem

    @Override
    public long getItemId(int position) {
        return position;
    }//getItemid

    /*****************************************************************************
     *
     * FR - Cette méthode permet de construire la vue pour les alarmes
     * EN - This method is used to build the ListView for the Alarm Activity
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     *
     * ***************************************************************************/
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){

        /* EN - We set the format for the time */
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        /* EN - get and instance of Calendat */
        calendar = Calendar.getInstance();
        /* EN - and a ViewHolder */
        final ViewHolder viewHolder;

        /* FR - si la convertView est null cela veut dire qu'il faut lui dire ou aller chercher le layout.
           EN - if the convertView is null it means we need to tell him where to look for the layout */
        if(convertView == null){
            /* FR - on utilise la convertView pour et on lui ajoute le layout.
               EN - We used the convertView and tell him to inflate the specific layout */
            view = mInflater.inflate(R.layout.alarme_list_item, null);
            viewHolder = new ViewHolder();
            /* FR - on attribue les élements du layout aux élements de la ViewHolder afin de pouvoir interagir. On passe le tout à la ViewHolder pour la construction
            *  EN - We set the elements from the layout to the viewHolder. Than we set the view tag = viewHolder like this we can interact with the element from the layout */
            viewHolder.chkBox_day = (CheckBox) view.findViewById(R.id.checkBox_str_Jour);
            viewHolder.txtView_hour = (TextView) view.findViewById(R.id.txtView_int_Heure);
            view.setTag(viewHolder);

        }/* EN - elese if the convertView is not null we get the tag of the view, an set the element = the element from the view holder */
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            txtView_Heure = viewHolder.txtView_hour;
            chkBox_day = viewHolder.chkBox_day;
        }

        /* EN - Than we can interact with the elements set the check box text = the name of the day and the txtView_hour = the time of the alam */
        viewHolder.chkBox_day.setText(alarmeObjectArrayList.get(position).getDayName());
        viewHolder.txtView_hour.setText(alarmeObjectArrayList.get(position).getAlarmeTime());
        context = parent.getContext();

        /* EN - On clickListener on the txtView_hour to display the timePicker (RadialTimePicker) */
        viewHolder.txtView_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FR - on récupère le FragmentManager depuis l'activité afin de l'utilisé pour l'affichage du TimePickerDialog
                  EN - Getting back the Fragment manager from the activity, then use it to display the TimePickerDialog*/
                FragmentManager fmng = ((Activity) context).getFragmentManager();

               /* FR - On crée une nouvelle instance de TimePickerDialog
                  EN - We create a new instance of the TimePickerDialog */
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        AlarmeAdapter.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true,
                        position
                );
               /* EN - if the timePickerDialog is clicked we set the checkbox to true */
                if (!viewHolder.chkBox_day.isChecked()) {
                    viewHolder.chkBox_day.performClick();
                }
               /* EN - Dispaly the timeDialofPicker */
                timePickerDialog.show(fmng, "");
            }
        });

        /* FR - Cette petite fonction permet de changer la valeur de la checkbox et donc de l'alarme
        *  EN - If the alarm status from the ArrayList is true we need to set it to true also on the checkbox of the same day */
        if (alarmeObjectArrayList.get(position).getAlarmeStatus()) {
            viewHolder.chkBox_day.setChecked(true);
        } else {
            viewHolder.chkBox_day.setChecked(false);
        }

        /* EN - onClickListener for the checkBox. */
        viewHolder.chkBox_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.chkBox_day.isChecked()){
                    // EN - we change the alarmStatus into the alarm that is in the ArrayList
                    alarmeObjectArrayList.get(position).setAlarme(true);
                }else{
                    alarmeObjectArrayList.get(position).setAlarme(false);

                }
                /* EN - only to check if the alarm into the ArrayList and the value of the check box are the same. They need to be both on true or False. Can be erased after debugging*/
                Toast.makeText(context,alarmeObjectArrayList.get(position).getAlarmeHour()+":"+alarmeObjectArrayList.get(position).getAlarmeMin() + " - "+alarmeObjectArrayList.get(position).getAlarmeStatus(),Toast.LENGTH_SHORT).show();
            }
        });

        /* FR - retour de la vue construite
        * EN - return of the constructed view */
        return view;
    }

    /* FR - Méthode permettant de mettre a jour le temps de l'alarme*/
    /* EN - Method witch update the alarm time into the alarmeObjectArrayList
    * set new Hour and Minute to the alarm with id = id */
    public void updateTime(String time, int id, int hour, int min){
        /* EN - check to be on the mainThread */
        ThreadPreconditions.checkOnMainThread();
        // FR - il faut modifier l'objet lorsque l'on clique dessus
        /* EN - We update alarm (string) time and (int) hour and minutes */
        this.alarmeObjectArrayList.get(id).setAlarmeTime(time);
        this.alarmeObjectArrayList.get(id).setHourAndMinutes(hour, min);
        /* EN - Notification to the Adapter that some data have changed and that we need an update */
        notifyDataSetChanged();
    }//updateTime()


    /* FR - Méthode appellée lorsque le RadialTimePicker Dialog est appelé*/
    /* EN - Method called when RadialTimePicker is called. RadialPicker is a github Library that display a time picker in a radial Way*/
    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute, int position) {
        // FR - on récupère l'heure choisie
        /* EN - We get back choosed time and pass it throw the formatHHMMValue method */
        time = formatHHMMValue(hourOfDay)+":"+formatHHMMValue(minute);
        // FR - on change l'heure choisie dans la textView
        /* EN - We setText in the textView and put the selected time */
        updateTime(time,position,hourOfDay,minute);
    }

    /* FR - class privée ViewHolder qui permet de construire la vue qui ira dans chaque ligne de la liste */
    /* EN - private class used to build the view that will goes into each line of the listView on Alarm Activity
    * see alarme_list_item.xml */
    private class ViewHolder {
        public CheckBox chkBox_day;
        public TextView txtView_hour;
    }//ViewHolder

    /* EN - Method needed to display hour with zeros before singel numbers
    * Exemple: 1:4 will be displayed 01:04
    *
    * params (value) -> number we need to convert
    * */
    private String formatHHMMValue(int value){
        String retour;
        /* FR - si value est plus petit que 10 cela veut dire qu'il manque un zéro avant l'unité et il faut le rajouter */
        /* EN - if value is minus the 10 it means we need to add zero in front of value*/
        if(value < 10){
            retour = "0"+value;
        }else{
            retour = value + "";
        }
        //return the String with a 0 before
        return retour;
    }//formatHHMMValue
}//AlarmeAdapter.java
