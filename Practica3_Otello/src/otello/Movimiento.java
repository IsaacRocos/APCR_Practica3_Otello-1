package otello;


public class Movimiento {
	public int fila;
	public int columna;
	
	public Movimiento(int f, int c) {
		fila = f;
		columna = c;
	}

    @Override
    public String toString() {
        return "Movimiento en: " + fila +","+ columna;
    }
        
        
}
