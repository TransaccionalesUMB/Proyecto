package com.example.transactional.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Modelo para representar ubicaciones específicas dentro de las bodegas
 * Permite gestionar la ubicación exacta de los productos en el almacén
 */
@Entity
@Table(name = "ubicacion_bodega")
public class UbicacionBodega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer id;
    
    @Column(name = "id_bodega", nullable = false)
    private Integer idBodega;
    
    @Column(name = "codigo", nullable = false)
    private String codigo;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "pasillo")
    private String pasillo;
    
    @Column(name = "estante")
    private String estante;
    
    @Column(name = "nivel")
    private String nivel;
    
    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;
    
    @Column(name = "capacidad_maxima_kg")
    private BigDecimal capacidadMaximaKg;
    
    @Column(name = "capacidad_maxima_volumen")
    private BigDecimal capacidadMaximaVolumen;
    
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoUbicacion tipo;
    
    @Column(name = "activa")
    private Boolean activa;
    
    // Relación con bodega
    @ManyToOne
    @JoinColumn(name = "id_bodega", insertable = false, updatable = false)
    private Bodega bodega;
    
    // Enum para los tipos de ubicación
    public enum TipoUbicacion {
        Recepcion, Almacenamiento, Despacho
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdBodega() {
        return idBodega;
    }
    
    public void setIdBodega(Integer idBodega) {
        this.idBodega = idBodega;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getPasillo() {
        return pasillo;
    }
    
    public void setPasillo(String pasillo) {
        this.pasillo = pasillo;
    }
    
    public String getEstante() {
        return estante;
    }
    
    public void setEstante(String estante) {
        this.estante = estante;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public BigDecimal getCapacidadMaximaKg() {
        return capacidadMaximaKg;
    }
    
    public void setCapacidadMaximaKg(BigDecimal capacidadMaximaKg) {
        this.capacidadMaximaKg = capacidadMaximaKg;
    }
    
    public BigDecimal getCapacidadMaximaVolumen() {
        return capacidadMaximaVolumen;
    }
    
    public void setCapacidadMaximaVolumen(BigDecimal capacidadMaximaVolumen) {
        this.capacidadMaximaVolumen = capacidadMaximaVolumen;
    }
    
    public TipoUbicacion getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoUbicacion tipo) {
        this.tipo = tipo;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public Bodega getBodega() {
        return bodega;
    }
    
    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }
    
    /**
     * Devuelve la representación completa de la ubicación
     * @return Código de bodega + código de ubicación
     */
    public String getUbicacionCompleta() {
        if (bodega != null) {
            return bodega.getNombre() + " - " + codigo;
        }
        return codigo;
    }
    
    /**
     * Verifica si la ubicación está disponible para almacenar productos
     * @return true si está activa y es de tipo Almacenamiento
     */
    public boolean isDisponibleParaAlmacenar() {
        return activa != null && activa && tipo == TipoUbicacion.Almacenamiento;
    }
}
