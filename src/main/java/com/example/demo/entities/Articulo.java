package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.time.DateTimeException;
import java.util.Date;

@Entity
@Table(name = "articulo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
public class Articulo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArticulo;

    @Column(name = "nombreArticulo")
    private String nombreArticulo;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "fechaAlta")
    private Date fechaAlta;

    @Column(name = "fechaBaja")
    private Date fechaBaja;
}
