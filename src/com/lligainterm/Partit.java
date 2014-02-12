package com.lligainterm;

import java.util.Date;

public class Partit {
	public int jornada;
	public Date data;
	public Equip local, visitant;
	public int golsLocal, golsVisitant;
	
    public Partit(){
        super();
    }
    
    public Partit(int jornada, Date data, Equip local, Equip visitant, int golsLocal, int golsVisitant){
        super();
        this.jornada = jornada;
        this.data = data;
        this.local = local;
        this.visitant = visitant;
        this.golsLocal = golsLocal;
        this.golsVisitant = golsVisitant;
    }
}
