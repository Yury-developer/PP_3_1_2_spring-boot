package academy.kata.model.entry;

import lombok.*;

import jakarta.persistence.*;


@Entity
@Table(name = "emails")
@NoArgsConstructor
@Getter
@Setter
public class EmailEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emailEntry_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "emailEntry_description")
    private String description;

    @Column(name = "emailEntry_value")
    private String value;


    public EmailEntry(String description, String value) {
        this.description = description;
        this.value = value;
    }


    @Override
    public String toString() {
        return "description='" + description + '\'' +
                ", value='" + value + "\n";
    }
}
