package digital101.io.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "menus")
@Data
public class Menu {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", unique = true)
    private Shop shop;

    @OneToMany(mappedBy = "menu")
    private Set<MenuItem> items;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

}
