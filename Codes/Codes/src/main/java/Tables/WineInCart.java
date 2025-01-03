package Tables;

import DataSources.data.Wine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data @Getter @NoArgsConstructor @AllArgsConstructor
public class WineInCart {
    private Cart cartId;
    private Wine wineId;
    private int quantity;

}

