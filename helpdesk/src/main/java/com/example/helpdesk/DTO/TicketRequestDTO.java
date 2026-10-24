package com.example.helpdesk.DTO;

import com.example.helpdesk.enums.TicketPriority;
import com.example.helpdesk.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {
    private String title;
    private String description;
    private TicketPriority priority;
}
