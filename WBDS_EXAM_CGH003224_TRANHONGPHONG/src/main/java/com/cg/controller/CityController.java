package com.cg.controller;

import com.cg.model.City;
import com.cg.model.CityDTO;
import com.cg.model.Nation;
import com.cg.service.city.ICityService;
import com.cg.service.nation.INationService;
import com.cg.utils.ValidDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private ICityService cityService;

    @Autowired
    private INationService nationService;

    @GetMapping
    public ModelAndView showListCity() {
        ModelAndView modelAndView = new ModelAndView("/city/list");
        Iterable<City> cities = cityService.findAll();
        modelAndView.addObject("cities", cities);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showFormCreate() {
        ModelAndView modelAndView = new ModelAndView("/city/create");
        Iterable<Nation> nations = nationService.findAll();

        modelAndView.addObject("city", new CityDTO());
        modelAndView.addObject("nations",nations);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createCustomer(@Validated @ModelAttribute("city") CityDTO cityDTO, BindingResult bindingResult) {
        ModelAndView modelAndView;
        Iterable<City> cities;
        List<String> errors = new ArrayList<>();

        String area = cityDTO.getArea();
        String gdp = cityDTO.getGdp();
        String population = cityDTO.getPopulation();

        if(!ValidDateUtils.isNumberValid(area)){
            errors.add("Vui lòng nhập diện tích hợp lệ! Diện tích của một thành phố phải là một số dương!");
        } else if (area.length() > 10) {
            errors.add("Diện tích quá lớn vượt quá diện tích thực của thành phố!");
        }else {
            if(Integer.parseInt(area) <= 0) {
                errors.add("Diện tích của một thành phố phải là một số dương!");
            }
        }

        if(!ValidDateUtils.isNumberValid(gdp)){
            errors.add("Vui lòng GDP  hợp lệ! GDP của một thành phố phải là một số dương!");
        } else if (gdp.length() > 10) {
            errors.add("GDP quá lớn vượt quá GDP thực của thành phố!");
        }else {
            if(Integer.parseInt(gdp) <= 0) {
                errors.add("GDP của một thành phố phải là một số dương!");
            }
        }

        if(!ValidDateUtils.isNumberValid(population)){
            errors.add("Vui lòng nhập dân số hợp lệ! Dân số của một thành phố phải là một số dương!");
        } else if (area.length() > 10) {
            errors.add("Dân số quá lớn vượt quá dân số thực của thành phố!");
        }else {
            if(Integer.parseInt(population) <= 0) {
                errors.add("Dân số của một thành phố phải là một số dương!");
            }
        }

        if(bindingResult.hasErrors() || errors.size() > 0){
            modelAndView = new ModelAndView("/city/create");
            Iterable<Nation> nations = nationService.findAll();
            modelAndView.addObject("hasError",true);
            modelAndView.addObject("errors",errors);
            modelAndView.addObject("nations",nations);
            return modelAndView;
        }

        Optional<Nation> nation = nationService.findById(Long.parseLong(cityDTO.getNation()));
        City city = cityDTO.toCity(nation.get());
        city.setId(0L);
//        city.getNation().setId()
        modelAndView = new ModelAndView("/city/list");
        cityService.save(city);

        cities = cityService.findAll();

        modelAndView.addObject("cities",cities);
        modelAndView.addObject("success", "ĐÃ TẠO MỚI THÀNH CÔNG!");

        return modelAndView;
    }

    @GetMapping("/info/{id}")
    public ModelAndView showInfoCity(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/city/info");

        Optional<City> city = cityService.findById(id);
        Iterable<City> cities =cityService.findAll();

        Iterable<Nation> nations = nationService.findAll();
        modelAndView.addObject("cities",cities);
        modelAndView.addObject("city",city.get());
        modelAndView.addObject("nations",nations);
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/city/delete");

        Iterable<Nation> nations = nationService.findAll();
        modelAndView.addObject("nations",nations);
        Optional<City> city = cityService.findById(id);
        modelAndView.addObject("city",city);
        return modelAndView;
    }
    @PostMapping("/delete")
    public ModelAndView doDelete(@ModelAttribute ("city") City city){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/city/delete");
        Iterable<Nation> nations = nationService.findAll();
        modelAndView.addObject("nations",nations);
        Iterable<City> cities = cityService.findAll();
        cityService.remove(city.getId());
        modelAndView.addObject("message", "ĐÃ XÓA THÀNH CÔNG!");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        ModelAndView modelAndView;
        List<String> errors = new ArrayList<>();
        Optional<City> city = Optional.empty();
        Iterable<Nation> nations = nationService.findAll();
        if(!ValidDateUtils.isNumberValid(id)){
            errors.add("Id không hợp lệ!");
        }else {
            city = cityService.findById(Long.parseLong(id));
            if(!city.isPresent()){
                errors.add("Không tồn tại khách hàng có id là: " + id);
            }
        }

        if(errors.size() > 0) {
            modelAndView = new ModelAndView("/city/edit");
            modelAndView.addObject("errors",errors);
            modelAndView.addObject("hasError",true);
            modelAndView.addObject("nations",nations);
            return modelAndView ;
        }

        modelAndView = new ModelAndView("/city/edit");
        modelAndView.addObject("nations",nations);
        modelAndView.addObject("city", city.get());
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editCustomer(@PathVariable String id, @Validated @ModelAttribute("city") CityDTO cityDTO, BindingResult bindingResult){
        ModelAndView modelAndView;

        List<String> errors = new ArrayList<>();
        Iterable<Nation> nations = nationService.findAll();

        String area = cityDTO.getArea();
        String gdp = cityDTO.getGdp();
        String population = cityDTO.getPopulation();

        if(!ValidDateUtils.isNumberValid(area)){
            errors.add("Vui lòng nhập diện tích hợp lệ! Diện tích của một thành phố phải là một số dương!");
        } else if (area.length() > 10) {
            errors.add("Diện tích quá lớn vượt quá diện tích thực của thành phố!");
        }else {
            if(Integer.parseInt(area) <= 0) {
                errors.add("Diện tích của một thành phố phải là một số dương!");
            }
        }

        if(!ValidDateUtils.isNumberValid(gdp)){
            errors.add("Vui lòng GDP tích hợp lệ! GDP của một thành phố phải là một số dương!");
        } else if (gdp.length() > 10) {
            errors.add("GDP quá lớn vượt quá GDP thực của thành phố!");
        }else {
            if(Integer.parseInt(gdp) <= 0) {
                errors.add("GDP của một thành phố phải là một số dương!");
            }
        }

        if(!ValidDateUtils.isNumberValid(population)){
            errors.add("Vui lòng nhập dân số hợp lệ! Dân số của một thành phố phải là một số dương!");
        } else if (area.length() > 10) {
            errors.add("Dân số quá lớn vượt quá dân số thực của thành phố!");
        }else {
            if(Integer.parseInt(population) <= 0) {
                errors.add("Dân số của một thành phố phải là một số dương!");
            }
        }

        if(bindingResult.hasErrors() || errors.size() > 0){
            modelAndView = new ModelAndView("/city/edit");
            City city = cityService.findById(cityDTO.getId()).get();
            modelAndView.addObject("hasError",true);
            modelAndView.addObject("errors",errors);
            modelAndView.addObject("nations",nations);
            modelAndView.addObject("city",city);

            return modelAndView;
        }


        Optional<Nation> nation = nationService.findById(Long.parseLong(cityDTO.getNation()));
        City city = cityDTO.toCity(nation.get());
        city.setId(cityDTO.getId());

        modelAndView = new ModelAndView("/city/list");
        cityService.save(city);

        Iterable<City> cities = cityService.findAll();
        modelAndView.addObject("cities",cities);
        modelAndView.addObject("nations",nations);
        
        modelAndView.addObject("success", "ĐÃ SỬA THÀNH CÔNG!");
        return modelAndView;
    }
}
