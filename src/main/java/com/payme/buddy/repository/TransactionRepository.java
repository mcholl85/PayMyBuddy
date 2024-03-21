package com.payme.buddy.repository;

import com.payme.buddy.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long>, JpaRepository<Transaction, Long> {
    Page<Transaction> findByRecipient_Email(String email, Pageable pageable);
    Page<Transaction> findBySender_Email(String email, Pageable pageable);
}
