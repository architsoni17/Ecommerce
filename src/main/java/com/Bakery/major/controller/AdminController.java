package com.Bakery.major.controller;

import com.Bakery.major.dto.ProductDTO;
import com.Bakery.major.model.Category;
import com.Bakery.major.model.Product;
import com.Bakery.major.service.ProductService;
import com.Bakery.major.service.CategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//<input type="hidden" name="id"  th:field="*{id}"> add functionality for update also check if id present then it ll update it
//iStat -- responsible for id, we have a for each loop in categories.html pageb-- row one arrays and list start from zero thats why using istat.index+1
@Controller
public class AdminController {
	//user.dir takes us to the directory -- root one, getProperty has many functions
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;


	@GetMapping("/admin")
	public String adminHome()
	{
		return "adminHome";
	}
	@GetMapping("/admin/categories")
	public String getCategories(Model model)
	{
		model.addAttribute("categories", categoryService.getAllCategory());
		return "categories";
	}
	@GetMapping("/admin/categories/add")
	public String getaddCategories(Model model)
	{
		model.addAttribute("category", new Category()); //used category because its treated as key mentioned in categoriesAdd.html
		return "categoriesAdd";

	}
	@PostMapping("/admin/categories/add")
	public String postaddCategories(@ModelAttribute("category") Category category)
	{
		categoryService.addCategory(category);
		return "redirect:/admin/categories";

	}
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id)
	{
		categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/update/{id}")
	public String updateCategory(@PathVariable int id, Model model)
	{
		Optional<Category> category = categoryService.getCategoryById(id);
		if(category.isPresent())
		{
			model.addAttribute("category",category.get());
			return "categoriesAdd";
		}
		else
			return "404";
	}


	//Product Section
	@GetMapping("/admin/products")
	public String getProduct(Model model)
	{
		model.addAttribute("products", productService.getAllProduct());
		return "products";
	}

	@GetMapping("/admin/products/add")
	public String getAddProduct(Model model)
	{
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", categoryService.getAllCategory()); // sending all categories to show in list
		return "productsAdd";
	}


	//@requestparam - the things which are not wrapped in the object like the image in the form
	// CONVERTING PRODUCTDTO--->PRODUCT OBJECT either using map or setting them
	@PostMapping("/admin/products/add")
	public String postAddProduct(@ModelAttribute("productDTO") ProductDTO productDTO, @RequestParam("productImage") MultipartFile file,
								 @RequestParam("imgName") String imgName) throws IOException
	{
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());  // getting category id and setting it in product
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setDescription(productDTO.getDescription());
		String imageUUID;
		if(!file.isEmpty())
		{
			imageUUID  = file.getOriginalFilename();

			Path fileNameAndPath = Paths.get(uploadDir, imageUUID); // file path creation
			Files.write(fileNameAndPath, file.getBytes()); // file --> in bytes form
		}else
		{
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productService.addProduct(product);


		return "redirect:/admin/products" ;

	}

	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable long id)
	{
		productService.removeProductById(id);
		return "redirect:/admin/products";
	}

	@GetMapping("/admin/product/update/{id}")
	public String updateProduct(@PathVariable long id, Model model)
	{

		Product product = productService.getProductById(id).get();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setDescription(product.getDescription());
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setImageName(product.getImageName());

		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("productDTO", productDTO);

		return "productsAdd";

	}



}
