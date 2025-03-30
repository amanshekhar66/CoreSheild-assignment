package CoreSheild.assignment.MapData.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    private String id;
    private String type;
    private Double rating;
    private Integer reviews;
}
