package com.mytaxi.domainobject;

import com.mytaxi.domainvalue.EngineType;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "car")
@Getter
@NoArgsConstructor
public class CarDO
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(unique = true, nullable = false)
    @NotNull(message = "Username can not be null and should be unique!")
    private String licensePlate;

    @Column(nullable = false)
    @Setter
    private int seatCount;

    @Column(nullable = false)
    @Setter
    private boolean convertible;

    @Column(nullable = false)
    @Setter
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private EngineType engineType;

    @Setter
    private boolean available;

    @OneToOne
    @NotNull(message = "Car should have a manufacturer.")
    private ManufacturerDO manufacturer;


    public CarDO(String licensePlate, ManufacturerDO manufacturer)
    {
        this.licensePlate = licensePlate;
        this.manufacturer = manufacturer;
        this.seatCount = 5;
        this.convertible = false;
        this.rating = 3;
        this.engineType = EngineType.GAS;
        this.available = true;
    }

}
