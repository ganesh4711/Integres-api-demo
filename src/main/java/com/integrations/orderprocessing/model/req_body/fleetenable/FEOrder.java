package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrder {
    @NotBlank(message = "id should not be blank")
    private String id;

    @NotBlank(message = "customer_order_number should not be blank")
    private String customer_order_number;

    @NotBlank(message = "order_type should not be blank")
    private String order_type;

    @NotNull(message = "hawb should not be null")
    private String hawb;

    @NotNull(message = "mawb should not be null")
    private String mawb;

    @NotBlank(message = "status should not be blank")
    private String status;

    @NotNull(message ="wh_dock should not be null")
    private String wh_dock;

    @NotBlank(message = "carrier_name should not be blank")
    private String carrier_name;

    @NotBlank(message = "carrier_id should not be blank")
    private String carrier_id;

    @NotBlank(message = "shipper_name should not be blank")
    private String shipper_name;

    @NotBlank(message = "shipper_id should not be blank")
    private String shipper_id;

    @NotBlank(message = "warehouse_name should not be blank")
    private String warehouse_name;

    @NotBlank(message = "warehouse_id should not be blank")
    private String warehouse_id;

    @NotNull(message = "reference_number should not be null")
    private String reference_number;

    @NotNull(message = "levelOfService should not be null")
    private String levelOfService;

    @NotNull(message = "wh_storage should not be null")
    private boolean wh_storage;

    @NotNull(message = "los_name should not be null")
    private String los_name;

    @NotNull(message = "is_hazardous should not be null")
    private boolean is_hazardous;

    private double weight;

    @NotNull(message = "weight_uom should not be null")
    private String weight_uom;

    private double height;

    private double width;

    private double length;

    private int quantity;

    private double surface_area;

    private int total_no_of_cubes;

    @NotNull(message = "dims_uom should not be null")
    private String dims_uom;

    private int pallets;

    @NotNull(message = "received_at should not be null")
    private String received_at;

    @Valid
    @NotNull(message = "originAddress should not be null")
    private FEOrderAddress originAddress;

    @Valid
    @NotNull(message = "destinationAddress should not be null")
    private FEOrderAddress destinationAddress;

    //@NotNull(message = "driver_id should not be null")
    //private String driver_id;

    //@NotNull(message = "driver_name should not be null")
    //private String driver_name;

    //@NotNull(message = "trailer_number should not be null")
    //private String trailer_number;
    
    @Valid
    @NotNull(message = "drivers should not be null")
    private List<FEDriver> drivers;

    @Valid
    @NotNull(message = "appointments should not be null")
    private List<FEAppointment> appointments;

    @Valid
    @NotNull(message = "item_details should not be null")
    private List<FEOrderItem> item_details;

    private Boolean muiltiday;

    private String related_Order;

    private Boolean is_dependency;

    private String actual_start_date_time;

    private String actual_end_date_time;

    private String stop_type;

}
