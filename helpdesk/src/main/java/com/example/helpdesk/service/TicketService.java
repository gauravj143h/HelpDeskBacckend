package com.example.helpdesk.service;

import com.example.helpdesk.DTO.TicketRequestDTO;
import com.example.helpdesk.DTO.TicketResponseDTO;
import com.example.helpdesk.DTO.TicketUpdateDTO;
import com.example.helpdesk.entity.TicketEntity;
import com.example.helpdesk.entity.UserEntity;
import com.example.helpdesk.enums.TicketPriority;
import com.example.helpdesk.enums.TicketStatus;
import com.example.helpdesk.enums.UserRole;
import com.example.helpdesk.exceptions.InvalidOperationException;
import com.example.helpdesk.exceptions.ResourseNotfoundException;
import com.example.helpdesk.repository.TicketRepo;
import com.example.helpdesk.repository.UserRepo;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TicketService {
    TicketRepo ticketRepo;
    UserRepo userRepo;
    public TicketService(TicketRepo ticketRepo,UserRepo userRepo){
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
    }

    public TicketResponseDTO CreateTicket(TicketRequestDTO ticket,long id){
        UserEntity user =  userRepo.findById(id).orElseThrow(()-> new ResourseNotfoundException("User wih this id is not found"));
        TicketEntity ticketresponse = DtoToEntity(ticket);
        ticketresponse.setCreatedBy(user);
        UserEntity AssignUser = whoisfree();
        ticketresponse.setAssignedTo(AssignUser);
        ticketresponse.setStatus(TicketStatus.OPEN);
        TicketEntity response = ticketRepo.save(ticketresponse);
        return EntityToDto(response);
    }

    public TicketResponseDTO GetTicketByID(long id){
        TicketEntity ticket = ticketRepo.findById(id).orElseThrow(()-> new ResourseNotfoundException("Ticket with this id is not found"));
        return EntityToDto(ticket);
    }

    public List<TicketResponseDTO> getTicketsByStatus(TicketStatus status){
        List<TicketEntity> tickets = ticketRepo.findByStatus(status);
        List<TicketResponseDTO> dtos = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            dtos.add(EntityToDto(ticket));
        }
        return dtos;
    }

    public List<TicketResponseDTO> getTicketsByPriority(TicketPriority priority){
        List<TicketEntity> tickets = ticketRepo.findByPriority(priority);
        List<TicketResponseDTO> dtos = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            dtos.add(EntityToDto(ticket));
        }
        return dtos;
    }

    public List<TicketResponseDTO> getTicketsByuserId(long id){
        List<TicketEntity> tickets = ticketRepo.findByCreatedById(id);
        List<TicketResponseDTO> dtos = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            dtos.add(EntityToDto(ticket));
        }
        return dtos;
    }

    public Page<TicketResponseDTO> getAllTickets(Pageable pageable){
        Page<TicketEntity> ticketPage = ticketRepo.findAll(pageable);
        return ticketPage.map(ticket -> EntityToDto(ticket));
    }

    public TicketResponseDTO assignTicket(Long ticketId, Long agentId){
        // 1. find the ticket
        TicketEntity ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new ResourseNotfoundException("Ticket not found with id: " + ticketId));

        // 2. find the agent
        UserEntity agent = userRepo.findById(agentId)
                .orElseThrow(() -> new ResourseNotfoundException("User not found with id: " + agentId));

        // 3. BUSINESS RULE: the user must be a support agent
        if (agent.getRole() != UserRole.SUPPORT_AGENT) {
            throw new InvalidOperationException("User " + agentId + " is not a support agent and cannot be assigned tickets");
        }

        // 4. assign and save
        ticket.setAssignedTo(agent);
        TicketEntity saved = ticketRepo.save(ticket);

        return EntityToDto(saved);
    }

    public List<TicketResponseDTO> getTicketsByassignedTo(long id){
        List<TicketEntity> tickets = ticketRepo.findByassignedTo(id);
        List<TicketResponseDTO> dtos = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            dtos.add(EntityToDto(ticket));
        }
        return dtos;
    }

    public void deleteTicket(Long id){
        // confirm it exists first (clean 404 if not)
        TicketEntity ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new ResourseNotfoundException("Ticket not found with id: " + id));

        ticketRepo.delete(ticket);
    }

    public TicketResponseDTO updateTicket(Long id, TicketUpdateDTO dto){
        TicketEntity ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new ResourseNotfoundException("Ticket not found with id: " + id));

        // update only the editable fields
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());

        TicketEntity saved = ticketRepo.save(ticket);
        return EntityToDto(saved);
    }

    public TicketEntity DtoToEntity(TicketRequestDTO ticket){
        TicketEntity tc = new TicketEntity();
        tc.setTitle(ticket.getTitle());
        tc.setDescription(ticket.getDescription());
        tc.setPriority(ticket.getPriority());
        return tc;
    }

    public TicketResponseDTO EntityToDto(TicketEntity response){
        TicketResponseDTO tc = new TicketResponseDTO();
        tc.setPriority(response.getPriority());
        tc.setTitle(response.getTitle());
        tc.setDescription(response.getDescription());
        tc.setStatus(response.getStatus());
        tc.setCreatedBy(response.getCreatedBy().getName());
        if (response.getAssignedTo() != null) {
            tc.setAssignedBy(response.getAssignedTo().getName());
        }

        return tc;
    }

    public UserEntity whoisfree(){
        List<UserEntity> SupportAgents= userRepo.findByRole(UserRole.SUPPORT_AGENT);

        if(SupportAgents.isEmpty()){
            return null;
        }

        UserEntity LeastBusy = SupportAgents.get(0);
        long LowestCount = ticketRepo.countByAssignedToId(LeastBusy.getId());

        for(UserEntity user : SupportAgents){
           long Count = ticketRepo.countByAssignedToId(user.getId());
           if(Count < LowestCount){
               LowestCount = Count;
               LeastBusy = user;
           }
        }
        return LeastBusy;
    }
}
