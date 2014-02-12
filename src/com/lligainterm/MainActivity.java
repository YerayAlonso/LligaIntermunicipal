package com.lligainterm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import net.simonvt.menudrawer.MenuDrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements ActionBar.TabListener, MenuAdapter.MenuListener {

	private String ResClasURL = "http://www.girona.cat/gestio_esportiva/resultats_classificacions/pdf_res_clas.php?";
	// www.girona.cat/gestio_esportiva/resultats_classificacions/mostra_res_clas.php?id_temporada="223"&id_esport="4"&id_categoria="13"&grup="FS 12-13"
	
	private Temporada temporada[];
	private Fase faseActual;
	
	private ListView classifView, resultView;
	private int nJornadaAct, nTemporadaAct;
	private View activity_classif, activity_result;
	private TextView tvJornada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addTemporades();
		setTemporada(1);
		
		temporada[nTemporadaAct].equip_data = new Equip[temporada[nTemporadaAct].nEquips-1][temporada[nTemporadaAct].nEquips];
		temporada[nTemporadaAct].jornades = new Partit[temporada[nTemporadaAct].nEquips-1][temporada[nTemporadaAct].nEquips/2];
		nJornadaAct = getNJornadaActual();
		
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
		
		addMenu();
		addTabs();
	}
	
	private void setTemporada(int nTemp) {
		nTemporadaAct = nTemp;
		switch (nTemporadaAct) {
			case 0: {
				faseActual = new Fase("1a Fase", R.drawable.yellow, 1213, 1);
				break;
			}
			case 1: {
				faseActual = new Fase("1a Fase", R.drawable.green, 1314, 1);
				break;
			}
		}
	}
		
	private void addTemporades() {
		temporada = new Temporada[2];
		temporada[0] = new Temporada();
		temporada[1] = new Temporada();
		
		temporada[0].datesJornades = Dates.t1213;
		temporada[1].datesJornades = Dates.t1314;
		
		temporada[0].nEquips = 18;
		temporada[1].nEquips = 14;
	}

	private void addTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_classif);
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_result);
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);
	}

	private MenuDrawer mMenuDrawer;
	protected MenuAdapter mAdapter;
	protected ListView mList;
	private int mActivePosition = 0;
	
	private void addMenu() {
		mMenuDrawer = MenuDrawer.attach(this);

		List<Object> items = new ArrayList<Object>();
		items.add(new Category("Futbol Sala '13-'14"));
		items.add(new Fase("1a Fase", R.drawable.green, 1314, 1));
		items.add(new Fase("2a Fase", R.drawable.yellow, 1314, 2));
		items.add(new Fase("3a Fase", R.drawable.red, 1314, 3));
		items.add(new Category("Futbol Sala '12-'13"));
		items.add(new Fase("1a Fase", R.drawable.yellow, 1213, 1));
		items.add(new Fase("2a Fase", R.drawable.red, 1213, 2));
		
        mList = new ListView(this);
        mAdapter = new MenuAdapter(this, items);
        mAdapter.setListener(this);
        mAdapter.setActivePosition(mActivePosition);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(mItemClickListener);
        mMenuDrawer.setMenuView(mList);
		mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
		mMenuDrawer.setDrawerIndicatorEnabled(true);
	}
	
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mAdapter.setActivePosition(position);
            onMenuItemClicked(position, (Fase) mAdapter.getItem(position));
        }
    };
    
    protected void onMenuItemClicked(int position, Fase fase) {
    	if (((fase.nTemporada == 1213) && (fase.nFase == 2)) ||
    		((fase.nTemporada == 1314) && (fase.nFase > 1))) {
    		Toast toast = Toast.makeText(getApplicationContext(), "Fase no disponible", Toast.LENGTH_SHORT);
    		toast.show();
    	}
    	else {
    		mMenuDrawer.closeMenu();
    		switch (fase.nTemporada) {
    			case 1213: {
    				if (nTemporadaAct != 0) {
    					setTemporada(0);
    					nJornadaAct = getNJornadaActual();
    					showTab(getSupportActionBar().getSelectedTab().getPosition());
    				}
    				break;
    			}
    			case 1314: {
    				if (nTemporadaAct != 1) {
    					setTemporada(1);
    					nJornadaAct = getNJornadaActual();
    					showTab(getSupportActionBar().getSelectedTab().getPosition());
    				}
    				break;
    			}
    		}
    	}
    }
	
	private int getNJornadaActual() {
		int result = 1;
		Date avui = new Date();
		for (int i=0; i<temporada[nTemporadaAct].datesJornades.length; i++) {
			if (avui.after(temporada[nTemporadaAct].datesJornades[i])) 
				result = i+1;
		}
		
		return result;
	}
	
	private void getJornada(int j) {
		setSupportProgressBarIndeterminateVisibility(true);
		DownloadInfoTask downloadTask = new DownloadInfoTask();
		downloadTask.setContext(this);
		downloadTask.setFase(faseActual);
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
		Log.i("info", myLine);
		
		int startLine = 9 + (temporada[nTemporadaAct].nEquips / 2);
		int endLine = startLine + temporada[nTemporadaAct].nEquips - 1;
		String[] equips = extreuLinies(myLine.split(System.getProperty("line.separator")), startLine/*18*/, endLine/*35*/);
		
		// Equip, Punts, PJ, PG, PE, PP, NP, GF, GC
		temporada[nTemporadaAct].equip_data[j-1] = new Equip[equips.length];
		for (int i = 0; i < equips.length; i++) {
			String[] tmpEquip = equips[i].split(" ");
			String tmpNomEquip = tmpEquip[0];
			for (int n = 1; n < tmpEquip.length - 8; n++) {
				tmpNomEquip += " " + tmpEquip[n];
			}
			temporada[nTemporadaAct].equip_data[j-1][i] = new Equip(formatNom(tmpNomEquip), i + 1, Integer.parseInt(tmpEquip[tmpEquip.length - 8]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 7]), Integer.parseInt(tmpEquip[tmpEquip.length - 6]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 5]), Integer.parseInt(tmpEquip[tmpEquip.length - 4]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 3]), Integer.parseInt(tmpEquip[tmpEquip.length - 2]),
					Integer.parseInt(tmpEquip[tmpEquip.length - 1]));
		}
		
		startLine = 7;
		endLine = 7 + (temporada[nTemporadaAct].nEquips / 2) - 1;
		String[] partits = extreuLinies(myLine.split(System.getProperty("line.separator")), startLine /*7*/, endLine/*15*/);
		
		// Data, EquipLocal, EquipVisitant, Resultat
		temporada[nTemporadaAct].jornades[j-1] = new Partit[partits.length];
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
				
			temporada[nTemporadaAct].jornades[j-1][i] = new Partit(j, data, local, visitant, golsLocal, golsVisitant);
		}
		setSupportProgressBarIndeterminateVisibility(false);
	}

	private Equip getEquip(String nom) {
		for (int i=0; i<temporada[nTemporadaAct].nEquips; i++) {
			if (nom.compareTo(temporada[nTemporadaAct].equip_data[nJornadaAct-1][i].nomEquip) == 0)
				return temporada[nTemporadaAct].equip_data[nJornadaAct-1][i];
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
		showTab(tab.getPosition());
	}

	private void showTab(int position) {
		switch (position) {
		case 0:
			if (temporada[nTemporadaAct].equip_data[nJornadaAct-1][0] == null)
				getJornada(nJornadaAct);
			
			EquipAdapter classifAdapter = new EquipAdapter(this, R.layout.classif_item_row, temporada[nTemporadaAct].equip_data[nJornadaAct-1]);
			classifView.setAdapter(classifAdapter);
			mMenuDrawer.setContentView(activity_classif);
			break;
		case 1:
			if (temporada[nTemporadaAct].jornades[nJornadaAct-1][0] == null)
				getJornada(nJornadaAct);
			
			PartitAdapter partitAdapter = new PartitAdapter(this, R.layout.resultats_item_row, temporada[nTemporadaAct].jornades[nJornadaAct-1]);
			tvJornada.setText("Jornada " + String.valueOf(nJornadaAct));
			resultView.setAdapter(partitAdapter);
			mMenuDrawer.setContentView(activity_result);
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
	
    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }

	@Override
	public void onActiveViewChanged(View v) {
		mMenuDrawer.setActiveView(v, mActivePosition);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	mMenuDrawer.toggleMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
