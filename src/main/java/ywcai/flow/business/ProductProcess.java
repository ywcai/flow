package ywcai.flow.business;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ywcai.flow.hibernate.Product;
import ywcai.flow.util.HibernateUtil;
import ywcai.flow.util.MyLog;

public class ProductProcess {
	
	public Product queryProduct(int productId)
	{
		Session session=HibernateUtil.getCurrentSession();
		List<?> list=null;
		Product product=null;
		String hql="from Product where productid = ?0 "; 
		Transaction transaction=null;
		try {
			transaction=session.beginTransaction();
			Query<?> query = session.createQuery(hql);
			query.setParameter(0, productId);
			list = (List<?>) query.list();
			transaction.commit();
		} catch (Exception e) {
			MyLog.ERR(e.getMessage());
			return null;
		}
		if(list!=null)
		{
			product=(Product)list.get(0);
		}
		return product;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> queryProducts(String province,int local)
	{
		List<Product> list=null;
		Session session=HibernateUtil.getCurrentSession();
		String hql="from Product where province = ?0 and local = ?1 and delflag = 1 "; 
		Transaction transaction=null;
		try {
			MyLog.INFO(province+"|"+local);
			transaction=session.beginTransaction();
			Query<?> query = session.createQuery(hql);
			query.setParameter(0, province);
			query.setParameter(1, local);
			list = (List<Product>) query.list();
			transaction.commit();
		} catch (Exception e) {
			MyLog.ERR(e.toString());
		}
		return list;
	}
}
