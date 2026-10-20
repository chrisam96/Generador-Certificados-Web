package mx.csam.certificados.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CertificadosController extends Application{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizarArchFX;

    @FXML
    private Button btnCargarCer;

    @FXML
    private Button btnGenerar;

    @FXML
    private Label lblCargarCer;

    @FXML
    private Label lblCertificados;

    @FXML
    private Label lblPrik;

    @FXML
    private Label lblPubk;

    @FXML
    private ScrollPane scrollEscritura;

    @FXML
    private ScrollPane scrollLectura;

    @FXML
    private Label txtCargarCer;

    @FXML
    void initialize() {
        assert btnAnalizarArchFX != null : "fx:id=\"btnAnalizarArchFX\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnCargarCer != null : "fx:id=\"btnCargarCer\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnGenerar != null : "fx:id=\"btnGenerar\" was not injected: check your FXML file 'Main.fxml'.";
        assert lblCargarCer != null : "fx:id=\"lblCargarCer\" was not injected: check your FXML file 'Main.fxml'.";
        assert lblCertificados != null : "fx:id=\"lblCertificados\" was not injected: check your FXML file 'Main.fxml'.";
        assert lblPrik != null : "fx:id=\"lblPrik\" was not injected: check your FXML file 'Main.fxml'.";
        assert lblPubk != null : "fx:id=\"lblPubk\" was not injected: check your FXML file 'Main.fxml'.";
        assert scrollEscritura != null : "fx:id=\"scrollEscritura\" was not injected: check your FXML file 'Main.fxml'.";
        assert scrollLectura != null : "fx:id=\"scrollLectura\" was not injected: check your FXML file 'Main.fxml'.";
        assert txtCargarCer != null : "fx:id=\"txtCargarCer\" was not injected: check your FXML file 'Main.fxml'.";

    }
    
    @FXML
    void abrirFormulario() {
    	JOptionPane.showMessageDialog(null, "Apreto ABRIR FORMULARIO", "abrirFormulario()", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @FXML
    void abrirLeerArchivos() {
    	JOptionPane.showMessageDialog(null, "Apreto LEER ARCHIVOS", "LeerArchivos()", JOptionPane.QUESTION_MESSAGE);
    }
    
	@Override
    public void start(Stage stage) throws Exception {

        /*Parent root = FXMLLoader.load(
                getClass().getResource("/JavaFX/Main.fxml")
        );*/
		VBox vbox = new VBox();
		//AnchorPane vbox = new AnchorPane();
		
		FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/JavaFX/Main.fxml")
        );

		
		loader.setRoot(vbox);
		
		Parent root = loader.load();
		
        Scene scene = new Scene(root, 900, 700);

        stage.setTitle("Gestión de Clientes");
        stage.setScene(scene);
        stage.show();
    }
	
    public static void main(String[] args) {
        launch(args);
    }
}
