// Clase que representa al jugador no humano. Cuando le toca tirar, la clase Juego llama a 
// su método run

import java.util.ArrayList;


public class JugadorMaquina extends Jugador {
	boolean m_rojo; //vale true si es el jugador 1 (rojo)
	private Tablero tablero;
	private int MAXIMO_NIVEL=5; // Nivel de profundidad del árbol de jugadas. A mayor nivel, mejor
								// jugará la máquina, pero el coste de cada tirada también será mayor
	
	public JugadorMaquina(boolean rojo) {
		m_rojo = rojo;
	}
	
	public void setTablero(Tablero t) {
		tablero = t;
	}
	
	public Movimiento run() {
		// Movimiento NO inteligente. La máquina tira en la primera casilla libre donde pueda hacerlo
		/*int fila, columna;
		
		try {
		Thread.sleep(2000);
		} catch (Exception e) {}
		
		fila = 0; columna = -1;
		boolean correcto = false;
		
		while (!correcto) {
			columna++;
			if (columna >= tablero.getCeldas()) {
				columna = 0;
				fila++;
			}
			
			correcto = tablero.movimientoCorrecto(new Movimiento(fila,columna),2);
		}*/
		
		// Retardo de dos segundos introducido para que le de tiempo al humano a comprobar
		// el resultado de su tirada
		try {
			Thread.sleep(2000);
			} catch (Exception e) {}
		
		Movimiento m = alfabeta();
		
		return m;
	}
	
	// Función a partir de la que comienza el algoritmo Alfa Beta mediante llamadas recursivas
	// al método alfabeta_recursivo. En el método alfabeta se procesa el nodo raíz del árbol,
	// que es un nodo max
	private Movimiento alfabeta() {
		// Inicialización de los valores de alfa y beta como menos infinito y más infinito respectivamente
		int alfa=-Integer.MAX_VALUE;
		int beta = Integer.MAX_VALUE;
		
		int filaM = -1, columnaM = -1;
		int jugador = 2;
		
		// Estos dos arraylists van a almacenar los movimientos correspondientes a los sucesores
		// del nodo actal
		ArrayList<Integer> filasS = new ArrayList<Integer>();
		ArrayList<Integer> columnasS = new ArrayList<Integer>();
		
		// Determinamos el color de la ficha del jugador correspondiente al nodo
		if (m_rojo) jugador = 1;
		// Obtenemos los movimientos de los hijos
		sucesores(tablero, filasS, columnasS, jugador);
		int i=0;
		// Vamos procesando los hijos hasta procesarlos todos o hasta que se produzca una poda
		// (que se producirá si alfa>=beta)
		while (alfa < beta && i < filasS.size()) {
			// Generamos un nuevo tablero en el que se refleje el movimiento del nodo hijo
			Tablero nodo = new Tablero(tablero);
			nodo.ponerFicha(columnasS.get(i), filasS.get(i), jugador);
			// Y llamamos a alfabeta_recursivo para procesar dicho nodo.
			int devuelto = alfabeta_recursivo(nodo, 1,!m_rojo,alfa,beta);
			// alfa será el máximo entre el valor ya existente y el valor devuelto por
			// alfabeta recursivo, porque el nodo raíz es un nodo MAX
			if (devuelto > alfa) {
				alfa = devuelto;
				// En el nodo raíz almacenamos el movimiento correspondiente al hijo más prometedor
				// (el de mayor alfa)
				filaM = filasS.get(i);
				columnaM = columnasS.get(i);
			}
			
			i++;
		}
		
		return new Movimiento(filaM, columnaM);
	}
	
