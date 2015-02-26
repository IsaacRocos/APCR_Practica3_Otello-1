
public class Tablero {
	int [][]tablero;
	// Un 0 significa que no hay ninguna ficha en la casilla correspondiente
	// Un 1 significa que hay una ficha del primer jugador
	// Un 2 significa que hya una ficha del segundo jugador
	int celdas;
	
	public Tablero(int CELDAS)
	{
		celdas = CELDAS;
		tablero = new int[CELDAS][CELDAS];
		for (int i=0;i<CELDAS;i++)
			for (int j=0;j<CELDAS;j++)
				tablero[i][j] = 0;
		
		tablero[CELDAS/2-1][CELDAS/2-1] = 1;
		tablero[CELDAS/2][CELDAS/2-1] = 2;
		tablero[CELDAS/2-1][CELDAS/2] = 2;
		tablero[CELDAS/2][CELDAS/2] = 1;
	}
	
	public Tablero(Tablero t)
	{
		this.celdas = t.getCeldas();
		tablero = new int[this.celdas][this.celdas];
		
		int [][]tab = t.getTablero();
		
		for (int i=0;i<celdas;i++)
			for (int j=0;j<celdas;j++)
				tablero[i][j] = tab[i][j];
	}
	
	public int getCeldas()
	{
		return celdas;
	}
	
	public int[][]getTablero()
	{
		return tablero;
	}
	
	public void ponerFicha(int x, int y, int color)
	{	
		tablero[y][x] = color;
		
		int []posy = {-1, -1, -1, 0, 1, 1, 1, 0};
		int []posx = {-1, 0, 1, 1, 1, 0, -1, -1};
		
		for (int i=0;i<8;i++)
			if (direccionCorrecta(y, x, posy[i], posx[i], color))
				rellenarColor(y, x, posy[i], posx[i],  color);
		
	}
	
	public boolean estaLleno()
	{
		boolean lleno = true;
		
		int i=0, j=0;
		
		while (lleno && i <celdas)
		{
			while (lleno && j < celdas)
			{
				if (tablero[i][j] == 0)
					lleno = false;
				j++;
			}
			j = 0;
			i++;
		}
		
		return lleno;
	}
	
	public int obtenerCasilla(int fila, int columna) {
		return tablero[fila][columna];
	}
	
	private void rellenarColor(int f, int c, int dy, int dx, int color) {
		int fila = f+dy;
		int columna = c+dx;
		
		while (fila >= 0 && fila < celdas && columna >= 0 && columna < celdas && tablero[fila][columna] != color)
		{
			tablero[fila][columna] = color;
			fila += dy;
			columna += dx;
		}
	}
	
	private boolean direccionCorrecta(int f, int c, int dy, int dx, int color) {
		int fila, columna;
		
		fila = f + dy;
	    columna = c + dx;
	    
	    if (fila < 0 || fila >= celdas || columna < 0 || columna >= celdas)
	    	return false;
	    
	    if (tablero[fila][columna] != color && tablero[fila][columna] != 0)
	    {
	    	boolean correcto = false;
	    	
	    	fila = f + dy;
		    columna = c + dx;
	    	
	    	while (fila >= 0 && fila < celdas && columna >=0 && columna < celdas && !correcto)
	    	{
	    		if (tablero[fila][columna] == color)
	    			correcto = true;
	    		
	    		fila += dy;
	    		columna += dx;	
	    	}
	    	
	    	return correcto;
	    	
	    }
	    else
	    	return false;
	}
	
	public boolean puedeTirar(int color) {
		int i, j;
		
		i = 0;
		j = 0;
		while (i<celdas && !movimientoCorrecto(new Movimiento(i,j),color))
		{
			j++;
			if (j>=celdas)
			{
				i++;
				j = 0;
			}
		}
		
		return (i<celdas);
	}
	
	public boolean movimientoCorrecto(Movimiento m, int color) {
		boolean correcto = true;
		
		if (tablero[m.fila][m.columna] != 0)
			correcto = false;
		else {
			return (direccionCorrecta(m.fila, m.columna, -1,-1,color) ||
					direccionCorrecta(m.fila, m.columna, -1,0,color) ||
					direccionCorrecta(m.fila, m.columna, -1,1,color) ||
					direccionCorrecta(m.fila, m.columna, 0,1,color) ||
					direccionCorrecta(m.fila, m.columna, 1,1,color) ||
					direccionCorrecta(m.fila, m.columna, 1,0,color) ||
					direccionCorrecta(m.fila, m.columna, 1, -1,color) ||
					direccionCorrecta(m.fila, m.columna,0,-1,color));
		}
		
		return correcto;
	}
	
	public int ganador() {
		int []numFichas = {0,0,0};
		
		for (int f=0;f<celdas;f++)
			for (int c=0;c<celdas;c++)
				numFichas[tablero[f][c]]++;
		
		if (numFichas[1] > numFichas[2]) return 1;
		else if (numFichas[2] > numFichas[1]) return 2;
		else return 0;
	}
}
