package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for car table.
 * <p/>
 */
public interface CarRepository extends CrudRepository<CarDO, Long>
{
    List<CarDO> findByManufacturer(ManufacturerDO manufacturerDO);

    CarDO findByLicensePlate(String licensePlate);
}
