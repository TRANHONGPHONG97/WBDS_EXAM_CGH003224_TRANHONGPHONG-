package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CityDTO {
    private Long id;

    @NotBlank(message = "Tên thành phố không được để trống!")
    @Size(min = 3, max = 30, message = "Tên thành phố phải từ 3-30 kí tự!")
//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Tên thành phố phải là chữ, không có kí tự đặc biệt và số")
    private String cityName;

    @NotBlank(message = "Tên quốc gia không được để trống!")
    private String nation;

    @NotBlank(message = "Diện tích không được để trống!")
    private String area;

    @NotBlank(message = "Dân số không được để trống!")
    private String population;

    @NotBlank(message = "GDP không được để trống!")
    private String gdp;

    @NotBlank(message = "Mô tả không được để trống!")
    @Size(min = 5, max = 255, message = "Mô tả từ 5 - 255 kí tự")
    private String description;

    public City toCity(Nation nation) {
        return new City()
                .setCityName(cityName)
                .setNation(nation)
                .setArea(Integer.parseInt(area))
                .setGdp(Integer.parseInt(gdp))
                .setPopulation(Integer.parseInt(population))
                .setDescription(description);
    }

}
