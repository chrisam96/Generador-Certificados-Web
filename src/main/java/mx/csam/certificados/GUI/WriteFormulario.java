package mx.csam.certificados.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JCalendar;

import mx.csam.certificados.utilerias.constantes.CertificadosUtils;

public class WriteFormulario /*extends JDialog*/ {

	private static final long serialVersionUID = 1L;
	//VENTANA tipo Dialog
	public JDialog dia = null;
	//SCROLL
	private JScrollPane scroll = new JScrollPane();	
	//PANEL CONTENEDOR
	private JPanel content = new JPanel();
	//SUB PANELES
	private JPanel fAlg = null;
	private JPanel fDNusuario = null;
	private JPanel fDNemisor = null;
	private JPanel fFecVigSer = null;
	private JPanel fDatosKPG = null;
	private JPanel fCerOrCsr = null;
	private JPanel fDatosCifrado = null;
	private JPanel fCodifArch = null;
	private JPanel fBotones = null;

	//OBJS. DEL F_ALG
	private JLabel lblAlg = new JLabel();
	//private JButton btnAlg = new JButton();
	private JTextField txtAlg = new JTextField();
	private JComboBox<String> cobAlg = new JComboBox<>();
	
	//OBJS. DEL F_dn_usuario
	private JLabel lblDNusuario = new JLabel();
	private JTextField txtDNusuario = new JTextField();
	private JLabel lblDNusuario1 = new JLabel();
	private JTextField txtDNusuario1 = new JTextField();
	private JLabel lblDNusuario2 = new JLabel();
	private JTextField txtDNusuario2 = new JTextField();
	
	private JLabel lblDNusuario3 = new JLabel();
	private JTextField txtDNusuario3 = new JTextField();
	private JLabel lblDNusuario4 = new JLabel();
	private JTextField txtDNusuario4 = new JTextField();
	private JLabel lblDNusuario5 = new JLabel();
	private JTextField txtDNusuario5 = new JTextField();
	
	private JComboBox<String> cobDNusuario = new JComboBox<>();
	private JButton btnDNusuario = new JButton();
	
	//OBJS. DEL F_dn_emisor
	public JCheckBox chbDNemisor = new JCheckBox();
	private JComboBox cobDNemisor = new JComboBox();
	
	private JLabel lblDNemisor = new JLabel();
	private JTextField txtDNemisor = new JTextField();
	private JLabel lblDNemisor1 = new JLabel();
	private JTextField txtDNemisor1 = new JTextField();
	private JLabel lblDNemisor2 = new JLabel();
	private JTextField txtDNemisor2 = new JTextField();
	
	private JLabel lblDNemisor3 = new JLabel();
	private JTextField txtDNemisor3 = new JTextField();
	private JLabel lblDNemisor4 = new JLabel();
	private JTextField txtDNemisor4 = new JTextField();
	private JLabel lblDNemisor5 = new JLabel();
	private JTextField txtDNemisor5 = new JTextField();
	
	private JButton btnDNemisor = new JButton();
	
	//OBJS. DEL F_fech_vig_ser
	private JLabel lblFecVigSer = new JLabel();
	private JButton btnFecVigSer = new JButton();
	private JTextField txtFecVigSer = new JTextField();
	private JComboBox<String> cobFecVigSer = new JComboBox<>();
	
	private JLabel lblFecVigSer1 = new JLabel();
	private JTextField txtFecVigSer1 = new JTextField();	
	private JLabel lblFecVigSer2 = new JLabel();
	private JTextField txtFecVigSer2 = new JTextField();
	private JLabel lblFecVigSer3 = new JLabel();
	private JTextField txtFecVigSer3 = new JTextField();
	
	private JLabel lblFecVigSer4 = new JLabel();
	private JTextField txtFecVigSer4 = new JTextField();
	private JLabel lblFecVigSer5 = new JLabel();
	private JTextField txtFecVigSer5 = new JTextField();
	private JLabel lblFecVigSer6 = new JLabel();
	private JTextField txtFecVigSer6 = new JTextField();
	
	private JLabel lblFecVigSer7 = new JLabel();
	private JTextField txtFecVigSer7 = new JTextField();
	private JLabel lblFecVigSer8 = new JLabel();
	
	//OBJS. DEL datos_kpg
	private JLabel lblDatosKPG = new JLabel();
	private JComboBox<String> cobDatosKPG = new JComboBox<>();
	private JLabel lblDatosKPG1 = new JLabel();
	private JComboBox<String> cobDatosKPG1 = new JComboBox<>();
	private JLabel lblDatosKPG2 = new JLabel();
	private JTextField txtDatosKPG = new JTextField();
	
	private JButton btnDatosKPG = new JButton();
	
	//OBJS. DEL cer_or_csr
	private JLabel lblCerOrCsr = new JLabel();
	private JButton btnCerOrCsr = new JButton();
	private JTextField txtCerOrCsr = new JTextField();
	private JComboBox<String> cobCerOrCsr = new JComboBox<>();
	
	//OBJS. DEL datos_cifrado
	public JCheckBox chbDatosCifrado = new JCheckBox();
	private JLabel lblDatosCifrado = new JLabel();
	private JTextField txtDatosCifrado = new JTextField();
	private JLabel lblDatosCifrado1 = new JLabel();
	private JComboBox<String> cobDatosCifrado = new JComboBox<>();
	
	private JButton btnDatosCifrado = new JButton();
	
	//OBJS. DEL codif_arch
	private JLabel lblCodifArch = new JLabel();
	private JButton btnCodifArch = new JButton();
	private JTextField txtCodifArch = new JTextField();
	private JComboBox<String> cobCodifArch = new JComboBox<>();
	
	//OBJS. DEL F_botones
	private JLabel lblBotones = new JLabel();
	public JButton btnOK = new JButton();
	public JButton btnCancel = new JButton();

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
	
	///DIMENSIONES
	private int wDialog = 1100;
	private int hDialog = 700;
	
	//TAMAÑOS AUXILIARES
	private int altTxt = 25; 
	private int altLbl = 20; 
	private int altCob = 20; 
	private int altChb = 20;
	private int altBtn = 25;
	private int altGenInit = 35; 
	private int ancGenInit = 10; 
	private int spaceVer = 10; 
	private int spaceHor = 25;//30
	private int tamAncTex = -1;
	private int tamAltoTex = -1;
	
	//CLASES PARA LAS ACCIONES DE LOS EVENTOS
	private Eventos eventos =  null;
	
