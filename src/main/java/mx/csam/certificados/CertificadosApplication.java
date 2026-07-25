package mx.csam.certificados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mx.csam.certificados.GUI.CertificadosView;

@SpringBootApplication
public class CertificadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertificadosApplication.class, args);
		new CertificadosView().setVisible(true);
	}

}
