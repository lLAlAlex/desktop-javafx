package model;

public class Promo {
	private Integer promoId;
	private String promoCode;
	private Integer promoDisc;
	private String promoNote;
	
	public Promo(Integer promoId, String promoCode, Integer promoDisc, String promoNote) {
		super();
		this.promoId = promoId;
		this.promoCode = promoCode;
		this.promoDisc = promoDisc;
		this.promoNote = promoNote;
	}

	public Integer getPromoId() {
		return promoId;
	}

	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Integer getPromoDisc() {
		return promoDisc;
	}

	public void setPromoDisc(Integer promoDisc) {
		this.promoDisc = promoDisc;
	}

	public String getPromoNote() {
		return promoNote;
	}

	public void setPromoNote(String promoNote) {
		this.promoNote = promoNote;
	}

}
