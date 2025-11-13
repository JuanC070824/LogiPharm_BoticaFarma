import { useState } from "react";

export interface Toast {
  id: string;
  title?: string;
  description?: string;
  action?: React.ReactNode;
}

export function useToast() {
  const [toasts, setToasts] = useState<Toast[]>([]);

  return {
    toasts,
    toast: (props: Omit<Toast, "id">) => {
      const id = Math.random().toString(36);
      setToasts((prev) => [...prev, { ...props, id }]);
    },
  };
}
