package com.seacarbon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seacarbon.model.PostProduct;

@Repository
public interface PostProductRepository extends JpaRepository<PostProduct, Long> {
	
	public List<PostProduct> findAllByTituloContainingIgnoreCase(String titulo);

}

