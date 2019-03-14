//Mridul Singh Gangwar
//mgangwar

package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Model {

	static ObservableMap<String,Product> productsMap = FXCollections.observableHashMap();
	static ObservableMap<String,Nutrient> nutrientsMap = FXCollections.observableHashMap();
	ObservableList<Product> searchResultsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();



	void readProducts(String filename) {
		// reading products map		
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				Product product = new Product(csvRecord.get(0), csvRecord.get(1),csvRecord.get(4), csvRecord.get(7));
				productsMap.put(csvRecord.get(0), product);
			}
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }
	}

	void readNutrients(String filename) {
		//		populating nutrients map
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				Nutrient nutrient = new Nutrient(csvRecord.get(1), csvRecord.get(2),csvRecord.get(5));
				nutrientsMap.put(csvRecord.get(1), nutrient);

				Product p = productsMap.get(csvRecord.get(0));
				if (!p.getProductNutrients().containsKey(csvRecord.get(1)) && Float.parseFloat(csvRecord.get(4).trim())>0 ) {
					Product product = new Product();
					Product.ProductNutrient pm = product.new ProductNutrient(csvRecord.get(1),
							Float.parseFloat(csvRecord.get(4).trim()));
					p.getProductNutrients().put(csvRecord.get(1), pm);
				}
			}
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }		

	}

	void readServingSizes(String filename) {
		//		reading serving size
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				if(csvRecord.get(1).isEmpty() || csvRecord.get(2).isEmpty() || csvRecord.get(3).isEmpty() || csvRecord.get(4).isEmpty()) {
					productsMap.get(csvRecord.get(0)).setServingUom(csvRecord.get(2));
					productsMap.get(csvRecord.get(0)).setHouseholdUom(csvRecord.get(4));
				}
				else {
					productsMap.get(csvRecord.get(0)).setServingSize(Float.parseFloat(csvRecord.get(1)));
					productsMap.get(csvRecord.get(0)).setServingUom(csvRecord.get(2));
					productsMap.get(csvRecord.get(0)).setHouseholdSize(Float.parseFloat(csvRecord.get(3)));
					productsMap.get(csvRecord.get(0)).setHouseholdUom(csvRecord.get(4));
				}
			}

		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

	}

	public boolean readProfiles(String filename) {
		// TODO Auto-generated method stub
		String fileExtension = filename.substring(filename.length()-3, filename.length());

		if(fileExtension.equals("csv")) {
			CSVFiler file = new CSVFiler();
			return file.readFile(filename);
		}
		else if(fileExtension.equals("xml")) {
			XMLFiler file = new XMLFiler();
			return file.readFile(filename);
		}

		return false;
	}

//	Writing profiles in csv format
	public static void writeProfile(String profileString) {
		String userProfile = profileString;


		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file for profile");
		fileChooser.setInitialDirectory(new File ("profiles"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("csv Files","*.csv"));
		File file= fileChooser.showSaveDialog(null);
		if (file !=null) {
			try {
				PrintWriter pw = new PrintWriter(file.getAbsolutePath());

				String[] data = NutriByte.person.storeProd.toString().split("\n");

				pw.write(userProfile);
				pw.write("\n");
				for(int i=0;i<data.length;i++) {
					pw.write(data[i].toString());
					pw.write("\n");
				}

				pw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} 
	}
}
