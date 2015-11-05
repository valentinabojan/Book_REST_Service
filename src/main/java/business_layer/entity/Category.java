package business_layer.entity;

import javax.persistence.*;

@Entity
public class Category {

    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_app_seq")
    @SequenceGenerator(name = "books_app_seq", sequenceName = "books_app_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "CATEGORY_NAME")
    private String name;

    public Integer getId() {
        return id;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
