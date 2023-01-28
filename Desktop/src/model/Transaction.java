package model;

public class Transaction {
	private Integer transactionId;
	private String paymentMethod;
	private String cardNumber;
	private String timestamp;
	private Integer totalPrice;
	
	public Transaction(Integer transactionId, String paymentMethod, String cardNumber, String timestamp, Integer totalPrice) {
		super();
		this.transactionId = transactionId;
		this.paymentMethod = paymentMethod;
		this.cardNumber = cardNumber;
		this.timestamp = timestamp;
		this.totalPrice = totalPrice;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

}
