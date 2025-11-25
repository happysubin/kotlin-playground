package com.dblab

import com.dblab.jpa.proxy.Prod
import com.dblab.jpa.proxy.ProdForExternalSystem
import com.dblab.jpa.proxy.ProdForExternalSystemRepository
import com.dblab.jpa.proxy.ProdRepository
import com.dblab.jpa.proxy.Vendor
import com.dblab.jpa.proxy.VendorRepository
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator
import net.ttddyy.dsproxy.QueryCountHolder
import net.ttddyy.dsproxy.listener.QueryCountStrategy
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
class DbLabApplicationTests {

    @Autowired
    lateinit var prodRepository: ProdRepository

    @Autowired
    lateinit var prodForExternalSystemRepository: ProdForExternalSystemRepository

    @Autowired
    lateinit var vendorRepository: VendorRepository

    @Autowired
    lateinit var queryCountStrategy: QueryCountStrategy


    @BeforeEach
    fun setUp() {
        println("=== Before Test ===")

        // clear 전
        println("Before clear - Grand Total: ${QueryCountHolder.getGrandTotal().insert}")
        println("Before clear - DataSources: ${QueryCountHolder.getDataSourceNames()}")

        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }

        // clear 후
        println("After clear - Grand Total: ${QueryCountHolder.getGrandTotal().insert}")
        println("After clear - DataSources: ${QueryCountHolder.getDataSourceNames()}")
    }

    @Test
    fun `연관관계를 조회한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.findById(vendor.id).orElseThrow())
        prodRepository.save(prod)
        SQLStatementCountValidator.assertTotalCount(3)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(1)
    }

    @Test
    fun `연관관계 없이 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = ProdForExternalSystem(name = "상품", vendorId = vendor.id)
        prodForExternalSystemRepository.save(prod)
        SQLStatementCountValidator.assertTotalCount(2)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(0)
    }

    @Test
    fun `프록시를 사용한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.getReferenceById(vendor.id))
        prodRepository.save(prod)
        SQLStatementCountValidator.assertTotalCount(2)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(0)
    }

    private fun createVendor() = Vendor(
        name = "test_vendor",
        code = "code"
    )
}

