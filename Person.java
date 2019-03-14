//Mridul Singh Gangwar
//mgangwar

package hw3;

import hw3.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public  abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];


	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableMap<String,RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();
	static StringBuilder storeProd= new StringBuilder();

	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();

	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		//write your code here
		this.age=age;
		this.weight=weight;
		this.height=height;
		this.physicalActivityLevel=physicalActivityLevel;
		this.ingredientsToWatch=ingredientsToWatch;

		if(age<=0.25f) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_3M;}
		else if (age<=0.5f) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_6M;}
		else if (age<=1) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_1Y;}
		else if (age<=3) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_3Y;}
		else if (age<=8) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_8Y;}
		else if (age<=13) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_13Y;}
		else if (age<=18) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_18Y;}
		else if (age<=30) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_30Y;}
		else if (age<=50) {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_50Y;}
		else {ageGroup =NutriProfiler.AgeGroupEnum.MAX_AGE_ABOVE;}

	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the 
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc. 

	float[] calculateNutriRequirement() {
		//write your code here
		float[] nutriRequirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];

		for (int i=0;i<nutriRequirement.length;i++) {
			if (i==0) {
				nutriRequirement[i] = nutriConstantsTable[i][ageGroup.getAgeGroupIndex()]*this.weight;
			} else if (i<3) {
				nutriRequirement[i] = nutriConstantsTable[i][ageGroup.getAgeGroupIndex()];
			} else nutriRequirement[i] = (nutriConstantsTable[i][ageGroup.getAgeGroupIndex()])*this.weight/1000;
		}

		return nutriRequirement;
	}

	//Helper method to store diet nutrient list
	public void storeProducts(Product prod) {
		storeProd.append(prod.getNdbNumber() + "," + prod.getServingSize() + "," + prod.getHouseholdSize() + "\n");
    }
	
	void populateDietNutrientsMap() {
		dietNutrientsMap.clear();	
		storeProd.setLength(0);
		
		for(Product p : NutriByte.model.dietProductsList) {
			storeProducts(p);
			for(ProductNutrient pn : p.getProductNutrients().values()) {
				if(dietNutrientsMap.containsKey(pn.getNutrientCode())) {
					RecommendedNutrient rn = dietNutrientsMap.get(pn.getNutrientCode());					
					float nutrientQuantity = rn.getNutrientQuantity() + (p.getServingSize() * pn.getNutrientQuantity()/100);					
					dietNutrientsMap.put(pn.getNutrientCode(), new RecommendedNutrient(pn.getNutrientCode(),nutrientQuantity));
				} else {
					float nutrientQuantity = p.getServingSize() * pn.getNutrientQuantity()/100;					
					dietNutrientsMap.put(pn.getNutrientCode(), new RecommendedNutrient(pn.getNutrientCode(),nutrientQuantity));
				}
			}
		}
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(age);
		result = prime * result + ((ageGroup == null) ? 0 : ageGroup.hashCode());
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(physicalActivityLevel);
		result = prime * result + Float.floatToIntBits(weight);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (Float.floatToIntBits(age) != Float.floatToIntBits(other.age))
			return false;
		if (ageGroup != other.ageGroup)
			return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(physicalActivityLevel) != Float.floatToIntBits(other.physicalActivityLevel))
			return false;
		if (Float.floatToIntBits(weight) != Float.floatToIntBits(other.weight))
			return false;
		return true;
	}		
}

