package model;

public class House {

  private int id;
  private String address;
  private String description;
  private double price;
  private double size;
  private int realtorId;

  public House() {
  }

  public House(String address, String description, double price, double size, int realtorId) {
    this.address = address;
    this.description = description;
    this.price = price;
    this.size = size;
    this.realtorId = realtorId;
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public int getRealtorId() {
    return realtorId;
  }

  public void setRealtorId(int realtorId) {
    this.realtorId = realtorId;
  }

  @Override
  public String toString() {
    return address;
  }
}
