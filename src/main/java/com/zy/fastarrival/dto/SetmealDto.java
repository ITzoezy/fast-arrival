package com.zy.fastarrival.dto;

import com.zy.fastarrival.entity.Setmeal;
import com.zy.fastarrival.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
