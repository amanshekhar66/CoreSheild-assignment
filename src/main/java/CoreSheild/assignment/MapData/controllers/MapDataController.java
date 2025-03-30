package CoreSheild.assignment.MapData.controllers;

import CoreSheild.assignment.MapData.dtos.Identifiers;
import CoreSheild.assignment.MapData.dtos.MergedData;
import CoreSheild.assignment.MapData.dtos.MetaData;
import CoreSheild.assignment.MapData.dtos.ProcessedData;
import CoreSheild.assignment.MapData.services.MapDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapDataController {
    private final MapDataService mapDataService;

    @PostMapping("/process")
    public ResponseEntity<ProcessedData> processMapData(
            @RequestParam("locationFile") MultipartFile locationFile,
            @RequestParam("metadataFile") MultipartFile metadataFile) {

        try {
            List<Identifiers> identifiers = mapDataService.loadLocations(locationFile.getInputStream());
            List<MetaData> metadata = mapDataService.loadMetadata(metadataFile.getInputStream());
            List<MergedData> mergedDataList = mapDataService.getMergedDataFromJSON(identifiers,metadata);
            Map<String, Long> validPointsByType = mapDataService.countValidPointsByType(mergedDataList);
            Map<String, Double> averageRatingByType = mapDataService.calculateAverageRatingByType(mergedDataList);
            MergedData locationWithMostReviews = mapDataService.findLocationWithMostReviews(mergedDataList);
            List<String> locationsWithIncompleteData = mapDataService.identifyLocationsWithIncompleteData(identifiers, metadata);
            ProcessedData processedData = ProcessedData.builder()
                    .mergedDataList(mergedDataList)
                    .validPointsByType(validPointsByType)
                    .averageRatingByType(averageRatingByType)
                    .locationWithMostReviews(locationWithMostReviews)
                    .locationsWithIncompleteData(locationsWithIncompleteData)
                    .build();
            return new ResponseEntity<>(processedData,HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
