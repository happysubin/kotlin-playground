package com.dblab.jpa.proxy

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "prod")
@Entity
class Prod(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name")
    val name: String,

    @JoinColumn(name = "vendor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val vendor: Vendor,

    /**
     * 더 연관 관계가 있다고 가정
     */
)