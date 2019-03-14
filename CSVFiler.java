//Mridul Singh Gangwar
//mgangwar

package hw3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVFiler extends DataFiler{
	
	@Override
	public void writeFile(String filename) {}

	@Override
	public boolean readFile(String filename) {
		// TODO Auto-generated method stub

		CSVFormat csvFormat = CSVFormat.DEFAULT;
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			int count = 1;			
			for(CSVRecord csvRecord : csvParser) {
				if(count == 1) {
					// validate person data here
					StringBuilder personDataSb = new StringBuilder();
					for(int i = 0; i < csvRecord.size(); i++) {
						personDataSb.append(csvRecord.get(i) + ",");
					}
					if(personDataSb.length() > 0) {
						personDataSb.setLength(personDataSb.length() - 1);
						NutriByte.person = validatePersonData(personDataSb.toString());
						if(NutriByte.person == null) 
							return false;
					}			
					count++;
				} else {
					//validate product data here
					StringBuilder productDataSb = new StringBuilder();
					for(int i = 0; i < csvRecord.size(); i++) {
						productDataSb.append(csvRecord.get(i) + ",");
					}
					if(productDataSb.length() > 0) {
						productDataSb.setLength(productDataSb.length() - 1);
						Product p = validateProductData(productDataSb.toString());
						if(p != null) {
							NutriByte.model.dietProductsList.add(p);
						}
					}					
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	//validating person data
	Person validatePersonData(String data) {
		
		String[] details = data.split(",");

		String gender = null;
		float age = 0;
		float weight = 0;
		boolean status = false;
		boolean flag = true;
        StringBuilder ingredientsToWatch = new StringBuilder();

		
        try {
        	gender = details[0].trim();
            if(!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female"))  throw new InvalidProfileException("The profile must have gender: Female or Male as first word");
        } catch (Exception e){
            new InvalidProfileException("Could not read profile data");
            flag = false;
        }
		
        if (flag==true) {
            try {
                age = Float.parseFloat(details[1].trim());
            } catch (NumberFormatException n) {
                new InvalidProfileException("Invalid data for Age: " + details[1] + "\nAge must be a number");
                new InvalidProfileException("Could not read profile data");
                flag = false;
            }
        }
 
        if (flag==true) {
            try {
                weight = Float.parseFloat(details[2].trim());
            } catch (NumberFormatException n) {
                new InvalidProfileException("Invalid data for weight: " + details[2] + "\nWeight must be a number");
                new InvalidProfileException("Could not read profile data");
                flag = false;
            }
        }      
        
        if (flag==true) {
            for (NutriProfiler.PhysicalActivityEnum physicalenum : NutriProfiler.PhysicalActivityEnum.values()){
                if (Float.parseFloat(details[4].trim())==physicalenum.getPhysicalActivityLevel()) {
                    status = true;
                }
            }
            if (status == false) {
                new InvalidProfileException("Invalid physical activity level: " + details[4] + "\nMust be 1.0, 1.1, 1.25, or 1.48");
                new InvalidProfileException("Could not read profile data");
                flag = false;
            }
        }
       
        if (flag==true) {
            try {
                Float.parseFloat(details[3].trim());
            } catch(NumberFormatException n) {
                new InvalidProfileException("Invalid data for height: " + details[3] + "\nHeight must be a number");
                new InvalidProfileException("Could not read profile data");
                flag =false;
            }
        }      
 
        if (flag==true) {
 
            for (int i = 5;i < details.length;i++){
                ingredientsToWatch.append(details[i].trim()+ ", ");
            }
 
            if (ingredientsToWatch.length() > 0)
                ingredientsToWatch.replace(ingredientsToWatch.toString().length()-2, ingredientsToWatch.toString().length()-1, "");
            
            
            if (age != 0 && weight !=0 && gender != null) {
	            switch (gender.toLowerCase()) {
					case("female"):
						NutriByte.person = new Female(age, weight, Float.parseFloat(details[3].trim()),Float.parseFloat(details[4].trim()), ingredientsToWatch.toString());
						return NutriByte.person;
					case("male"):					
						NutriByte.person = new Male(age, weight, Float.parseFloat(details[3].trim()),Float.parseFloat(details[4].trim()), ingredientsToWatch.toString());
						return NutriByte.person;
				default:
					break;
				}
	        }
        }
		return null;
        
    }
			
//	validating products data
	Product validateProductData(String data) {
		float servingSize = 0;
		float householdSize = 0;
		String productIdentifier =null;
		
		try {
			
			if(data.split(",").length < 3) {
				throw new InvalidProfileException("Cannot Read: " + data + "\nThe data must be - String, number, number - for ndb number," +"\n serving size, household size");
			}
			
			if(!Model.productsMap.containsKey(data.split(",")[0].trim())) {
				throw new InvalidProfileException("No product found with this code: " + data.split(",")[0]);
			}
			
			productIdentifier = data.split(",")[0].trim();
			
			try {
				servingSize = Float.parseFloat(data.split(",")[1].trim());
			} 
			catch (NumberFormatException e) {
				throw new InvalidProfileException("Cannot Read: " + data + "\nThe data must be - String, number, number - for ndb number," +"\n serving size, household size");
			}
			
			if(servingSize <= 0) {
				throw new InvalidProfileException("Serving size must be greater than or equal to 0");
			}
			Model.productsMap.get(productIdentifier).setServingSize(servingSize);
			
			try {
				householdSize = Float.parseFloat(data.split(",")[2].trim());
			} catch (NumberFormatException e) {
				throw new InvalidProfileException("Cannot Read: " + data + "\nThe data must be - String, number, number - for ndb number," +"\n serving size, household size");
			}
			
			if(householdSize <= 0) {
				throw new InvalidProfileException("Household size must be greater than or equal to 0");
			}
			Model.productsMap.get(productIdentifier).setHouseholdSize(householdSize);
			
			
			return Model.productsMap.get(productIdentifier);
			
		} catch(InvalidProfileException e) {
			return null;
		}	
	}
	
}