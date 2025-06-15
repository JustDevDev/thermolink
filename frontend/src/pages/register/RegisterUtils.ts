import { RegisterEnums } from "./store/RegisterStore";

/**
 * Checks if the password meets the required minimum length.
 * @returns True if password length is sufficient, otherwise false.
 */
export const checkPasswordLength = (password: string): boolean => {
  return password.length >= RegisterEnums.passwordLength;
};

/**
 * Checks if the password contains at least one special character.
 * @returns True if a special character is present, otherwise false.
 */
export const containsSpecialCharacter = (password: string): boolean => {
  const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
  return specialCharRegex.test(password);
};
