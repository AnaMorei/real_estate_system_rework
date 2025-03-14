package model;

import java.sql.Date;

public class Transaction {

  private int id;
  private Date date;
  private double amount;
  private int buyerId; // ID do comprador
  private int realtorId; // ID do responsável
  private int houseId; // ID da casa que foi vendida

  public Transaction() {
  }

  public Transaction(Date date, double amount, int buyerId, int realtorId, int houseId) {
    this.date = date;
    this.amount = amount;
    this.buyerId = buyerId;
    this.realtorId = realtorId;
    this.houseId = houseId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public int getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(int buyerId) {
    this.buyerId = buyerId;
  }

  public int getRealtorId() {
    return realtorId;
  }

  public void setRealtorId(int realtorId) {
    this.realtorId = realtorId;
  }

  public int getHouseId() {
    return houseId;
  }

  public void setHouseId(int houseId) {
    this.houseId = houseId;
  }

}
