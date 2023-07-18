package com.atmingshi.dto;


import com.atmingshi.pojo.OrderDetail;
import com.atmingshi.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private Integer sumNum;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
