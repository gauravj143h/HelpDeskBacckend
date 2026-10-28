package com.example.helpdesk.repository;

import com.example.helpdesk.entity.TicketEntity;
import com.example.helpdesk.entity.UserEntity;
import com.example.helpdesk.enums.TicketPriority;
import com.example.helpdesk.enums.TicketStatus;
import com.example.helpdesk.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<TicketEntity,Long> {
    List<TicketEntity> findByStatus(TicketStatus status);
    List<TicketEntity> findByPriority(TicketPriority priority);
    List<TicketEntity> findByCreatedById(Long id);
    Page<TicketEntity> findAll(Pageable pageable);
    long countByAssignedToId(Long userId);
    List<TicketEntity> findByAssignedToId(Long id);

}
