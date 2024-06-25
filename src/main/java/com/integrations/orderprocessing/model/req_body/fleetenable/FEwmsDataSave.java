package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEwmsDataSave {
	private String refer;

    @NotBlank(message ="refer_id should not be  blank")
    private String refer_id;

    private String organization_id;
    private String exception_code;
    private String exception_message;
    private String picture_comments;
    private String door_number;
    private String truck_number;
    private String wh_refer;
    private String wh_comment;
    private List<FEwmsPictureSave> pictures;
}
