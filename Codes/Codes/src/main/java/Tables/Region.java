package Tables;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Region {
    private int regionId;
    private String name;
    private String country;
}
