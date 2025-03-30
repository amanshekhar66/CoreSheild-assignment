package CoreSheild.assignment.MapData.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Identifiers {

    private String id;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
