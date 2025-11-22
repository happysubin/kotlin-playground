package com.dblab.jpa.proxy

import org.springframework.data.jpa.repository.JpaRepository

interface ProdRepository: JpaRepository<Prod, Long> {
}