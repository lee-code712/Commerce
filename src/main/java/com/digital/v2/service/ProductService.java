package com.digital.v2.service;

import static com.digital.v2.lucene.DataHandler.findHardly;
import static com.digital.v2.lucene.DataHandler.wildCardQuery;
import static com.digital.v2.lucene.DataHandler.write;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.springframework.stereotype.Component;

import com.digital.v2.schema.Category;
import com.digital.v2.schema.Product;
import com.digital.v2.schema.ProductList;

@Component
public class ProductService {
	
	@Resource
	CategoryService categorySvc;
	
	public boolean productWrite (Product product) throws Exception {
		
		try {
			// product 중복 여부 확인
			if (productSearch(product.getProductName()).getProductName() != null) {
				throw new Exception("이미 등록된 상품입니다.");
			}
			
			// 중복이 아니면 write
			product.setProductId(System.currentTimeMillis());
			product.setInventoryId(System.currentTimeMillis() + 1);
			Document doc = new Document();
			
			doc.add(new TextField("productid", "" + product.getProductId(), Store.YES));
			doc.add(new TextField("price", "" + product.getPrice(), Store.YES));
			doc.add(new TextField("productname", "" + product.getProductName(), Store.YES));
			doc.add(new TextField("productcategoryid", "" + product.getCategoryId(), Store.YES));
			doc.add(new TextField("productinventoryid", "" + product.getInventoryId(), Store.YES));
			
			write(doc);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}


	public Product productSearch (String productName) throws Exception {
		
		String key = "productname";
		String value = productName;
		
		Document productDoc = findHardly(key, value);
		
		Product product = new Product();
		if (productDoc != null) {
			product.setProductId(Long.parseLong(productDoc.get("productid")));
			product.setPrice(Long.parseLong(productDoc.get("price")));
			product.setProductName(productDoc.get("productname"));
			product.setCategoryId(Long.parseLong(productDoc.get("productcategoryid")));
			product.setInventoryId(Long.parseLong(productDoc.get("productinventoryid")));
		}
		
		return product;
	}

	public ProductList productSearchByKeyword (String keyword) throws Exception {
		
		String key = "productname";
		String value = keyword;
		
		List<Document> productDocList = wildCardQuery(key, value);
		
		ProductList productList = new ProductList(); 
		List<Product> products = new ArrayList<Product>();
		for (Document productDoc : productDocList) {
			
			Product product = new Product();
			if (productDoc != null) {
				product.setProductId(Long.parseLong(productDoc.get("productid")));
				product.setPrice(Long.parseLong(productDoc.get("price")));
				product.setProductName(productDoc.get("productname"));
				product.setCategoryId(Long.parseLong(productDoc.get("productcategoryid")));
				product.setInventoryId(Long.parseLong(productDoc.get("productinventoryid")));
			}
			products.add(product);
		}
		
		productList.setProducts(products);
		
		return productList;
	}
	
	public ProductList productSearchByCategory (String categoryName) throws Exception {
		
		String key = "productcategoryid";
		String value;
		
		Category category = categorySvc.categorySearch(categoryName);

		ProductList productList = new ProductList();
		if (category.getCategoryName() != null) {
			value = "" + category.getCategoryId();
			
			List<Document> productDocList = wildCardQuery(key, value);
			
			List<Product> products = new ArrayList<Product>();	
			for (Document productDoc : productDocList) {
	 
				Product product = new Product();
				if (productDoc != null) {
					product.setProductId(Long.parseLong(productDoc.get("productid")));
					product.setPrice(Long.parseLong(productDoc.get("price")));
					product.setProductName(productDoc.get("productname"));
					product.setCategoryId(Long.parseLong(productDoc.get("productcategoryid")));
					product.setInventoryId(Long.parseLong(productDoc.get("productinventoryid")));
				}
				products.add(product);
			}
			
			productList.setProducts(products);
		}
		
		return productList;
	}
	
	public Product productSearchById (long productId) throws Exception {
		
		String key = "productid";
		String value = "" + productId;
		
		Document productDoc = findHardly(key, value);
		
		Product product = new Product();
		if (productDoc != null) {
			product.setProductId(Long.parseLong(productDoc.get("productid")));
			product.setPrice(Long.parseLong(productDoc.get("price")));
			product.setProductName(productDoc.get("productname"));
			product.setCategoryId(Long.parseLong(productDoc.get("productcategoryid")));
			product.setInventoryId(Long.parseLong(productDoc.get("productinventoryid")));
		}
		
		return product;
	}

}
