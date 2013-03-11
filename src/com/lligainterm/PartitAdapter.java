package com.lligainterm;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PartitAdapter extends ArrayAdapter<Partit> {
	Context context;
    int layoutResourceId;
    Partit data[] = null;
    
    public PartitAdapter(Context context, int layoutResourceId, Partit[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PartitHolder holder = null;
        
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new PartitHolder();
            holder.txtData = (TextView)row.findViewById(R.id.txtData);
            holder.txtNomLocal = (TextView)row.findViewById(R.id.txtNomLocal);
            holder.txtNomVisitant = (TextView)row.findViewById(R.id.txtNomVisitant);
            holder.txtGolsLocal = (TextView)row.findViewById(R.id.txtGolsLocal);
            holder.txtGolsVisitant = (TextView)row.findViewById(R.id.txtGolsVisitant);
            
            row.setTag(holder);
        }
        else
            holder = (PartitHolder)row.getTag();
        
        Partit partit = data[position];
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        holder.txtData.setText(df.format(partit.data));
        holder.txtNomLocal.setText(partit.local.nomEquip);
        holder.txtNomVisitant.setText(partit.visitant.nomEquip);
        
        if ((partit.golsLocal == -1) && (partit.golsVisitant == -1)) {
        	holder.txtGolsLocal.setText("X");
        	holder.txtGolsVisitant.setText("X");
        	holder.txtGolsLocal.setBackgroundColor(Color.DKGRAY);
        	holder.txtGolsVisitant.setBackgroundColor(Color.DKGRAY);
        }
        else {
        	holder.txtGolsLocal.setText(String.valueOf(partit.golsLocal));
        	holder.txtGolsVisitant.setText(String.valueOf(partit.golsVisitant));
        }
        
        return row;
    }
    
    static class PartitHolder {
    	TextView txtData;
        TextView txtNomLocal;
        TextView txtNomVisitant;
        TextView txtGolsLocal;
        TextView txtGolsVisitant;
    }
}
