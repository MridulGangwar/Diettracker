//Mridul Singh Gangwar
//mgangwar

package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers 

	/**Uncomment the following three lines if you want to try out the full-size data files */
//		static final String PRODUCT_FILE = "data/Products.csv";
//		static final String NUTRIENT_FILE = "data/Nutrients.csv";
//		static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image 

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  
		setupBindings();
		stage.setTitle("NutriByte 2.0");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	ObjectBinding<Person> personBinding = new ObjectBinding<Person>() {
		{
			super.bind(view.ageTextField.textProperty(), view.weightTextField.textProperty(), view.heightTextField.textProperty(),
					view.genderComboBox.valueProperty(), view.physicalActivityComboBox.valueProperty());
		}

		@Override
		protected Person computeValue() {
			float age= 0, weight = 0, height = 0;
			float activityLevel = 1;
			int var = 0;
			String ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText();
			TextField heightField = view.heightTextField;
			TextField weightField = view.weightTextField;
			TextField textField = view.ageTextField;

			try {
				textField.setStyle("-fx-text-inner-color: black;");
				age = Float.parseFloat(textField.getText().trim());
				if(age < 0.25 || age >150 || view.ageTextField==null) {  //handle it here
					view.ageTextField.setStyle("-fx-text-inner-color: red;");
					var = 1;
				};
			} catch (NumberFormatException e) {
				view.ageTextField.setStyle("-fx-text-inner-color: red;");
				var = 1;
			}

			try {

				weightField.setStyle("-fx-text-inner-color: black;");
				textField = view.weightTextField;
				weight = Float.parseFloat(textField.getText().trim());
				if (weight < 0 || view.weightTextField==null) {  //handle it here
					view.weightTextField.setStyle("-fx-text-inner-color: red;");
					var = 1;
				};
			} catch (NumberFormatException f) {
				view.weightTextField.setStyle("-fx-text-inner-color: red;");
				var = 1;
			}

			try {
				heightField.setStyle("-fx-text-inner-color: black;");
				textField = view.heightTextField;
				height = Float.parseFloat(textField.getText().trim());
				if(height <0 || view.heightTextField==null) {
					view.heightTextField.setStyle("-fx-text-inner-color: red;");
					var = 1;
				};
			}catch(NumberFormatException g){
				view.heightTextField.setStyle("-fx-text-inner-color: red;");
				var = 1;
			}

			if (view.physicalActivityComboBox.getValue()!= null) {
				for (NutriProfiler.PhysicalActivityEnum activityEnum : NutriProfiler.PhysicalActivityEnum.values()) {
					if ((activityEnum.getName()).equalsIgnoreCase(NutriByte.view.physicalActivityComboBox.getValue())) {
						activityLevel = activityEnum.getPhysicalActivityLevel();
					}
				}
			}
			//Display ingredients to watch
			if (ingredientsToWatch.isEmpty()) {
				ingredientsToWatch = " ";
			}

			if(var==0) {
				if(NutriByte.view.genderComboBox.getValue()!=null)
				{
					if(NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("female")) {
						NutriByte.person = new Female(age, weight, height, activityLevel, ingredientsToWatch);
					}
					else {
						NutriByte.person = new Male(age, weight, height, activityLevel, ingredientsToWatch);
					}
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
					NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
				}
			}
			return person;
		}

	};

	void setupBindings() {
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());


		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);

		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.productsComboBox.setOnAction(controller.new ProductsComboBoxListener());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());

		//Person binding
		personBinding.addListener((observable, oldValue, newValue) -> {
			if(person!=null) {
				NutriProfiler.createNutriProfile(NutriByte.person);
				NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
			}
			else {
				NutriByte.view.recommendedNutrientsTableView.setItems(null);
			}
		});
		
		//Search product binding
		selectedProductBinding.addListener((observable, oldValue, newValue) -> {				
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			if(newValue != null) {
				NutriByte.view.productIngredientsTextArea.setText("Product ingredients: " + newValue.getIngredients());				
				NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(newValue.getProductNutrients().values()));						
			}
		});
	
	}

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};


	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			//write your code here
			return new SimpleStringProperty(String.format("%.2f",arg0.getValue().getNutrientQuantity()));
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			//write your code here
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			return (new SimpleStringProperty(String.format("%.2f", arg0.getValue().getNutrientQuantity())));
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};

	ObjectBinding<Product> selectedProductBinding = new ObjectBinding<Product>() {

		{
			super.bind(NutriByte.view.productsComboBox.getSelectionModel().selectedItemProperty());
		}

		@Override
		protected Product computeValue() {
			return NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
		}		

	};
}
