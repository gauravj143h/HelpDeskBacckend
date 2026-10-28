package com.example.helpdesk.DTO;

import com.example.helpdesk.entity.UserEntity;
import com.example.helpdesk.enums.TicketPriority;
import com.example.helpdesk.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String CreatedBy;
    private String AssignedBy;
    private Long id;
}
