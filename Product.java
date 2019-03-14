//Mridul Singh Gangwar
//mgangwar

package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product {
	private StringProperty ndbNumber = new SimpleStringProperty();
	private StringProperty productName = new SimpleStringProperty();
	private StringProperty manufacturer = new SimpleStringProperty();
	private StringProperty ingredients = new SimpleStringProperty();
	private FloatProperty servingSize = new SimpleFloatProperty();
	private StringProperty servingUom = new SimpleStringProperty();
	private FloatProperty householdSize = new SimpleFloatProperty();
	private StringProperty householdUom = new SimpleStringProperty();
	private ObservableMap<String,ProductNutrient> productNutrients = FXCollections.observableHashMap(); 
	
	public Product(Product original) {
		this.ndbNumber.set(original.ndbNumber.get());
		this.productName.set(original.productName.get());
		this.manufacturer.set(original.manufacturer.get());
		this.ingredients.set(original.ingredients.get());
		this.servingSize.set(original.servingSize.get());
		this.servingUom.set(original.servingUom.get());
		this.householdSize.set(original.householdSize.get());
		this.householdUom.set(original.householdUom.get());
		this.productNutrients.putAll(original.getProductNutrients());
	}
	
	@Override
	public String toString() {
		return this.getProductName() + " by " + this.getManufacturer();
	}

	public Product(){
		ndbNumber.set(""); 
		productName.set("");
		manufacturer.set("");
		ingredients.set("");
		servingUom.set("");
		householdUom.set("");
	}

	public Product(String ndbNumber, String productName, String manufacturer, String ingredients){
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manufacturer);
		this.ingredients.set(ingredients);
	}

	public final String getNdbNumber() {return ndbNumber.get();}
	public final String getProductName() {return productName.get();}
	public final String getManufacturer() {return manufacturer.get();}
	public final String getIngredients() {return ingredients.get();}
	public final Float getServingSize() {return servingSize.get();}
	public final String getServingUom() {return servingUom.get();}
	public final Float getHouseholdSize() {return householdSize.get();}
	public final String getHouseholdUom() {return householdUom.get();}
    
	

	public ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	public final void setNdbNumber(String ndbNumber) {this.ndbNumber.set(ndbNumber);}
	public final void setProductName(String productName) {	this.productName.set(productName);}
	public final void setManufacturer(String manufacturer) {this.manufacturer.set(manufacturer);}
	public final void setIngredients(String ingredients) {this.ingredients.set(ingredients);}
	public final void setServingSize(Float servingSize) {this.servingSize.set(servingSize);}
	public final void setServingUom(String servingUom) {this.servingUom.set(servingUom);}
	public final void setHouseholdSize(Float householdSize) {this.householdSize.set(householdSize);}
	public final void setHouseholdUom(String householdUom) {this.householdUom.set(householdUom);}


	public final StringProperty ndbNumberProperty() {return ndbNumber;}
	public final StringProperty productNameProperty() {return productName;}
	public final StringProperty manufacturerProperty() {return manufacturer;}
	public final StringProperty ingredientsProperty() {return ingredients;}
	public final FloatProperty servingSizeProperty() {return servingSize;}
	public final StringProperty servingUomProperty() {return servingUom;}
	public final FloatProperty householdSizeProperty() {return householdSize;}
	public final StringProperty householdUomProperty() {return householdUom;}

	class ProductNutrient{
		private StringProperty nutrientCode = new SimpleStringProperty();
		private FloatProperty nutrientQuantity = new SimpleFloatProperty();

		public ProductNutrient(){
			nutrientCode.set("");
		}

		public ProductNutrient(String nutrientCode, float nutrientQuantity) {
			this.nutrientCode.set(nutrientCode);;
			this.nutrientQuantity.set(nutrientQuantity);;
		}

		public final String getNutrientCode() {return nutrientCode.get();}
		public final Float getNutrientQuantity() {return nutrientQuantity.get();}

		public final void setNutrientCode(String nutrientCode) {this.nutrientCode.set(nutrientCode);}
		public final void setNutrientQuantity(Float nutrientQuantity) {this.nutrientQuantity.set(nutrientQuantity);}

		public final StringProperty nutrientCodeProperty() {return nutrientCode;}
		public final FloatProperty nutrientQuantityProperty() {return nutrientQuantity;}

	}

}
