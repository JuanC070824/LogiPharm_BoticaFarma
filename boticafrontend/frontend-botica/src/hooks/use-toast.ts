// src/hooks/use-toast.ts
import { useCallback } from "react";
import { toast } from "react-toastify";

export function useToast() {
  const showToast = useCallback((message: string, type: "success" | "error" | "info" = "info") => {
    switch (type) {
      case "success":
        toast.success(message);
        break;
      case "error":
        toast.error(message);
        break;
      default:
        toast.info(message);
    }
  }, []);

  return { toast: showToast };
}
