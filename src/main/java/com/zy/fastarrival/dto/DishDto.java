package com.zy.fastarrival.dto;

import com.zy.fastarrival.entity.Dish;
import com.zy.fastarrival.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
