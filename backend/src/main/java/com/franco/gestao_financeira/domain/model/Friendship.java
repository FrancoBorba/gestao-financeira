package com.franco.gestao_financeira.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import com.franco.gestao_financeira.domain.model.Enums.FriendshipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "friendship", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requester_id", "receiver_id"})
})
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // QUEM ENVIOU O PEDIDO
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // QUEM RECEBEU O PEDIDO
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // STATUS (PENDING, ACCEPTED, REJECTED)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendshipStatus statusFriendship;

    // DATA DE CRIAÇÃO (Automática)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // DATA DE ATUALIZAÇÃO
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

 

    public Friendship() {
    }

    public Friendship(Long id, User requester, User receiver, FriendshipStatus statusFriendship, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.statusFriendship = statusFriendship;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public FriendshipStatus getStatusFriendship() {
        return statusFriendship;
    }

    public void setStatusFriendship(FriendshipStatus statusFriendship) {
        this.statusFriendship = statusFriendship;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }



    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        // Se o ID for null, as entidades não são iguais a menos que sejam o mesmo objeto em memória
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // HashCode constante para evitar problemas com Hibernate Lazy Loading
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", requester=" + (requester != null ? requester.getName() : "null") +
                ", receiver=" + (receiver != null ? receiver.getName() : "null") +
                ", status=" + statusFriendship +
                '}';
    }

}