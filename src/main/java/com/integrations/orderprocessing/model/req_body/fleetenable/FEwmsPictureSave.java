package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEwmsPictureSave {
	private String image_type;
    private int latitude;
    private int longitude;
    private String sign_by;
    private String picture_title;
    private String picture_code;
    private String ack_id;
    private String captured_at;
    private String title_by_relation;
    private String picture;
}
