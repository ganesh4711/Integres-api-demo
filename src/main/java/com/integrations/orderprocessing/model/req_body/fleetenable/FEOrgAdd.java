package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrgAdd {
   @NotBlank(message = "id should not be blank")
   private String id;

   @NotBlank(message = "name should not be blank")
   private String name;

   @NotBlank(message = "code should not be blank")
   private String code;
}
