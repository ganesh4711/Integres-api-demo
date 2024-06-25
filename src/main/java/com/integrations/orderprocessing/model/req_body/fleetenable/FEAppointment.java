package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

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
public class FEAppointment {

    @NotNull(message = "slots should not be null")
    private List<String> slots;

    @NotNull(message = "confirmed should not be null")
    private boolean confirmed;

    @NotBlank(message = "start_time should not be blank")
    private String start_time;

    @NotBlank(message = "end_time should not be blank")
    private String end_time;

    @NotBlank(message = "appt_date should not be blank")
    private String appt_date;
    
    @NotBlank(message = "appt_type should not be blank")
    private String appt_type;
}
