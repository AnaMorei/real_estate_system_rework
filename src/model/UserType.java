/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Clara
 */
public enum UserType {
  REALTOR("Realtor"),
  BUYER("Buyer");

  private final String typeName;

  UserType(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public static UserType fromTypeName(String typeName) {
    for (UserType type : values()) {
      if (type.typeName.equalsIgnoreCase(typeName)) {
        return type;
      }
    }

    throw new IllegalArgumentException("Invalid UserType: " + typeName);
  }

}
