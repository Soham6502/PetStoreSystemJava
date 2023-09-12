package com.petstoresystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    private final Connection connection;
    private final DbConnection DbConnObj;
    
    
    public PetDAO() {
    	DbConnObj = new DbConnection();
    	connection = DbConnObj.getConnection();
	}



    // Add new pet details to the database
    public int addPetDetails(Pet pet) {
        String INSERT_PET_SQL = "INSERT INTO pets (petId, petCategory, petType, color, age, price, isVaccinated, foodHabits) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = 0;

        try  {
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PET_SQL);
            preparedStatement.setInt(1, pet.getPetId());
            preparedStatement.setString(2, pet.getPetCategory());
            preparedStatement.setString(3, pet.getPetType());
            preparedStatement.setString(4, pet.getColor());
            preparedStatement.setInt(5, pet.getAge());
            preparedStatement.setDouble(6, pet.getPrice());  
            preparedStatement.setString(7, pet.isVaccinated() ? "Y" : "N");
//            preparedStatement.setBoolean(7, pet.isVaccinated());
            preparedStatement.setString(8, pet.getFoodHabits());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	System.out.println("Error while Adding pet details , Please try Again");
           // e.printStackTrace();
        }
        return rowsAffected;
    }

    // Update pet price and vaccination status in the database
    public boolean updatePetPriceAndVaccinationStatus(int petId, double newPrice, boolean newVaccinationStatus) {
        String UPDATE_PET_SQL = "UPDATE pets SET price = ?, isVaccinated = ? WHERE petId = ?";
        boolean updated = false;

        try {
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PET_SQL);

            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setString(2, newVaccinationStatus ? "Y" : "N");
//            preparedStatement.setBoolean(2, newVaccinationStatus);
            preparedStatement.setInt(3, petId);

            int rowsAffected = preparedStatement.executeUpdate();
            updated = rowsAffected > 0;
        } catch (SQLException e) {
        	System.out.println("Error while Updating , Please try Again");
          //  e.printStackTrace();
        }
        return updated;
    }

    // Delete a pet by its ID from the database
    public boolean deletePetDetails(int petId) {
        String DELETE_PET_SQL = "DELETE FROM pets WHERE petId = ?";
        boolean deleted = false;

        try {
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PET_SQL);

            preparedStatement.setInt(1, petId);

            int rowsAffected = preparedStatement.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
        	System.out.println("Error while deleting , Please try Again");
         //   e.printStackTrace();
        }
        return deleted;
    }

    // Retrieve all pets from the database
    public List<Pet> showAllPets() {
        List<Pet> pets = new ArrayList<>();
        String SELECT_ALL_PETS_SQL = "SELECT * FROM pets";

        try {
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PETS_SQL) ;

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int petId = resultSet.getInt("petId");
                String petCategory = resultSet.getString("petCategory");
                String petType = resultSet.getString("petType");
                String color = resultSet.getString("color");
                int age = resultSet.getInt("age");
                double price = resultSet.getDouble("price");
                boolean isVaccinated = "Y".equals(resultSet.getString("isVaccinated"));
               // boolean isVaccinated = resultSet.getBoolean("isVaccinated");
                String foodHabits = resultSet.getString("foodHabits");

                Pet pet = new Pet(petId, petCategory, petType, color, age, price, isVaccinated, foodHabits);
                pets.add(pet);
            }
        } catch (SQLException e) {
        	System.out.println("Error while Showing , Please try Again");
           // e.printStackTrace();
        }

        return pets;
    }

	public List<Pet> searchByPrice(double minPrice, double maxPrice) {
		  String SEARCH_PETS_SQL = "SELECT * FROM pets WHERE price >= ? AND price <= ?";
	        List<Pet> matchingPets = new ArrayList<>();

	        try {
	             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PETS_SQL); 

	            preparedStatement.setDouble(1, minPrice);
	            preparedStatement.setDouble(2, maxPrice);

	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                int petId = resultSet.getInt("petId");
	                String petCategory = resultSet.getString("petCategory");
	                String petType = resultSet.getString("petType");
	                String color = resultSet.getString("color");
	                int age = resultSet.getInt("age");
	                double price = resultSet.getDouble("price");
//	                boolean isVaccinated = resultSet.getBoolean("isVaccinated");
	                boolean isVaccinated = "Y".equals(resultSet.getString("isVaccinated"));
	                String foodHabits = resultSet.getString("foodHabits");

	                Pet pet = new Pet(petId, petCategory, petType, color, age, price, isVaccinated, foodHabits);
	                matchingPets.add(pet);
	            }
	        } catch (SQLException e) {
	        	System.out.println("Error while Searching , Please try Again");
	        //	e.printStackTrace();
	        }

	        return matchingPets;
	}

	public int countByVaccinationStatusForPetType(String petType) {

		 String COUNT_VACCINATED_SQL = "SELECT COUNT(*) FROM pets WHERE petType = ? AND isVaccinated = 'Y' ";
	        int count = 0;

	        try {
	             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_VACCINATED_SQL) ;

	            preparedStatement.setString(1, petType);

	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	        	System.out.println("Error while counting , Please try Again");
	        	//e.printStackTrace();
	        }
	        return count;
	}

	public int countPetsByCategory(String category) {
		 String COUNT_PETS_SQL = "SELECT COUNT(*) FROM pets WHERE petCategory = ?";
	        int count = 0;

	        try {
	             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_PETS_SQL);

	            preparedStatement.setString(1, category);

	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	        	System.out.println("Error while Counting , Please try Again");
	        	//e.printStackTrace();
	        }
	        return count;
	}

	
}
