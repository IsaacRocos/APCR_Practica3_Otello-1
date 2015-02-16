package otello;

import java.awt.*;

public class MiCanvas extends Panel {
	private static final long serialVersionUID = 1L;
	int anchura = 400;
	int altura = anchura;
	int celdas;
	Tablero tablero;
	boolean procesaClick = false;
	Movimiento m;
	
	public MiCanvas(int celdas) 
	{
		this.celdas = celdas;
		this.setSize(anchura,anchura);
	}
	
	public void setTablero(Tablero t)
	{
		tablero = new Tablero(t);
	}
	
        @Override
	public void paint(Graphics g)
	{
		int [][]tab = tablero.getTablero();
		
		setBackground(Color.WHITE);
		// Dibujando las l�neas del tablero
		int anchuraCelda = anchura/celdas;
		g.setColor(Color.BLACK);
		for (int i=0;i<=anchura;i+=anchuraCelda)
		{
			g.drawLine(0,i,anchura,i);
			g.drawLine(i, 0, i, anchura);
		}
		
		// Dibujando las fichas
		for (int i=0;i<celdas;i++)
			for (int j=0;j<celdas;j++)
			{
				switch(tab[i][j])
				{
				case 0: g.setColor(Color.WHITE);
						break;
				case 1: g.setColor(Color.RED);
						break;
				case 2: g.setColor(Color.YELLOW);
						break;
				}
				g.fillOval(j*anchuraCelda+2, i*anchuraCelda+2, anchuraCelda-2, anchuraCelda-2);
			}
	}
	
	// Indicarle al canvas que estamos esperando que mueva el humano
	public void esperandoClick() {
		procesaClick = true;
	}
	
	// Calcula la casilla sobre la que el jugador humano hizo click
	public void calcularMovimiento(int x, int y) {
		int anchuraCelda = anchura/celdas;
		int fila = y/anchuraCelda, columna = x/anchuraCelda;
		
		if (tablero.obtenerCasilla(fila,columna) == 0)
		{
			m = new Movimiento(y/anchuraCelda,x/anchuraCelda);
			procesaClick = false;
                        System.out.println(m);
		}
                
	}
	
	public Movimiento obtenerMovimiento() {
		return m;
	}
	
	public boolean estaEsperandoClick() {
		return procesaClick;
	}
}
