# Processing Map Data with Metadata

## Overview
This project is a Spring Boot application that processes and analyzes map data based on two JSON files: one containing location coordinates and another containing metadata for these locations. The application merges the data, counts valid locations per type, calculates average ratings, identifies the most reviewed location, and detects incomplete data.

## Features
- Upload two JSON files: `locations.json` and `metadata.json`
- Merge location data with metadata based on `id`
- Count valid locations per type (e.g., restaurants, hotels, cafes)
- Calculate average rating per type
- Identify the location with the highest number of reviews
- Detect locations with missing metadata or incomplete data fields
- RESTful API for processing JSON data

## Tech Stack
- Java
- Spring Boot
- Jackson (for JSON processing)
- Maven

## Installation
1. Clone the repository:
   ```sh
   git clone (https://github.com/amanshekhar66/CoreSheild-assignment.git)
)
   ```
2. Navigate to the project directory:
   ```sh
   cd repository-name
   ```
3. Build and run the application using Maven:
   ```sh
   mvn spring-boot:run
   ```

## API Endpoint
### Process JSON Data
**Endpoint:** `POST /api/process`

**Request:**
- `locations` (MultipartFile) - JSON file containing location data
- `metadata` (MultipartFile) - JSON file containing metadata

**Response:** JSON object containing:
- `valid_counts`: Count of valid locations per type
- `average_ratings`: Average rating per type
- `most_reviewed_location`: Location with the highest number of reviews
- `missing_metadata`: List of locations missing metadata
- `missing_fields`: Locations with incomplete data fields

**Example Request (Postman / cURL):**
```sh
curl -X POST "http://localhost:8080/api/process" \
     -F "locations=@path/to/locations.json" \
     -F "metadata=@path/to/metadata.json"
```

## Sample JSON Data
### locations.json
```json
[
  {"id": "loc_01", "latitude": 37.7749, "longitude": -122.4194},
  {"id": "loc_02", "latitude": 34.0522, "longitude": -118.2437}
]
```

### metadata.json
```json
[
  {"id": "loc_01", "type": "restaurant", "rating": 4.5, "reviews": 120},
  {"id": "loc_02", "type": "hotel", "rating": 4.2, "reviews": 200}
]
```

## Frontend (Optional)
A simple frontend page is placed in the `resources/static` directory to allow users to upload JSON files through a web interface.

## License
This project is licensed under the MIT License.

## Author
Aman Shekhar

