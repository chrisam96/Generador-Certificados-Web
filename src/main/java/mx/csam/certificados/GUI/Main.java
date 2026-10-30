package mx.csam.certificados.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	public JButton btnCargarCer = new JButton("");
	public final JTextField txtCargarCer = new JTextField("");
	
	private JLabel lblCargarPubk = new JLabel("");
	public JButton btnCargarPubk = new JButton("");
	public final JTextField txtCargarPubk = new JTextField("");
	
	private JLabel lblCargarPrik = new JLabel("");	
	public JButton btnCargarPrik = new JButton("");
	public final JTextField txtCargarPrik = new JTextField("");
	
	public JButton btnAnalizar = new JButton("");	
	
	public File arch_cert = null;
	public File arch_prik = null;
	public File arch_pubk = null;	
	
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
	///DIMENSIONES
	//JFrame
	static int wJFrame = 1150;
	static int hJFrame = 700;
	
	//Content Pane
	private int wContentPane = 1136;
	private int hContentPane = 663;

	private int ancTxtArea = 640;
	private int altTxtArea = 440;
	
	private int ancPanelLectura = 1350;
	private int altPanelLectura = 700;
		
	private int ancLblPanelLectura = 140;
	private int ancTxtPanelLectura = 650;
	
	private int posY_LblPanelLectura = 0;
	//Bordes JFrame
	
	/*
	final int bordes = 7;
	final int bordeSup = 30;
	final int hJMenuBar = 23;
	final int h_Scroll_Hor = 23;
	*/	
	
	//Posiciones de los bordes
	final int posInitIzq = 0;
	final int posInitSup = 0;
	
	//ScrollEscritura
	//int ancScrollEsc = wContentPane;
	int altScrollEsc = 160;

	//Separador
	final int separador = 10;
	
	//ScrollLectura
	//int ancScrollLec = wContentPane;
	int altScrollLec = hContentPane - altScrollEsc - separador ;
	int posY_hScrollLec = hContentPane - altScrollLec;		
	
	//TAMAÑOS AUXILIARES
	private int altTxt = 25; 
	private int altLbl = 20; 
	private int altCob = 20; 
	private int altChb = 20;
	private int altBtn = 25;
	
	private int altGenInit = 30; 
	private int ancGenInit = 10; 
	
	private int spaceVer = 10; 
	private int spaceHor = 25;
	
	private int tamAncTex = -1;
	private int tamAltoTex = -1;
	
	//FUENTES
	Font fteTitulos = new Font(Font.MONOSPACED, Font.BOLD, 20);
	Font fteTextos = new Font(Font.DIALOG, Font.PLAIN, 15);
	
	Font fteDialog = new Font(Font.DIALOG, Font.BOLD, 15);
	//Font fteDialogInput = new Font(Font.DIALOG_INPUT, Font.BOLD, 15); //Identico a MONOSPACED
	//Font fteSANSERIF = new Font(Font.SANS_SERIF, Font.BOLD, 15); // Identico a DIALOG
	Font fteSERIF = new Font(Font.SERIF, Font.BOLD, 15);
	Font fteMONOSPACED = new Font(Font.MONOSPACED, Font.BOLD, 15);
	
	Color colTitulos = Color.BLACK;
	Color colTxt = Color.BLACK;
	Color colAux = Color.GRAY;
	
	
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
		ven.setType(Type.POPUP);
		ven.setBounds(new Rectangle(100, 100, wJFrame, hJFrame));		
		//Forzar la visualización (para obtener las medidas del ContentPane)
		ven.setVisible(true);
		ven.addNotify();		
		//-----------------------------------------------------------------
		ven.setResizable(false);
		ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//AbsoluteLayout
		ven.getContentPane().setLayout(null);
		//Aparecer en el Centro 
		ven.setLocationRelativeTo(null);		

		//Gestor de eventos
		Eventos eventos = new Eventos(this);
		
		Rectangle rVen = ven.getContentPane().getBounds();
		System.err.println("Rectangle JDialog " + rVen);
		
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

		//-----------------------------------------------------------------
		
		
		
		//>> MEDIDAS DE LOS CONTENEDORES INTERNOS DEL JSCROLLPANE (scrollEscritura) 
		

		
		
		//-----------------------------------------------------------------
		//ETIQUETAS DE LOS NOMBRES DE LOS ARCHIVOS CREADOS
		
		
		lblPanelNomArchivos.setName("lblPanelNomArchivos");
		lblPanelNomArchivos.setText("Oprima el botón \"Generar\" y rellene el formulario para crear el Certificado .cer y las keys correspondientes");
		lblPanelNomArchivos.setFont(fteTextos);
		lblPanelNomArchivos.setForeground(Color.BLACK);
		tamAncTex = getTamanioAnchoTexto(lblPanelNomArchivos);
		lblPanelNomArchivos.setBounds(10, 5, tamAncTex + spaceHor, altLbl);
		
		txtCerti.setText("certificado.cer");
		txtCerti.setForeground(Color.GRAY);
		txtCerti.setEnabled(true);
		txtCerti.setEditable(false);// Evita que el usuario escriba
		txtCerti.setBorder(null); // Quita el borde de caja de texto
		txtCerti.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCerti.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtCerti.setFont(fteTextos);
		txtCerti.setBounds(10, 35, tamAncTex + spaceHor, altLbl);
				
		txtPubK.setText("public_key.pubkey");		
		txtPubK.setForeground(Color.GRAY);
		txtPubK.setEnabled(true);
		txtPubK.setEditable(false);// Evita que el usuario escriba
		txtPubK.setBorder(null); // Quita el borde de caja de texto
		txtPubK.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtPubK.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtPubK.setFont(fteTextos);
		txtPubK.setBounds(10, txtCerti.getBounds().y + spaceVer + altLbl, tamAncTex + spaceHor, altLbl);
		
		txtPriK.setText("private_key.key");		
		txtPriK.setForeground(Color.GRAY);
		txtPriK.setEnabled(true);
		txtPriK.setEditable(false);// Evita que el usuario escriba
		txtPriK.setBorder(null); // Quita el borde de caja de texto
		txtPriK.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtPriK.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtPriK.setFont(fteTextos);
		txtPriK.setBounds(10, txtPubK.getBounds().y + spaceVer + altLbl, tamAncTex + spaceHor, altLbl);

		
		//-----------------------------------------------------------------
		//DATOS JPANEL panelNomsArchivos - CON LAS ETIQUETAS DE LOS ARCHIVOS GENERADOS
		panelNomsArchivos = new JPanel();
		panelNomsArchivos.setName("panelEscritura");
		panelNomsArchivos.setLayout(null);
		panelNomsArchivos.setBackground(new Color(128, 255, 128));		
		
		panelNomsArchivos.add(lblPanelNomArchivos);
		panelNomsArchivos.add(txtCerti);
		panelNomsArchivos.add(txtPubK);
		panelNomsArchivos.add(txtPriK);
		
		//-----------------------------------------------------------------
		//JBUTTON Escritura 
		
		btnEscritura = new JButton("Generar");
		btnEscritura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wf = new WriteFormulario(main);
				wf.dia.setVisible(true);
				System.out.println("**** DESPUES DE SET_VISIBLE()");
			}
		});
		btnEscritura.setName("btnGenerar");
		btnEscritura.setFont(fteTextos);
		btnEscritura.setBounds(920, altGenInit, 120, altBtn);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL ESCRITURA
		panelEscritura = new JPanel();
		panelEscritura.setName("panelEscritura");
		panelEscritura.setLayout(null);		

		panelEscritura.add(panelNomsArchivos);
		panelEscritura.add(btnEscritura);
		
		//-----------------------------------------------------------------
		//DATOS JSCROLLPANE scrollEscritura
		scrollEscritura.setName("scrollEscritura");		
		scrollEscritura.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollEscritura.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/*Corresponde a cuando se arrastra un elemento dentro de un contenedor
		 * y la pantalla se mueve con esa acción*/ 
		//scrollEscritura.setAutoscrolls(false); 
		scrollEscritura.setViewportBorder(new TitledBorder(null, "Escritura", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, colTitulos));
	
		scrollEscritura.setBounds(posInitIzq, posInitSup, rVen.width, altScrollEsc);
		
		// AHORA sí: obtener el espacio real del viewport
		Rectangle rectSE = scrollEscritura.getBounds();
		System.out.println("Viewport: " + rectSE);
		
		
		panelNomsArchivos.setBounds(
			0, 0, 
			(int) (rectSE.width * (4.0/5.0)),			
			(int) (rectSE.height)
		);
		//Se llena todo el espacio interno de JScrollPane con el JPaznel
		panelEscritura.setBounds(0, 0, rectSE.width, rectSE.height);
		panelEscritura.validate();
		
		
		
		scrollEscritura.setBounds(posInitIzq, posInitSup, rVen.width, altScrollEsc);
		scrollEscritura.setViewportView(panelEscritura);		
		// PRIMERO: agregar el JScrollPane al JFrame
		ven.getContentPane().add(scrollEscritura);			
		// FORZAR EL LAYOUT
		ven.validate();
		//-----------------------------------------------------------------
		//SEPARADOR FIJO
		Component horizontalStrut = Box.createHorizontalStrut(altBtn);
		horizontalStrut.setName("horizontalStrut");
		horizontalStrut.setBounds(posInitIzq, altScrollEsc, wContentPane, separador);
		ven.getContentPane().add(horizontalStrut);

		
		//-----------------------------------------------------------------
		//DATOS JPANEL LECTURA
		panelLectura = new JPanel();
		panelLectura.setName("panelLectura");		
		panelLectura.setLayout(null);
		//Con esto se fuerza a que aparezcan los scrollbars
		panelLectura.setPreferredSize(new Dimension(ancPanelLectura, altPanelLectura));
				
		
		//-----------------------------------------------------------------
		//DATOS JSCROLLPANE LECTURA		
		scrollLectura.setName("scrollPaneLectura");
		scrollLectura.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLectura.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollLectura.setAutoscrolls(true); 
		scrollLectura.setViewportBorder(new TitledBorder(null, "Lectura", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, colTitulos));
		scrollLectura.setViewportView(panelLectura);
		scrollLectura.setBounds(new Rectangle(posInitIzq, posY_hScrollLec, rVen.width, altScrollLec));
		ven.getContentPane().add(scrollLectura);
		
		ven.validate();
		
		rectSE = scrollLectura.getViewport().getBounds();
		
		//Se llena todo el espacio interno de JScrollPane con el JPanel
		panelLectura.setBounds(0, 0, rectSE.width, rectSE.height);
		
		//-----------------------------------------------------------------
		//ETIQUETAS LECTURA
		
		lblPanelLectura.setName("lblPanelLectura");
		lblPanelLectura.setText("Aquí abajo se mostrará la información sobre los archivos");
		lblPanelLectura.setForeground(Color.BLACK);
		lblPanelLectura.setFont(fteTextos);
		tamAncTex = getTamanioAnchoTexto(lblPanelLectura);
		lblPanelLectura.setBounds(10, altGenInit - 20, tamAncTex + spaceHor, altLbl);		
				
		txA_Lectura.setEditable(false);
		txA_Lectura.setEnabled(true);
		txA_Lectura.setText("Sexo");
		txA_Lectura.setFont(fteTextos);
		txA_Lectura.setBounds(10, 40 , ancTxtArea, altTxtArea);
		
		//-----------------------------------------------------------------
				
		lblCargarCer.setText("Cargar Certificado");
		lblCargarCer.setForeground(Color.BLACK);
		lblCargarCer.setFont(fteTextos);
		lblCargarCer.setBounds(680, altGenInit, ancLblPanelLectura, altLbl);
		
		
		posY_LblPanelLectura = lblCargarCer.getY() + lblCargarCer.getHeight();
		btnCargarCer.setText("cargar");
		btnCargarCer.setName("btnCargarCer");
		btnCargarCer.addActionListener(eventos);
		btnCargarCer.setBounds(680, posY_LblPanelLectura, 84, altBtn);
		
		
		posY_LblPanelLectura = btnCargarCer.getY() + btnCargarCer.getHeight();
		txtCargarCer.setText("seleccione un archivo");
		txtCargarCer.setForeground(Color.GRAY);
		txtCargarCer.setBackground(Color.WHITE);
		txtCargarCer.setFont(fteTextos);
		txtCargarCer.setEnabled(true);
		txtCargarCer.setEditable(false);// Evita que el usuario escriba
		txtCargarCer.setBorder(null); // Quita el borde de caja de texto
		txtCargarCer.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCargarCer.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtCargarCer.setBounds(680, posY_LblPanelLectura, ancTxtPanelLectura, altLbl);
				
		
		//-----------------------------------------------------------------
		
		posY_LblPanelLectura = txtCargarCer.getY() + txtCargarCer.getHeight();
		posY_LblPanelLectura += (altGenInit);		
		
		
		lblCargarPubk.setText("Cargar Public Key");
		lblCargarPubk.setFont(fteTextos);
		lblCargarPubk.setForeground(Color.BLACK);
		lblCargarPubk.setBounds(680, posY_LblPanelLectura, ancLblPanelLectura, altLbl);//110
		
		
		posY_LblPanelLectura = lblCargarPubk.getY() + lblCargarPubk.getHeight();
		btnCargarPubk.setText("cargar");
		btnCargarPubk.setName("btnCargarPubk");
		btnCargarPubk.addActionListener(eventos);
		btnCargarPubk.setBounds(680, posY_LblPanelLectura, 84, altBtn);//130
		
		
		posY_LblPanelLectura = btnCargarPubk.getY() + btnCargarPubk.getHeight();
		txtCargarPubk.setText("seleccione un archivo");
		txtCargarPubk.setForeground(Color.GRAY);
		txtCargarPubk.setBackground(Color.WHITE);
		txtCargarPubk.setFont(fteTextos);
		txtCargarPubk.setEnabled(true);
		txtCargarPubk.setEditable(false);// Evita que el usuario escriba
		txtCargarPubk.setBorder(null); // Quita el borde de caja de texto
		txtCargarPubk.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCargarPubk.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtCargarPubk.setBounds(680, posY_LblPanelLectura, ancTxtPanelLectura, altLbl);//155
		
		
		//-----------------------------------------------------------------
		
		
		posY_LblPanelLectura = txtCargarPubk.getY() + txtCargarPubk.getHeight();
		posY_LblPanelLectura += (altGenInit);
		
		
		lblCargarPrik.setText("Cargar Private Key");
		lblCargarPrik.setForeground(Color.BLACK);
		lblCargarPrik.setFont(fteTextos);
		lblCargarPrik.setBounds(680, posY_LblPanelLectura, ancLblPanelLectura, altLbl);//210
		
		
		posY_LblPanelLectura = lblCargarPrik.getY() + lblCargarPrik.getHeight();
		btnCargarPrik.setText("cargar");
		btnCargarPrik.setName("btnCargarPrik");
		btnCargarPrik.addActionListener(eventos);
		btnCargarPrik.setBounds(680, posY_LblPanelLectura,  84, altBtn);//230

		
		posY_LblPanelLectura = btnCargarPrik.getY() +btnCargarPrik.getHeight();
		txtCargarPrik.setText("seleccione un archivo");
		txtCargarPrik.setForeground(Color.GRAY);
		txtCargarPrik.setBackground(Color.WHITE);
		txtCargarPrik.setFont(fteTextos);
		txtCargarPrik.setEnabled(true);
		txtCargarPrik.setEditable(false);// Evita que el usuario escriba
		txtCargarPrik.setBorder(null); // Quita el borde de caja de texto
		txtCargarPrik.setOpaque(false);// Hace el fondo transparente como un JLabel
		txtCargarPrik.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtCargarPrik.setBounds(680, posY_LblPanelLectura, ancTxtPanelLectura, altLbl);//255
		
		
		//-----------------------------------------------------------------
		posY_LblPanelLectura = txtCargarPrik.getY() + txtCargarPrik.getHeight();
		posY_LblPanelLectura += (altGenInit * 2);
		
		
		btnAnalizar.setText("Analizar");
		btnAnalizar.setName("btnAnalizar");		
		btnAnalizar.addActionListener(eventos);
		btnAnalizar.setBounds(680, posY_LblPanelLectura, 160, altBtn);//300
		
		
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
		
		//infoSize(ven.getContentPane().getComponents());
		//infoVentanaMenuBar(ven, menuBar);
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
	
	private void infoVentanaMenuBar(JFrame ven, JMenuBar menuBar) {
		
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
	}


	private int getTamanioAncho(Component c) {
		double d = c.getSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "-TamAnch: " + i);
		
		return i;
	}
	
	private int getTamanioAnchoTexto(Component c) {
		double d = c.getPreferredSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		i += 10;
		System.out.println(c.getName() + "-TamAnchTexto: " + i);
		
		return i;
	}
	
	private int getPosicionAncho(Component c) {
		Rectangle r = c.getBounds();
		double d = r.width + r.x;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "-PosAnch: " + i);
		
		return i;
	}
	
	private int getPosicionAlto(Component c) {
		Rectangle r = c.getBounds();
		double d = r.height + r.y;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "-PosAlto: " + i);
		
		return i;
	}
	
	
	private class Eventos implements ActionListener{

		//private JFrame jF = null;
		private Main main = null;
		
		public Eventos(Main jFrame) {
			this.main = jFrame; 
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == main.btnCargarCer) {
				JFileChooser fc = new JFileChooser();
				
				//Configurarlo en modo SOLO ARCHIVOS
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				fc.setCurrentDirectory(new File("D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key"));
				
				//Configura las extensiones permitidas8
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("Certificado (.cer)", "cer");
				//FileNameExtensionFilter filtro2 = new FileNameExtensionFilter("Filtro 2 (.cer)", "cer");
				
				fc.addChoosableFileFilter(filtro);
				//fc.addChoosableFileFilter(filtro2);
				
				//Establece el filtro predeterminado para JFileChooser
				fc.setFileFilter(filtro);
				
				//Configura la selección múltiple de elementos a la vez
				fc.setMultiSelectionEnabled(false);
				
				//Desactiva la opción "Todos los archivos (*.*)" 
		        fc.setAcceptAllFileFilterUsed(false);
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_cert = fc.getSelectedFile();
					main.txtCargarCer.setText(arch_cert.getName());
				}else {
					main.arch_cert = null;
					main.txtCargarCer.setText("seleccione un archivo");					
				}
				
			}

			
			if(e.getSource() == main.btnCargarPrik) {
				JFileChooser fc = new JFileChooser();
				
				//Configurarlo en modo SOLO ARCHIVOS
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				fc.setCurrentDirectory(new File("D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key"));
				
				//Configura las extensiones permitidas8
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("Private Key (.key)", "key");
				//FileNameExtensionFilter filtro2 = new FileNameExtensionFilter("Filtro 2 (.cer)", "cer");
				
				fc.addChoosableFileFilter(filtro);
				//fc.addChoosableFileFilter(filtro2);
				
				//Establece el filtro predeterminado para JFileChooser
				fc.setFileFilter(filtro);
				
				//Configura la selección múltiple de elementos a la vez
				fc.setMultiSelectionEnabled(false);
				
				//Desactiva la opción "Todos los archivos (*.*)" 
		        fc.setAcceptAllFileFilterUsed(false);
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_prik = fc.getSelectedFile();
					main.txtCargarPrik.setText(arch_prik.getName());
				}else {
					main.arch_prik = null;
					main.txtCargarPrik.setText("seleccione un archivo");					
				}
				
			}
			
			
			if(e.getSource() == main.btnCargarPubk) {
				JFileChooser fc = new JFileChooser();
				
				//Configurarlo en modo SOLO ARCHIVOS
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				fc.setCurrentDirectory(new File("D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key"));
				
				//Configura las extensiones permitidas8
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("Public Key (.key/.pubkey)", "key","pubkey");
				//FileNameExtensionFilter filtro2 = new FileNameExtensionFilter("Filtro 2 (.cer)", "cer");
				
				fc.addChoosableFileFilter(filtro);
				//fc.addChoosableFileFilter(filtro2);
				
				//Establece el filtro predeterminado para JFileChooser
				fc.setFileFilter(null);
				
				//Configura la selección múltiple de elementos a la vez
				fc.setMultiSelectionEnabled(false);
				
				//Desactiva la opción "Todos los archivos (*.*)" 
		        fc.setAcceptAllFileFilterUsed(false);
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_pubk = fc.getSelectedFile();
					main.txtCargarPubk.setText(arch_pubk.getName());
				}else {
					main.arch_pubk = null;
					main.txtCargarPubk.setText("seleccione un archivo");					
				}
				
			}


		}
		
		
	}
}
