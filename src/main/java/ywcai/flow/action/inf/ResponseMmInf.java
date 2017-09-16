package ywcai.flow.action.inf;
import java.util.List;

import ywcai.flow.hibernate.Product;
import ywcai.flow.model.PayNotify;
import ywcai.flow.model.PayNotifyResult;

public interface ResponseMmInf {
	public PayNotifyResult mmPayCallBack(PayNotify payEndBack);

	public List<Product> getProducts(String province, int local);
}
