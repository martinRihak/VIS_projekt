package Tables;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@NoArgsConstructor
public class Wine {
    private int wineId;
    private String name;
    private String type;
    private int year;
    private double price;
    private String description;
    private int stockQuantity;
    private int wineryId;
    private int regionId;

    public Wine(int wineId,String name, String type, int year, double price,
                String description, int stockQuantity, int wineryId, int regionId) {
        this.wineId = wineId;
        this.name = name;
        this.type = type;
        this.year = year;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.wineryId = wineryId;
        this.regionId = regionId;
    }
}
