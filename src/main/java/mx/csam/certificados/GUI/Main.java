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
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import mx.csam.certificados.logic.read.ReadCertificados;
import mx.csam.certificados.logic.write.WriteCertificados;
import mx.csam.certificados.utilerias.RegistradorEventos;
import mx.csam.certificados.utilerias.pojo.WriteCertificadosPOJO;
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
	private JLabel lblCargarPrik1 = new JLabel("");	
	public final JTextField txtCargarPrik1 = new JTextField("");
	
	public JButton btnAnalizar = new JButton("");	

	//TAMAÑOS
	
	//JFrame
	private static int wJFrame = 1150;
	private static int hJFrame = 700;
	
	//Separador
	private final int separador = 10;

	//Posiciones de los bordes
	private final int posInitIzq = 0;
	private final int posInitSup = 0;
	
	//ScrollEscritura
	int altScrollEsc = 160;

	//ScrollLectura
	int altScrollLec = hJFrame - altScrollEsc - separador ;		
	int posY_elemScrollLec = altScrollEsc + separador;		

	//JPanel Lectura
	private int ancPanelLectura = 1350;
	private int altPanelLectura = 700;
	
	//JTextArea
	private int ancTxtArea = 640;
	private int altTxtArea = 440;
	
	private int posY_LblPanelLectura = -1;
	
	private int ancLblPanelLectura = 140;
	private int ancTxtPanelLectura = 650;
	

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
	
	//OBJETOS	
	//Archivos
	public File arch_cert = null;
	public File arch_prik = null;
	public File arch_pubk = null;	
	
	//Lógica
	private ReadCertificados rc = new ReadCertificados();
	private RegistradorEventos re = new RegistradorEventos();
	private WriteCertificados wc = new WriteCertificados();
	@Deprecated
	public CertificadoWriteGUIRecord rec = null;	
	private WriteCertificadosPOJO pojo = new WriteCertificadosPOJO();
	
	//Lista de objetos recuperados del Certificados
	ArrayList<Object> listaObjsCert = new ArrayList<Object>();
	PrivateKey priv = null;
	PublicKey pub = null;
	String pass = null;
	
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
		
		//-----------------------------------------------------------------
		//DATOS DE JFRAME
		ven =  new JFrame();
		ven.setName("ven");
		ven.setType(Type.POPUP);
		ven.setBounds(new Rectangle(100, 100, wJFrame, hJFrame));		
		//Forzar la visualización (para obtener las medidas del ContentPane)
		ven.setVisible(true);
		ven.addNotify();
		ven.validate();
		//-----------------------------------------------------------------
		ven.setResizable(false);
		ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//AbsoluteLayout
		ven.getContentPane().setLayout(null);
		//Aparecer en el Centro 
		ven.setLocationRelativeTo(null);		

		//-----------------------------------------------------------------
		//DATOS DE JFRAME
		//Gestor de eventos
		Eventos eventos = new Eventos(this);
		
		Rectangle rVen = ven.getContentPane().getBounds();
		
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
		//DATOS DE LOS ELEMENTOS DE panelNomsArchivos
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
		//panelNomsArchivos.setBackground(new Color(128, 255, 128));		
		panelNomsArchivos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, colTitulos));	
		
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
				//pojo = wf.pojo;
				pojo = wf.getPojo();
				System.out.println(
					pojo.toString()					
				);
				
				//Escribir los archivos
				try {
					Object[] lista = wc.generarCertificadoOrCsr(
						pojo.getAlgoritmo(), 
						pojo.getTitularDN(), 
						pojo.getEmisorDN(), 
						pojo.getFecha_vig_y_serial(), 
						pojo.getDatosKPG(), 
						pojo.getCerOrCsr(), 
						pojo.getDatosPKCifrado(), 
						pojo.getCodif_archivo(), 
						pojo.get_SAN()
					);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		
		//-----------------------------------------------------------------
		//MEDIDAS DE LOS PANELES Y ENCAPSULAMIENTO GRAFICO DE 
		// panelNomsArchivos > panelEscritura
		// > scrollEscritura >  ven (JFrame)
		
		panelNomsArchivos.setBounds(
			0, 0, 			
			(int) (rVen.width * (4.0/5.0)),
			(int) (altScrollEsc)
		);
		
		panelEscritura.setBounds(0, 0, rVen.width, altScrollEsc);
					
		//Se llena todo el espacio interno de JScrollPane con el JPanel
		scrollEscritura.setBounds(0, 0, rVen.width, altScrollEsc);
		scrollEscritura.setViewportView(panelEscritura);
		
		// PRIMERO: agregar el JScrollPane al JFrame
		ven.getContentPane().add(scrollEscritura);			
		// FORZAR EL LAYOUT
		ven.validate();
		
		//-----------------------------------------------------------------
		//SEPARADOR FIJO
		Component horizontalStrut = Box.createHorizontalStrut(altBtn);
		horizontalStrut.setName("horizontalStrut");
		horizontalStrut.setBounds(posInitIzq, altScrollEsc, rVen.width, separador);
		ven.getContentPane().add(horizontalStrut);

		//-----------------------------------------------------------------
		//DATOS DE LOS COMPONENTES DEL panelLectura
		
		lblPanelLectura.setName("lblPanelLectura");
		lblPanelLectura.setText("Aquí abajo se mostrará la información sobre los archivos");
		lblPanelLectura.setForeground(Color.BLACK);
		lblPanelLectura.setFont(fteTextos);
		tamAncTex = getTamanioAnchoTexto(lblPanelLectura);
		lblPanelLectura.setBounds(10, altGenInit - 20, tamAncTex + spaceHor, altLbl);		
				
		txA_Lectura.setEditable(false);
		txA_Lectura.setEnabled(true);
		//txA_Lectura.setText("Sexo");
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
		txtCargarCer.setText("[Seleccione un archivo]");
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
		txtCargarPubk.setText("[Seleccione un archivo]");
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
		
		
		//JLabel y JTextField de la Contraseña (solo Encrypted Private Key)
		posY_LblPanelLectura = lblCargarPrik.getY() + lblCargarPrik.getHeight();
		
		lblCargarPrik1.setName("lblCargarPrik1");
		lblCargarPrik1.setText("(Opcional) Contraseña:");
		lblCargarPrik1.setForeground(Color.GRAY);
		lblCargarPrik1.setBackground(Color.WHITE);
		lblCargarPrik1.setFont(fteTextos);
		int anc_LblCargarPrik1 = getTamanioAnchoTexto(lblCargarPrik1);		
		lblCargarPrik1.setBounds(
			680,//posX_LblCargarPrik1 + spaceHor, 
			posY_LblPanelLectura, 
			anc_LblCargarPrik1, 
			altLbl //210
		);
		
		int posX_LblCargarPrik1 = getPosicionAncho(lblCargarPrik1);
		
		txtCargarPrik1.setText("txtCargarPrik1");
		txtCargarPrik1.setText("");
		txtCargarPrik1.setForeground(Color.GRAY);
		txtCargarPrik1.setBackground(Color.WHITE);
		txtCargarPrik1.setFont(fteTextos);			
		txtCargarPrik1.setEnabled(true);
		txtCargarPrik1.setEditable(true);
		//txtCargarPrik1.setOpaque(true);// Hace el fondo transparente como un JLabel
		//txtCargarPrik1.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtCargarPrik1.setBounds(
			posX_LblCargarPrik1, 
			posY_LblPanelLectura, 
			210, 
			altLbl //210
		);
		
		//posY_LblPanelLectura = lblCargarPrik.getY() + lblCargarPrik.getHeight();
		posY_LblPanelLectura = lblCargarPrik1.getY() + lblCargarPrik1.getHeight();
		btnCargarPrik.setText("cargar");
		btnCargarPrik.setName("btnCargarPrik");
		btnCargarPrik.addActionListener(eventos);
		btnCargarPrik.setBounds(680, posY_LblPanelLectura,  84, altBtn);//230	
		
		
		posY_LblPanelLectura = btnCargarPrik.getY() +btnCargarPrik.getHeight();
		txtCargarPrik.setText("[Seleccione un archivo]");
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
		
		
		btnAnalizar.setText(
			"<html>" +
			"Verificar la integridad " + 
			"criptográfica de los archivos" +
			"</html>"
		);
		btnAnalizar.setName("btnAnalizar");		
		btnAnalizar.addActionListener(eventos);
		btnAnalizar.setBounds(680, posY_LblPanelLectura, 360, (altBtn * 2));//300
		
		
		//-----------------------------------------------------------------
		//DATOS JPANEL LECTURA
		panelLectura = new JPanel();
		panelLectura.setName("panelLectura");		
		panelLectura.setLayout(null);
		
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
		panelLectura.add(lblCargarPrik1);
		panelLectura.add(txtCargarPrik1);
		panelLectura.add(btnAnalizar);
		
		//panelLectura.setBounds(0,0, rVen.width, 500);
		//Con esto se fuerza a que aparezcan los scrollbars
		//System.err.println("panelLectura.setPreferredSize(): " + panelLectura.getBounds());
		
		//-----------------------------------------------------------------
		//DATOS JSCROLLPANE LECTURA		
		scrollLectura.setName("scrollPaneLectura");
		scrollLectura.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLectura.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollLectura.setViewportBorder(new TitledBorder(null, "Lectura", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, colTitulos));				
		
		//panelLectura.setBounds(0, 0, rectSE.width, rectSE.height);

		//-----------------------------------------------------------------
		//MEDIDAS DE LOS PANELES Y ENCAPSULAMIENTO GRAFICO DE 
		// panelLectura > scrollLectura >  ven (JFrame)
		panelLectura.setPreferredSize(new Dimension(ancPanelLectura, altPanelLectura));
		scrollLectura.setBounds(
			new Rectangle(
				0, 
				posY_elemScrollLec, 
				rVen.width, 
				//altScrollLec - ven.getInsets().bottom - ven.getInsets().top
				//(int) rVen.getHeight() - altScrollEsc 
				(int) rVen.getHeight() - posY_elemScrollLec 
			)
		);
		scrollLectura.setViewportView(panelLectura);		
		
		ven.getContentPane().add(scrollLectura);
		scrollLectura.validate();
		ven.validate();
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

	@Deprecated
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

	@Deprecated
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
	
	@Deprecated
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

	@Deprecated
	private int getTamanioAncho(Component c) {
		double d = c.getSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "- Tamaño del Ancho del Componente: " + i);
		
		return i;
	}
	
	private int getTamanioAnchoTexto(Component c) {
		double d = c.getPreferredSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		i += 10;
		System.out.println(c.getName() + "- Tamaño del Ancho del Texto: " + i);
		
		return i;
	}
	
	private int getPosicionAncho(Component c) {
		Rectangle r = c.getBounds();
		double d = r.width + r.x;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "- Posición X Límite del Ancho del Componente: " + i);
		
		return i;
	}
	
	private int getPosicionAlto(Component c) {
		Rectangle r = c.getBounds();
		double d = r.height + r.y;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "- Posición Y Límite del Alto del Componente: " + i);
		
		return i;
	}
	
	
	private class Eventos implements ActionListener{

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
		        fc.setAcceptAllFileFilterUsed(true);
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_cert = fc.getSelectedFile();
					
					try {
						listaObjsCert = rc.leerCertificado(fc.getSelectedFile().getAbsolutePath());
						main.txtCargarCer.setText(main.arch_cert.getName());
						
					} catch (CertificateException | NoSuchProviderException | IOException ex) {
						JOptionPane.showMessageDialog(
							main.ven, 
							ex.getMessage(),
							"Aviso",
							JOptionPane.ERROR_MESSAGE
						);
						
						RegistradorEventos.registarExcepcionAlGUI("Lectura del Certificado", ex.getMessage());
						main.txtCargarCer.setText("[Error al analizar el certificado]");					
						ex.printStackTrace();
					}
					
					main.txA_Lectura.setText(RegistradorEventos.getUltimoMsjToString());
					return;
				}else {
					main.arch_cert = null;
					main.txtCargarCer.setText("seleccione un archivo");					
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
				fc.setFileFilter(filtro);
				
				//Configura la selección múltiple de elementos a la vez
				fc.setMultiSelectionEnabled(false);
				
				//Desactiva la opción "Todos los archivos (*.*)" 
		        fc.setAcceptAllFileFilterUsed(true);
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_pubk = fc.getSelectedFile();
					
					try {
						pub = rc.leerPublicKey(fc.getSelectedFile().getAbsolutePath());						
						main.txtCargarPubk.setText(main.arch_pubk.getName());
					
					} catch (IOException | IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(
							main.ven, 
							ex.getMessage(),
							"Aviso",
							JOptionPane.ERROR_MESSAGE
						);
						
						RegistradorEventos.registarExcepcionAlGUI("Lectura del Public Key", ex.getMessage());
						main.txtCargarPubk.setText("[Error al analizar el archivo]");
						ex.printStackTrace();
					}	
					
					main.txA_Lectura.setText(RegistradorEventos.getUltimoMsjToString());
					return;
				}else {
					main.arch_pubk = null;
					main.txtCargarPubk.setText("[Seleccione un archivo]");					
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
		        fc.setAcceptAllFileFilterUsed(true);//false
				
				//Muestra la ventana para abrir archivo
				int resul = fc.showOpenDialog(main);
				
				if(resul == JFileChooser.APPROVE_OPTION) {
					//File arch = fc.getSelectedFile();
					main.arch_prik = fc.getSelectedFile();
					main.pass = txtCargarPrik1.getText();
					try {
						priv = rc.leerPrivateKey(fc.getSelectedFile().getAbsolutePath(), main.pass);
						main.txtCargarPrik.setText(main.arch_prik.getName());
					} catch (IOException | IllegalArgumentException | OperatorCreationException | PKCSException ex) {
						JOptionPane.showMessageDialog(
							main.ven, 
							ex.getMessage(),
							"Aviso",
							JOptionPane.ERROR_MESSAGE
						);
						
						RegistradorEventos.registarExcepcionAlGUI("Lectura de la Private Key", ex.getMessage());
						main.txtCargarPrik.setText("[Error al analizar el archivo]");					
						ex.printStackTrace();
					}					 
					main.txA_Lectura.setText(RegistradorEventos.getUltimoMsjToString());
					return;
				}else {
					main.arch_prik = null;
					main.txtCargarPrik.setText("[Seleccione un archivo]");					
				}
				
			}
			
			if(e.getSource() == main.btnAnalizar) {
				String certi = main.arch_cert != null ? main.arch_cert.getAbsolutePath() : null;
				String priK = main.arch_prik != null ? main.arch_prik.getAbsolutePath() : null;
				String pubK = main.arch_pubk != null ? main.arch_pubk.getAbsolutePath() : null;
				String pass = main.pass; //main.pass = txtCargarPrik1.getText();
				System.out.println(
					"\n\n\n\n" +
					certi + "\n" +
					priK + "\n" +
					pubK + "\n" +
					pass + "\n" 
				);
				try {
					listaObjsCert = rc.leerArchivos(certi, priK, pubK, pass);
					JOptionPane.showMessageDialog(
						main.ven, 
						"<html>Los archivos son criptográficamente válidos entre sí.<br>"
						+ "La integridad entre el certificado, la public key y private key "
						+ "es correcta."
						+ "</html>",
						"Terminado",
						JOptionPane.INFORMATION_MESSAGE
					);
					main.txA_Lectura.setText(
					RegistradorEventos.getUltimosMsjToString(12)+
					"""
					Los archivos son integros entre sí. 
					Su validación criptográfica ha sido comprobada con éxito.
					""".replaceAll("\t", ""));
				} catch (IOException | IllegalArgumentException | OperatorCreationException | PKCSException | CertificateException | NoSuchProviderException ex) {
					JOptionPane.showMessageDialog(
						main.ven, 
						ex.getMessage(),
						"Aviso",
						JOptionPane.ERROR_MESSAGE
					);
					
					RegistradorEventos.registarExcepcionAlGUI("Validación criptográfica de los archivos", ex.getMessage());
					main.txA_Lectura.setText("""
					Falló la comprobación criptográfica entre los archivos.
					Puede que no sean validos criptográficamente entre sí (es decir,
					no corresponden entre ellos).
					""");
					ex.printStackTrace();
				}					 
				//main.txA_Lectura.setText(RegistradorEventos.getUltimoMsjToString());
				return;
			}
			
			

		}
		
		
	}
}
