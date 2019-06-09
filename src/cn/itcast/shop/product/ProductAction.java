package cn.itcast.shop.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import cn.itcast.shop.category.Category;
import cn.itcast.shop.category.CategoryService;
import cn.itcast.shop.categorysecond.CategorySecond;
import cn.itcast.shop.categorysecond.CategorySecondService;
import cn.itcast.shop.utils.PageBean;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 商品访问的Action类
 * 
 * @author 传智.郭嘉
 * 
 */
public class ProductAction extends ActionSupport implements
		ModelDriven<Product> {
	// 文件上传需要的三个属性:
	private File upload;	// 上传文件
	private String uploadContentType; // 上传文件的MIME类型
	private String uploadFileName; // 上传文件名称
	
	// 接收cid
	private Integer cid;
	// 接收二级分类id
	private Integer csid;
	// 接收当前页数
	private Integer page;
	// 注入一级分类的Service
	private CategoryService categoryService;
	// 注入二级分类的Service
	private CategorySecondService categorySecondService;
	// 注入商品的Service
	private ProductService productService;
	// 分页的商品显示:
	private PageBean<Product> pageBean;
	// 模型驱动类
	private Product product = new Product();
	// 文件上传使用的属性
	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public void setCategorySecondService(CategorySecondService categorySecondService) {
		this.categorySecondService = categorySecondService;
	}

	public Product getModel() {
		return product;
	}

	public void setCsid(Integer csid) {
		this.csid = csid;
	}

	public Integer getCsid() {
		return csid;
	}

	public PageBean<Product> getPageBean() {
		return pageBean;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	// 查询商品的方法:
	public String findByCid() {
		// 查询分类:
		// 查询所有一级分类:
		List<Category> categoryList = categoryService.findAll();
		// 获得值栈:
		ActionContext.getContext().getValueStack()
				.set("categoryList", categoryList);

		// 查询商品:
		pageBean = productService.findByPage(cid, page);

		return "findByCidSuccess";
	}

	// 查询商品详情:
	public String findByPid() {
		// 查询所有一级分类:
		List<Category> categoryList = categoryService.findAll();
		// 获得值栈:
		ActionContext.getContext().getValueStack()
				.set("categoryList", categoryList);

		product = productService.findByPid(product.getPid());
		return "findByPidSuccess";
	}

	// 查询二级分类的商品:
	public String findByCsid() {
		// 查询所有一级分类:
		List<Category> categoryList = categoryService.findAll();
		// 获得值栈:
		ActionContext.getContext().getValueStack()
				.set("categoryList", categoryList);

		pageBean = productService.findByCsid(csid, page);
		return "findByCsidSuccess";
	}
	
	/**
	 * 后台:查询所有商品的方法
	 */
	public String adminFindAll(){
		pageBean = productService.findByPage(page);
		return "adminFindAllSuccess";
	}
	/*
	 * 后台：编辑商品信息
	 * 
	 * */
	public String edit(){
		product =  productService.findByPid(product.getPid());
		// 查询所有的二级分类 :
		List<CategorySecond> csList = categorySecondService.findAll();
		ActionContext.getContext().getValueStack().set("csList", csList);
		return "editSuccess";
	}
	
	/**
	 * 后台:修改商品信息:
	 */
	public String update(){
		productService.update(product);
		return "updateSuccess";
	}
	/**
	 * 跳转到添加页面
	 */
	public String addPage(){
		// 查询所有的二级分类 :
		List<CategorySecond> csList = categorySecondService.findAll();
		ActionContext.getContext().getValueStack().set("csList", csList);
		return "addPageSuccess";
	}
	
	/**
	 * 保存商品:同时上传图片
	 * @throws IOException 
	 */
	public String save() throws IOException{
		// 文件上传的操作:
		// 获得上传的路径:
		String path = ServletActionContext.getServletContext().getRealPath("/products");
		String realPath = path+"/"+csid+"/"+uploadFileName;
		File diskFile = new File(realPath);
		// 文件上传:
		FileUtils.copyFile(upload, diskFile);
		// 保存到数据库:
		// 设置二级分类
		CategorySecond categorySecond = new CategorySecond();
		categorySecond.setCsid(csid);
		product.setCategorySecond(categorySecond);
		// 设置时间:
		product.setPdate(new Date());
		// 设置图片上传路径:
		product.setImage("products/"+csid+"/"+uploadFileName);
		
		// 调用Serviec保存商品:
		productService.save(product);
		return "saveSuccess";
	}

}
