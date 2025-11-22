package com.dblab.jpa.proxy

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Vendor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name")
    val name: String,

    @Column(name = "code")
    val code: String,

    @OneToMany(mappedBy = "vendor", cascade = [CascadeType.MERGE, CascadeType.ALL])
    val prods: MutableList<Prod> = mutableListOf(),
)