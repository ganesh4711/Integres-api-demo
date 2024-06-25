package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrderItem {
    @NotBlank(message = "item_id should not be blank")
    private String item_id;

    private double item_length;

    @NotNull(message = "item_length_uom should not be null")
    private String item_length_uom;

    private double item_width;

    @NotNull(message = "item_width_uom should not be null")
    private String item_width_uom;

    private double item_height;

    @NotNull(message = "item_height_uom should not be null")
    private String item_height_uom;

    private int item_quantity;

    private double item_weight;

    @NotNull(message = "item_weight_uom should not be null")
    private String item_weight_uom;

    private int no_of_cubes;

    private double dim_weight;

    private double dim_factor;

    @NotNull(message = "wh_dock should not be null")
    private String wh_dock;

    @NotNull(message = "item_name should not be null")
    private String item_name;

    @NotBlank(message = "customer_order_number should not be blank")
    private String customer_order_number;

    @NotNull(message = "item_type should not be null")
    private String item_type;

    @NotNull(message = "item_model should not be null")
    private String item_model;

    @NotNull(message = "item_status should not be null")
    private String item_status;

    @NotNull(message = "serial_number should not be null")
    private String serial_number;
}
