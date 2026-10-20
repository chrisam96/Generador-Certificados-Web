package mx.csam.certificados.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import mx.csam.certificados.utilerias.records.CertificadoWriteGUIRecord;

public class Main extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Main main = this;
	public static CertificadoWriteGUIRecord param = null;
	public WriteFormulario wf = null;

	//private JFrame ven =  new JFrame();
	private static JFrame ven =  null;
	private JScrollPane scrollEscritura = new JScrollPane();
	private JScrollPane scrollLectura = new JScrollPane();
	private JPanel panelEscritura;// = new JPanel();
	private JPanel panelNomsArchivos;
	private JPanel panelLectura;

	private final JLabel lblPanelNomArchivos = new JLabel();
	private final JTextField txtCerti = new JTextField("");
	private final JTextField txtPubK = new JTextField("");
	private final JTextField txtPriK = new JTextField("");
	
	private final JLabel lblPanelLectura = new JLabel("");
	private final JTextArea txA_Lectura = new JTextArea();

	private JButton btnEscritura;
	
	private JLabel lblCargarCer = new JLabel("");
	private JButton btnCargarCer = new JButton("");
	private final JTextField txtCargarCer = new JTextField("");
	private JButton btnCargarPubk = new JButton("");
	private JLabel lblCargarPubk = new JLabel("");
	private final JTextField txtCargarPubk = new JTextField("");
	private JLabel lblCargarPrik = new JLabel("");	
	private JButton btnCargarPrik = new JButton("");
	private final JTextField txtCargarPrik = new JTextField("");
	private JButton btnAnalizar = new JButton("");	
	
	/*
	 * JFrame: 600x400
	 * ContentPane: 586x340
	 * size:java.awt.Dimension[width=600,height=400]
	 * size ContentPane:java.awt.Dimension[width=586,height=340]
	 * Insets: java.awt.Insets[top=30,left=7,bottom=7,right=7]
	 * JMenuBar = 400 - 7 bottom - 30 top - 340 ContentPane = 23 height
	 * 
	 * Ambos bordes del JFrame descuentan 14
	 */	
	
	//TAMAÑOS
	//JFrame
	static int wJFrame = 900;
	static int hJFrame = 700;
	
	//Content Pane
	int wContentPane = 886;
	int hContentPane = 663;
	
	//Bordes JFrame
	/*
	final int bordes = 7;
	final int bordeSup = 30;
	final int hJMenuBar = 23;
	final int h_Scroll_Hor = 23;
	*/
		
	
	//Posiciones de los bordes
	final int pos_bordeIzq = 0;
	final int pos_bordeSup = 0;
	
	//ScrollEscritura
	int wScrollEsc = wContentPane;
	int hScrollEsc = 120;

	//Separador
	final int separador = 10;
	
	//ScrollLectura
	int wScrollLec = wContentPane;
	int hScrollLec = hContentPane - hScrollEsc - separador ;
	int pos_hScrollLec = hContentPane - hScrollLec;		

	
	static {
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.ven.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//DATOS DE JFRAME
		ven =  new JFrame();
		ven.setName("ven");
		ven.setBounds(new Rectangle(100, 100, wJFrame, hJFrame));
		System.out.println(" PRIMERO DE TODOS\n"+ven.getBounds() + "\n\n\n");
		ven.setType(Type.POPUP);
		//ven.setBounds(new Rectangle(100, 100, wJFrame, hJFrame));
		//Forzar la visualización (para obtener las medidas del ContentPane)
		ven.setVisible(true);
		ven.addNotify();
		System.out.println("**** Dimensiones del ContentPane(): "+ven.getContentPane().getBounds());
		//-----------------------------------------------------------------
		ven.setResizable(false);
		ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//AbsoluteLayout
		ven.getContentPane().setLayout(null);
		//Aparecer en el Centro 
		ven.setLocationRelativeTo(null);		
		
		
		/*NOTAS:
		 * >> Contenedores internos de los componentes:
		 * 
		 * Para acceder al espacio del área visible disponible
		 * dentro de algún contenedor, varia con métodos:
		 * 
		 * - getContentPane() es propio de un JFrame/JDialog
		 * - getViewport() es propio es JScrollPane
		 * - JPanel es accesible directamente (sin intermediarios)
		 * 
		 * Para obtener sus medidas es atráves de:
		 * - JFrame/JDialog -> getSize()/getBounds()
		 * - JScrollPane -> getExtentSize()/getBounds()
		 * - JPanel -> getSize()/getBounds()
		 * 
		 * Normamlente:
		 * getSize() devuelve un Dimension
		 * getBounds() devuelve un Rectangle
		 * 
		 * >> Hacer que JPanel ocupe todo el viewport
		 * Para hacer que un JPanel ocupe todo el espacio interno 
		 * del JScrollPane, es decir, su viewport, se debe settear
		 * las medidas obtenidas a partir del Viewport del JScrollPane
		 * mediante
		 * 
		 * Dimension d = jScrollPane.getViewport().getExtentSize()
		 * 
		 * jPanel.setBounds(x, y, d.width, d.height)
		 * */
		
		//-----------------------------------------------------------------
		//DATOS JPANEL ESCRITOR
		panelNomsArchivos = new JPanel();
		panelNomsArchivos.setName("panelEscritura");
		panelNomsArchivos.setLayout(null);
		panelNomsArchivos.setBackground(new Color(128, 255, 128));
		//panelEscritor.setBounds(new Rectangle(0, 0, 200, 60));
		
		//-----------------------------------------------------------------
		//DATOS JPANEL ESCRITURA
		panelEscritura = new JPanel();
		panelEscritura.setName("panelEscritura");
		panelEscritura.setLayout(null);
		panelEscritura.add(panelNomsArchivos);
		
		//-----------------------------------------------------------------
		//DATOS JSCROLLPANE ESCRITURA
		scrollEscritura.setName("scrollEscritura");		
		scrollEscritura.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollEscritura.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollEscritura.setAutoscrolls(false);
		scrollEscritura.setBounds(pos_bordeIzq, pos_bordeSup, wScrollEsc, hScrollEsc);
		scrollEscritura.setViewportBorder(new TitledBorder(null, "Escritura", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollEscritura.setViewportView(panelEscritura);		
		
		//>> MEDIDAS DE LOS CONTENEDORES INTERNOS DEL JSCROLLPANE (scrollEscritura) 
		
		// PRIMERO: agregar el JScrollPane al JFrame
		ven.getContentPane().add(scrollEscritura);			
		// FORZAR EL LAYOUT
		ven.validate();
		// AHORA sí: obtener el espacio real del viewport
		Dimension d = scrollEscritura.getViewport().getExtentSize();
		System.out.println("Viewport: " + d);
		//Se llena todo el espacio interno de JScrollPane con el JPanel
		panelEscritura.setBounds(0, 0, d.width, d.height);
		panelNomsArchivos.setBounds(
			0, 0, 
			(int) (d.width * (4.0/5.0)), 
			//(int) (d.height * (2.0/3.0))
			(int) (d.height)
		);
		
		//-----------------------------------------------------------------
		//ETIQUETAS
		lblPanelNomArchivos.setBounds(10, 5, 640, 16);
		lblPanelNomArchivos.setText("Oprima el botón \"Generar\" y rellene el formulario para crear el Certificado .cer y las keys correspondientes");
		lblPanelNomArchivos.setForeground(Color.BLACK);
		
		txtCerti.setBounds(10, 35, 640, 16);
		txtCerti.setText("certificado.cer");
		txtCerti.setForeground(Color.GRAY);
		txtCerti.setEnabled(true);
		txtCerti.setEditable(false);// Evita que el usuario escriba
		txtCerti.setBorder(null); // Quita el borde de caja de texto
		txtCerti.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCerti.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		txtPubK.setBounds(10, 55, 640, 16);
		txtPubK.setText("public_key.pubkey");		
		txtPubK.setForeground(Color.GRAY);
		txtPubK.setEnabled(true);
		txtPubK.setEditable(false);// Evita que el usuario escriba
		txtPubK.setBorder(null); // Quita el borde de caja de texto
		txtPubK.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtPubK.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		txtPriK.setBounds(10, 75, 640, 16);
		txtPriK.setText("private_key.key");		
		txtPriK.setForeground(Color.GRAY);
		txtPriK.setEnabled(true);
		txtPriK.setEditable(false);// Evita que el usuario escriba
		txtPriK.setBorder(null); // Quita el borde de caja de texto
		txtPriK.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtPriK.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		//-----------------------------------------------------------------
		//JBUTTON Escritura
		btnEscritura = new JButton("Generar");
		btnEscritura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//wf = new WriteFormulario(main, "Formulario", true);
				//wf = new WriteFormulario(new JDialog(main, "Formulario", true));
				wf = new WriteFormulario(main);
				wf.dia.setVisible(true);
				System.out.println("**** DESPUES DE SET_VISIBLE()");
			}
		});
		btnEscritura.setBounds(742, 10, 84, 20);
		btnEscritura.setName("btnGenerar");
				
		panelNomsArchivos.add(lblPanelNomArchivos);
		panelNomsArchivos.add(txtCerti);
		panelNomsArchivos.add(txtPubK);
		panelNomsArchivos.add(txtPriK);
		
		panelEscritura.add(btnEscritura);
		
		//-----------------------------------------------------------------
		//SEPARADOR FIJO
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setName("horizontalStrut");
		horizontalStrut.setBounds(pos_bordeIzq, hScrollEsc, wContentPane, separador);
		ven.getContentPane().add(horizontalStrut);

		//-----------------------------------------------------------------
		//DATOS JPANEL LECTURA
		panelLectura = new JPanel();
		panelLectura.setName("panelLectura");		
		panelLectura.setLayout(null);
		//Con esto se fuerza a que aparezcan los scrollbars
		panelLectura.setPreferredSize(new Dimension(2000, 900));
				
		
		//-----------------------------------------------------------------
		//DATOS JSCROLLPANE LECTURA		
		scrollLectura.setName("scrollPaneLectura");
		scrollLectura.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLectura.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollLectura.setAutoscrolls(true); 
		scrollLectura.setBounds(new Rectangle(pos_bordeIzq, pos_hScrollLec, wScrollLec, hScrollLec));
		scrollLectura.setViewportBorder(new TitledBorder(null, "Lectura", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		scrollLectura.setViewportView(panelLectura);
		ven.getContentPane().add(scrollLectura);
		
		ven.validate();
		
		d = scrollLectura.getViewport().getExtentSize();
		Rectangle bounds = scrollLectura.getBounds();
		
		System.out.println("Viewport Lectura: " + d);
		System.out.println("Viewport Lectura (Bounds): " + bounds + "\n\n");
		//Se llena todo el espacio interno de JScrollPane con el JPanel
		panelLectura.setBounds(0, 0, d.width, d.height);
		
		//-----------------------------------------------------------------
		//ETIQUETAS LECTURA
		bounds = panelLectura.getBounds();
		
		lblPanelLectura.setText("Aquí se mostrará la información sobre los archivos");
		lblPanelLectura.setBounds(10, 10, 640, 16);		
		lblPanelLectura.setForeground(Color.BLACK);
				
		txA_Lectura.setBounds(10, 40 , 640, 440);
		txA_Lectura.setEditable(false);
		txA_Lectura.setEnabled(true);
		txA_Lectura.setText("Sexo");
		
				
		lblCargarCer.setBounds(680, 10, 140, 16);
		lblCargarCer.setText("Cargar Certificado");
		lblCargarCer.setForeground(Color.BLACK);
		
		final Font fuente = txtCargarCer.getFont();
		infoFuente(fuente);
		
		btnCargarCer.setBounds(680, 30, 84, 20);
		btnCargarCer.setText("cargar");
		btnCargarCer.setName("btnCargarCer");
		
		txtCargarCer.setBounds(680, 55, 400, 16);
		txtCargarCer.setText("seleccione un archivo");
		txtCargarCer.setForeground(Color.GRAY);
		txtCargarCer.setBackground(Color.WHITE);
		txtCargarCer.setFont(new Font(fuente.getName(), fuente.BOLD, fuente.getSize()));
		infoFuente(txtCargarCer.getFont());
		txtCargarCer.setEnabled(true);
		txtCargarCer.setEditable(false);// Evita que el usuario escriba
		txtCargarCer.setBorder(null); // Quita el borde de caja de texto
		txtCargarCer.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtCargarCer.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		lblCargarPubk.setBounds(680, 110, 140, 16);
		lblCargarPubk.setText("Cargar Public Key");
		lblCargarPubk.setForeground(Color.BLACK);
		
		btnCargarPubk.setBounds(680, 130, 84, 20);
		btnCargarPubk.setText("cargar");
		btnCargarPubk.setName("btnCargarPubk");
		
		txtCargarPubk.setBounds(680, 155, 400, 16);
		txtCargarPubk.setText("seleccione un archivo");
		txtCargarPubk.setForeground(Color.GRAY);
		txtCargarPubk.setBackground(Color.WHITE);
		txtCargarPubk.setFont(new Font(fuente.getName(), fuente.BOLD, fuente.getSize()));
		txtCargarPubk.setEnabled(true);
		txtCargarPubk.setEditable(false);// Evita que el usuario escriba
		txtCargarPubk.setBorder(null); // Quita el borde de caja de texto
		txtCargarPubk.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtCargarPubk.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		lblCargarPrik.setBounds(680, 210, 140, 16);
		lblCargarPrik.setText("Cargar Private Key");
		lblCargarPrik.setForeground(Color.BLACK);
		
		btnCargarPrik.setBounds(680, 230, 84, 20);
		btnCargarPrik.setText("cargar");
		btnCargarPrik.setName("btnCargarPrik");

		txtCargarPrik.setBounds(680, 255, 400, 16);
		txtCargarPrik.setText("seleccione un archivo");
		txtCargarPrik.setForeground(Color.GRAY);
		txtCargarPrik.setBackground(Color.WHITE);
		txtCargarPrik.setFont(new Font(fuente.getName(), fuente.BOLD, fuente.getSize()));
		txtCargarPrik.setEnabled(true);
		txtCargarPrik.setEditable(false);// Evita que el usuario escriba
		txtCargarPrik.setBorder(null); // Quita el borde de caja de texto
		txtCargarPrik.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCargarPrik.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		
		btnAnalizar.setBounds(680, 300, 160, 20);
		btnAnalizar.setText("Analizar");
		btnAnalizar.setName("btnAnalizar");
		//btnAnalizar.addActionListener(e -> null);
		btnAnalizar.addActionListener(new ListenerBtn());
		
		panelLectura.add(lblPanelLectura);
		panelLectura.add(txA_Lectura);
		panelLectura.add(lblCargarCer);
		panelLectura.add(btnCargarCer);
		panelLectura.add(txtCargarCer);
		panelLectura.add(lblCargarPubk);
		panelLectura.add(btnCargarPubk);
		panelLectura.add(txtCargarPubk);
		panelLectura.add(lblCargarPrik);
		panelLectura.add(btnCargarPrik);
		panelLectura.add(txtCargarPrik);
		panelLectura.add(btnAnalizar);
		//-----------------------------------------------------------------
		//DATOS DEL JMENUBAR
		JMenuBar menuBar = new JMenuBar();
		menuBar.setName("menuBar");
		
		JMenu mnuEscritura = new JMenu("Escritura");
		menuBar.add(mnuEscritura);
		
		JMenu mnuLectura = new JMenu("Lectura");
		menuBar.add(mnuLectura);
		
		JMenu mnuCSR = new JMenu("CSR");
		menuBar.add(mnuCSR);
		
		JMenu mnuAcercaDe = new JMenu("Acerca de");
		menuBar.add(mnuAcercaDe);			
		
		//ven.setJMenuBar(menuBar);
		
		System.out.println(""
			+ "JFrame: " + ven.getBounds()
			+ "\nJMenuBar size: " + menuBar.getBounds()
		    + "\nContentPane: "
		    + ven.getContentPane().getBounds()
		    + "\nInsets: " + ven.getInsets() 
		    + "\n\nScrollPane arriba: " 
		    + ven.getContentPane().getComponents()[0].getBounds()
		    + "\nScrollPane abajo: " 
		    + ven.getContentPane().getComponents()[2].getBounds()
		);
		System.out.println();
		
		//infoSize(ven.getContentPane().getComponents());
	}
	
	private void infoFuente(Font fuente) {
		System.out.println("\n" + fuente);
		System.out.println("Tamaño Fuente: " + fuente.getSize()
			+ "| nombre (font): " + fuente.getFontName()
			+ "| nombre (name): " + fuente.getName()
			+ "| estilo: " + fuente.getStyle()
			+ "| angulo: " + fuente.getItalicAngle()
			+ "| familia: " + fuente.getFamily()
			+ "\n\n"
		);
	}

	private void infoSize(Component[] cm) {
		System.out.println(
			    "Components.size: "
			    + cm.length
			);
		
		System.out.println();

		for(Component c: cm) {
			System.out.println(
			    "Name: "
			    + c.getName()
			    + "\nBounds: "
			    + c.getBounds()
			    + "\nPosicion: " 
			    + c.getX() + "x"+ c.getY()
			    + "\nPosicion Limite Inferior de altura: " 
			    + (c.getY() + c.getHeight())
			    + "\nAncho: " 
			    + c.getWidth()
			    + "\nSize: " 
			    + c.getSize()
			    + "\nPrefered Size: " 
			    + c.getPreferredSize()
			    + "\nInsets: " 
			    //+ c.getinsets
			    + "\nClass name: " 
			    + c.getClass().getName()
			    + "\n\n" 
			);
		}
	}

	private class ListenerBtn implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnAnalizar) {
				JOptionPane.showMessageDialog(
						null, 
						"La sal debe ser un número",
						"Salt requerido",
						JOptionPane.ERROR_MESSAGE
					);
			}
			
		}
		
	}
}
