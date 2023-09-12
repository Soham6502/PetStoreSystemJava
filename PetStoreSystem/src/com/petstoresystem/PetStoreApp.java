package com.petstoresystem;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PetStoreApp {

    public static void main(String[] args) throws PetException {
        Scanner scanner = new Scanner(System.in);
        PetDAO petDAO = new PetDAO();
   	 Set<Integer> set = new HashSet<Integer>();
     // Adding Entries that were already present in data base
     set.add(1);
     set.add(2);
     set.add(3);
     set.add(4);
     set.add(5);
       
        int choice;
        do {
            // Display the menu
            System.out.println("Menu:");
            System.out.println("1. Add new pet details");
            System.out.println("2. Update pet price and Vaccination Status");
            System.out.println("3. Delete pet details");
            System.out.println("4. View all pets");
            System.out.println("5. Count pets by category");
            System.out.println("6. Find by price");
            System.out.println("7. Find by vaccination status for pet type");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
				switch (choice) {
				    case 1:
				        // Add new pet details
				        addNewPet(scanner, petDAO,set) ;
				        break;
				    case 2:
				        // Update pet price and Vaccination Status
				        updatePetDetails(scanner, petDAO);
				        break;
				    case 3:
				        // Delete pet details
				        deletePetDetails(scanner, petDAO,set);
				        break;
				    case 4:
				        // View all pets
				        viewAllPets(petDAO);
				        break;
				    case 5:
				        // Count pets by category
				        countPetsByCategory(scanner, petDAO);
				        break;
				    case 6:
				        // Find by price
				        searchByPrice(scanner, petDAO);
				        break;
				    case 7:
				        // Find by vaccination status for pet type
				        countByVaccinationStatusForPetType(scanner, petDAO);
				        break;
				    case 8:
				        System.out.println("Exiting the Pet Store System. Goodbye!");
				        break;
				    default:
				        System.out.println("Invalid choice. Please try again.");
				}
			} catch (PetException e) {
				
				System.out.println("Some error Occured while taking your choices , Please try Again !");
				//e.printStackTrace();
			}
        } while (choice != 8);
        
        // Close the scanner when done
        scanner.close();
    }

    private static void addNewPet(Scanner scanner, PetDAO petDAO, Set<Integer> set) throws PetException {
        // Prompt the user for pet details
        System.out.println("Enter Pet Details:");
        System.out.print("Pet ID: ");
        
        int petId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        try {
            if(set.contains(petId)) {
                  String statmentonerror = "PetId Already Exist";
                  throw new PetException(statmentonerror);
            }
            else {
            	
            	System.out.print("Pet Category: ");
                String petCategory = scanner.nextLine().trim().toLowerCase();
                System.out.print("Pet Type: ");
                String petType = scanner.nextLine().trim().toLowerCase();
                System.out.print("Color: ");
                String color = scanner.nextLine().trim().toLowerCase();
                System.out.print("Age: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character
                System.out.print("Is Vaccinated (true/false): ");
                boolean isVaccinated = scanner.nextBoolean();
                scanner.nextLine(); // Consume the newline character
                System.out.print("Food Habits: ");
                String foodHabits = scanner.nextLine();

                // Create a Pet object with the user-provided details
                Pet pet = new Pet(petId, petCategory, petType, color, age, price, isVaccinated, foodHabits);

                // Call the PetDAO method to add the pet to the database
                int rowsAffected = petDAO.addPetDetails(pet);
        try {
                if (rowsAffected > 0) {
                    System.out.println("Pet added successfully.");
                    set.add(petId);
                } else {
                	String statmentsonerror = "Failed to add pet. Please try again.";
                	throw new PetException(statmentsonerror);
                }
        }catch(PetException p) {}
        }
    } catch(PetException p) {}
        
    }


    private static void updatePetDetails(Scanner scanner, PetDAO petDAO) throws PetException {
        System.out.print("Enter Pet ID to update: ");
        int petId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();
        System.out.print("Is the pet vaccinated (true/false): ");
        boolean newVaccinationStatus = scanner.nextBoolean();

        boolean updated = petDAO.updatePetPriceAndVaccinationStatus(petId, newPrice, newVaccinationStatus);
try {
        if (updated) {
            System.out.println("Pet details updated successfully.");
        } else {
        	String statmentsonerror  = "Failed to update pet details. Pet ID not found.";
        	throw new PetException(statmentsonerror);
        }
} catch(PetException p) {}
    }


    
    private static void deletePetDetails(Scanner scanner, PetDAO petDAO,Set<Integer> set) throws PetException{
        System.out.print("Enter Pet ID to delete: ");
        int petId = scanner.nextInt();
        scanner.nextLine();
        boolean deleted = petDAO.deletePetDetails(petId);

        try {
        	
        if (deleted) {
            System.out.println("Pet deleted successfully.");
            set.remove(petId);
        } else {
        	String statmentsonerror  =  "Failed to delete pet. Pet ID not found.";
        	throw new PetException(statmentsonerror);
        }
        } catch (PetException p){   }
        
    }


    private static void viewAllPets(PetDAO petDAO) throws PetException{
        List<Pet> pets = petDAO.showAllPets();
try {
        if (!pets.isEmpty()) {
            System.out.println("All Pets:");
            for (Pet pet : pets) {
                System.out.println(pet);
                System.out.println("--------------");
            }
        } else {
        	String statmentsonerror = "No pets available in the database.";
        	throw new PetException(statmentsonerror);
                 } } catch(PetException p) {}
    }


    private static void countPetsByCategory(Scanner scanner, PetDAO petDAO) {
        System.out.print("Enter pet category to count: ");
        String category = scanner.nextLine().trim().toLowerCase();

        int count = petDAO.countPetsByCategory(category);
        System.out.println("Number of pets in category '" + category + "': " + count);
    }

    private static void searchByPrice(Scanner scanner, PetDAO petDAO) throws PetException{
        System.out.print("Enter minimum price: ");
        double minPrice = scanner.nextDouble();
        System.out.print("Enter maximum price: ");
        double maxPrice = scanner.nextDouble();

        List<Pet> matchingPets = petDAO.searchByPrice(minPrice, maxPrice);
try {
        if (!matchingPets.isEmpty()) {
            System.out.println("Matching Pets:");
            for (Pet pet : matchingPets) {
                System.out.println(pet);
                System.out.println("--------------");
            }
        } else {
        	String statmentsonerror = "No pets found within the specified price range.";
        	throw new PetException(statmentsonerror);
        } } catch(PetException p) {}
    }

    private static void countByVaccinationStatusForPetType(Scanner scanner, PetDAO petDAO) {
    	
        System.out.print("Enter pet type: ");
        String petType = scanner.nextLine().trim().toLowerCase();
        int count = petDAO.countByVaccinationStatusForPetType(petType);
        System.out.println("Number of vaccinated pets for '" + petType + "': " + count);
    }

}
