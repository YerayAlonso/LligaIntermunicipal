package com.lligainterm;

public class Equip {
	//Posicio, Equip, Punts, PJ, PG, PE, PP, NP, GF, GC
	public String nomEquip;
	public int posicio;
	public int punts;
	public int jugats;
	public int guanyats;
	public int empatats;
	public int perduts;
	public int np;
	public int golsF;
	public int golsC;
    
    public Equip(){
        super();
    }
    
    public Equip(String nom, int pos, int punts, int jugats, int guanyats, int empatats, int perduts, int np, int golsF, int golsC) {
        super();
        this.nomEquip = nom;
        this.posicio = pos;
        this.punts = punts;
        this.jugats = jugats;
        this.guanyats = guanyats;
        this.empatats = empatats;
        this.perduts = perduts;
        this.np = np;
        this.golsF = golsF;
        this.golsC = golsC;
    }
}
