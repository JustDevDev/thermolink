/**
 * CookieManager class for handling cookie operations
 */
export class CookieManager {
  /**
   * Retrieves the value of a cookie by its name
   * @param name - The name of the cookie to retrieve
   * @returns The decoded value of the cookie if found, null otherwise
   */
  static getCookie(name: string): string | null {
    const cookies = document.cookie.split(";");
    for (const cookie of cookies) {
      const [cookieName, cookieValue] = cookie.split("=").map((c) => c.trim());
      if (cookieName === name) {
        return decodeURIComponent(cookieValue);
      }
    }
    return null;
  }
}
