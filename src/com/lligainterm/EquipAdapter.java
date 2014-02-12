package com.lligainterm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EquipAdapter extends ArrayAdapter<Equip>{
	Context context; 
    int layoutResourceId;    
    Equip data[] = null;
    
    public EquipAdapter(Context context, int layoutResourceId, Equip[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EquipHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new EquipHolder();
            holder.txtPosisio = (TextView)row.findViewById(R.id.txtPosisio);
            holder.txtNomEquip = (TextView)row.findViewById(R.id.txtNomEquip);
            holder.txtPunts = (TextView)row.findViewById(R.id.txtPunts);
            holder.txtJugats = (TextView)row.findViewById(R.id.txtJugats);
            holder.txtGuanyats = (TextView)row.findViewById(R.id.txtGuanyats);
            holder.txtEmpatats = (TextView)row.findViewById(R.id.txtEmpatats);
            holder.txtPerduts = (TextView)row.findViewById(R.id.txtPerduts);
            holder.txtNp = (TextView)row.findViewById(R.id.txtNp);
            holder.txtGolsF = (TextView)row.findViewById(R.id.txtGolsF);
            holder.txtGolsC = (TextView)row.findViewById(R.id.txtGolsC);
            
            row.setTag(holder);
        }
        else
        {
            holder = (EquipHolder)row.getTag();
        }
        
        Equip equip = data[position];
        holder.txtPosisio.setText(String.valueOf(equip.posicio));
        holder.txtNomEquip.setText(equip.nomEquip);
        holder.txtPunts.setText(String.valueOf(equip.punts));
        holder.txtJugats.setText(String.valueOf(equip.jugats));
        holder.txtGuanyats.setText(String.valueOf(equip.guanyats));
        holder.txtEmpatats.setText(String.valueOf(equip.empatats));
        holder.txtPerduts.setText(String.valueOf(equip.perduts));
        holder.txtNp.setText(String.valueOf(equip.np));
        holder.txtGolsF.setText(String.valueOf(equip.golsF));
        holder.txtGolsC.setText(String.valueOf(equip.golsC));
        
        return row;
    }
    
    static class EquipHolder
    {
        TextView txtPosisio;
        TextView txtNomEquip;
        TextView txtPunts;
        TextView txtJugats;
        TextView txtGuanyats;
        TextView txtEmpatats;
        TextView txtPerduts;
        TextView txtNp;
        TextView txtGolsF;
        TextView txtGolsC;
    }
}
