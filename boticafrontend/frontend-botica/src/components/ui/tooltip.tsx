import React from "react"

interface TooltipProviderProps {
  children: React.ReactNode
}

export const TooltipProvider = ({ children }: TooltipProviderProps) => {
  return <>{children}</>
}