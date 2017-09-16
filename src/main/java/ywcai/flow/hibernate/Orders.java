package ywcai.flow.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders", catalog = "mrapp")
public class Orders {

	private Integer uid;
	private String timestamp;
	private String ordernum;
	private String openid;
	private String mobile;
	private int productid;
	private int totalfee;
	private int payfast;
	private String echo;
	private int orderstatus;
	private int delflag;
	private String packcode;
	private int localproduct;
	private String clientip;
	private int price,rate;
	private String orderdesc;
	

//	public Orders() {
//	}
//
//	public Orders(String ordernum, String openid, String mobile, int productid, int totalfee, int payfast, String echo,
//			int orderstatus, int delflag, String packcode, int localproduct, String clientip) {
//		this.ordernum = ordernum;
//		this.openid = openid;
//		this.mobile = mobile;
//		this.productid = productid;
//		this.totalfee = totalfee;
//		this.payfast = payfast;
//		this.echo = echo;
//		this.orderstatus = orderstatus;
//		this.delflag = delflag;
//		this.packcode = packcode;
//		this.localproduct = localproduct;
//		this.clientip = clientip;
//	}



	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "uid", unique = true, nullable = false)
	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}


	@Column(name = "timestamp", nullable = false, length = 40)
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
	@Column(name = "ordernum", nullable = false, length = 50)
	public String getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "openid", nullable = false, length = 100)
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "mobile", nullable = false, length = 20)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "productid", nullable = false)
	public int getProductid() {
		return this.productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	@Column(name = "totalfee", nullable = false)
	public int getTotalfee() {
		return this.totalfee;
	}

	public void setTotalfee(int totalfee) {
		this.totalfee = totalfee;
	}

	@Column(name = "payfast", nullable = false)
	public int getPayfast() {
		return this.payfast;
	}

	public void setPayfast(int payfast) {
		this.payfast = payfast;
	}

	@Column(name = "echo", nullable = false, length = 50)
	public String getEcho() {
		return this.echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

	@Column(name = "orderstatus", nullable = false)
	public int getOrderstatus() {
		return this.orderstatus;
	}

	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}

	@Column(name = "delflag", nullable = false)
	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}

	@Column(name = "packcode", nullable = false, length = 50)
	public String getPackcode() {
		return this.packcode;
	}

	public void setPackcode(String packcode) {
		this.packcode = packcode;
	}

	@Column(name = "localproduct", nullable = false)
	public int getLocalproduct() {
		return this.localproduct;
	}

	public void setLocalproduct(int localproduct) {
		this.localproduct = localproduct;
	}

	@Column(name = "clientip", nullable = false, length = 45)
	public String getClientip() {
		return this.clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
	
	@Column(name = "price", nullable = false)
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Column(name = "rate", nullable = false)
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	

	@Column(name = "orderdesc", nullable = false)
	public String getOrderdesc() {
		return orderdesc;
	}

	public void setOrderdesc(String orderdesc) {
		this.orderdesc = orderdesc;
	}
	

	
}
