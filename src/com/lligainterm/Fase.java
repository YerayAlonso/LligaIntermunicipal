package com.lligainterm;

public class Fase {

    String mTitle;
    int mIconRes;
    int nTemporada;
    int nFase;
    
	String id_temporada = "223";
	String id_esport = "4";
	String id_categoria = "13";
	String grup = "FS 12-13";

    Fase(String title, int iconRes, int tempo, int fase) {
        mTitle = title;
        mIconRes = iconRes;
        nTemporada = tempo;
        nFase = fase;
                
        switch (tempo) {
        	case 1213: {
        		id_temporada = "223";
        		grup = "FS 12-13";
        		break;
        	}
        	case 1314: {
        		id_temporada = "261";
        		grup = "1 VOLTA";
        		break;
        	}
        }
    }
}
