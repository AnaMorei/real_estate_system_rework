package model;

public class User {

  private int id;
  private String name;
  private String email;
  private String cpf;
  private UserType type; // "Realtor" or "Buyer"

  public User() {
  }

  public User(String name, String email, String cpf, UserType type) {
    this.name = name;
    this.email = email;
    this.cpf = cpf;
    this.type = type;
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return name;
  }
}
