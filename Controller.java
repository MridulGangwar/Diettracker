//Mridul Singh Gangwar
//mgangwar

package hw3;

import java.io.File;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {

	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here
			TextField ageField = NutriByte.view.ageTextField;
			TextField heightField = NutriByte.view.heightTextField;
			TextField weightField = NutriByte.view.weightTextField;

			try {
				//Check exception for Gender ComboBox
				if(NutriByte.view.genderComboBox.getValue()==null) throw new InvalidProfileException("Missing Gender Information");

				//Check exceptions for Age InputField
				if(ageField.getText().isEmpty()) throw new InvalidProfileException("Missing age information");
				try {
					Float.parseFloat(ageField.getText());
				}catch(NumberFormatException e){ 
					throw new InvalidProfileException("Incorrect age input. Must be a number");
				}
				if(Float.parseFloat(ageField.getText().trim())<0 ) throw new InvalidProfileException("Age must be a positive number");

				//Check exception for Weight InputField
				if(weightField.getText().isEmpty()) throw new InvalidProfileException("Missing weight information");
				try {
					Float.parseFloat(weightField.getText());
				}
				catch(NumberFormatException e){ 
					throw new InvalidProfileException("Incorrect weight input. Must be a number");
				}
				if(Float.parseFloat(weightField.getText().trim())<0 ) throw new InvalidProfileException("Weight must be a positive number");


				//Check exception for Height InputField
				if(heightField.getText().isEmpty()) throw new InvalidProfileException("Missing height information");
				try {
					Float.parseFloat(heightField.getText());
				} catch(NumberFormatException e){ 
					throw new InvalidProfileException("Incorrect height input. Must be a number");
				}
				if(Float.parseFloat(heightField.getText().trim())<0 ) throw new InvalidProfileException("Height must be a positive number");


				float age=0.0f;
				float weight=0.0f;
				float height=0.0f;

				//checking whether age, weight, height are null or not			
				if (NutriByte.view.ageTextField.getText().isEmpty()) ; 
				else  age = Float.parseFloat(NutriByte.view.ageTextField.getText().trim());
				if (NutriByte.view.weightTextField.getText().isEmpty()) ; 
				else  weight = Float.parseFloat(NutriByte.view.weightTextField.getText().trim());
				if (NutriByte.view.heightTextField.getText().isEmpty()) ;
				else height = Float.parseFloat(NutriByte.view.heightTextField.getText().trim());

				//default physical activity level is sedentary
				float physicalActivity = 1f;
				if(NutriByte.view.physicalActivityComboBox.getSelectionModel().isEmpty()) ;
				else if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().equals("Sedentary")) {
					physicalActivity = 1f;
				} else if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().equals("Low active")) {
					physicalActivity =1.1f;
				} else if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().equals("Active")) {
					physicalActivity =1.25f;
				} else if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().equals("High active")) {
					physicalActivity =1.48f;
				}	

				//Initialize person class only if gender is present
				if(!NutriByte.view.genderComboBox.getSelectionModel().isEmpty()) {
					if("Male".equalsIgnoreCase(NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem())) {
						NutriByte.person = new Male(age, weight, height, physicalActivity, NutriByte.view.ingredientsToWatchTextArea.getText());
					} else if ("Female".equalsIgnoreCase(NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem())){
						NutriByte.person = new Female(age, weight, height, physicalActivity, NutriByte.view.ingredientsToWatchTextArea.getText());
					}
					NutriProfiler.createNutriProfile(NutriByte.person);
				}


				NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

			}
			catch(InvalidProfileException | NullPointerException e) {

			}
		}			
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//to open saved profiles
			if (NutriByte.person!=null) {
				NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File (NutriByte.NUTRIBYTE_PROFILE_PATH));
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files (*.csv)","*.csv"));
			File file= null;

			if ((file = fileChooser.showOpenDialog(NutriByte.view.root.getScene().getWindow())) !=null) {
				String Path = file.getAbsolutePath();
				NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
				NutriByte.view.initializePrompts();		
				NutriByte.person = null;
				NutriByte.view.nutriChart.clearChart();
				NutriByte.view.productNutrientsTableView.getItems().clear();
				NutriByte.view.productIngredientsTextArea.clear();
				NutriByte.view.servingSizeLabel.setText("");
				NutriByte.view.productsComboBox.getItems().clear();			
				NutriByte.model.dietProductsList.clear();
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.householdSizeLabel.setText("");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
				NutriByte.model.readProfiles(Path);

				if (NutriByte.person != null) {

					if (NutriByte.person instanceof Male) 
						NutriByte.view.genderComboBox.setValue("Male");
					else
						NutriByte.view.genderComboBox.setValue("Female");
					if (NutriByte.person.ingredientsToWatch !=null) {
						NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
					}

					if (NutriByte.person.physicalActivityLevel==1.48f)
						NutriByte.view.physicalActivityComboBox.setValue(NutriProfiler.PhysicalActivityEnum.VERY_ACTIVE.getName() );
					else if (NutriByte.person.physicalActivityLevel==1f) 
						NutriByte.view.physicalActivityComboBox.setValue(NutriProfiler.PhysicalActivityEnum.SEDENTARY.getName() );
					else if  (NutriByte.person.physicalActivityLevel==1.1f)
						NutriByte.view.physicalActivityComboBox.setValue(NutriProfiler.PhysicalActivityEnum.LOW_ACTIVE.getName() );
					else if (NutriByte.person.physicalActivityLevel==1.25f)
						NutriByte.view.physicalActivityComboBox.setValue(NutriProfiler.PhysicalActivityEnum.ACTIVE.getName() );

					NutriByte.view.ageTextField.setText(String.format("%.2f", NutriByte.person.age));
					NutriByte.view.weightTextField.setText(String.format("%.2f", NutriByte.person.weight));
					NutriByte.view.heightTextField.setText(String.format("%.2f", NutriByte.person.height));

					NutriProfiler.createNutriProfile(NutriByte.person);
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);			
				}

				if(NutriByte.model.dietProductsList.size() > 0) {					
					NutriByte.view.dietProductsTableView.setItems(NutriByte.model.dietProductsList);
					NutriByte.view.dietServingSizeTextField.setText("");
					NutriByte.view.dietHouseholdSizeTextField.setText("");
					NutriByte.model.searchResultsList.clear();
					NutriByte.model.searchResultsList.addAll(NutriByte.model.dietProductsList);
					NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);            
					NutriByte.view.productsComboBox.setValue(NutriByte.model.searchResultsList.get(0));
					NutriByte.view.dietServingUomLabel.setText(NutriByte.model.searchResultsList.get(0).getServingUom());
					NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.model.searchResultsList.get(0).getHouseholdUom());
					NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");
					NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.model.searchResultsList.get(0).getProductNutrients().values()));
					NutriByte.view.productIngredientsTextArea.setText("Product ingredients: " + NutriByte.model.searchResultsList.get(0).getIngredients());				
					NutriByte.view.servingSizeLabel.setText(String.format("%.2f",Model.productsMap.get(NutriByte.model.searchResultsList.get(0).getNdbNumber()).getServingSize()) + " " + NutriByte.model.searchResultsList.get(0).getServingUom());
					NutriByte.view.householdSizeLabel.setText(String.format("%.2f",Model.productsMap.get(NutriByte.model.searchResultsList.get(0).getNdbNumber()).getHouseholdSize()) + " " + NutriByte.model.searchResultsList.get(0).getHouseholdUom());
				}

				if(NutriByte.person != null && NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.person.populateDietNutrientsMap();
					NutriByte.view.nutriChart.updateChart();
				}
			}		
		}
	}

	class SaveMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent arg0) {
			StringBuilder userProfile = new StringBuilder();

			TextField heightField = NutriByte.view.heightTextField;
			TextField weightField = NutriByte.view.weightTextField;
			TextField ageField = NutriByte.view.ageTextField;
			float activityLevel =1;

			boolean errors = false;

			try {

				//Checking exception for gender 
				if(NutriByte.view.genderComboBox.getValue()==null) throw new InvalidProfileException("Missing Gender Information");

				//Checking exception for age 
				if(ageField.getText().isEmpty()) throw new InvalidProfileException("Missing age information");

				try {
					Float.parseFloat(ageField.getText());
				}
				catch(NumberFormatException e){ throw new InvalidProfileException("Incorrect age input. Must be a number");}
				if(Float.parseFloat(ageField.getText().trim())<0 ) throw new InvalidProfileException("Age must be a positive number");

				//Checking exception for weight 
				if(weightField.getText().isEmpty()) throw new InvalidProfileException("Missing weight information");
				try {
					Float.parseFloat(weightField.getText());
				}
				catch(NumberFormatException e){ throw new InvalidProfileException("Incorrect weight input. Must be a number");}
				if(Float.parseFloat(weightField.getText().trim())<0 ) throw new InvalidProfileException("Weight must be a positive number");


				//Checking exception for height 
				if(heightField.getText().isEmpty()) throw new InvalidProfileException("Missing height information");
				try {
					Float.parseFloat(heightField.getText());
				}
				catch(NumberFormatException e){ throw new InvalidProfileException("Incorrect height input. Must be a number");}
				if(Float.parseFloat(heightField.getText().trim())<0 ) throw new InvalidProfileException("Height must be a positive number");
			}
			catch(InvalidProfileException | NullPointerException | NumberFormatException e ){errors = true;}

			if(errors==false) {
				userProfile.append(NutriByte.view.genderComboBox.getValue() + ",");
				userProfile.append(ageField.getText() + ",");
				userProfile.append(weightField.getText() + ",");
				userProfile.append(heightField.getText() + ",");

				for (NutriProfiler.PhysicalActivityEnum activityEnum : NutriProfiler.PhysicalActivityEnum.values()) {
					if ((activityEnum.getName()).equalsIgnoreCase(NutriByte.view.physicalActivityComboBox.getValue())) {
						activityLevel = activityEnum.getPhysicalActivityLevel();
					}
				}
				userProfile.append(activityLevel + ",");
				userProfile.append(NutriByte.view.ingredientsToWatchTextArea.getText());

				NutriByte.model.writeProfile(userProfile.toString());
			}
		}
	}


	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			if(NutriByte.person != null) {
				NutriByte.person.recommendedNutrientsList.clear();
				NutriByte.view.nutriChart.clearChart();
				NutriByte.model.searchResultsList.clear();
				NutriByte.view.productNutrientsTableView.getItems().clear();
				NutriByte.view.productsComboBox.getItems().clear();
				NutriByte.view.dietProductsTableView.getItems().clear();
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.servingSizeLabel.setText("");
				NutriByte.view.householdSizeLabel.setText("");
				NutriByte.view.physicalActivityComboBox.setValue(NutriProfiler.PhysicalActivityEnum.SEDENTARY.getName() );
				NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
			}
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String productSearch = null;
			String nutrientSearch = null;
			String ingredientSearch = null;
			String nutrientCode = null;

			NutriByte.model.searchResultsList.clear();

			// creating 8 different scenarios for search criteria

			if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.nutrientSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText();
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						nutrientSearch = NutriByte.view.nutrientSearchTextField.getText();
						for (Map.Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
							if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
								nutrientCode = nutName.getValue().getNutrientCode();
								if (prodName.getValue().getProductNutrients().containsKey(nutrientCode)) {
									if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
										NutriByte.model.searchResultsList.add(prodName.getValue());
								}
							}  
						}  
					}
				}
			} else if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.nutrientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						nutrientSearch = NutriByte.view.nutrientSearchTextField.getText();
						for (Map.Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
							if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
								nutrientCode = nutName.getValue().getNutrientCode();
								if (prodName.getValue().getProductNutrients().containsKey(nutrientCode)) {
									NutriByte.model.searchResultsList.add(prodName.getValue());
								}
							}
						}
					}
				}
			} else if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText();
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
							NutriByte.model.searchResultsList.add(prodName.getValue());
					}
				}
			} else if (!NutriByte.view.nutrientSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {    
					nutrientSearch = NutriByte.view.nutrientSearchTextField.getText();
					for (Map.Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
						if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
							nutrientCode = nutName.getValue().getNutrientCode();
							if (prodName.getValue().getProductNutrients().containsKey(nutrientCode)) {
								if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
									NutriByte.model.searchResultsList.add(prodName.getValue());
							}
						}
					}      
				}              
			} else if (!NutriByte.view.productSearchTextField.getText().isEmpty()) {
				productSearch = NutriByte.view.productSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {        
						NutriByte.model.searchResultsList.add(prodName.getValue());
					}
				}
			} else if (!NutriByte.view.nutrientSearchTextField.getText().isEmpty() ) {
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {            
					nutrientSearch = NutriByte.view.nutrientSearchTextField.getText();
					for (Map.Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
						if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
							nutrientCode = nutName.getValue().getNutrientCode();
							if (prodName.getValue().getProductNutrients().containsKey(nutrientCode)) {
								NutriByte.model.searchResultsList.add(prodName.getValue());
							}
						}  
					}
				}
			} else if (!NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText();
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
						NutriByte.model.searchResultsList.add(prodName.getValue());
				}
			}
			else {
				for (Map.Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet())
					NutriByte.model.searchResultsList.add(prodName.getValue());
			}

			if (NutriByte.model.searchResultsList.size()>0) {
				NutriByte.view.searchResultSizeLabel.setText(String.valueOf(NutriByte.model.searchResultsList.size())+" product(s) found");
				NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
				NutriByte.view.productsComboBox.setValue(NutriByte.model.searchResultsList.get(0));
				Product product = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
				NutriByte.view.productIngredientsTextArea.setText("Product ingredients: "+product.getIngredients());
				NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.model.searchResultsList.get(0).getProductNutrients().values()));
			}

		}		
	}	

	class ProductsComboBoxListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0) {
				NutriByte.view.servingSizeLabel.setText(String.format("%.2f", NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize()) + " "
						+ NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				NutriByte.view.householdSizeLabel.setText(String.format("%.2f", NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize()) + " " +
						NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				NutriByte.view.dietServingUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				NutriByte.view.productIngredientsTextArea.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngredients());
			}			
		}
	}

	class AddDietButtonHandler implements EventHandler<ActionEvent> {
//		adding product to the diet
		@Override
		public void handle(ActionEvent arg0) {
			if(!NutriByte.view.productsComboBox.getSelectionModel().isEmpty()) {
				Product prod = new Product(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem());				
				float servingSizeHouseHoldSizeRatio = prod.getServingSize()/prod.getHouseholdSize();

				if(NutriByte.view.dietServingSizeTextField.getText().trim() != null && NutriByte.view.dietServingSizeTextField.getText().trim().length() > 0) {
					prod.setServingSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()));
					prod.setHouseholdSize(prod.getServingSize()/servingSizeHouseHoldSizeRatio);
					NutriByte.model.dietProductsList.add(prod);
				} else if(NutriByte.view.dietHouseholdSizeTextField.getText().trim() != null && NutriByte.view.dietHouseholdSizeTextField.getText().trim().length() > 0) {
					prod.setHouseholdSize(Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText().trim()));
					prod.setServingSize(servingSizeHouseHoldSizeRatio * prod.getHouseholdSize());
					NutriByte.model.dietProductsList.add(prod);
				} else {
					NutriByte.model.dietProductsList.add(prod);
				}
			}
			NutriByte.view.dietProductsTableView.setItems(NutriByte.model.dietProductsList);
			NutriByte.view.dietServingSizeTextField.setText("");
			NutriByte.view.dietHouseholdSizeTextField.setText("");

			if(NutriByte.person != null && NutriByte.model.dietProductsList.size() > 0) {
				NutriByte.person.populateDietNutrientsMap();
				NutriByte.view.nutriChart.updateChart();
			}
		}		
	}

	class RemoveDietButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {

			if(!NutriByte.view.dietProductsTableView.getSelectionModel().isEmpty()) {
				Product productRemove = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
				NutriByte.model.dietProductsList.remove(productRemove);
				//NutriByte.person.populateDietNutrientsMap(); 
				if(NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.view.nutriChart.updateChart();
				} else {
					NutriByte.view.nutriChart.clearChart();
				}
			}
			if(NutriByte.person != null) {
				NutriByte.person.populateDietNutrientsMap();
				if(NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.view.nutriChart.updateChart();
				} else {
					NutriByte.view.nutriChart.clearChart();
				}
			}
		}	


	}

	class CloseMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.initializePrompts();
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.productSearchTextField.setText("");
			NutriByte.view.nutrientSearchTextField.setText("");
			NutriByte.view.ingredientSearchTextField.setText("");
			NutriByte.view.productIngredientsTextArea.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.productsComboBox.getItems().clear();
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
		}

	}

	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 3.0 \nRelease 3.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}


}
