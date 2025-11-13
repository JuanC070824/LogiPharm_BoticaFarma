import React from "react"

interface ToastProps extends React.HTMLAttributes<HTMLDivElement> {
  children?: React.ReactNode
}

export const ToastProvider = ({ children }: { children: React.ReactNode }) => {
  return <>{children}</>
}

export const Toast = React.forwardRef<HTMLDivElement, ToastProps>(
  ({ className, children, ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={`pointer-events-auto relative flex w-full items-center justify-between space-x-4 overflow-hidden rounded-md border p-6 pr-8 shadow-lg transition-all ${className || ''}`}
        {...props}
      >
        {children}
      </div>
    )
  }
)
Toast.displayName = "Toast"

export const ToastViewport = () => {
  return (
    <div className="fixed top-0 z-[100] flex max-h-screen w-full flex-col-reverse p-4 sm:bottom-0 sm:right-0 sm:top-auto sm:flex-col md:max-w-[420px]" />
  )
}