import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Okno extends JFrame {

	public int gracz;									//numer gracza: 1 - kolko(host), 2 - krzyzyk(klient)
	public int tura=0;									//0 - tura przeciwnika, 1 - wlasna tura
	public int wygrana;									//0 - gra trwa; 1 - wygral gracz pierwszy; 2 - wygral gracz drugi, 3 - remis
	public int[][] tablica = new int[3][3];				//plansza: 0 - puste, 1 - kolko, 2 - krzyzyk
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private Okno(int gr, String ip){						//konstruktor (przyjmuje numer gracza oraz ip z maina
		
		super("XO");										//naglowek
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//wylaczenie programu przy zamknieciu okna
		setVisible(true);									//ustaw okno jako widoczne
		setResizable(false);								//zabron zmieniania rozmiaru
		setSize(900, 923);									//ustaw rozmiar
		setLocationRelativeTo(null);						//usta na srodku ekranu
		
		Panel panel = new Panel(this);						//stworz panel (bedzie sluzyl do wyswietlania planszy)
		add(panel);											//naloz panel na okno
		reset();											//resetuj plansze
		gracz=gr;											//ustaw gracza
		
		try {															//blok polaczeniowy
			if(gracz==1) {												//jesli host
				ServerSocket serverSocket = new ServerSocket(4999);		//przyjecie
				serverSocket.setReuseAddress(true);
				socket = serverSocket.accept();
			}
			else {														//jesli klient
				socket = new Socket(ip, 4999);							//dolaczenie
			}
			out = new PrintWriter(socket.getOutputStream(), true);								//utworzenie wysylacza
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));			//utworzenie odbieracza
		}
		catch(Exception e) {											//wyjatek w razie nie udanej proby polaczenia
			JOptionPane.showMessageDialog(this, "Nie mo¿na po³¹czyæ z drugim graczem.", "Nawi¹zanie po³¹czenie", JOptionPane.ERROR_MESSAGE);
			System.exit(0);												//powiadomienie i wylaczenie programu
		}
		
		if(gracz==1)													//jesli host
			tura=1;														//host zaczyna
		while(true) {													//petla odbierajaca
			for(int i=0;i<3;i++)									
				for(int j=0;j<3;j++)
					tablica[i][j]=Integer.parseInt(odbierz());			//czekaj az bedziesz mogl odebrac
			panel.repaint();											//przemaluj panel  nowo odebranymi danymi
			tura=1;														//otrzymaj ture
			sprawdzenie();												//sprawdzenie konca rundy

		}
	}
	
	public void reset() {					//funkcja resetujaca plansze
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				tablica[i][j]=0;			//wyzerowanie wartosci kazdego pola planszy
	}
	
	private String odbierz() {				//funkcja odbierajaca
		try {
		return in.readLine();				//czekaj na odebranie
		}
		catch(Exception e) {				//wyjatek jesli nie da sie odebrac
			JOptionPane.showMessageDialog(this, "Utracono po³¹czenie z drugim graczem.", "Utracone po³¹czenie", JOptionPane.ERROR_MESSAGE);
			System.exit(0);					//powiadomienie i wylaczenie programu
		}
		return null;
	}
	
	public void wyslij(String tresc) {		//funkcja wysylajaca
		try {
			out.println(tresc);				//wyslij
		}
		catch(Exception e) {				//wyjatek jesli nie da sie wyslac
			JOptionPane.showMessageDialog(this, "Utracono po³¹czenie z drugim graczem.", "Utracone po³¹czenie", JOptionPane.ERROR_MESSAGE);
			System.exit(0);					//powiadomienie i wylaczenie programu
		}
	}
	
	public void sprawdzenie() {				//funkcja sprawdzajace rezultat gry
		if(tablica[0][0]==tablica[0][1]&&tablica[0][1]==tablica[0][2]&&tablica[0][2]==gracz		//kolumna 1
			||tablica[1][0]==tablica[0][1]&&tablica[1][1]==tablica[0][2]&&tablica[1][2]==gracz	//kolumna 2
			||tablica[2][0]==tablica[0][1]&&tablica[2][1]==tablica[2][2]&&tablica[2][2]==gracz	//kolumna 3
			||tablica[0][0]==tablica[1][0]&&tablica[1][0]==tablica[2][0]&&tablica[2][0]==gracz	//wiersz 1
			||tablica[0][1]==tablica[1][1]&&tablica[1][1]==tablica[2][1]&&tablica[2][1]==gracz	//wiersz 2
			||tablica[0][2]==tablica[1][2]&&tablica[1][2]==tablica[2][2]&&tablica[2][2]==gracz	//wiersz 3
			||tablica[0][0]==tablica[1][1]&&tablica[1][1]==tablica[2][2]&&tablica[2][2]==gracz	//skos 1
			||tablica[0][2]==tablica[1][1]&&tablica[1][1]==tablica[2][0]&&tablica[2][0]==gracz	//skos 2
			)
			wygrana=gracz;																		//wygral gracz
		
		else if(tablica[0][0]==tablica[0][1]&&tablica[0][1]==tablica[0][2]&&tablica[0][2]!=0	//kolumna 1
			||tablica[1][0]==tablica[0][1]&&tablica[1][1]==tablica[0][2]&&tablica[1][2]!=0		//kolumna 2
			||tablica[2][0]==tablica[0][1]&&tablica[2][1]==tablica[2][2]&&tablica[2][2]!=0		//kolumna 3
			||tablica[0][0]==tablica[1][0]&&tablica[1][0]==tablica[2][0]&&tablica[2][0]!=0		//wiersz 1
			||tablica[0][1]==tablica[1][1]&&tablica[1][1]==tablica[2][1]&&tablica[2][1]!=0		//wiersz 2
			||tablica[0][2]==tablica[1][2]&&tablica[1][2]==tablica[2][2]&&tablica[2][2]!=0		//wiersz 3
			||tablica[0][0]==tablica[1][1]&&tablica[1][1]==tablica[2][2]&&tablica[2][2]!=0		//skos 1
			||tablica[0][2]==tablica[1][1]&&tablica[1][1]==tablica[2][0]&&tablica[2][0]!=0		//skos 2
			)
			if(gracz==1)																		//wygral przeciwnik
				wygrana=2;
			else
				wygrana=1;
		
		else if(tablica[0][0]!=0&&tablica[1][0]!=0&&tablica[2][0]!=0		//czy wszystkie pola niepuste
			&&tablica[0][1]!=0&&tablica[1][1]!=0&&tablica[2][1]!=0
			&&tablica[0][2]!=0&&tablica[1][2]!=0&&tablica[2][2]!=0
			)
			wygrana=3;														//remis
		
		else							//gra trwa dalej
			wygrana=0;
		if(wygrana==gracz) {			//jesli wygral gracz
			reset();
		}
		
		else if(wygrana==3) {			//jesli jest remis
			reset();
		}
		
		else if(wygrana!=0) {			//jesli wygral przeciwnik
			reset();
		}
		
		
	}
	
	public static void main(String[] args) {				//main rozpoczynajacy program
		
		//okno dialogowe pozwalajace wybrac hostowanie lub dolaczenie
        String[] options = {"Hostuj", "Do³¹cz"};
        String wybor = (String)JOptionPane.showInputDialog(null, "Wybierz czy chcesz byæ hostem, czy do³¹czyæ do gry:",
                "Host/Join", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        //okno dialogowe, jesli wybrano hosta, wyswietlajace ip i pozwalajace uruchomic gre
        if(wybor.charAt(0)=='H') {
        	try {
        	JOptionPane.showConfirmDialog(null,"IP serwera: " + InetAddress.getLocalHost().getHostAddress()+"\nNaciœnij ok, aby uruchomiæ grê jako host","Host", JOptionPane.DEFAULT_OPTION);
        			new Okno(1,null);			//wlaczenie gry jako gracz numer 1 (czyli host grajacy kolkiem) nie podajac ip
        	}
        	catch(Exception e) {				//wyjatek dotyczacy niemoznosci uzyskania ip
        		System.exit(0);
        	}
        }
        //okno dialowoe, jesli nie wybrano hosta, pozwalajace wpisac ip i uruchomic gre
        else {
    		String ip = (String) JOptionPane.showInputDialog(null, "Podaj IP hosta", "IP hosta",
    				JOptionPane.INFORMATION_MESSAGE, null, null, "localhost");
    		new Okno(2,ip);						//wlaczenie gry jako gracz numer 2 (czyli klient grajacy krzyzykiem) podajac ip jako String
        }
	}
}
