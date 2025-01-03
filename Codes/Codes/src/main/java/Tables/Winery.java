package Tables;
import lombok.*;

@Getter @Setter @ToString
public class Winery {
    private int wineryId;
    private String name;
    private String address;
    private String website;
    private String phone;

    public Winery(int wineryId, String name, String address, String website, String phone) {
        this.wineryId = wineryId;
        this.name = name;
        this.address = address;
        this.website = website;
        this.phone = phone;
    }
}
