package com.dblab

import com.dblab.jpa.proxy.Prod
import com.dblab.jpa.proxy.ProdForExternalSystem
import com.dblab.jpa.proxy.ProdForExternalSystemRepository
import com.dblab.jpa.proxy.ProdRepository
import com.dblab.jpa.proxy.Vendor
import com.dblab.jpa.proxy.VendorRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DbLabApplicationTests {

    @Autowired
    lateinit var prodRepository: ProdRepository

    @Autowired
    lateinit var prodForExternalSystemRepository: ProdForExternalSystemRepository

    @Autowired
    lateinit var vendorRepository: VendorRepository

    @Test
    fun `연관관계를 조회한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.findById(vendor.id).orElseThrow())
        prodRepository.save(prod)
    }

    @Test
    fun `연관관계 없이 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = ProdForExternalSystem(name = "상품", vendorId = vendor.id)
        prodForExternalSystemRepository.save(prod)
    }

    @Test
    fun `프록시를 사용한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.getReferenceById(vendor.id))
        prodRepository.save(prod)
    }

    private fun createVendor() = Vendor(
        name = "test_vendor",
        code = "code"
    )
}
