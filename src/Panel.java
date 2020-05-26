import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener{

	private Okno okno;		//wskaznik na obiekt macierzysty
	
	public Panel(Okno okno){		//konstruktor (przyjmuje wskaznik na obiekt macierzysty
		this.okno=okno;				//ustawienie wskaznika na obiekt macierzysty
		addMouseListener(this);		//uruchomienie MouseListenera niezbednego do wykorzystania myszki
	}
	
	public void paint(Graphics g){															//funkcja renderujaca obraz na panelu
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(okno.tablica[i][j]==0)
					new ImageIcon("pictures/0.png").paintIcon(this, g, i*300, j*300);		//rysowanie pustego pola
				else if(okno.tablica[i][j]==1)
					new ImageIcon("pictures/1.png").paintIcon(this, g, i*300, j*300);		//rysowanie kolka
				else if(okno.tablica[i][j]==2)
					new ImageIcon("pictures/2.png").paintIcon(this, g, i*300, j*300);		//rysowanie krzyzyka
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {									//puszczenie myszki uruchamia funkcje
		if(okno.tura==1&&okno.tablica[e.getX()/300][e.getY()/300]==0) {			//jesli wlasciwa tura i nacisniete pole puste
			okno.tablica[e.getX()/300][e.getY()/300]=okno.gracz;				//nalozenie figury
			repaint();															//render planszy
			for(int i=0;i<3;i++)
				for(int j=0;j<3;j++)
					okno.wyslij(okno.tablica[i][j]+"");							//wyslanie calej tablicy drugiemu graczowi
			okno.tura=0;														//oddanie tury
			okno.sprawdzenie();													//sprawdzenie konca rundy
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {		//nieuzywane funkcje dotyczace obslugi myszki
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
}
