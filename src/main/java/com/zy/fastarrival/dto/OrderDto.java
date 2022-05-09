package com.zy.fastarrival.dto;

import com.zy.fastarrival.entity.OrderDetail;
import com.zy.fastarrival.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetail;

}
