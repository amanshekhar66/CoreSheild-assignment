package CoreSheild.assignment.MapData.dtos;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedData {
    private Map<String, Long> validPointsByType;
    private Map<String, Double> averageRatingByType;
    private MergedData locationWithMostReviews;
    private List<String> locationsWithIncompleteData;
    private List<MergedData> mergedDataList;

}
