package md.manastirli.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Book {
    private Long id;
    private String title;
    private String author;
    private double price;
}
