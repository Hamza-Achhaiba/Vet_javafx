# VetCare360 - Veterinary Management System

VetCare360 is a comprehensive veterinary management system built with JavaFX. It allows veterinary clinics to manage owners, pets, visits, and veterinarians.

## Prerequisites

- Java 17 or later
- Maven (optional, as the project includes Maven wrapper)

## Getting Started

### For Windows Users

1. Navigate to the VetCare360 directory
2. Double-click on the `start_project.bat` file
3. The application will build and start automatically

### For macOS/Linux Users

1. Open a terminal
2. Navigate to the VetCare360 directory
3. Make the Maven wrapper executable:
   ```
   chmod +x mvnw
   ```
4. Run the application:
   ```
   ./mvnw clean javafx:run
   ```

## Manual Start (All Platforms)

If you prefer to use Maven directly or encounter issues with the scripts:

1. Open a terminal or command prompt
2. Navigate to the VetCare360 directory
3. Run the following command:
   ```
   mvn clean javafx:run
   ```
   Or using the Maven wrapper:
   ```
   ./mvnw clean javafx:run    # For macOS/Linux
   mvnw.cmd clean javafx:run  # For Windows
   ```

## Troubleshooting

- **Java not found**: Ensure Java 17 or later is installed and added to your PATH
- **Build errors**: Make sure all dependencies are available and your internet connection is working
- **JavaFX errors**: The project includes JavaFX dependencies, but if you encounter issues, ensure your Java installation supports JavaFX

## Features

- Manage pet owners (add, edit, search)
- Track pets and their medical history
- Schedule and manage visits
- Maintain veterinarian information

## License

This project is licensed under the MIT License - see the LICENSE file for details.