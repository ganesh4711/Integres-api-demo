package com.integrations.orderprocessing.model.req_body.optioryx;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Barcodes {

    private List<String> barcodes;

    private List<Boolean> exact;
}
