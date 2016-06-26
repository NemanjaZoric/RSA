package nz.kripto.rsa.view;

import java.math.BigInteger;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.kripto.rsa.MainApp;

/**
 * @author Nemanja
 *
 * Kontroler za RSAView.
 * Ovdje obavljamo sve sto je potrebno za algoritam.
 * 
 * Korisnik, u ime Alise, moze da generise podatke na klik dugmeta, i nakon sto generise sve 
 * podatke, salje poruku.
 * 
 * Vrsi se enkripcija, i prikazuje se kriptovana poruka u bajtovima.
 * 
 * Korisnik onda moze, u ime Boba, da dekriptuje poruku klikom na dugme, pri cemu mu se 
 * prikazuje poruka u bajtovima, kao i poruka kako je izgledala prilikom slanja.
 * 
 * Koristi se BigInteger klasa jave, radi mogucnosti rada sa izuzetno velikim brojevima 
 */

public class RSAViewController {
	
	@FXML BigInteger p; //prost broj p
	@FXML BigInteger q; //prost broj q
	@FXML BigInteger N; //proizvod p i q
	@FXML BigInteger e; //eksponent za enkripciju
	@FXML BigInteger d; //tajni kljuc, eksponent za dekripciju
	@FXML BigInteger phi;
	
	
	//Labele za ispisivanje vrijednosti. Neke od njih se ne koriste jer su zamijenjene
	//dugmadima koje pozivaju modalne prozore
	@FXML Label pLabel;
	@FXML Label qLabel;
	@FXML Label eLabel;
	@FXML Label kriptovana;
	@FXML Label dekriptovanaBit;
	@FXML Label dekriptovana;
	@FXML TextField poruka;
	@FXML Label dLabel;
	@FXML Label nLabel;
	@FXML Label phiLabel;
	
	byte[] encrypted; //niz za enkripciju
	byte[] decrypted; //niz za dekripciju
	int bitlength = 1024;
	int blocksize = 256;
	
	private MainApp mainApp;
	
	private Random r; //random broj
	
	@FXML
	private void handleGenerateP(){
		r = new Random(); //generisemo novi random broj
		
		/* Pozivamo probablePrime iz biblioteke BigInteger, i nalazimo prost broj 
		 * odredjene bitlength*/
		p = BigInteger.probablePrime(bitlength, r);
	}
	
	@FXML
	private void handleGenerateQ(){
		r = new Random();
		
		/* Pozivamo probablePrime iz biblioteke BigInteger, i nalazimo prost broj 
		 * odredjene bitlength*/
		q = BigInteger.probablePrime(bitlength, r);
	}
	
	@FXML
	private void handleGenerateE(){
		r = new Random();
		//e je eksponent enkripcije
		e = BigInteger.probablePrime(bitlength/2, r);
		N = p.multiply(q); //proizvod dva bigInteger broja
		
		//Nalazimo PHI(N) i provjeravamo da li je uzajamno prost sa e
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		while(phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0){
			e.add(BigInteger.ONE);
		}
		//d je eksponent dekripcije
		d = e.modInverse(phi);
	}

	@FXML
	private void handleEncrypt(){
		String teststring = poruka.getText(); //uzimamo uneseni string
		encrypted = encrypt(teststring.getBytes()); //pozivamo metod encrypt
		//String writeEncryptedInBytes = bytesToString(encrypted); 
		//kriptovana.setText(writeEncryptedInBytes); //upisujemo encryptovani string u bajtovima
	}
	
	@FXML
	private void showEncrypted(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Enkriptovana poruka: " + bytesToString(encrypted), ButtonType.OK);
		alert.showAndWait();
	}
	
	@FXML
	private void handleDecrypt(){
		decrypted = decrypt(encrypted); //pozivamo metod decrypt
		//u bajtovima:
		//dekriptovanaBit.setText(bytesToString(decrypted));
		//U stringu:
		//dekriptovana.setText(new String(decrypted));
	}
	
	@FXML
	private void showDecryptedByte(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Dekriptovana poruka: " + bytesToString(decrypted), ButtonType.OK);
		alert.showAndWait();
	}
	
	@FXML
	private void showDecrypted(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Dekriptovana poruka: " + new String(decrypted), ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * @return modalni prozor sa prostim brojem P
	 */
	@FXML
	private void showP(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "P: " + p.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * @return modalni prozor sa prostim brojem Q
	 */
	@FXML
	private void showQ(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Q: " + q.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * @return modalni prozor sa brojem E koji je eksponent enkripcije
	 */
	@FXML
	private void showE(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "E: " + e.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * @return modalni prozor sa brojem D koji je eksponent dekripcije
	 */
	@FXML
	private void showD(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "D: " + d.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	
	/**
	 * @return modalni prozor sa brojem N koji je proizvod p i q 
	 */
	@FXML
	private void showN(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "N: " + N.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * @return modalni prozor sa brojem PHI(N)
	 */
	@FXML
	private void showPhi(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Phi: " + phi.toString(), ButtonType.OK);
		alert.showAndWait();
	}
	
	
	/**
	 * @paaram byte[] encrypted - niz bajtova sa porukom encrypted
	 * @return string bajtova
	 */
	private static String bytesToString(byte[] encrypted) {
		String test = "";
		for (byte b : encrypted) {
			test += Byte.toString(b);
		}
		return test;
	}
	
	/**
	 * @return enkriptovani BigInteger
	 * 
	 * c = m^e (mod n)
	 */
	public byte[] encrypt(byte[] message) {
		 
		return (new BigInteger(message)).modPow(e, N).toByteArray();
 
	}
 
	/**
	 * @return dekriptovani BigInteger
	 * 
	 * c^d = (m^e)^d = m (mod n) 
	 */
	public byte[] decrypt(byte[] message) {
 
		return (new BigInteger(message)).modPow(d, N).toByteArray();
 
	}
	
	/** 
	 * 
	 * Elementi za File meni
	 * 
	 */
	
	@FXML public void handleFileExit(){
		System.exit(0);	//Izadji iz programa
	}
	
	@FXML public void handleMenuHelpAbout(){
		Alert alert = new Alert(AlertType.NONE, "Clanovi tima: " + '\n' + "Nemanja Zoric"+ '\n' + 
	"Ljubo Maricevic" + '\n'  + "Nikolina Sekulic"+ '\n' + "Ivana Cvorovic", ButtonType.OK);
		alert.showAndWait();
	}
	
	@FXML public void handleMenuHelpTutorial(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uputstvo za koriscenje");
		alert.setHeaderText("Koraci za enkripciju i dekripciju");
		alert.setContentText("1. Klikni na Generisi p." + '\n'
				+ "2. Vidi P, tako sto ces kliknuti na Prikazi p." + '\n'
				+ "3. Klikni na Generisi q." + '\n'
				+ "4. Vidi Q, tako sto ces kliknuti na Prikazi q." + '\n'
				+ "5. Klikni na Generisi e." + '\n'
				+ "6. Vidi E, D, N i Phi tako sto ces kliknuti redom na: Prikazi E, Prikazi D, Prikazi N, Prikazi Phi." + '\n'
				+ "7. Unesi tekst poruke koju zelis enkriptovati i klikni Posalji tekst." + '\n'
				+ "8. Klikni na Izvrsi dekripciju kako bi vidio dekriptovanu poruku." + '\n'
				);
		
		alert.showAndWait();
	}

	
	public void setMainApp(MainApp mainApp) {
    	
    	this.mainApp = mainApp;
    
    }
	
	
}
