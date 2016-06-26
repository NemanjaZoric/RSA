package nz.kripto.rsa;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nz.kripto.rsa.view.RSAViewController;


/**
 * @author Nemanja
 * 
 * Glavni program u kojem se pozivaju kontroler i pogledi koje ta klasa kontrolise.
 * 
 * Inicijalizuju se pogled RootLayout() koji prikazuje osnovni prozor sa granicama,
 * kao i RSAView koji sluzi za prikaz svega sto nam je potrebno za aplikaciju.
 *
 */
public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("RSA Enkripcija i Dekripcija");
		
		initRootLayout();
		
		showRsaView();
	}
	
	public void initRootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void showRsaView() {
	    try {
	        // Ucitava RSAView.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/RSAView.fxml"));
	        AnchorPane rsaView = (AnchorPane) loader.load();

	        // Postavlja ga u centar layout-a
	        rootLayout.setCenter(rsaView);

	        // Daje kontroloru pristup glavnom programu
	        RSAViewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
