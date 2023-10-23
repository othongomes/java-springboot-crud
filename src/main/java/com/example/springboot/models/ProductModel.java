package com.example.springboot.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="TB_PRODUCTS") //
public class ProductModel extends RepresentationModel<ProductModel>  implements Serializable { //Serializable: Mostra
    // para a JVM que esta
    // classe está
    // habilitada a passar por serealizações
    //Numero de controle de versõa de cada classe
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Define como vai ser gerado os identificadores
    private UUID idProduct;
    private String name;
    private BigDecimal value;

    //Métodos getters and setters
    public UUID getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
