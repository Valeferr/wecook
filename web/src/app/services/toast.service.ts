import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private MAX_TOASTS = 3;
  private ToastIcons = {
    SUCCESS: 'check_circle_outline',
    WARNING: 'report_problem',
    ERROR: 'highlight_off',
  };
  constructor() {}
  private drawToasts(toasts: HTMLElement[]): void {
    toasts.forEach((toast, index) => {
      toast.style.top = `calc(20px + ${index * 65}px)`;
    });
  }
  private addNewToast(newToast: HTMLElement): void {
    let toasts = Array.from(document.getElementsByClassName('toast')) as HTMLElement[];
    toasts.push(newToast);
    if (toasts.length > this.MAX_TOASTS) {
      toasts[0].remove();
      toasts.shift();
    }
    this.drawToasts(toasts);
    document.body.appendChild(newToast);
    setTimeout(() => {
      newToast.remove();
      this.drawToasts(Array.from(document.getElementsByClassName('toast')) as HTMLElement[]);
    }, 3000);
  }
  public showToast(message: string, type: 'SUCCESS' | 'WARNING' | 'ERROR'): void {
    const newToast = document.createElement('div');
    newToast.className = `toast toast-${type.toLowerCase()} rubik-300`;
    const toastIcon = document.createElement('span');
    toastIcon.classList.add('material-icons-outlined', 'md-18');
    toastIcon.innerHTML = this.ToastIcons[type];
    const toastMessage = document.createElement('span');
    toastMessage.innerText = message;
    newToast.appendChild(toastIcon);
    newToast.appendChild(toastMessage);
    this.addNewToast(newToast);
  }
}