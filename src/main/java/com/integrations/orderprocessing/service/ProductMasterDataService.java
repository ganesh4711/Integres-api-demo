package com.integrations.orderprocessing.service;

import static com.integrations.orderprocessing.constants.SHARPConstants.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.ProductMasterData;
import com.integrations.orderprocessing.primary_ds.repository.ProductMasterDataRepository;
import com.integrations.orderprocessing.secondary_ds.entity.Product;
import com.integrations.orderprocessing.secondary_ds.repository.ProductRepository;

@Service
public class ProductMasterDataService {
	
	@Autowired
	ProductRepository productRepository;
	
	public Optional<Product> getProductByName(String prodId) {
		return productRepository.getByProductName(prodId);
	}
	
	public String getParcelType(String prodId) {
		String parcelType = SHPMNT_IN_PARCEL_TYPE_DFLT;

		Optional<Product> prodData = getProductByName(prodId);
		if (prodData.isPresent()) {
			String isSmallParcel = prodData.get().getSmallParcel();
			parcelType = isSmallParcel.equalsIgnoreCase(SHPMNT_IN_IS_PARCEL_SMALL) ? SHPMNT_IN_PARCEL_TYPE_SMALL
					: SHPMNT_IN_PARCEL_TYPE_LARGE;
		}

		return parcelType;
	}

}
