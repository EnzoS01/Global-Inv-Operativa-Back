package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metodoError")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetodoError extends Base {
    @Column(name = "nombreMetodoError")
    private String nombreMetodoError;
}
