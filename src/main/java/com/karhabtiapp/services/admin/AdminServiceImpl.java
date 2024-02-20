package com.karhabtiapp.services.admin;


import com.karhabtiapp.dtos.CarDto;
import com.karhabtiapp.entities.Car;
import com.karhabtiapp.repositories.CarRepsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CarRepsitory carRepsitory;


    @Override
    public boolean postCar(CarDto carDto) {

        try {

             Car car= new Car();
             car.setName(car.getName());
            car.setColor(carDto.getColor());
            car.setBrand(car.getBrand());
            car.setType(carDto.getType());
            car.setPrice(carDto.getPrice());
            car.setDescription(carDto.getDescription());
            car.setModelYear(carDto.getModelYear());
            car.setTransmission(carDto.getTransmission());
            car.setImage(carDto.getImage().getBytes());

            carRepsitory.save(car);

            return true ;

        }catch (Exception e){

            return false;

        }

}



}
