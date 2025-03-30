package CoreSheild.assignment.MapData.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MergedData {

    private String id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String type;
    private Double rating;
    private Integer reviews;
}
