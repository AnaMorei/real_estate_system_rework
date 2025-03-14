/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

  private static final BCrypt.Version DEFAULT_VERSION = BCrypt.Version.VERSION_2A;
  private static final int DEFAULT_COST = 10;

  public static String hashPassword(char[] password) {
    return BCrypt.with(DEFAULT_VERSION).hashToString(DEFAULT_COST, password);
  }

  public static boolean verifyPassword(char[] password, String hashedPassword) {
    return verifyPassword(password, hashedPassword, DEFAULT_COST);
  }

  public static boolean verifyPassword(char[] password, String hashedPassword, int cost) {
    BCrypt.Result result = BCrypt.verifyer().verify(password, hashedPassword);
    return result.verified;
  }

}
