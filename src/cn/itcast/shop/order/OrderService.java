package cn.itcast.shop.order;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.itcast.shop.user.User;
import cn.itcast.shop.utils.PageBean;

@Transactional
public class OrderService {
	// 注入DAO
	private OrderDao orderDao;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}



	// 业务层保存订单
	public Integer save(Order order) {
		return orderDao.save(order);
	}



	public Order findByOid(Integer oid) {
		return orderDao.findByOid(oid);
	}



	public void update(Order currOrder) {
		orderDao.update(currOrder);
	}



	public List<Order> findByUid(User existUser) {
		return orderDao.findByUid(existUser);
	}


	// 业务层按状态查询订单
	public PageBean<Order> findByPage(Integer state, Integer page) {
		PageBean<Order> pageBean = new PageBean<Order>();
		// 封装分页类的数据:
		// 设置当前页数
		pageBean.setPage(page);
		// 设置每页显示的记录数:
		Integer limit = 10;
		pageBean.setLimit(limit);
		// 设置总记录数:
		Integer totalCount = orderDao.findCount(state);
		pageBean.setTotalCount(totalCount);
		// 设置总页数:
		Integer totalPage = 0;
		if(totalCount % limit == 0){
			totalPage = totalCount / limit;
		}else{
			totalPage = totalCount / limit+1;
		}
		pageBean.setTotalPage(totalPage);
		// 设置每页显示的数据:
		Integer begin = (page - 1) * limit;
		List<Order> list = orderDao.findByPage(state,begin,limit);
		pageBean.setList(list);
		return pageBean;
	}


	// 业务层查询所有订单
	public PageBean<Order> findByPage(Integer page) {
		PageBean<Order> pageBean = new PageBean<Order>();
		// 封装分页类的数据:
		// 设置当前页数
		pageBean.setPage(page);
		// 设置每页显示的记录数:
		Integer limit = 10;
		pageBean.setLimit(limit);
		// 设置总记录数:
		Integer totalCount = orderDao.findCount();
		pageBean.setTotalCount(totalCount);
		// 设置总页数:
		Integer totalPage = 0;
		if(totalCount % limit == 0){
			totalPage = totalCount / limit;
		}else{
			totalPage = totalCount / limit+1;
		}
		pageBean.setTotalPage(totalPage);
		// 设置每页显示的数据:
		Integer begin = (page - 1) * limit;
		List<Order> list = orderDao.findByPage(begin,limit);
		pageBean.setList(list);
		return pageBean;
	}

}
