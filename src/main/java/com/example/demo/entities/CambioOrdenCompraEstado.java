package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "cambioOrdenCompraEstado")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CambioOrdenCompraEstado extends Base{

    @Column(name = "fechaCambio")
    private Date fechaCambio;

}
