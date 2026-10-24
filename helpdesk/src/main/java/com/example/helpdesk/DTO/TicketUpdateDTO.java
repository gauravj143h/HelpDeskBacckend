package com.example.helpdesk.DTO;

import com.example.helpdesk.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketUpdateDTO {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotNull(message = "Priority is required")
    private TicketPriority priority;
    // getters/setters
}
