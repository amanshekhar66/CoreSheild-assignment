package CoreSheild.assignment.MapData.services;

import CoreSheild.assignment.MapData.dtos.Identifiers;
import CoreSheild.assignment.MapData.dtos.MergedData;
import CoreSheild.assignment.MapData.dtos.MetaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapDataService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MergedData> getMergedDataFromJSON(List<Identifiers> identifiers, List<MetaData> metaData) {
        Map<String,Identifiers> locationMap = identifiers.stream()
                .collect(Collectors.toMap(Identifiers::getId,identifiers1 -> identifiers1));

        List<MergedData> mergedLocations = new ArrayList<>();

        for (MetaData meta : metaData) {
            String id = meta.getId();
            Identifiers identifier = locationMap.get(id);

            if (identifier != null) {
                mergedLocations.add(new MergedData(
                        id,
                        identifier.getLatitude(),
                        identifier.getLongitude(),
                        meta.getType(),
                        meta.getRating(),
                        meta.getReviews()
                ));
            }
        }

        return mergedLocations;
    }

    public List<Identifiers> loadLocations(InputStream inputStream) throws IOException {
        return objectMapper.readValue(
                inputStream,
                new TypeReference<List<Identifiers>>() {}
        );
    }

    public List<MetaData> loadMetadata(InputStream inputStream) throws IOException {
        return objectMapper.readValue(
                inputStream,
                new TypeReference<List<MetaData>>() {}
        );
    }

    public Map<String, Long> countValidPointsByType(List<MergedData> mergedDataList) {
        return mergedDataList.stream()
                .filter(this::isValidData)
                .collect(Collectors.groupingBy(
                        MergedData::getType,
                        Collectors.counting()
                ));
    }
    // to check if all the data in the json is valid and not empty
    private boolean isValidData(MergedData mergedData) {
        return mergedData.getId() != null &&
                !mergedData.getId().isEmpty() &&
                mergedData.getLatitude() != null &&
                mergedData.getLongitude() != null &&
                mergedData.getType() != null &&
                mergedData.getRating() != null &&
                mergedData.getReviews() != null &&
                !mergedData.getLatitude().equals(BigDecimal.valueOf(0.0)) &&
                !mergedData.getLongitude().equals(BigDecimal.valueOf(0.0)) &&
                !mergedData.getType().isEmpty() &&
                mergedData.getRating() >= 0.0 &&
                mergedData.getReviews() >= 0;
    }

    public Map<String, Double> calculateAverageRatingByType(List<MergedData> mergedDataList) {
        Map<String, List<Double>> ratingsByType = mergedDataList.stream()
                .filter(this::isValidData)
                .collect(Collectors.groupingBy(
                        MergedData::getType,
                        Collectors.mapping(MergedData::getRating, Collectors.toList())
                ));

        Map<String, Double> averageRatingByType = new HashMap<>();

        for (Map.Entry<String, List<Double>> entry : ratingsByType.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            averageRatingByType.put(entry.getKey(), average);
        }

        return averageRatingByType;
    }

    public MergedData findLocationWithMostReviews(List<MergedData> mergedDataList) {
        return mergedDataList.stream()
                .filter(this::isValidData)
                .max(Comparator.comparing(MergedData::getReviews))
                .orElse(null);
    }

    public List<String> identifyLocationsWithIncompleteData(List<Identifiers> identifiers, List<MetaData> metadata) {
        // Create sets of IDs from both datasets
        Set<String> identifierIds = identifiers.stream()
                .map(Identifiers::getId)
                .collect(Collectors.toSet());

        Set<String> metadataIds = metadata.stream()
                .map(MetaData::getId)
                .collect(Collectors.toSet());

        // Find IDs that are in one set but not the other
        Set<String> incompleteIds = new HashSet<>();

        for (String id : identifierIds) {
            if (!metadataIds.contains(id)) {
                incompleteIds.add(id);
            }
        }

        for (String id : metadataIds) {
            if (!identifierIds.contains(id)) {
                incompleteIds.add(id);
            }
        }

        // Also check for locations with missing or invalid data
        for (Identifiers identifier : identifiers) {
            if (identifier.getId() == null ||
                    identifier.getId().isEmpty() ||
                    identifier.getLatitude()==null ||
                    identifier.getLongitude()==null ||
                    identifier.getLatitude().equals(BigDecimal.valueOf(0.0)) ||
                    identifier.getLongitude().equals(BigDecimal.valueOf(0.0))) {
                incompleteIds.add(identifier.getId());
            }
        }

        for (MetaData meta : metadata) {
            if (meta.getId() == null
                    || meta.getId().isEmpty() ||
                    meta.getType() == null ||
                    meta.getType().isEmpty() ||
                    meta.getRating() == null ||
                    meta.getReviews() == null ||
                    meta.getRating() <= 0.0 ||
                    meta.getReviews() < 0) {
                incompleteIds.add(meta.getId());
            }
        }

        return new ArrayList<>(incompleteIds);
    }
}
