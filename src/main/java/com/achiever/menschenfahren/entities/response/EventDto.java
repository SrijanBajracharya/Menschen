package com.achiever.menschenfahren.entities.response;

import javax.annotation.Nonnull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventDto extends EventCreateDto {

	private static final long serialVersionUID = 6716632698180385976L;

	@Schema(description = "The id of the user")
	@Nonnull
	private String id;

	@Schema(description = "If the user is voided", example = "false")
	@Nonnull
	private boolean voided;

}
