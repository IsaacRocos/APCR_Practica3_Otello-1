// Clase que controla el funcionamiento del juego (cuándo le toca tirar a cada jugador, cuando
// se acaba la partida, etc.)

import javax.swing.JButton;


public class Juego extends Thread{
	
	private MiCanvas C;
	private int CELDAS;
	private Tablero t;
	JButton b1, b2;
	JugadorHumano jugador1;
	JugadorMaquina jugador2;
	
	public Juego(MiCanvas canvas, int CELD, JButton bot1, JButton bot2)
	{
		super();
		
		C = canvas;
		CELDAS = CELD;
		b1 = bot1;
		b2 = bot2;
	}
	
	private void inicializar() {
		t = new Tablero(CELDAS);
		C.setTablero(t);
		C.repaint();
		
		jugador1 = new JugadorHumano();
		jugador2 = new JugadorMaquina(false); // El valor false indica que el jugador máquina juega
											  // con las amarillas
	}
	
	
	public void run() {
		inicializar();
		
		boolean turno_rojo = true;
		int color;
		Movimiento m;
		
		// La partida se desarrolla hasta que el tablero esté lleno o ya no se puedan tirar más veces
		// (por ejemplo porque alguno de los jugadores se haya quedado sin fichas)
		while(!t.estaLleno() && (t.puedeTirar(1) || t.puedeTirar(2)))
		{
			m = null;
			color = 0;
			// Lo que se hace realmente es esperar el movimiento devuelto por el método run tanto
			// del humano como de la máquina
			if (turno_rojo) {
				if (t.puedeTirar(1)) {
					jugador1.setTablero(t);
					jugador1.setCanvas(C);
					m = jugador1.run();
					color = 1;
				} else {
					System.out.println("El jugador rojo no puede tirar");
				}
			} else {
				if (t.puedeTirar(2)) {
					jugador2.setTablero(t);
					m = jugador2.run();
					color = 2;
				} else {
					System.out.println("El jugador amarillo no puede tirar");
				}
			}
			turno_rojo = !turno_rojo;
			
			if (m!=null) {
				t.ponerFicha(m.columna,m.fila,color);
				C.setTablero(t);
				C.repaint();
			}
		}
		
		switch(t.ganador()){
		case 0:
			System.out.println("Ha habido un empate");
			break;
		case 1:
			System.out.println("Ha ganado el jugador rojo");
			break;
		case 2:
			System.out.println("Ha ganado el jugador amarillo");
			break;
		}
		
		// Con esto la partida termina y se activa el botón de comenzar una nueva partida.
		b1.setEnabled(true);
		b2.setEnabled(false);
	}
}
