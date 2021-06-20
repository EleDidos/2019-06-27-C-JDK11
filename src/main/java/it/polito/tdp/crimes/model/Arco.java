package it.polito.tdp.crimes.model;

public class Arco {
	
	private String reato1;
	private String reato2;
	private int peso; //n° di stazioni in cui sono avvenuti entrambi
	public Arco(String reato1, String reato2, int peso) {
		super();
		this.reato1 = reato1;
		this.reato2 = reato2;
		this.peso = peso;
	}
	public String getReato1() {
		return reato1;
	}
	public void setReato1(String reato1) {
		this.reato1 = reato1;
	}
	public String getReato2() {
		return reato2;
	}
	public void setReato2(String reato2) {
		this.reato2 = reato2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reato1 == null) ? 0 : reato1.hashCode());
		result = prime * result + ((reato2 == null) ? 0 : reato2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		if (reato1 == null) {
			if (other.reato1 != null)
				return false;
		} else if (!reato1.equals(other.reato1))
			return false;
		if (reato2 == null) {
			if (other.reato2 != null)
				return false;
		} else if (!reato2.equals(other.reato2))
			return false;
		return true;
	}
	
	public String toString() {
		return this.reato1+" - "+this.reato2+" ( "+this.peso+" )";
	}
	
	

}