	// Método recursivo con el que se procesarán los nodos internos del árbol y las hojas (todos los
	// nodos menos la raíz)
	private int alfabeta_recursivo(Tablero nodo, int nivel, boolean color, int alfa, int beta) {
		int jugador = 2;
		ArrayList<Integer> filasS = new ArrayList<Integer>();
		ArrayList<Integer> columnasS = new ArrayList<Integer>();
		
		if (color) jugador = 1;
		
		// Si el nodo es un nodo hoja se evalúa
		if (esHoja(nodo,nivel,jugador))
			return funcionEvaluacion(nodo);
		
		// Si no es un nodo hoja se obtienen los movimientos correspondientes a sus nodos hijos
		sucesores(nodo, filasS, columnasS, jugador);
		int i = 0;
		
		if (nivel%2 ==0) { // NODO MAX
			// Procesamos todos los sucesores hasta el final o hasta que se produzca una poda
			// (porque alfa>=beta)
			while (alfa < beta && i < filasS.size()) {
				// Creamos el nodo hijo y llamamos recursivamente a alfabeta recursivo
				Tablero nodo2 = new Tablero(nodo);
				nodo2.ponerFicha(columnasS.get(i), filasS.get(i), jugador);
				int devuelto = alfabeta_recursivo(nodo2, nivel+1,!color,alfa,beta);
				// Como es un nodo max se actualiza el valor de alfa. Alfa será el máximo entre
				// el valor ya existente y el devuelto por el hijo
				if (devuelto > alfa) {
					alfa = devuelto;
				}
		
				i++;
			}
			// Si se produce una poda, como estamos en un nodo MAX se deuvelve el valor de beta. Si
			// se han procesado todos los hijos sin podas se devuelve el valor de MAX.
			if (alfa<beta) return alfa; else return beta;
			
		}
		else // NODO MIN
		{
			// El procesamiento es exactamente igual que en el caso de un nodo MAX. Las únicas diferencias
			// son que se actualiza el valor de beta en lugar del valor de alfa (siendo beta el mínimo entre
			// el valor que ya se tenía y el devuelto por la llamada recursiva para el hijo), que si no hay
			// poda se devuelve el valor de beta y que si hay poda se devuelve el valor de alfa
			while (alfa < beta && i < filasS.size()) {
				Tablero nodo2 = new Tablero(nodo);
				nodo2.ponerFicha(columnasS.get(i), filasS.get(i), jugador);
				int devuelto = alfabeta_recursivo(nodo2, nivel+1,!color,alfa,beta);
				if (devuelto < beta) {
					beta = devuelto;
				}
		
				i++;
			}
			if (alfa<beta) return beta; else return alfa;
		}
	}
	
	// Un nodo será un nodo hoja y no será evaluado si no tiene hijos (no se pueden realizar más 
	// movimientos). Esto sucederá o bien porque ha terminado la partida (el tablero está lleno
	// o ninguno de los jugadores puede tirar) o bien porque se ha alcanzado el máximo nivel
	// de profundidad establecido para el árbol
	private boolean esHoja(Tablero nodo, int nivel, int jugador)
	{
		return (nivel == MAXIMO_NIVEL || nodo.estaLleno() || !nodo.puedeTirar(jugador)); 
		
	}
	
	// Función que evalúa un nodo hoja, devolviendo un valor numérico que indica lo favorable
	// que es la situación correspndiente al nodo hoja para la máquina. Cuanto mayor sea el valor
	// mejor para la máquina
	private int funcionEvaluacion(Tablero nodo) {
		int i, j;
		int valor = 0;
		int jugador = 2;
		if (m_rojo) jugador = 1;
		
		// Primero contamos el número de fichas de cada color, añadiendo uno por cada ficha
		// del color de la máquina y restando 1 por cada ficha del color del jugador
		for (i=0;i<nodo.getCeldas();i++)
			for (j=0;j<nodo.getCeldas();j++)
				if (nodo.obtenerCasilla(i, j) != 0)
					if (nodo.obtenerCasilla(i,j) == jugador)
						valor++;
					else
						valor--;
		
		// Y ahora añadimos mucha puntuación si la máquina tiene fichas en la esquina, y quitamos
		// mucha puntuación si el humano tiene fichas en las esquinas. En el juego del otelo
		// tener fichas en la esquina da mucha ventaja estratégica
		if (nodo.obtenerCasilla(0,0) != 0)
		{
			if (nodo.obtenerCasilla(0,0) == jugador)
				valor+=100;
			else
				valor-=100;
		}
		if (nodo.obtenerCasilla(0,nodo.getCeldas()-1) != 0)
		{
			if (nodo.obtenerCasilla(0,nodo.getCeldas()-1) == jugador)
				valor+=100;
			else
				valor-=100;
		}
		if (nodo.obtenerCasilla(nodo.getCeldas()-1,0) != 0)
		{
			if (nodo.obtenerCasilla(nodo.getCeldas()-1,0) == jugador)
				valor+=100;
			else
				valor-=100;
		}
		if (nodo.obtenerCasilla(nodo.getCeldas()-1,nodo.getCeldas()-1) != 0)
		{
			if (nodo.obtenerCasilla(nodo.getCeldas()-1,nodo.getCeldas()-1) == jugador)
				valor+=100;
			else
				valor-=100;
		}
		
		return valor;
	}
	
	// Método para determinar los posibles movimientos que puede realizar el jugador 'jugador' a partir
	// del tablero almacenado en 'nodo'
	private void sucesores(Tablero nodo, ArrayList<Integer>filas, ArrayList<Integer>columnas, int jugador) {
		int fila, columna;
		boolean correcto;
		
		fila = 0; columna = 0;
		
		do {
			correcto = nodo.movimientoCorrecto(new Movimiento(fila,columna),jugador);
			if (correcto)
			{
				filas.add(fila);
				columnas.add(columna);
			}
			
			columna++;
			if (columna >= tablero.getCeldas()) {
				columna = 0;
				fila++;
			}
		} while (fila<nodo.getCeldas() && columna<nodo.getCeldas()) ;
	}
}
