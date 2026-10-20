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

import ch.qos.logback.core.net.SyslogOutputStream;
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
	
	private JComboBox cobDNusuario = new JComboBox();
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
	private JLabel lblFechVigSer = new JLabel();
	private JButton btnFechVigSer = new JButton();
	private JTextField txtFechVigSer = new JTextField();
	private JComboBox cobFechVigSer = new JComboBox();
	
	//OBJS. DEL datos_kpg
	private JLabel lblDatosKPG = new JLabel();
	private JButton btnDatosKPG = new JButton();
	private JTextField txtDatosKPG = new JTextField();
	private JComboBox cobDatosKPG = new JComboBox();
	
	//OBJS. DEL cer_or_csr
	private JLabel lblCerOrCsr = new JLabel();
	private JButton btnCerOrCsr = new JButton();
	private JTextField txtCerOrCsr = new JTextField();
	private JComboBox cobCerOrCsr = new JComboBox();
	
	//OBJS. DEL datos_cifrado
	private JLabel lblDatosCifrado = new JLabel();
	private JButton btnDatosCifrado = new JButton();
	private JTextField txtDatosCifrado = new JTextField();
	private JComboBox cobDatosCifrado = new JComboBox();
	
	//OBJS. DEL codif_arch
	private JLabel lblCodifArch = new JLabel();
	private JButton btnCodifArch = new JButton();
	private JTextField txtCodifArch = new JTextField();
	private JComboBox cobCodifArch = new JComboBox();
	
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
	private ButtonActionListener btnActionListener =  null;
	
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
		
		btnActionListener = new ButtonActionListener(this);
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
		cobAlg.addActionListener(new ComboBoxListener(cobAlg, txtAlg));
		
		fAlg.add(lblAlg);
		fAlg.add(txtAlg);
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
		chbDNemisor.setBounds(ancGenInit, altGenInit, 500, altCob);
		chbDNemisor.addActionListener(btnActionListener);
		chbDNemisor.addItemListener(btnActionListener);
		
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
		
		//-----------------------------------------------------------------
		//DATOS JPANEL DATOS KEY PAIR GENERATOR
		fDatosKPG = new JPanel();
		fDatosKPG.setName("panelfDatosKPG");		
		fDatosKPG.setLayout(null);
		//agregar el TitleBorder 
		fDatosKPG.setBorder(new TitledBorder(null, "Algoritmos para cifrar la Private Key", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
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
			new DefaultComboBoxModel<>(
				CertificadosUtils.formato_tipo_certificado
			)
		);
		cobCerOrCsr.setSelectedIndex(-1);
		cobCerOrCsr.setBounds(spaceHor + tamAncTex, altGenInit, 300, altCob);//360
		cobCerOrCsr.addActionListener(new ComboBoxListener(cobCerOrCsr, txtCerOrCsr));
		
		fCerOrCsr.add(lblCerOrCsr);		
		fCerOrCsr.add(cobCerOrCsr);
		
		//-----------------------------------------------------------------
		//DATOS JPANEL DATOS DE CIFRADO DE LA PRIVATE KEY
		fDatosCifrado = new JPanel();
		fDatosCifrado.setName("panelfDatosCifrado");		
		fDatosCifrado.setLayout(null);
		//agregar el TitleBorder 
		fDatosCifrado.setBorder(new TitledBorder(null, "Ciframiento de la Private Key", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
		//-----------------------------------------------------------------
		//DATOS JPANEL CODIFICACION DEL ARCHIVO
		fCodifArch = new JPanel();
		fCodifArch.setName("panelfCodifArch");		
		fCodifArch.setLayout(null);
		//agregar el TitleBorder 
		fCodifArch.setBorder(new TitledBorder(null, "Formato de los archivos ", TitledBorder.LEADING, TitledBorder.TOP, fteTitulos, null));
		
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
		btnOK.addActionListener(btnActionListener);
		
		tamAncTex = getTamanioAncho(btnOK);
		
		btnCancel.setText("Cancelar");
		btnCancel.setName("btnCancel");
		//btnCancel.setActionCommand("OK");
		btnCancel.setBounds(tamAncTex + (spaceHor*2), altGenInit, 100, altBtn);
		btnCancel.addActionListener(btnActionListener);
		
		fBotones.add(btnOK);
		fBotones.add(btnCancel);
		//-----------------------------------------------------------------
		//MEDIDAS DE LOS PANELES Y ENCAPSULAMIENTO GRAFICO
		
		Rectangle r = dia.getContentPane().getBounds();
		System.out.println("Rectangle JDialog " + r);

		//Se otorgan los tamaños
		fAlg.setBounds(0, 0, r.width, 100);
		fDNusuario.setBounds(0, 100, r.width, 150);
		fDNemisor.setBounds(0, 250, r.width, 200);
		fFecVigSer.setBounds(0, 450, r.width, 100);
		fDatosKPG.setBounds(0, 550, r.width, 100);
		fCerOrCsr.setBounds(0, 650, r.width, 100);
		fDatosCifrado.setBounds(0, 750, r.width, 100);
		fCodifArch.setBounds(0, 850, r.width, 100);
		fBotones.setBounds(0, 950, r.width, 100);
		
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
		content.setPreferredSize(new Dimension(r.width, 1100));
		//System.out.println("Rectangle panel Content " + r);
		
		
		// > DIMENSIONES DEL JScrollPanel 
		//r = scroll.getViewport().getBounds();
		
		//dimensiones del ScrollPane
		scroll.setBounds(0, 0, r.width, r.height);
		//agregar el panel al scroll
		scroll.setViewportView(content);
		
		
		//agregar el scroll al JDialog
		dia.getContentPane().add(scroll);
		
		System.out.println(
			    "Número de ItemListeners: " +
			    chbDNemisor.getItemListeners().length
			);	
		
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
	
	private class ButtonActionListener implements java.awt.event.ActionListener, ItemListener{

		//private JDialog jDialog = null;
		private WriteFormulario jD = null;
		
		/*public <T extends JFrame> ButtonActionListener(T t){
			
		}*/
		
		//public ButtonActionListener(JDialog jd) {
		public ButtonActionListener(WriteFormulario jDialog) {
			this.jD = jDialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == jD.btnCancel) {
				jD.dia.dispose();
			}
			
			if (e.getSource() == jD.chbDNemisor) {
				System.out.println("Se ejecutó itemStateChanged()");
				//if(jD.chbDNemisor.isSelected() == true) {
				if(jD.chbDNemisor.isSelected()) {
					JOptionPane.showMessageDialog(
						jD.dia, 
						"Bloqueado",
						"Requerido",
						JOptionPane.ERROR_MESSAGE
					);
					return;
				}else {
					JOptionPane.showMessageDialog(
						jD.dia, 
						"Habilitado",
						"Requerido",
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
						null, 
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
				
				System.out.println();
				System.out.println("isSelected(): " + jD.chbDNemisor.isSelected());
				System.out.println("getStateChange(): " + (e.getStateChange() == ItemEvent.SELECTED));
				System.out.println("gsC: " + e.getStateChange());
				System.out.println();
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
				 
				 //Inversión par hacer funcionar a setEnabled()
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
