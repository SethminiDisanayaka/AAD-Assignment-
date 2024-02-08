package lk.ijse.gese66.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemDTO {
    private String itemID;
    private String description;
    private String unitPrice;
    private String quantity;
}
