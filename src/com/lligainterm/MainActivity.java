package com.lligainterm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockActivity implements ActionBar.TabListener {

	private String ResClasURL = "http://www.girona.cat/gestio_esportiva/resultats_classificacions/pdf_res_clas.php?";
	// www.girona.cat/gestio_esportiva/resultats_classificacions/mostra_res_clas.php?id_temporada="223"&id_esport="4"&id_categoria="13"&grup="FS 12-13"
	
	//NOTA: els mesos comencen al 0 pel gener 
	private Date datesJornades[] = {new GregorianCalendar(2012, 10, 5).getTime(),
							  new GregorianCalendar(2012, 10, 12).getTime(),
							  new GregorianCalendar(2012, 10, 19).getTime(),
							  new GregorianCalendar(2012, 10, 26).getTime(),
							  new GregorianCalendar(2012, 10, 10).getTime(),
							  new GregorianCalendar(2012, 10, 17).getTime(),
							  new GregorianCalendar(2013, 0, 7).getTime(),
							  new GregorianCalendar(2013, 0, 14).getTime(),
							  new GregorianCalendar(2013, 0, 21).getTime(),
							  new GregorianCalendar(2013, 0, 28).getTime(),
							  new GregorianCalendar(2013, 1, 4).getTime(),
							  new GregorianCalendar(2013, 1, 11).getTime(),
							  new GregorianCalendar(2013, 1, 18).getTime(),
							  new GregorianCalendar(2013, 1, 25).getTime(),
							  new GregorianCalendar(2013, 2, 4).getTime(),
							  new GregorianCalendar(2013, 2, 11).getTime(),
							  new GregorianCalendar(2013, 2, 18).getTime()};
		
	private ListView classifView, resultView;
	private Equip equip_data[][];
	private Partit jornades[][];
	private int nJornadaAct, nJornada;
	private View activity_classif, activity_result;
	private TextView tvJornada;
	
	private final int nEquips = 18;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		equip_data = new Equip[nEquips-1][nEquips];
		jornades = new Partit[nEquips-1][nEquips/2];
		nJornadaAct = nJornada = getNJornadaActual();
		
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		activity_classif = inflator.inflate(R.layout.activity_classif, null);
		activity_result = inflator.inflate(R.layout.activity_result, null);
		classifView = (ListView) activity_classif.findViewById(R.id.classifView);
		resultView = (ListView) activity_result.findViewById(R.id.resultView);
		View header_classif = (View) getLayoutInflater().inflate(R.layout.classif_header_row, null);
		View header_result = (View) getLayoutInflater().inflate(R.layout.resultats_header_row, null);
		classifView.addHeaderView(header_classif);
		resultView.addHeaderView(header_result);
		tvJornada = (TextView) header_result.findViewById(R.id.txtJornada);
		
		addTabs();
	}

	private void addTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText("Classificació");
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText("Resultats");
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);
	}

	private int getNJornadaActual() {
		int result = 1;
		Date avui = new Date();
		for (int i=0; i<datesJornades.length; i++) {
			if (avui.after(datesJornades[i])) 
				result = i+1;
		}
		
		return result;
	}
	
	private void getJornada(int j) {
		setSupportProgressBarIndeterminateVisibility(true);
		DownloadInfoTask downloadTask = new DownloadInfoTask();
		downloadTask.setContext(this);
		downloadTask.setJornada(String.valueOf(j));
		downloadTask.execute(ResClasURL);

		String myLine = "";
		try {
			myLine = downloadTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		String[] equips = extreuLinies(myLine.split(System.getProperty("line.separator")), 18, 35);		
		// Equip, Punts, PJ, PG, PE, PP, NP, GF, GC
		equip_data[j-1] = new Equip[equips.length];
		for (int i = 0; i < equips.length; i++) {
			String[] tmpEquip = equips[i].split(" ");
			String tmpNomEquip = tmpEquip[0];
			for (int n = 1; n < tmpEquip.length - 8; n++) {
				tmpNomEquip += " " + tmpEquip[n];
			}
			equip_data[j-1][i] = new Equip(formatNom(tmpNomEquip), i + 1, Integer.parseInt(tmpEquip[tmpEquip.length - 8]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 7]), Integer.parseInt(tmpEquip[tmpEquip.length - 6]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 5]), Integer.parseInt(tmpEquip[tmpEquip.length - 4]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 3]), Integer.parseInt(tmpEquip[tmpEquip.length - 2]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 1]));
		}
		
		String[] partits = extreuLinies(myLine.split(System.getProperty("line.separator")), 7, 15);
		// Data, EquipLocal, EquipVisitant, Resultat
		jornades[j-1] = new Partit[partits.length];
		for (int i = 0; i < partits.length; i++) {
			String[] tmp = partits[i].split(" ");
			SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
			Date data = null;
			try {
				data = df.parse(tmp[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String tmpNomEquip1 = formatNom(tmp[1]);
			Equip local = getEquip(tmpNomEquip1);
			int n=2;
			while ((n<tmp.length) && (local==null)) {
				tmpNomEquip1 = formatNom(tmpNomEquip1 + " " + tmp[n]);
				local = getEquip(tmpNomEquip1);
				n++;
			}
				
			String tmpNomEquip2 = formatNom(tmp[n]);
			n++;
			Equip visitant = getEquip(tmpNomEquip2);
			while ((n<tmp.length) && (visitant==null)) {
				tmpNomEquip2 = formatNom(tmpNomEquip2 + " " + tmp[n]);
				visitant = getEquip(tmpNomEquip2);
				n++;
			}
				
			int golsLocal = -1;
			int golsVisitant = -1;
			if (tmp[tmp.length-1].compareTo("JUGAT") != 0) {
				golsLocal = Integer.valueOf(tmp[tmp.length-3]);
				golsVisitant = Integer.valueOf(tmp[tmp.length-1]);
			}
				
			jornades[j-1][i] = new Partit(j, data, local, visitant, golsLocal, golsVisitant);
		}
		setSupportProgressBarIndeterminateVisibility(false);
	}

	private Equip getEquip(String nom) {
		for (int i=0; i<nEquips; i++) {
			if (nom.compareTo(equip_data[nJornadaAct-1][i].nomEquip) == 0)
				return equip_data[nJornadaAct-1][i];
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*SubMenu subMenu1 = menu.addSubMenu("Actualitzar");
        
        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.ic_menu_refresh);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/
        
		return super.onCreateOptionsMenu(menu);
	}

	private String formatNom(String str) {
		String result = "";
		boolean up = true;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				up = true;
				result += ' ';
			} else {
				if (up) {
					result += str.charAt(i);
					up = false;
				} else
					result += Character.toLowerCase(str.charAt(i));
			}
		}
		return result;
	}
	
	private String[] extreuLinies(String[] source, int fromLine, int toLine) {
		String[] result = new String[toLine-fromLine+1];
		for (int i=fromLine; i<=toLine; i++)
			result[i-fromLine] = source[i];
		return result;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch (tab.getPosition()) {
		case 0:
			if (equip_data[nJornada-1][0] == null)
				getJornada(nJornada);
			
			EquipAdapter classifAdapter = new EquipAdapter(this, R.layout.classif_item_row, equip_data[nJornada-1]);
			classifView.setAdapter(classifAdapter);
			setContentView(activity_classif);
			break;
		case 1:
			if (jornades[nJornada-1][0] == null)
				getJornada(nJornada);
			
			PartitAdapter partitAdapter = new PartitAdapter(this, R.layout.resultats_item_row, jornades[nJornada-1]);
			tvJornada.setText("Jornada " + String.valueOf(nJornadaAct));
			resultView.setAdapter(partitAdapter);
			setContentView(activity_result);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

}
