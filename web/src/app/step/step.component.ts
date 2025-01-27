import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-step',
  standalone: true,
  imports: [],
  templateUrl: './step.component.html',
  styleUrl: './step.component.css'
})
export class StepComponent {
  @Input() title: string = 'Nuovo Step';
  @Input() count: number = 1;
  @Output() onDelete = new EventEmitter<void>();

  delete() {
    this.onDelete.emit();
  }
  
}
