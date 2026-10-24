package com.example.helpdesk.controller;

import com.example.helpdesk.DTO.TicketRequestDTO;
import com.example.helpdesk.DTO.TicketResponseDTO;
import com.example.helpdesk.DTO.TicketUpdateDTO;
import com.example.helpdesk.enums.TicketPriority;
import com.example.helpdesk.enums.TicketStatus;
import com.example.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ticket")
public class TicketController {

    TicketService ticketService;
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }


    @PostMapping
    public ResponseEntity<TicketResponseDTO> CreateTicket(@RequestBody  TicketRequestDTO ticket, @RequestParam long id){
        TicketResponseDTO response = ticketService.CreateTicket(ticket,id);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/GetTicketById")
    public ResponseEntity<TicketResponseDTO> GetTicketById(@RequestParam  long id){
        TicketResponseDTO response = ticketService.GetTicketByID(id);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/GetTicketByStatus")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByStatus(@RequestParam TicketStatus status){
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping("/GetTicketByPriority")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByPriority(@RequestParam TicketPriority priority){
        return ResponseEntity.ok(ticketService.getTicketsByPriority(priority));
    }

    @GetMapping("/GetTicketUserId")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUserId(@RequestParam long id){
        return ResponseEntity.ok(ticketService.getTicketsByuserId(id));
    }

    @GetMapping("/GetTicketPagenumber")
    public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(Pageable pageable){
        return ResponseEntity.ok(ticketService.getAllTickets(pageable));
    }

    @PatchMapping("/{ticketId}/assign")
    public ResponseEntity<TicketResponseDTO> assignTicket(
            @PathVariable Long ticketId,
            @RequestParam Long agentId){
        return ResponseEntity.ok(ticketService.assignTicket(ticketId, agentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();   // 204
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateDTO dto){
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }

}
