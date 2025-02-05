import { Component, input } from '@angular/core';
import { Step } from '../../../model/Step.model';

@Component({
  selector: 'app-step',
  standalone: true,
  imports: [],
  templateUrl: './step.component.html',
  styleUrl: './step.component.css'
})
export class StepComponent {
  public step = input.required<Step>();

  
}
