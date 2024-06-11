package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Modelo extends Base{
    private String nombreModelo;

    //ver si agregar fechaAlta y fechaBaja
}
