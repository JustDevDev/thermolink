import { rootStore } from "@/stores/root/rootStore";

// Define the type for request options, including URL, method, body, headers, and credentials
type RequestOptions<Req> = {
  url: string;
  method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
  body?: Req;
  headers?: HeadersInit;
  credentials?: RequestCredentials;
};

// Define the type for API error response
type ApiErrorResponse = {
  error?: string;
  message?: string;
  status?: number;
  code?: string;
};

// Define the type for fetch response, including status, data, and error
export type FetchResponse<Res> = {
  status: number;
  data: Res | null;
  error: string | null;
};

// Generic function to make a fetch request
async function fetchRequest<TRequest = undefined, TResponse = undefined>(
  options: RequestOptions<TRequest>
): Promise<FetchResponse<TResponse>> {
  const { url, method, body, headers } = options;

  // Set up fetch options
  const fetchOptions: RequestInit = {
    method,
    headers: {
      "Content-Type": "application/json",
      ...(headers || {}),
    },
    credentials: "include",
  };

  // Add body to fetch options if provided
  if (body) {
    fetchOptions.body = JSON.stringify(body);
  }

  try {
    // Make the fetch request
    const response = await fetch(url, fetchOptions);
    const status: number = response.status;

    // Handle 204 No Content responses
    if (status === 204) {
      return { status, data: null, error: null };
    }

    let data: TResponse | null = null;
    try {
      data = await response.json();
    } catch {
      return {
        status: response.status,
        data: null,
        error: "Failed to parse response data",
      };
    }

    // Handle unauthorized responses
    if (status === 401) {
      await rootStore.loginStore.logoutUser();
    }

    // Handle non-OK responses
    if (!response.ok) {
      const errorResponse = data as unknown as ApiErrorResponse;
      return {
        status,
        data: null,
        error:
          errorResponse?.error || errorResponse?.message || `Error: ${status}`,
      };
    }

    // Return successful response
    return { status, data, error: null };
  } catch (error) {
    // Handle fetch errors
    return {
      status: 500,
      data: null,
      error: error instanceof Error ? error.message : "Unknown error occurred",
    };
  }
}

// Function to make an authenticated GET request
export async function authGet<TResponse = undefined>(
  url: string,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<undefined, TResponse>({
    url,
    method: "GET",
    headers: {
      ...(headers || {}),
    },
    credentials: "include",
  });
}

// Function to make a GET request
export async function get<TResponse = undefined>(
  url: string,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<undefined, TResponse>({
    url,
    method: "GET",
    headers,
  });
}

// Function to make a POST request
export async function post<TRequest = undefined, TResponse = undefined>(
  url: string,
  body: TRequest,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<TRequest, TResponse>({
    url,
    method: "POST",
    body,
    headers,
  });
}

// Function to make a PUT request
export async function put<TRequest = undefined, TResponse = undefined>(
  url: string,
  body: TRequest,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<TRequest, TResponse>({
    url,
    method: "PUT",
    body,
    headers,
  });
}

// Function to make a PATCH request
export async function patch<TRequest = undefined, TResponse = undefined>(
  url: string,
  body: TRequest,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<TRequest, TResponse>({
    url,
    method: "PATCH",
    body,
    headers,
  });
}

// Function to make a DELETE request
export async function del<TRequest = undefined, TResponse = undefined>(
  url: string,
  body?: TRequest,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return fetchRequest<TRequest, TResponse>({
    url,
    method: "DELETE",
    body,
    headers,
  });
}

// Function to delete a resource by ID
export async function deleteById<TResponse = undefined>(
  baseUrl: string,
  id: string | number,
  headers?: HeadersInit
): Promise<FetchResponse<TResponse>> {
  return del<undefined, TResponse>(`${baseUrl}/${id}`, undefined, headers);
}
