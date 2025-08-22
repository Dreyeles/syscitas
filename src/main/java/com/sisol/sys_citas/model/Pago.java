package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "codigo_transaccion", nullable = false, unique = true)
    private Long codigoTransaccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private com.sisol.sys_citas.enums.EstadoPago estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "comprobante", nullable = false)
    private com.sisol.sys_citas.enums.Comprobante comprobante;

    // Relación con Cita
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    // --- Constructores ---
    public Pago() {}

    public Pago(BigDecimal monto, LocalDateTime fechaPago, Long codigoTransaccion, Cita cita) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.codigoTransaccion = codigoTransaccion;
        this.cita = cita;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Long getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(Long codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }

    public com.sisol.sys_citas.enums.EstadoPago getEstado() { return estado; }
    public void setEstado(com.sisol.sys_citas.enums.EstadoPago estado) { this.estado = estado; }

    public com.sisol.sys_citas.enums.Comprobante getComprobante() { return comprobante; }
    public void setComprobante(com.sisol.sys_citas.enums.Comprobante comprobante) { this.comprobante = comprobante; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
} 