	static {
		//dia.setName("dia");
		//dia.setBounds(100, 100, 1100, 700);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			
			//JFrame jf = new JFrame();
			//jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			//WriteFormulario dialog = new WriteFormulario(jf);
			WriteFormulario dialog = new WriteFormulario(null);
			dialog.dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.dia.setVisible(true);
			//jf.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	//public WriteFormulario(JFrame jf, String titulo, boolean modal) {
	//public WriteFormulario(JDialog jDialog) {
	public WriteFormulario(Frame frame) {
		initialize(frame);
	}
	
	public void initialize(Frame frame) {
		//super(jDialog, titulo, modal);
		//this.dia = jDialog;
		
		dia = new JDialog(frame, "Formulario", true);
		dia.setName("dia");
		dia.setBounds(100, 100, wDialog, hDialog);
		dia.setLocationRelativeTo(null);
		dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dia.setResizable(false);
		//AbsoluteLayout
		dia.getContentPane().setLayout(null);
		//Forzar la visualización (para obtener las medidas del ContentPane)
		//dia.setVisible(true);
		dia.addNotify();
		dia.validate();
		
		eventos = new Eventos(this);
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
		//DATOS SCROLLPANE 
		scroll.setName("scroll");
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL CONTENT
		content.setName("panelContent");
		content.setLayout(null);		
		//content.add(fAlg);
		
		System.out.println("\n%%%%%%% DENTRO DEL CONSTRUCTOR");			

		//-----------------------------------------------------------------
		//DATOS JPANEL ALGORITMO
		fAlg = new JPanel();
		fAlg.setName("panelfAlg");		
		fAlg.setLayout(null);
		//agregar el TitleBorder
		fAlg.setBorder(new TitledBorder(null, "Algoritmo para cifrar el certificado", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, Color.BLACK));
				
		lblAlg.setBounds(ancGenInit, 35, 640, altLbl);
		lblAlg.setText("Seleccione el algoritmo simetrico del certificado:");
		lblAlg.setForeground(Color.BLACK);
		lblAlg.setFont(fteTextos);
		
		txtAlg.setBounds(ancGenInit, 60, 200, altTxt);
		txtAlg.setText("POP UP");
		txtAlg.setForeground(Color.GRAY);
		txtAlg.setBackground(Color.WHITE);
		txtAlg.setFont(fteTextos);
		txtAlg.setEnabled(true);
		txtAlg.setEditable(true);// Evita que el usuario escriba
		//txtAlg.setBorder(null); // Quita el borde de caja de texto
		txtAlg.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtAlg.setFocusable(true);
		
		cobAlg.setEditable(false);
		cobAlg.setFont(fteTextos);
		/*ALternativa para llenar los datos 
		 * pero es uno por uno * /
		String[] a = CertificadosUtils.algoritmo_asimetrico;
		for (String dato : a) {
			cobAlg.addItem(dato);
		}
		*/
		tamAncTex = getTamanioAnchoTexto(lblAlg);
		
		cobAlg.setModel(
			new DefaultComboBoxModel<>(
				CertificadosUtils.algoritmo_asimetrico
			)
		);
		cobAlg.setSelectedIndex(-1);
		cobAlg.setBounds(spaceHor + tamAncTex, 35, 100, altCob);//360
		cobAlg.addActionListener(eventos);
		
		fAlg.add(lblAlg);
		//fAlg.add(txtAlg);
		fAlg.add(cobAlg);
		//-----------------------------------------------------------------
		//DATOS JPANEL DN USUARIO
		
		fDNusuario = new JPanel();
		fDNusuario.setName("paneflfDNusuario");		
		fDNusuario.setLayout(null);
		//agregar el TitleBorder 
		fDNusuario.setBorder(new TitledBorder(null, "Distingished Name del Propietario ", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		// CN = Nombre del Titular			
		lblDNusuario.setName("lblDNUsuario");
		lblDNusuario.setText("Nombre del Titular (CN):");
		lblDNusuario.setFont(fteTextos);
		lblDNusuario.setForeground(Color.BLACK);
		int i = getTamanioAnchoTexto(lblDNusuario);
		lblDNusuario.setBounds(ancGenInit, altGenInit, i, altLbl);//165
		System.out.println(lblDNusuario.getName() + " : " + lblDNusuario.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNusuario);		
		
		txtDNusuario.setName("txtDNUsuario");		
		txtDNusuario.setForeground(Color.GRAY);
		txtDNusuario.setBackground(Color.WHITE);
		txtDNusuario.setFont(fteTextos);
		txtDNusuario.setEnabled(true);
		//txtDNusuario.setEditable(false);// Evita que el usuario escriba
		//txtDNusuario.setBorder(null); // Quita el borde de caja de texto
		txtDNusuario.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario.setBounds(tamAncTex + spaceHor, altGenInit, 300, altTxt);
		System.out.println(txtDNusuario.getName() + " : " + txtDNusuario.getBounds());
		
		// O = Organizacion
		tamAncTex = getPosicionAncho(txtDNusuario);
		
		lblDNusuario1.setName("lblDNUsuario1");
		lblDNusuario1.setText("Organización (O):");
		lblDNusuario1.setFont(fteTextos);
		lblDNusuario1.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario1);
		lblDNusuario1.setBounds(tamAncTex + spaceHor, altGenInit, i, altLbl);//165
		System.out.println(lblDNusuario1.getName() + " : " + lblDNusuario1.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNusuario1);
		
		txtDNusuario1.setName("txtDNUsuario1");
		txtDNusuario1.setForeground(Color.GRAY);
		txtDNusuario1.setBackground(Color.WHITE);
		txtDNusuario1.setFont(fteTextos);
		txtDNusuario1.setEnabled(true);
		txtDNusuario1.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario1.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario1.setBounds(tamAncTex, altGenInit, 300, altTxt);
		System.out.println(txtDNusuario1.getName() + " : " + txtDNusuario1.getBounds());
		
		// OU = Unidad Organizacional o subdivision de la organizacion
		tamAltoTex = getPosicionAlto(lblDNusuario);
				
		lblDNusuario2.setName("lblDNUsuario2");
		lblDNusuario2.setText("Subdivision de la organizacion (OU):");
		lblDNusuario2.setFont(fteTextos);
		lblDNusuario2.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario2);
		lblDNusuario2.setBounds(ancGenInit, tamAltoTex + spaceVer , i, altLbl);
		System.out.println(lblDNusuario2.getName() + " : " + lblDNusuario2.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNusuario2);
		
		txtDNusuario2.setName("txtDNUsuario2");
		txtDNusuario2.setForeground(Color.GRAY);
		txtDNusuario2.setBackground(Color.WHITE);
		txtDNusuario2.setFont(fteTextos);
		txtDNusuario2.setEnabled(true);
		txtDNusuario2.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario2.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario2.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNusuario2.getName() + " : " + txtDNusuario2.getBounds());
		
		// L = Ciudad o Localidad
		tamAncTex = getPosicionAncho(txtDNusuario2);
		
		lblDNusuario3.setName("lblDNUsuario3");
		lblDNusuario3.setText("Ciudad o Localidad (L): ");
		lblDNusuario3.setFont(fteTextos);
		lblDNusuario3.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario3);
		lblDNusuario3.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNusuario3.getName() + " : " + lblDNusuario3.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNusuario3);
		
		txtDNusuario3.setName("txtDNUsuario3");
		txtDNusuario3.setForeground(Color.GRAY);
		txtDNusuario3.setBackground(Color.WHITE);
		txtDNusuario3.setFont(fteTextos);
		txtDNusuario3.setEnabled(true);
		txtDNusuario3.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario3.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario3.setBounds(tamAncTex, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNusuario3.getName() + " : " + txtDNusuario3.getBounds());
		
		// ST = Estado o Provincia
		tamAltoTex = getPosicionAlto(lblDNusuario3);
		
		lblDNusuario4.setName("lblDNUsuario4");
		lblDNusuario4.setText("Estado/Provincia/Departamento (ST):");
		lblDNusuario4.setFont(fteTextos);
		lblDNusuario4.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario4);
		lblDNusuario4.setBounds(ancGenInit, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNusuario4.getName() + " : " + lblDNusuario4.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNusuario4);
		
		txtDNusuario4.setName("txtDNUsuario4");
		txtDNusuario4.setForeground(Color.GRAY);
		txtDNusuario4.setBackground(Color.WHITE);
		txtDNusuario4.setFont(fteTextos);
		txtDNusuario4.setEnabled(true);
		txtDNusuario4.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario4.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario4.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNusuario4.getName() + " : " + txtDNusuario4.getBounds());		
		
		// C = Codigo de 2 letras del Pais
		tamAncTex = getPosicionAncho(txtDNusuario4);
		
		lblDNusuario5.setName("lblDNUsuario5");
		lblDNusuario5.setText("País (C): ");
		lblDNusuario5.setFont(fteTextos);
		lblDNusuario5.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario5);
		lblDNusuario5.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNusuario5.getName() + " : " + lblDNusuario5.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNusuario5);
		
		txtDNusuario5.setName("txtDNUsuario5");
		txtDNusuario5.setForeground(Color.GRAY);
		txtDNusuario5.setBackground(Color.WHITE);
		txtDNusuario5.setFont(fteTextos);
		txtDNusuario5.setEnabled(true);
		txtDNusuario5.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNusuario5.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNusuario5.setBounds(tamAncTex, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNusuario5.getName() + " : " + txtDNusuario5.getBounds());
		
		fDNusuario.add(lblDNusuario);
		fDNusuario.add(txtDNusuario);
		fDNusuario.add(lblDNusuario1);
		fDNusuario.add(txtDNusuario1);
		fDNusuario.add(lblDNusuario2);
		fDNusuario.add(txtDNusuario2);
		fDNusuario.add(lblDNusuario3);
		fDNusuario.add(txtDNusuario3);
		fDNusuario.add(lblDNusuario4);
		fDNusuario.add(txtDNusuario4);
		fDNusuario.add(lblDNusuario5);
		fDNusuario.add(txtDNusuario5);
		//-----------------------------------------------------------------
		//DATOS JPANEL DN EMISOR
		fDNemisor = new JPanel();
		fDNemisor.setName("panelffDNemisor");		
		fDNemisor.setLayout(null);
		//agregar el TitleBorder 
		fDNemisor.setBorder(new TitledBorder(null, "Distingished Name del Emisor ", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		chbDNemisor.setText("Usar los mismos datos que el Distingished Name del Propietario");		
		chbDNemisor.setName("chbDNemisor");
		chbDNemisor.setFont(fteTextos);
		chbDNemisor.setBounds(ancGenInit, altGenInit, 500, altChb);
		chbDNemisor.addActionListener(eventos);
		chbDNemisor.addItemListener(eventos);
		
		tamAltoTex = getPosicionAlto(chbDNemisor);		
		
		// CN = Nombre del Titular			
		lblDNemisor.setName("lblDNemisor");
		lblDNemisor.setText("Nombre del Titular (CN):");
		lblDNemisor.setFont(fteTextos);
		lblDNemisor.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNemisor);
		lblDNemisor.setBounds(ancGenInit, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblDNemisor.getName() + " : " + lblDNemisor.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNemisor);		
		
		txtDNemisor.setName("txtDNemisor");		
		txtDNemisor.setForeground(Color.GRAY);
		txtDNemisor.setBackground(Color.WHITE);
		txtDNemisor.setFont(fteTextos);
		txtDNemisor.setEnabled(true);
		//txtDNusuario.setEditable(false);// Evita que el usuario escriba
		//txtDNusuario.setBorder(null); // Quita el borde de caja de texto
		txtDNemisor.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor.getName() + " : " + txtDNemisor.getBounds());
		
		// O = Organizacion
		tamAncTex = getPosicionAncho(txtDNemisor);
		
		lblDNemisor1.setName("lblDNemisor1");
		lblDNemisor1.setText("Organización (O):");
		lblDNemisor1.setFont(fteTextos);
		lblDNemisor1.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNemisor1);
		lblDNemisor1.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblDNemisor1.getName() + " : " + lblDNemisor1.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNemisor1);
		
		txtDNemisor1.setName("txtDNemisor1");
		txtDNemisor1.setForeground(Color.GRAY);
		txtDNemisor1.setBackground(Color.WHITE);
		txtDNemisor1.setFont(fteTextos);
		txtDNemisor1.setEnabled(true);
		txtDNemisor1.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor1.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor1.setBounds(tamAncTex, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor1.getName() + " : " + txtDNemisor1.getBounds());
		
		// OU = Unidad Organizacional o subdivision de la organizacion
		tamAltoTex = getPosicionAlto(lblDNemisor);
				
		lblDNemisor2.setName("lblDNemisor2");
		lblDNemisor2.setText("Subdivision de la organizacion (OU):");
		lblDNemisor2.setFont(fteTextos);
		lblDNemisor2.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNusuario2);
		lblDNemisor2.setBounds(ancGenInit, tamAltoTex + spaceVer , i, altLbl);
		System.out.println(lblDNemisor2.getName() + " : " + lblDNemisor2.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNemisor2);
		
		txtDNemisor2.setName("txtDNemisor2");
		txtDNemisor2.setForeground(Color.GRAY);
		txtDNemisor2.setBackground(Color.WHITE);
		txtDNemisor2.setFont(fteTextos);
		txtDNemisor2.setEnabled(true);
		txtDNemisor2.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor2.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor2.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor2.getName() + " : " + txtDNemisor2.getBounds());
		
		// L = Ciudad o Localidad
		tamAncTex = getPosicionAncho(txtDNemisor2);
		
		lblDNemisor3.setName("lblDNemisor3");
		lblDNemisor3.setText("Ciudad o Localidad (L): ");
		lblDNemisor3.setFont(fteTextos);
		lblDNemisor3.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNemisor3);
		lblDNemisor3.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNemisor3.getName() + " : " + lblDNemisor3.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNemisor3);
		
		txtDNemisor3.setName("txtDNemisor3");
		txtDNemisor3.setForeground(Color.GRAY);
		txtDNemisor3.setBackground(Color.WHITE);
		txtDNemisor3.setFont(fteTextos);
		txtDNemisor3.setEnabled(true);
		txtDNemisor3.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor3.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor3.setBounds(tamAncTex, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor3.getName() + " : " + txtDNemisor3.getBounds());
		
		// ST = Estado o Provincia
		tamAltoTex = getPosicionAlto(lblDNemisor3);
		
		lblDNemisor4.setName("lblDNemisor4");
		lblDNemisor4.setText("Estado/Provincia/Departamento (ST):");
		lblDNemisor4.setFont(fteTextos);
		lblDNemisor4.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNemisor4);
		lblDNemisor4.setBounds(ancGenInit, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNemisor4.getName() + " : " + lblDNemisor4.getBounds());
		
		tamAncTex = getTamanioAnchoTexto(lblDNemisor4);
		
		txtDNemisor4.setName("txtDNemisor4");
		txtDNemisor4.setForeground(Color.GRAY);
		txtDNemisor4.setBackground(Color.WHITE);
		txtDNemisor4.setFont(fteTextos);
		txtDNemisor4.setEnabled(true);
		txtDNemisor4.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor4.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor4.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor4.getName() + " : " + txtDNemisor4.getBounds());		
		
		// C = Codigo de 2 letras del Pais
		tamAncTex = getPosicionAncho(txtDNemisor4);
		
		lblDNemisor5.setName("lblDNemisor5");
		lblDNemisor5.setText("País (C): ");
		lblDNemisor5.setFont(fteTextos);
		lblDNemisor5.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDNemisor5);
		lblDNemisor5.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblDNemisor5.getName() + " : " + lblDNemisor5.getBounds());
		
		tamAncTex = getPosicionAncho(lblDNemisor5);
		
		txtDNemisor5.setName("txtDNemisor5");
		txtDNemisor5.setForeground(Color.GRAY);
		txtDNemisor5.setBackground(Color.WHITE);
		txtDNemisor5.setFont(fteTextos);
		txtDNemisor5.setEnabled(true);
		txtDNemisor5.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDNemisor5.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDNemisor5.setBounds(tamAncTex, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtDNemisor5.getName() + " : " + txtDNemisor5.getBounds());
		
		fDNemisor.add(chbDNemisor);
		fDNemisor.add(lblDNemisor);
		fDNemisor.add(txtDNemisor);
		fDNemisor.add(lblDNemisor1);
		fDNemisor.add(txtDNemisor1);
		fDNemisor.add(lblDNemisor2);
		fDNemisor.add(txtDNemisor2);
		fDNemisor.add(lblDNemisor3);
		fDNemisor.add(txtDNemisor3);
		fDNemisor.add(lblDNemisor4);
		fDNemisor.add(txtDNemisor4);
		fDNemisor.add(lblDNemisor5);
		fDNemisor.add(txtDNemisor5);

		//-----------------------------------------------------------------
		//DATOS JPANEL FECHA VIGENCIA Y SERIAL
		fFecVigSer = new JPanel();
		fFecVigSer.setName("panelfFecVigSer");		
		fFecVigSer.setLayout(null);
		//agregar el TitleBorder 
		fFecVigSer.setBorder(new TitledBorder(null, "Fecha de Vigencia y No. Serial ", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		
		lblFecVigSer.setText("<html>Seleccione la fecha de inicio"
				+ " <br> de válidez del Certificado:</html>");
		lblFecVigSer.setForeground(Color.BLACK);
		lblFecVigSer.setFont(fteTextos);
		i = getTamanioAnchoTexto(lblFecVigSer);
		lblFecVigSer.setBounds(ancGenInit, altGenInit, i, altLbl*2);
		lblFecVigSer.setEnabled(true);
		
		tamAltoTex = getPosicionAlto(lblFecVigSer);
		
		//Crea el calendario
		JCalendar calendario = new JCalendar();
		calendario.setBounds(ancGenInit, tamAltoTex + spaceVer, 200, 200);		
				
		/***/
		
		// Año
		tamAncTex = getPosicionAncho(calendario);
		
		lblFecVigSer1.setName("lblFecVigSer1");
		lblFecVigSer1.setText("Año(s):");
		lblFecVigSer1.setFont(fteTextos);
		lblFecVigSer1.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer1);
		lblFecVigSer1.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblFecVigSer1.getName() + " : " + lblFecVigSer1.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer1);		
		
		txtFecVigSer1.setName("txtFecVigSer1");		
		txtFecVigSer1.setForeground(Color.GRAY);
		txtFecVigSer1.setBackground(Color.WHITE);
		txtFecVigSer1.setFont(fteTextos);
		txtFecVigSer1.setEnabled(true);
		//txtFecVigSer1.setEditable(false);// Evita que el usuario escriba
		//txtFecVigSer1.setBorder(null); // Quita el borde de caja de texto
		txtFecVigSer1.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer1.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer1.setBounds(tamAncTex, tamAltoTex + spaceVer , 100, altTxt);
		System.out.println(txtFecVigSer1.getName() + " : " + txtFecVigSer1.getBounds());
		
		//Dias
		tamAncTex = getPosicionAncho(txtFecVigSer1);
		
		lblFecVigSer2.setName("lblFecVigSer2");
		lblFecVigSer2.setText("Dia(s):");
		lblFecVigSer2.setFont(fteTextos);
		lblFecVigSer2.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer2);
		lblFecVigSer2.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblFecVigSer2.getName() + " : " + lblFecVigSer2.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer2);
		
		txtFecVigSer2.setName("txtDNUsuario2");
		txtFecVigSer2.setForeground(Color.GRAY);
		txtFecVigSer2.setBackground(Color.WHITE);
		txtFecVigSer2.setFont(fteTextos);
		txtFecVigSer2.setEnabled(true);
		txtFecVigSer2.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer2.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer2.setBounds(tamAncTex, tamAltoTex + spaceVer, 150, altTxt);
		System.out.println(txtFecVigSer2.getName() + " : " + txtFecVigSer2.getBounds());
		
		//Horas
		//tamAltoTex = getPosicionAlto(lblFecVigSer2);
		tamAncTex = getPosicionAncho(txtFecVigSer2);
				
		lblFecVigSer3.setName("lblFecVigSer3");
		lblFecVigSer3.setText("Hora(s):");
		lblFecVigSer3.setFont(fteTextos);
		lblFecVigSer3.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer3);
		lblFecVigSer3.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer , i, altLbl);
		System.out.println(lblFecVigSer3.getName() + " : " + lblFecVigSer3.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer3);
		
		txtFecVigSer3.setName("txtFecVigSer3");
		txtFecVigSer3.setForeground(Color.GRAY);
		txtFecVigSer3.setBackground(Color.WHITE);
		txtFecVigSer3.setFont(fteTextos);
		txtFecVigSer3.setEnabled(true);
		txtFecVigSer3.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer3.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer3.setBounds(tamAncTex, tamAltoTex + spaceVer, 150, altTxt);
		System.out.println(txtFecVigSer3.getName() + " : " + txtFecVigSer3.getBounds());
		
		//Minutos
		tamAltoTex = getPosicionAlto(lblFecVigSer1);
		tamAncTex = getPosicionAncho(calendario);
		
		lblFecVigSer4.setName("lblFecVigSer4");
		lblFecVigSer4.setText("Minuto(s): ");
		lblFecVigSer4.setFont(fteTextos);
		lblFecVigSer4.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer4);
		lblFecVigSer4.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblFecVigSer4.getName() + " : " + lblFecVigSer4.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer4);
		
		txtFecVigSer4.setName("txtFecVigSer4");
		txtFecVigSer4.setForeground(Color.GRAY);
		txtFecVigSer4.setBackground(Color.WHITE);
		txtFecVigSer4.setFont(fteTextos);
		txtFecVigSer4.setEnabled(true);
		txtFecVigSer4.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer4.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer4.setBounds(tamAncTex, tamAltoTex + spaceVer, 150, altTxt);
		System.out.println(txtFecVigSer4.getName() + " : " + txtFecVigSer4.getBounds());
		
		//Segundos
		tamAncTex = getPosicionAncho(txtFecVigSer4);
		
		lblFecVigSer5.setName("lblFecVigSer5");
		lblFecVigSer5.setText("Segundo(s):");
		lblFecVigSer5.setFont(fteTextos);
		lblFecVigSer5.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer5);
		lblFecVigSer5.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblFecVigSer5.getName() + " : " + lblFecVigSer5.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer5);
		
		txtFecVigSer5.setName("txtFecVigSer5");
		txtFecVigSer5.setForeground(Color.GRAY);
		txtFecVigSer5.setBackground(Color.WHITE);
		txtFecVigSer5.setFont(fteTextos);
		txtFecVigSer5.setEnabled(true);
		txtFecVigSer5.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer5.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer5.setBounds(tamAncTex, tamAltoTex + spaceVer, 150, altTxt);
		System.out.println(txtFecVigSer5.getName() + " : " + txtFecVigSer5.getBounds());		
		
		//Milisegundos
		tamAncTex = getPosicionAncho(txtFecVigSer5);
		
		lblFecVigSer6.setName("lblFecVigSer6");
		lblFecVigSer6.setText("Milisegundo(s): ");
		lblFecVigSer6.setFont(fteTextos);
		lblFecVigSer6.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer6);
		lblFecVigSer6.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, i, altLbl);
		System.out.println(lblFecVigSer6.getName() + " : " + lblFecVigSer6.getBounds());
		
		tamAncTex = getPosicionAncho(lblFecVigSer6);
		
		txtFecVigSer6.setName("txtFecVigSer6");
		txtFecVigSer6.setForeground(Color.GRAY);
		txtFecVigSer6.setBackground(Color.WHITE);
		txtFecVigSer6.setFont(fteTextos);
		txtFecVigSer6.setEnabled(true);
		txtFecVigSer6.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer6.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer6.setBounds(tamAncTex, tamAltoTex + spaceVer, 150, altTxt);
		System.out.println(txtFecVigSer6.getName() + " : " + txtFecVigSer6.getBounds());
				
		/***/
		
		//No. Serial
		tamAltoTex = getPosicionAlto(txtFecVigSer6);
		tamAncTex = getPosicionAncho(calendario);
		
		lblFecVigSer7.setName("lblFecVigSer7");
		lblFecVigSer7.setText("Ingrese un Número de Serie: ");
		lblFecVigSer7.setFont(fteTextos);
		lblFecVigSer7.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblFecVigSer7);
		lblFecVigSer7.setBounds(tamAncTex + spaceHor, tamAltoTex + (spaceVer * 6), i, altLbl);
		System.out.println(lblFecVigSer7.getName() + " : " + lblFecVigSer7.getBounds());
		
		//tamAncTex = getPosicionAncho(lblFecVigSer7);
		tamAltoTex = getPosicionAlto(lblFecVigSer7);
		
		txtFecVigSer7.setName("txtFecVigSer7");
		txtFecVigSer7.setForeground(Color.GRAY);
		txtFecVigSer7.setBackground(Color.WHITE);
		txtFecVigSer7.setFont(fteTextos);
		txtFecVigSer7.setEnabled(true);
		txtFecVigSer7.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtFecVigSer7.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtFecVigSer7.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 300, altTxt);
		System.out.println(txtFecVigSer7.getName() + " : " + txtFecVigSer7.getBounds());
		
		/***/
		
		tamAncTex = getPosicionAncho(lblFecVigSer);
		
		lblFecVigSer8.setText("<html>Agregue la cantidad de tiempo"
				+ " de válidez del Certificado:</html>");
		lblFecVigSer8.setForeground(Color.BLACK);
		lblFecVigSer8.setFont(fteTextos);
		lblFecVigSer8.setBounds(tamAncTex + spaceHor, altGenInit, 440, altLbl*2);
		lblFecVigSer8.setEnabled(true);
		
		tamAltoTex = getPosicionAlto(lblFecVigSer8);
		
		/***/
		
		fFecVigSer.add(lblFecVigSer);
		fFecVigSer.add(txtFecVigSer);
		fFecVigSer.add(lblFecVigSer1);
		fFecVigSer.add(txtFecVigSer1);
		fFecVigSer.add(lblFecVigSer2);
		fFecVigSer.add(txtFecVigSer2);
		fFecVigSer.add(lblFecVigSer3);
		fFecVigSer.add(txtFecVigSer3);
		fFecVigSer.add(lblFecVigSer4);
		fFecVigSer.add(txtFecVigSer4);
		fFecVigSer.add(lblFecVigSer5);
		fFecVigSer.add(txtFecVigSer5);
		fFecVigSer.add(lblFecVigSer6);
		fFecVigSer.add(txtFecVigSer6);
		fFecVigSer.add(lblFecVigSer7);
		fFecVigSer.add(txtFecVigSer7);
		fFecVigSer.add(lblFecVigSer8);
		fFecVigSer.add(calendario);
		
		
		
		//-----------------------------------------------------------------
		//DATOS JPANEL DATOS KEY PAIR GENERATOR
		fDatosKPG = new JPanel();
		fDatosKPG.setName("panelfDatosKPG");		
		fDatosKPG.setLayout(null);
		//agregar el TitleBorder 
		fDatosKPG.setBorder(new TitledBorder(null, "Algoritmo para la Private Key", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));						
		
		//Selección de algoritmo simetrico para el cifrado de la Private Key
		lblDatosKPG.setText("Seleccione el tamaño de bits: ");
		lblDatosKPG.setForeground(Color.BLACK);
		lblDatosKPG.setFont(fteTextos);
		lblDatosKPG.setBounds(ancGenInit, altGenInit, 440, altLbl);
		lblDatosKPG.setEnabled(true);
		
		tamAncTex = getTamanioAnchoTexto(lblDatosKPG);
		
		cobDatosKPG.setEditable(false);
		cobDatosKPG.setFont(fteTextos);
		/*cobDatosKPG.setModel(
			new DefaultComboBoxModel<String>(
				CertificadosUtils.algoritmo_simetrico
			)
		);*/
		//cobDatosKPG.setSelectedIndex(-1);//también dispara eventos
		cobDatosKPG.setBounds(spaceHor + tamAncTex, altGenInit, 300, altCob);//360
		cobDatosKPG.addActionListener(eventos);
		cobDatosKPG.setEnabled(false);
		
		tamAltoTex = getPosicionAlto(lblDatosKPG);		
		
		//Selección de algoritmo simetrico para el cifrado de la Private Key
		lblDatosKPG1.setText("Seleccione el algoritmo simetrico: ");
		lblDatosKPG1.setForeground(Color.BLACK);
		lblDatosKPG1.setFont(fteTextos);
		lblDatosKPG1.setBounds(ancGenInit, tamAltoTex + spaceVer, 440, altLbl);
		lblDatosKPG1.setEnabled(true);
		
		tamAncTex = getTamanioAnchoTexto(lblDatosKPG1);
		
		cobDatosKPG1.setEditable(false);
		cobDatosKPG1.setFont(fteTextos);
		/*cobDatosKPG1.setModel(
			new DefaultComboBoxModel<String>(
				CertificadosUtils.algoritmo_simetrico
			)
		);*/
		//cobDatosKPG1.setSelectedIndex(-1);//también dispara eventos
		cobDatosKPG1.setBounds(spaceHor + tamAncTex, tamAltoTex + spaceVer, 300, altCob);//360
		cobDatosKPG1.addActionListener(eventos);
		cobDatosKPG1.setEnabled(false);
		
		tamAltoTex = getPosicionAlto(lblDatosKPG1);		
		
		// Clave(password) para el cifrado		
		lblDatosKPG2.setName("lblDatosCifrado");
		lblDatosKPG2.setText("Ingrese un tamaño de Salt (solo para RSA-PSS):");
		lblDatosKPG2.setFont(fteTextos);
		lblDatosKPG2.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDatosKPG2);
		lblDatosKPG2.setBounds(ancGenInit, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblDatosKPG2.getName() + " : " + lblDatosKPG2.getBounds());
		lblDatosKPG2.setEnabled(false);
		
		tamAncTex = getTamanioAnchoTexto(lblDatosKPG2);		
		
		txtDatosKPG.setName("txtDatosCifrado");		
		txtDatosKPG.setForeground(Color.GRAY);
		txtDatosKPG.setBackground(Color.DARK_GRAY);
		txtDatosKPG.setFont(fteTextos);		
		//txtDNusuario.setEditable(false);// Evita que el usuario escriba
		//txtDNusuario.setBorder(null); // Quita el borde de caja de texto
		txtDatosKPG.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDatosKPG.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDatosKPG.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 400, altTxt);
		System.out.println(txtDatosKPG.getName() + " : " + txtDatosKPG.getBounds());
		txtDatosKPG.setEnabled(false);
		
		fDatosKPG.add(lblDatosKPG);
		fDatosKPG.add(cobDatosKPG);
		fDatosKPG.add(lblDatosKPG1);
		fDatosKPG.add(cobDatosKPG1);
		fDatosKPG.add(lblDatosKPG2);
		fDatosKPG.add(txtDatosKPG);
		                     
		//-----------------------------------------------------------------
		//DATOS JPANEL CERTIFICADO O CSR
		fCerOrCsr = new JPanel();
		fCerOrCsr.setName("panelfCerOrCsr");		
		fCerOrCsr.setLayout(null);
		//agregar el TitleBorder 
		fCerOrCsr.setBorder(new TitledBorder(null, "Selección entre Certificado (.cer) o Solicitud de Certificado (CSR)", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		lblCerOrCsr.setBounds(ancGenInit, altGenInit, 640, altLbl);
		lblCerOrCsr.setText("Seleccione el archivo ha crear: ");
		lblCerOrCsr.setForeground(Color.BLACK);
		lblCerOrCsr.setFont(fteTextos);
				
		tamAncTex = getTamanioAnchoTexto(lblCerOrCsr);
		
		cobCerOrCsr.setEditable(false);
		cobCerOrCsr.setFont(fteTextos);
		cobCerOrCsr.setModel(
			new DefaultComboBoxModel<String>(
				CertificadosUtils.formato_tipo_certificado
			)
		);
		cobCerOrCsr.setSelectedIndex(0);
		cobCerOrCsr.setBounds(spaceHor + tamAncTex, altGenInit, 300, altCob);//360
		cobCerOrCsr.addActionListener(eventos);
		cobCerOrCsr.setEnabled(false);
		
		fCerOrCsr.add(lblCerOrCsr);		
		fCerOrCsr.add(cobCerOrCsr);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL DATOS DE CIFRADO DE LA PRIVATE KEY
		fDatosCifrado = new JPanel();
		fDatosCifrado.setName("panelfDatosCifrado");		
		fDatosCifrado.setLayout(null);
		//agregar el TitleBorder 
		fDatosCifrado.setBorder(new TitledBorder(null, "Ciframiento de la Private Key", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		chbDatosCifrado.setText("Cifrar la Private Key");		
		chbDatosCifrado.setName("chbDatosCifrado");
		chbDatosCifrado.setFont(fteTextos);
		chbDatosCifrado.setBounds(ancGenInit, altGenInit, 500, altChb);
		chbDatosCifrado.addActionListener(eventos);
		chbDatosCifrado.addItemListener(eventos);
		
		tamAltoTex = getPosicionAlto(chbDatosCifrado);		
		
		//Selección de algoritmo simetrico para el cifrado de la Private Key
		lblDatosCifrado.setText("Seleccione un algoritmo simetrico a aplicar en el cifrado: ");
		lblDatosCifrado.setForeground(Color.BLACK);
		lblDatosCifrado.setFont(fteTextos);
		lblDatosCifrado.setBounds(ancGenInit, tamAltoTex + spaceVer, 440, altLbl);
		lblDatosCifrado.setEnabled(false);
				
		tamAncTex = getTamanioAnchoTexto(lblDatosCifrado);
		
		cobDatosCifrado.setEditable(false);
		cobDatosCifrado.setFont(fteTextos);
		cobDatosCifrado.setModel(
			new DefaultComboBoxModel<String>(
				CertificadosUtils.algoritmo_simetrico
			)
		);
		cobDatosCifrado.setSelectedIndex(-1);
		cobDatosCifrado.setBounds(spaceHor + tamAncTex, tamAltoTex + spaceVer, 300, altCob);//360
		cobDatosCifrado.addActionListener(eventos);
		cobDatosCifrado.setEnabled(false);
		
		tamAltoTex = getPosicionAlto(lblDatosCifrado);		
		
		// Clave(password) para el cifrado		
		lblDatosCifrado1.setName("lblDatosCifrado");
		lblDatosCifrado1.setText("Ingrese una clave/un password:");
		lblDatosCifrado1.setFont(fteTextos);
		lblDatosCifrado1.setForeground(Color.BLACK);
		i = getTamanioAnchoTexto(lblDatosCifrado1);
		lblDatosCifrado1.setBounds(ancGenInit, tamAltoTex + spaceVer, i, altLbl);//165
		System.out.println(lblDatosCifrado1.getName() + " : " + lblDatosCifrado1.getBounds());
		lblDatosCifrado1.setEnabled(false);
		
		tamAncTex = getTamanioAnchoTexto(lblDatosCifrado1);		
		
		txtDatosCifrado.setName("txtDatosCifrado");		
		txtDatosCifrado.setForeground(Color.GRAY);
		txtDatosCifrado.setBackground(Color.DARK_GRAY);
		txtDatosCifrado.setFont(fteTextos);
		txtDatosCifrado.setEnabled(true);
		//txtDNusuario.setEditable(false);// Evita que el usuario escriba
		//txtDNusuario.setBorder(null); // Quita el borde de caja de texto
		txtDatosCifrado.setOpaque(true);// Hace el fondo transparente como un JLabel
		txtDatosCifrado.setFocusable(true);// Permite que el usuario haga clic para seleccionar
		txtDatosCifrado.setBounds(tamAncTex + spaceHor, tamAltoTex + spaceVer, 400, altTxt);
		System.out.println(txtDatosCifrado.getName() + " : " + txtDatosCifrado.getBounds());
		txtDatosCifrado.setEnabled(false);
		
		fDatosCifrado.add(chbDatosCifrado);
		fDatosCifrado.add(lblDatosCifrado);
		fDatosCifrado.add(cobDatosCifrado);
		fDatosCifrado.add(lblDatosCifrado1);
		fDatosCifrado.add(txtDatosCifrado);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL CODIFICACION DEL ARCHIVO
		fCodifArch = new JPanel();
		fCodifArch.setName("panelfCodifArch");		
		fCodifArch.setLayout(null);
		//agregar el TitleBorder 
		fCodifArch.setBorder(new TitledBorder(null, "Formato de Codificación de los archivos ", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));		
		
		lblCodifArch.setBounds(ancGenInit, altGenInit, 640, altLbl);
		lblCodifArch.setText("Seleccione el archivo ha crear: ");
		lblCodifArch.setForeground(Color.BLACK);
		lblCodifArch.setFont(fteTextos);
				
		tamAncTex = getTamanioAnchoTexto(lblCodifArch);
		
		cobCodifArch.setEditable(false);
		cobCodifArch.setFont(fteTextos);
		cobCodifArch.setModel(
			new DefaultComboBoxModel<String>(
				new String[]{ CertificadosUtils.CODIF_ARCHIVO_PEM, CertificadosUtils.CODIF_ARCHIVO_DER }				
			)
		);
		cobCodifArch.setSelectedIndex(-1);
		cobCodifArch.setBounds(spaceHor + tamAncTex, altGenInit, 300, altCob);//360
		cobCodifArch.addActionListener(new ComboBoxListener(cobCerOrCsr, txtCerOrCsr));
		
		fCodifArch.add(lblCodifArch);		
		fCodifArch.add(cobCodifArch);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL BOTONES				
		
		fBotones = new JPanel();
		fBotones.setName("panelfBotones");		
		fBotones.setLayout(null);
		//agregar el TitleBorder 
		fBotones.setBorder(new TitledBorder(null, "¿Acepta la configuración?", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
				
		btnOK.setText("OK");		
		btnOK.setName("btnOK");
		//btnOK.setActionCommand("OK");
		btnOK.setBounds(spaceHor, altGenInit, 100, altBtn);
		btnOK.addActionListener(eventos);
		
		tamAncTex = getTamanioAncho(btnOK);
		
		btnCancel.setText("Cancelar");
		btnCancel.setName("btnCancel");
		//btnCancel.setActionCommand("OK");
		btnCancel.setBounds(tamAncTex + (spaceHor*2), altGenInit, 100, altBtn);
		btnCancel.addActionListener(eventos);
		
		fBotones.add(btnOK);
		fBotones.add(btnCancel);
		//-----------------------------------------------------------------
		//MEDIDAS DE LOS PANELES Y ENCAPSULAMIENTO GRAFICO
		
		Rectangle r = dia.getContentPane().getBounds();
		System.out.println("Rectangle JDialog " + r);

		//Se otorgan los tamaños	
		fAlg.setBounds(0, 0, r.width, 100);
		tamAltoTex = getPosicionAlto(fAlg);
		
		fDNusuario.setBounds(0, tamAltoTex, r.width, 150);
		tamAltoTex = getPosicionAlto(fDNusuario);
		
		fDNemisor.setBounds(0, tamAltoTex, r.width, 200);
		tamAltoTex = getPosicionAlto(fDNemisor);
		
		fFecVigSer.setBounds(0, tamAltoTex, r.width, 300);
		tamAltoTex = getPosicionAlto(fFecVigSer);
		
		fDatosKPG.setBounds(0, tamAltoTex, r.width, 150);
		tamAltoTex = getPosicionAlto(fDatosKPG);
		
		fCerOrCsr.setBounds(0, tamAltoTex, r.width, 100);
		tamAltoTex = getPosicionAlto(fCerOrCsr);
		
		fDatosCifrado.setBounds(0, tamAltoTex, r.width, 150);
		tamAltoTex = getPosicionAlto(fDatosCifrado);
		
		fCodifArch.setBounds(0, tamAltoTex, r.width, 100);
		tamAltoTex = getPosicionAlto(fCodifArch);
		
		fBotones.setBounds(0, tamAltoTex, r.width, 100);
		tamAltoTex = getPosicionAlto(fBotones);
		
		//Se agregan al Content JPanel
		content.add(fAlg);
		content.add(fDNusuario);
		content.add(fDNemisor);
		content.add(fFecVigSer);
		content.add(fDatosKPG);
		content.add(fCerOrCsr);
		content.add(fDatosCifrado);
		content.add(fCodifArch);
		content.add(fBotones);
				
		
		// > DIMENSIONES DEL CONTENT JPanel 
		//dimensiones del Espacio interno del ScrollPane
			content.setBounds(0, 0, r.width, 1000);
		//Con esto se fuerza a que aparezcan los scrollbars
		content.setPreferredSize(new Dimension(r.width, 1400));
		//System.out.println("Rectangle panel Content " + r);
		
		
		// > DIMENSIONES DEL JScrollPanel 
		//r = scroll.getViewport().getBounds();
		
		//dimensiones del ScrollPane
		scroll.setBounds(0, 0, r.width, r.height);
		//agregar el panel al scroll
		scroll.setViewportView(content);
		
		
		//agregar el scroll al JDialog
		dia.getContentPane().add(scroll);
		
		
		content.validate();
		scroll.validate();
		dia.validate();

		
	}

	private int getTamanioAnchoTexto(Component c) {
		double d = c.getPreferredSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		i += 10;
		System.out.println(c.getName() + "-TamAnchTexto: " + i);
		
		return i;
	}
	
	private int getTamanioAncho(Component c) {
		double d = c.getSize().width;
		
		int i = (int) Math.ceil(d / 5.0) * 5;
		//i += 5;
		System.out.println(c.getName() + "-TamAnch: " + i);
		
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
	
	private class Eventos implements java.awt.event.ActionListener, ItemListener{

		//private JDialog jDialog = null;
		private WriteFormulario jD = null;
		
		/*public <T extends JFrame> ButtonActionListener(T t){
			
		}*/
		
		//public ButtonActionListener(JDialog jd) {
		public Eventos(WriteFormulario jDialog) {
			this.jD = jDialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == jD.cobAlg) {
				
				/*VALIDACIONES PARA EVITAR RECURSIVIDAD DE
				 * EVENTOS DE actionPerformed()*/
				
				//Validar que lo que se recupera del cobAlg no sea NULL				
				String algoritmo = (String) jD.cobAlg.getSelectedItem(); 
				
				//Evita procesar si no se obtuvo algo del cobAlg 
				if (algoritmo == null) {
					return;
				}
				
				
				/*RECUPERA LAS LISTAS DE LOS PARAMETROS DE c/ALGORITMO 
				 *  CertificadosUtils.listaParametrosKPG()*/
				
				Map<String, LinkedHashMap<String, List<String>>> lista =
					CertificadosUtils.listaParametrosKPG();
							    
			    LinkedHashMap<String, List<String>> params = lista.get(algoritmo);
			    
			    // Pasarle un arreglo vacío le indica a Java el tipo exacto que debe retornar
			    String[] arreglo = params.keySet().toArray(new String[0]);
			    
			    /*LIMPIEZA DEL cobDatosKPG1 */			    
			    jD.cobDatosKPG1.setModel(new DefaultComboBoxModel<String>());
			    jD.cobDatosKPG1.setSelectedIndex(-1);//No es necesario
			    jD.cobDatosKPG1.setEnabled(false);
			    
			    /*CARGAR DATOS A cobDatosKPG 
			     * SIN CONSERVAR LA SELECCION ANTERIOR */			    
			    jD.cobDatosKPG.setModel(
	    		new DefaultComboBoxModel<String>(
	    				arreglo
    				)
	    		);
			    jD.cobDatosKPG.setSelectedIndex(-1);
			    jD.cobDatosKPG.setEnabled(true);
			    
			    //Habilitar campo de la Salt
				if(algoritmo.equals("RSA-PSS")) {
					jD.lblDatosKPG2.setEnabled(true);
					jD.txtDatosKPG.setEnabled(true);
					jD.txtDatosKPG.setBackground(Color.WHITE);
					System.out.println("AJAJAJAAJ");
				}else {
					jD.lblDatosKPG2.setEnabled(false);
					jD.txtDatosKPG.setEnabled(false);
					jD.txtDatosKPG.setBackground(Color.DARK_GRAY);
					jD.txtDatosKPG.setText("");
					System.out.println("xdxdxdxdxdxdxdxdxdxd");
				}
			}
			
			if (e.getSource() == jD.cobDatosKPG) {
				
				/*VALIDACIONES PARA EVITAR RECURSIVIDAD DE
				 * EVENTOS DE actionPerformed()*/
				String selectParam_1 = (String) jD.cobDatosKPG.getSelectedItem();
				
				
				if(selectParam_1 == null) {
					jD.cobDatosKPG1.setModel(new DefaultComboBoxModel<String>());;			    
					jD.cobDatosKPG1.setSelectedIndex(-1);//también dispara eventos		    
					jD.cobDatosKPG1.setEnabled(false);
					return;
				}				

				/*RECUPERA LAS LISTAS DE LOS PARAMETROS DE c/ALGORITMO 
				 *EN  CertificadosUtils.listaParametrosKPG()*/
				
				Map<String, LinkedHashMap<String, List<String>>> lista =
						CertificadosUtils.listaParametrosKPG();
				
				String algoritmo = (String) jD.cobAlg.getSelectedItem();				
				LinkedHashMap<String, List<String>> param_1 = lista.get(algoritmo);
				
				List<String> param_2 = param_1.get(selectParam_1);
								
				/*CARGAR DATOS A cobDatosKPG1 
			     * SIN CONSERVAR LA SELECCION ANTERIOR */
				
				// Pasarle un arreglo vacío le indica a Java el tipo exacto que debe retornar
				String[] arreglo = param_2.toArray(new String[0]);				
				
				jD.cobDatosKPG1.setModel(
					new DefaultComboBoxModel<String>(
						arreglo
					)
				);
				jD.cobDatosKPG1.setSelectedIndex(-1);
				jD.cobDatosKPG1.setEnabled(true);
			}
			
			if (e.getSource() == jD.btnCancel) {
				jD.dia.dispose();
			}
			
			if (e.getSource() == jD.chbDNemisor) {
				System.out.println("Se ejecutó itemStateChanged()");
				//if(jD.chbDNemisor.isSelected() == true) {
				if(jD.chbDNemisor.isSelected()) {
					JOptionPane.showMessageDialog(
						jD.dia, 
						"Se usarán los mismos elementos del DN del Propietario",
						"Aviso",
						JOptionPane.WARNING_MESSAGE
					);
					return;
				}else {
					JOptionPane.showMessageDialog(
						jD.dia, 
						"Favor de agregar la información del DN del Emisor",
						"Aviso",
						JOptionPane.INFORMATION_MESSAGE
					);
					return;
				}
			}
			
			if (e.getSource() == jD.btnOK) {							
			
				//-----------------------------------------------------------------
				//DATOS JPANEL ALGORITMO
				
				if(jD.cobAlg.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(
						jD.dia, //Componente padre al que pertenece
						"Seleccione un algoritmo para el certificado",
						"Requerido",
						JOptionPane.ERROR_MESSAGE
					);
					return;
				}
	
				//-----------------------------------------------------------------
				//DATOS JPANEL DN USUARIO
				
				//-----------------------------------------------------------------
				//DATOS JPANEL DN EMISOR
				
	
				
				
				//-----------------------------------------------------------------
				//DATOS JPANEL FECHA VIGENCIA Y SERIAL
				
				//-----------------------------------------------------------------
				//DATOS JPANEL DATOS KEY PAIR GENERATOR
				
				//-----------------------------------------------------------------
				//DATOS JPANEL CERTIFICADO O CSR
				if(jD.cobCerOrCsr.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(
						null, 
						"Seleccione el tipo de archivo para el certificado",
						"Requerido",
						JOptionPane.ERROR_MESSAGE
					);
					return;
				}
				
				//-----------------------------------------------------------------
				//DATOS JPANEL DATOS DE CIFRADO DE LA PRIVATE KEY
				
				//-----------------------------------------------------------------
				//DATOS JPANEL CODIFICACION DEL ARCHIVO
				
				//-----------------------------------------------------------------
				//DATOS JPANEL BOTONES
				
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getSource() == jD.chbDNemisor) {
				
				/*System.out.println();
				System.out.println("isSelected(): " + jD.chbDNemisor.isSelected());
				System.out.println("getStateChange(): " + (e.getStateChange() == ItemEvent.SELECTED));
				System.out.println("gsC: " + e.getStateChange());
				System.out.println();*/
				 jD.chbDNemisor.isSelected();
				/*
				 * Si casilla esta desmarcada -> false
				 * Si casilla esta Marcada -> true
				 * 
				 * Si esta desmarcada -> Habilitar los JTextFields
				 * Si esta Marcada -> Bloquear los JTextFields
				 * 
				 * .:
				 * Para hacer funcionar a setEnabled() 
				 * se debe invertir el resultado de la casilla
				 * */
				 
				 boolean bloquear = (e.getStateChange() == ItemEvent.SELECTED);
				 
				 //Inversión para hacer funcionar a setEnabled()
				 bloquear = !bloquear;
				 
				jD.lblDNemisor.setEnabled(bloquear);
				jD.txtDNemisor.setEnabled(bloquear);
				jD.lblDNemisor1.setEnabled(bloquear);
				jD.txtDNemisor1.setEnabled(bloquear);
				jD.lblDNemisor2.setEnabled(bloquear);
				jD.txtDNemisor2.setEnabled(bloquear);
				jD.lblDNemisor3.setEnabled(bloquear);
				jD.txtDNemisor3.setEnabled(bloquear);
				jD.lblDNemisor4.setEnabled(bloquear);
				jD.txtDNemisor4.setEnabled(bloquear);
				jD.lblDNemisor5.setEnabled(bloquear);
				jD.txtDNemisor5.setEnabled(bloquear);
				
				
				if(e.getStateChange() == ItemEvent.DESELECTED) {					
					jD.lblDNemisor.setBackground(Color.WHITE);
					jD.txtDNemisor.setBackground(Color.WHITE);
					jD.lblDNemisor1.setBackground(Color.WHITE);
					jD.txtDNemisor1.setBackground(Color.WHITE);
					jD.lblDNemisor2.setBackground(Color.WHITE);
					jD.txtDNemisor2.setBackground(Color.WHITE);
					jD.lblDNemisor3.setBackground(Color.WHITE);
					jD.txtDNemisor3.setBackground(Color.WHITE);
					jD.lblDNemisor4.setBackground(Color.WHITE);
					jD.txtDNemisor4.setBackground(Color.WHITE);
					jD.lblDNemisor5.setBackground(Color.WHITE);
					jD.txtDNemisor5.setBackground(Color.WHITE);
				}else {					
					jD.lblDNemisor.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor.setBackground(Color.DARK_GRAY);
					jD.lblDNemisor1.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor1.setBackground(Color.DARK_GRAY);
					jD.lblDNemisor2.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor2.setBackground(Color.DARK_GRAY);
					jD.lblDNemisor3.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor3.setBackground(Color.DARK_GRAY);
					jD.lblDNemisor4.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor4.setBackground(Color.DARK_GRAY);
					jD.lblDNemisor5.setBackground(Color.DARK_GRAY);
					jD.txtDNemisor5.setBackground(Color.DARK_GRAY);
				}
			}
			
			else if (e.getSource() == jD.chbDatosCifrado) {
				/*
				 * Si casilla esta desmarcada -> false
				 * Si casilla esta Marcada -> true
				 * */
				boolean bloquear = (e.getStateChange() == ItemEvent.SELECTED);
				 
				//Inversión para hacer funcionar a setEnabled()
				//bloquear = !bloquear;
				
				jD.cobDatosCifrado.setEnabled(bloquear);
				jD.lblDatosCifrado.setEnabled(bloquear);
				jD.txtDatosCifrado.setEnabled(bloquear);
				jD.lblDatosCifrado1.setEnabled(bloquear);
				if(e.getStateChange() == ItemEvent.SELECTED) {
					jD.txtDatosCifrado.setBackground(Color.WHITE);
				}else {
					jD.txtDatosCifrado.setBackground(Color.DARK_GRAY);
				}
			}
		}
		
	}
	
	private class ComboBoxListener implements java.awt.event.ActionListener{

		private JTextField auxTxt = null;;
		private JComboBox<String> auxComB = null;;
		private JCheckBox auxCheB = null;;
		private JLabel auxLbl = null;
		private Object aux = null;
		
		private JComboBox Bsender = null;
		
		public ComboBoxListener(JComboBox sender, Component A) {
			switch(A) {
				case JTextField t -> {
					auxTxt = t;
				}
				case JComboBox t -> {
					auxComB = t;
				}
				case JCheckBox t -> {
					auxCheB = t;
				}
				case JLabel t -> {
					auxLbl = t;
				}
				default -> {
					aux = null;
					System.out.println("A no existe como componente");
				}
			}
			//auxTxt = (JTextField) c;
			this.Bsender = sender;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == btnCancel) {
				
			}
			
			if(e.getSource() == cobAlg) {
				/*if(Bsender.getSelectedIndex() == -1) {				
					JOptionPane.showMessageDialog(
						null, 
						"Seleccione un algoritmo para el certificado",
						"Requerido",
						JOptionPane.ERROR_MESSAGE
					);
				}*/
				auxTxt.setText(Bsender.getSelectedItem().toString());
			}
			else if(e.getSource() == cobCerOrCsr) {
				/*if(Bsender.getSelectedIndex() == -1) {				
					JOptionPane.showMessageDialog(
						null, 
						"Seleccione un algoritmo para el certificado",
						"Requerido",
						JOptionPane.ERROR_MESSAGE
					);
				}*/
				if(Bsender.getSelectedIndex() == -1) {
					System.out.println("Nada");
				}				
				else if(Bsender.getSelectedIndex() == -1) {}				
				else if(Bsender.getSelectedIndex() == -1) {}				
				
				auxTxt.setText(Bsender.getSelectedItem().toString());
				System.out.println(Bsender.getSelectedItem().toString());
			}
		}
		
	}

}
