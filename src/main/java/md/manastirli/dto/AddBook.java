package md.manastirli.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AddBook {
    private String title;
    private String author;
    private double price;
}
